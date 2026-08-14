package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String = "default_user_1",
    val fullName: String = "آرش محمدی",
    val email: String = "arash@tavana.edu",
    val roleName: String = "STUDENT",
    val selectedWorld: String = "TAVANA_PROFESSIONAL",
    val gradeLevel: String = "KONKUR",
    val fieldOfStudy: String = "EXPERIMENTAL_SCIENCES",
    val xpPoints: Int = 1450,
    val levelNumber: Int = 4,
    val streakDays: Int = 12,
    val totalCertificates: Int = 3,
    val avatarUrl: String = "",
    val isInclusiveBenefactor: Boolean = true,
    val hasFullScholarship: Boolean = true,
    val disabilityCategory: String = "روشندل / کم‌شنوا / کم‌توان حسی و حرکتی",
    val employmentPriorityBadge: String = "دارای اولویت ویژه استخدام و معافیت ۱۰۰٪ از شهریه آموزش"
)

@Entity(tableName = "courses")
data class CourseEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val category: String,
    val academyWorld: String, // TAVANA_PROFESSIONAL or GENERAL_AND_KONKUR
    val gradeLevel: String = "", // e.g. GRADE_12, KONKUR
    val instructorName: String,
    val rating: Float = 4.8f,
    val reviewCount: Int = 124,
    val priceToman: Long = 0L, // 0 for free
    val discountPercent: Int = 0,
    val isEnrolled: Boolean = false,
    val imageBannerRes: Int = 0,
    val totalLessons: Int = 18,
    val durationHours: Int = 12,
    val levelTitle: String = "متوسط تا پیشرفته",
    val tagsCsv: String = "برنامه‌نویسی,هوش مصنوعی"
)

@Entity(tableName = "lessons")
data class LessonEntity(
    @PrimaryKey val id: String,
    val courseId: String,
    val sectionTitle: String,
    val title: String,
    val durationMinutes: Int,
    val videoUrl: String = "",
    val isCompleted: Boolean = false,
    val isFreePreview: Boolean = false,
    val summaryText: String = ""
)

@Entity(tableName = "enrollments")
data class EnrollmentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: String,
    val courseId: String,
    val progressPercent: Int = 0,
    val completedLessonsCount: Int = 0,
    val lastAccessedTime: Long = System.currentTimeMillis()
)

@Entity(tableName = "skills")
data class SkillEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val skillName: String,
    val category: String,
    val levelMax5: Int = 3,
    val isVerified: Boolean = true,
    val verifiedBy: String = "منتور ارشد آکادمی توانا"
)

@Entity(tableName = "projects")
data class ProjectEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val title: String,
    val description: String,
    val category: String,
    val projectUrl: String = "",
    val score: Int = 95,
    val statusText: String = "تایید شده و افزوده‌شده به گذرنامه",
    val teacherFeedback: String = "پروژه بسیار تمیز با معماری استاندارد و کدنویسی بهینه."
)

@Entity(tableName = "exams")
data class ExamEntity(
    @PrimaryKey val id: String,
    val title: String,
    val gradeLevel: String,
    val subjectName: String,
    val durationMinutes: Int = 45,
    val questionCount: Int = 20,
    val difficultyLevel: String = "سخت (کنکوری)",
    val isAttempted: Boolean = false,
    val lastScorePercent: Int = 0
)

@Entity(tableName = "ai_chats")
data class AiChatMessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val senderRole: String, // "USER" or "AI"
    val messageText: String,
    val timestamp: Long = System.currentTimeMillis()
)
