package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.local.entity.CourseEntity
import com.example.data.local.entity.LessonEntity
import com.example.domain.model.UserRole
import com.example.ui.TavanaViewModel
import com.example.ui.screens.*
import com.example.ui.theme.TavanaCyan
import com.example.ui.theme.TavanaGold
import com.example.ui.theme.TavanaNavy
import com.example.ui.theme.TavanaTheme

enum class TavanaScreen(val titleFa: String, val titleEn: String, val icon: ImageVector) {
    HOME("خانه", "Home", Icons.Default.Home),
    PROFESSIONAL("تخصصی", "Professional", Icons.Default.Engineering),
    GENERAL("عمومی و کنکور", "General & Konkur", Icons.Default.School),
    AI_MENTOR("مربی AI", "AI Mentor", Icons.Default.AutoAwesome),
    SKILL_PASSPORT("گذرنامه مهارت", "Passport", Icons.Default.Badge),
    EXAMS("آزمون‌ها", "Exams", Icons.Default.Quiz),
    TEACHER_DASHBOARD("پنل مدرس", "Teacher Panel", Icons.Default.SpaceDashboard),
    ADMIN_DASHBOARD("پنل مدیریت", "Admin Panel", Icons.Default.AdminPanelSettings),
    COURSE_DETAIL("جزئیات دوره", "Course Detail", Icons.Default.Book),
    LESSON_PLAYER("مشاهده درس", "Lesson Player", Icons.Default.PlayCircle),
    PROFILE("پروفایل", "Profile", Icons.Default.Person)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: TavanaViewModel = viewModel()
            val lang by viewModel.selectedLanguage.collectAsState()
            val isHighContrast by viewModel.isHighContrastMode.collectAsState()

            TavanaTheme(lang = lang, highContrast = isHighContrast) {
                TavanaApp(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TavanaApp(viewModel: TavanaViewModel = viewModel()) {
    var currentScreen by remember { mutableStateOf(TavanaScreen.HOME) }

    val user by viewModel.userState.collectAsState()
    val selectedWorld by viewModel.selectedWorld.collectAsState()
    val courses by viewModel.coursesState.collectAsState()
    val userSkills by viewModel.userSkills.collectAsState()
    val userProjects by viewModel.userProjects.collectAsState()
    val exams by viewModel.examsState.collectAsState()
    val aiMessages by viewModel.aiMessagesState.collectAsState()
    val isAiLoading by viewModel.isAiMentorThinking.collectAsState()

    val lang by viewModel.selectedLanguage.collectAsState()
    val spokenText by viewModel.lastSpokenNarratorText.collectAsState()
    val isEn = lang == "en"

    val selectedCourse by viewModel.selectedCourse.collectAsState()
    val selectedLesson by viewModel.selectedLesson.collectAsState()
    val courseLessons by viewModel.courseLessonsState.collectAsState()

    val currentRole = try {
        UserRole.valueOf(user?.roleName ?: "STUDENT")
    } catch (e: Exception) {
        UserRole.STUDENT
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            Column {
                // Narrator Speech Output Banner
                if (!spokenText.isNullOrBlank()) {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color(0xFF15803D)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                                Icon(Icons.Default.RecordVoiceOver, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = spokenText!!,
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            IconButton(onClick = { viewModel.lastSpokenNarratorText.value = null }, modifier = Modifier.size(24.dp)) {
                                Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }

                NavigationBar(
                    containerColor = TavanaNavy,
                    tonalElevation = 8.dp
                ) {
                    val mainNavItems = mutableListOf(
                        TavanaScreen.HOME,
                        TavanaScreen.PROFESSIONAL,
                        TavanaScreen.GENERAL,
                        TavanaScreen.AI_MENTOR,
                        TavanaScreen.PROFILE
                    )

                    // Insert Role-specific dashboards if Teacher or Admin
                    if (currentRole == UserRole.TEACHER) {
                        mainNavItems[1] = TavanaScreen.TEACHER_DASHBOARD
                    } else if (currentRole == UserRole.ADMIN) {
                        mainNavItems[1] = TavanaScreen.ADMIN_DASHBOARD
                    }

                    mainNavItems.forEach { screen ->
                        val isSelected = currentScreen == screen
                        val screenTitle = if (isEn) screen.titleEn else screen.titleFa
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                currentScreen = screen
                                viewModel.speakAccessibilityText("صفحه $screenTitle فعال شد")
                            },
                            icon = {
                                Icon(
                                    imageVector = screen.icon,
                                    contentDescription = screenTitle,
                                    modifier = Modifier.size(22.dp)
                                )
                            },
                            label = {
                                Text(
                                    text = screenTitle,
                                    fontSize = 10.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = TavanaCyan,
                                selectedTextColor = TavanaCyan,
                                indicatorColor = TavanaNavy.copy(alpha = 0.5f),
                                unselectedIconColor = Color.White.copy(alpha = 0.6f),
                                unselectedTextColor = Color.White.copy(alpha = 0.6f)
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedContent(
                targetState = currentScreen,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "screenTransition"
            ) { screen ->
                when (screen) {
                    TavanaScreen.HOME -> HomeScreen(
                        viewModel = viewModel,
                        user = user,
                        courses = courses,
                        selectedWorld = selectedWorld,
                        onNavigateToCourse = { course ->
                            viewModel.openCourseDetails(course)
                            currentScreen = TavanaScreen.COURSE_DETAIL
                        },
                        onNavigateToAiMentor = { currentScreen = TavanaScreen.AI_MENTOR },
                        onNavigateToSkillPassport = { currentScreen = TavanaScreen.SKILL_PASSPORT },
                        onNavigateToExams = { currentScreen = TavanaScreen.EXAMS }
                    )

                    TavanaScreen.PROFESSIONAL -> ProfessionalAcademyScreen(
                        viewModel = viewModel,
                        courses = courses,
                        userProjects = userProjects,
                        onNavigateToCourse = { course ->
                            viewModel.openCourseDetails(course)
                            currentScreen = TavanaScreen.COURSE_DETAIL
                        },
                        onNavigateToSkillPassport = { currentScreen = TavanaScreen.SKILL_PASSPORT }
                    )

                    TavanaScreen.GENERAL -> GeneralAcademyScreen(
                        viewModel = viewModel,
                        courses = courses,
                        exams = exams,
                        onNavigateToCourse = { course ->
                            viewModel.openCourseDetails(course)
                            currentScreen = TavanaScreen.COURSE_DETAIL
                        },
                        onNavigateToExams = { currentScreen = TavanaScreen.EXAMS }
                    )

                    TavanaScreen.AI_MENTOR -> AiMentorScreen(
                        viewModel = viewModel,
                        messages = aiMessages,
                        isLoading = isAiLoading
                    )

                    TavanaScreen.SKILL_PASSPORT -> SkillPassportScreen(
                        user = user,
                        skills = userSkills,
                        projects = userProjects,
                        viewModel = viewModel
                    )

                    TavanaScreen.EXAMS -> ExamsScreen(
                        exams = exams,
                        viewModel = viewModel
                    )

                    TavanaScreen.TEACHER_DASHBOARD -> TeacherDashboardScreen(
                        courses = courses,
                        viewModel = viewModel
                    )

                    TavanaScreen.ADMIN_DASHBOARD -> AdminDashboardScreen(
                        viewModel = viewModel
                    )

                    TavanaScreen.COURSE_DETAIL -> CourseDetailScreen(
                        course = selectedCourse,
                        lessons = courseLessons,
                        viewModel = viewModel,
                        onBackClick = { currentScreen = TavanaScreen.HOME },
                        onStartLesson = { lesson ->
                            viewModel.selectLesson(lesson)
                            currentScreen = TavanaScreen.LESSON_PLAYER
                        }
                    )

                    TavanaScreen.LESSON_PLAYER -> LessonPlayerScreen(
                        lesson = selectedLesson,
                        viewModel = viewModel,
                        onBackClick = { currentScreen = TavanaScreen.COURSE_DETAIL },
                        onOpenAiMentorWithPrompt = { prompt ->
                            viewModel.sendAiMentorMessage(prompt)
                            currentScreen = TavanaScreen.AI_MENTOR
                        }
                    )

                    TavanaScreen.PROFILE -> ProfileScreen(
                        user = user,
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}
