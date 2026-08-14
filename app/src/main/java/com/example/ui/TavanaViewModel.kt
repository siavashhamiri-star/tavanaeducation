package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.entity.CourseEntity
import com.example.data.local.entity.LessonEntity
import com.example.data.repository.GeminiMentorRepository
import com.example.data.repository.TavanaRepository
import com.example.domain.model.AcademyWorld
import com.example.domain.model.UserRole
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TavanaViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)
    private val repository = TavanaRepository(db)
    private val geminiRepo = GeminiMentorRepository()

    val userState = repository.userFlow.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), null
    )

    val selectedWorld = MutableStateFlow(AcademyWorld.TAVANA_PROFESSIONAL)
    val searchQuery = MutableStateFlow("")

    val coursesState = combine(
        repository.allCoursesFlow,
        selectedWorld,
        searchQuery
    ) { courses, world, query ->
        courses.filter { course ->
            val matchWorld = course.academyWorld == world.name
            val matchQuery = query.isBlank() ||
                    course.title.contains(query, ignoreCase = true) ||
                    course.description.contains(query, ignoreCase = true) ||
                    course.category.contains(query, ignoreCase = true)
            matchWorld && matchQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val enrolledCourses = repository.enrolledCoursesFlow.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    val userSkills = repository.userSkillsFlow.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    val userProjects = repository.userProjectsFlow.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    val examsState = repository.examsFlow.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    val aiMessagesState = repository.aiMessagesFlow.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    // Language & Accessibility State
    val selectedLanguage = MutableStateFlow("fa") // "fa" or "en"
    val isHighContrastMode = MutableStateFlow(false)
    val isLargeTextMode = MutableStateFlow(false)
    val isScreenReaderEnabled = MutableStateFlow(false)
    val isCaptionsEnabled = MutableStateFlow(true)
    val lastSpokenNarratorText = MutableStateFlow<String?>(null)

    val selectedCourse = MutableStateFlow<CourseEntity?>(null)
    val selectedLesson = MutableStateFlow<LessonEntity?>(null)
    val courseLessonsState = MutableStateFlow<List<LessonEntity>>(emptyList())

    val isAiMentorThinking = MutableStateFlow(false)

    init {
        viewModelScope.launch {
            repository.seedInitialDataIfEmpty()
        }
    }

    fun setLanguage(lang: String) {
        selectedLanguage.value = lang
        speakAccessibilityText(if (lang == "fa") "زبان برنامه به فارسی تغییر یافت" else "App language set to English")
    }

    fun toggleHighContrast() {
        isHighContrastMode.value = !isHighContrastMode.value
        speakAccessibilityText(
            if (isHighContrastMode.value) "حالت کنتراست بالا برای کم‌بینایان فعال شد"
            else "حالت کنتراست بالا غیرفعال شد"
        )
    }

    fun toggleLargeText() {
        isLargeTextMode.value = !isLargeTextMode.value
        speakAccessibilityText(
            if (isLargeTextMode.value) "حالت درشت‌نمایی متن فعال شد"
            else "حالت درشت‌نمایی متن غیرفعال شد"
        )
    }

    fun toggleScreenReader() {
        isScreenReaderEnabled.value = !isScreenReaderEnabled.value
        if (isScreenReaderEnabled.value) {
            speakAccessibilityText("گوینده صوتی و متن‌خوان توانا برای نابینایان فعال گردید.")
        } else {
            lastSpokenNarratorText.value = null
        }
    }

    fun toggleCaptions() {
        isCaptionsEnabled.value = !isCaptionsEnabled.value
        speakAccessibilityText(
            if (isCaptionsEnabled.value) "زیرنویس همزمان برای کم‌شنوایان فعال شد"
            else "زیرنویس غیرفعال شد"
        )
    }

    fun speakAccessibilityText(text: String) {
        if (isScreenReaderEnabled.value || text.contains("فعال شد") || text.contains("تغییر یافت")) {
            lastSpokenNarratorText.value = text
        }
    }

    fun registerUserWithPhone(phone: String, fullName: String) {
        viewModelScope.launch {
            val currentUser = userState.value
            val updatedUser = (currentUser ?: com.example.data.local.entity.UserEntity()).copy(
                fullName = fullName.ifBlank { "کاربر توانا" },
                email = "$phone@tavana.edu"
            )
            db.userDao().insertOrUpdateUser(updatedUser)
            speakAccessibilityText("ثبت‌نام با شماره تلفن $phone با موفقیت انجام شد.")
        }
    }

    fun registerUserWithEmail(email: String, fullName: String) {
        viewModelScope.launch {
            val currentUser = userState.value
            val updatedUser = (currentUser ?: com.example.data.local.entity.UserEntity()).copy(
                fullName = fullName.ifBlank { "کاربر توانا" },
                email = email
            )
            db.userDao().insertOrUpdateUser(updatedUser)
            speakAccessibilityText("ثبت‌نام با ایمیل $email با موفقیت انجام شد.")
        }
    }

    fun selectWorld(world: AcademyWorld) {
        selectedWorld.value = world
        viewModelScope.launch {
            repository.updateSelectedWorld(world)
        }
    }

    fun switchRole(newRole: UserRole) {
        viewModelScope.launch {
            repository.updateRole(newRole)
        }
    }

    fun openCourseDetails(course: CourseEntity) {
        selectedCourse.value = course
        viewModelScope.launch {
            repository.getLessonsForCourse(course.id).collect { lessons ->
                courseLessonsState.value = lessons
                if (selectedLesson.value == null && lessons.isNotEmpty()) {
                    selectedLesson.value = lessons.first()
                }
            }
        }
    }

    fun enrollCurrentCourse() {
        val course = selectedCourse.value ?: return
        viewModelScope.launch {
            repository.enrollInCourse(course.id)
            selectedCourse.value = course.copy(isEnrolled = true)
        }
    }

    fun selectLesson(lesson: LessonEntity) {
        selectedLesson.value = lesson
    }

    fun sendAiMentorMessage(prompt: String) {
        if (prompt.isBlank()) return
        viewModelScope.launch {
            repository.saveAiChatMessage("USER", prompt)
            isAiMentorThinking.value = true

            val currentUser = userState.value
            val role = try {
                UserRole.valueOf(currentUser?.roleName ?: "STUDENT")
            } catch (e: Exception) {
                UserRole.STUDENT
            }

            val history = aiMessagesState.value.map { it.senderRole to it.messageText }

            val response = geminiRepo.sendPromptToMentor(
                userPrompt = prompt,
                userRole = role,
                selectedWorldTitle = selectedWorld.value.titleFa,
                gradeLevelFa = currentUser?.gradeLevel ?: "کنکور",
                historyMessages = history
            )

            isAiMentorThinking.value = false
            repository.saveAiChatMessage("AI", response)
        }
    }

    fun submitNewProject(title: String, description: String, category: String, url: String) {
        viewModelScope.launch {
            repository.submitProject(title, description, category, url)
        }
    }

    fun addNewSkill(skillName: String, category: String, level: Int) {
        viewModelScope.launch {
            repository.addSkill(skillName, category, level)
        }
    }

    fun recordExamResult(examId: String, score: Int) {
        viewModelScope.launch {
            repository.recordExamScore(examId, score)
        }
    }
}
