package com.example.data.repository

import com.example.R
import com.example.data.local.AppDatabase
import com.example.data.local.entity.AiChatMessageEntity
import com.example.data.local.entity.CourseEntity
import com.example.data.local.entity.EnrollmentEntity
import com.example.data.local.entity.ExamEntity
import com.example.data.local.entity.LessonEntity
import com.example.data.local.entity.ProjectEntity
import com.example.data.local.entity.SkillEntity
import com.example.data.local.entity.UserEntity
import com.example.domain.model.AcademyWorld
import com.example.domain.model.UserRole
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class TavanaRepository(private val db: AppDatabase) {

    val userFlow: Flow<UserEntity?> = db.userDao().getUserFlow()
    val allCoursesFlow: Flow<List<CourseEntity>> = db.courseDao().getAllCourses()
    val enrolledCoursesFlow: Flow<List<CourseEntity>> = db.courseDao().getEnrolledCourses()
    val userSkillsFlow: Flow<List<SkillEntity>> = db.skillDao().getUserSkills()
    val userProjectsFlow: Flow<List<ProjectEntity>> = db.projectDao().getUserProjects()
    val examsFlow: Flow<List<ExamEntity>> = db.examDao().getAllExams()
    val aiMessagesFlow: Flow<List<AiChatMessageEntity>> = db.aiChatDao().getAllChatMessages()

    fun getCoursesByWorld(world: AcademyWorld): Flow<List<CourseEntity>> {
        return db.courseDao().getCoursesByWorld(world.name)
    }

    fun getLessonsForCourse(courseId: String): Flow<List<LessonEntity>> {
        return db.lessonDao().getLessonsForCourse(courseId)
    }

    suspend fun updateRole(newRole: UserRole) {
        db.userDao().updateUserRole(newRole.name)
    }

    suspend fun updateSelectedWorld(newWorld: AcademyWorld) {
        db.userDao().updateSelectedWorld(newWorld.name)
    }

    suspend fun enrollInCourse(courseId: String) {
        db.courseDao().updateEnrollmentStatus(courseId, true)
        db.enrollmentDao().insertEnrollment(
            EnrollmentEntity(
                userId = "default_user_1",
                courseId = courseId,
                progressPercent = 10,
                completedLessonsCount = 1
            )
        )
        db.userDao().addXpPoints(150)
    }

    suspend fun submitProject(title: String, description: String, category: String, url: String) {
        val newProj = ProjectEntity(
            id = "proj_${System.currentTimeMillis()}",
            userId = "default_user_1",
            title = title,
            description = description,
            category = category,
            projectUrl = url,
            score = 90,
            statusText = "در حال ارزیابی منتور توانا",
            teacherFeedback = "پروژه دریافت شد و به زودی توسط منتور ارشد بررسی می‌شود."
        )
        db.projectDao().insertProject(newProj)
        db.userDao().addXpPoints(200)
    }

    suspend fun addSkill(skillName: String, category: String, level: Int) {
        val newSkill = SkillEntity(
            id = "skill_${System.currentTimeMillis()}",
            userId = "default_user_1",
            skillName = skillName,
            category = category,
            levelMax5 = level,
            isVerified = true
        )
        db.skillDao().insertSkill(newSkill)
        db.userDao().addXpPoints(100)
    }

    suspend fun saveAiChatMessage(role: String, text: String) {
        db.aiChatDao().insertMessage(
            AiChatMessageEntity(
                senderRole = role,
                messageText = text
            )
        )
    }

    suspend fun recordExamScore(examId: String, score: Int) {
        db.examDao().recordExamScore(examId, score)
        db.userDao().addXpPoints(120)
    }

    suspend fun seedInitialDataIfEmpty() {
        val currentUser = db.userDao().getUserOnce("default_user_1")
        if (currentUser == null) {
            db.userDao().insertOrUpdateUser(
                UserEntity(
                    id = "default_user_1",
                    fullName = "آرش محمدی",
                    email = "arash@tavana.edu",
                    roleName = UserRole.STUDENT.name,
                    selectedWorld = AcademyWorld.TAVANA_PROFESSIONAL.name,
                    xpPoints = 1850,
                    levelNumber = 5,
                    streakDays = 14,
                    totalCertificates = 2
                )
            )
        }

        val existingCourses = db.courseDao().getAllCourses().firstOrNull()
        if (existingCourses.isNullOrEmpty()) {
            val initialCourses = listOf(
                // TAVANA Professional Academy Courses
                CourseEntity(
                    id = "prof_python_ai",
                    title = "جامع پایتون و مهندسی هوش مصنوعی (AI)",
                    description = "یادگیری عمیق پایتون، یادگیری ماشین، مدل‌های زبان بزرگ (LLM) و ساخت پروژه‌های هوشمند کاربردی.",
                    category = "هوش مصنوعی و برنامه‌نویسی",
                    academyWorld = AcademyWorld.TAVANA_PROFESSIONAL.name,
                    instructorName = "دکتر رضا رضایی (عضو هیئت علمی توانا)",
                    rating = 4.9f,
                    reviewCount = 340,
                    priceToman = 1850000L,
                    discountPercent = 20,
                    isEnrolled = true,
                    imageBannerRes = R.drawable.hero_tavana_1786635401986,
                    totalLessons = 24,
                    durationHours = 32,
                    levelTitle = "مقدماتی تا پیشرفته",
                    tagsCsv = "Python,AI,Machine Learning,Gemini"
                ),
                CourseEntity(
                    id = "prof_android_kotlin",
                    title = "توسعه حرفه‌ای اپلیکیشن اندروید با کاتلین و جات‌پک کمپوز",
                    description = "معماری MVVM، دیزاین پترن‌ها، دیتابیس Room، اتصال به API و انتشار اپلیکیشن کافه بازار و گوگل پلی.",
                    category = "برنامه‌نویسی موبایل",
                    academyWorld = AcademyWorld.TAVANA_PROFESSIONAL.name,
                    instructorName = "مهندس سهراب حسینی",
                    rating = 4.8f,
                    reviewCount = 210,
                    priceToman = 1600000L,
                    discountPercent = 15,
                    isEnrolled = false,
                    imageBannerRes = R.drawable.tavana_icon_1786635357393,
                    totalLessons = 20,
                    durationHours = 28,
                    levelTitle = "متوسط تا پروژه محور",
                    tagsCsv = "Kotlin,Android,Compose,Room"
                ),
                CourseEntity(
                    id = "prof_fullstack_next",
                    title = "طراحی و توسعه وب فول‌استک با Next.js & React",
                    description = "پیاده‌سازی فرانت‌اند مدرن، سرور لس، دیتابیس‌های ابری و ساخت پلتفرم‌های مقیاس‌پذیر آموزشی و فروشگاهی.",
                    category = "توسعه وب",
                    academyWorld = AcademyWorld.TAVANA_PROFESSIONAL.name,
                    instructorName = "مهندس مریم کاظمی",
                    rating = 4.9f,
                    reviewCount = 185,
                    priceToman = 1950000L,
                    discountPercent = 10,
                    isEnrolled = true,
                    imageBannerRes = R.drawable.hero_tavana_1786635401986,
                    totalLessons = 26,
                    durationHours = 36,
                    levelTitle = "پیشرفته",
                    tagsCsv = "Next.js,TypeScript,Tailwind,Node"
                ),
                CourseEntity(
                    id = "prof_cyber_security",
                    title = "امنیت سایبری و تست نفوذ (Ethical Hacking)",
                    description = "اصول امنیت شبکه، تحلیل آسیب‌پذیری وب، تست نفوذ اخلاقی و ایمن‌سازی سیستم‌های نرم‌افزاری.",
                    category = "امنیت سایبری",
                    academyWorld = AcademyWorld.TAVANA_PROFESSIONAL.name,
                    instructorName = "دکتر کامران شریفی",
                    rating = 4.7f,
                    reviewCount = 142,
                    priceToman = 2100000L,
                    discountPercent = 25,
                    isEnrolled = false,
                    imageBannerRes = R.drawable.hero_tavana_1786635401986,
                    totalLessons = 18,
                    durationHours = 24,
                    levelTitle = "حرفه‌ای",
                    tagsCsv = "Security,Pentest,Network,Linux"
                ),
                CourseEntity(
                    id = "prof_freelance_biz",
                    title = "فریلنسینگ بین‌المللی، کسب‌وکار و کارآفرینی دیجیتال",
                    description = "استراتژی گرفتن پروژه‌های ارزی، ساخت پورتفولیو رزومه‌ساز، اصول قیمت‌گذاری و مذاکره با کارفرما.",
                    category = "کسب‌وکار و فریلنسینگ",
                    academyWorld = AcademyWorld.TAVANA_PROFESSIONAL.name,
                    instructorName = "استاد علی اکبرزاده",
                    rating = 4.9f,
                    reviewCount = 295,
                    priceToman = 1200000L,
                    discountPercent = 30,
                    isEnrolled = false,
                    imageBannerRes = R.drawable.hero_tavana_1786635401986,
                    totalLessons = 15,
                    durationHours = 18,
                    levelTitle = "شغلی و مهارتی",
                    tagsCsv = "Freelancing,Business,Management"
                ),

                // General Education & Konkur Academy Courses
                CourseEntity(
                    id = "gen_math_konkur",
                    title = "جامع ریاضیات کنکور سراسری ۱۰۰٪ (پایه ۱۰، ۱۱، ۱۲)",
                    description = "آموزش کامل مفاهیم، تکنیک‌های تست‌زنی فوق‌سریع، تحلیل سوالات کنکور ۱۰ سال اخیر و آزمون جامع.",
                    category = "ریاضی و فیزیک",
                    academyWorld = AcademyWorld.GENERAL_AND_KONKUR.name,
                    gradeLevel = "KONKUR",
                    instructorName = "استاد محمدامین خسروی",
                    rating = 4.9f,
                    reviewCount = 520,
                    priceToman = 1400000L,
                    discountPercent = 25,
                    isEnrolled = true,
                    imageBannerRes = R.drawable.hero_konkur_1786635416518,
                    totalLessons = 30,
                    durationHours = 45,
                    levelTitle = "کنکوری و نهایی",
                    tagsCsv = "ریاضی,کنکور,تست,دوازدهم"
                ),
                CourseEntity(
                    id = "gen_physics_exp",
                    title = "فیزیک جامع کنکور تجربی و ریاضی (مفهومی و تست)",
                    description = "بررسی خط به خط کتاب درسی، حل ۵۰۰ تست نشان‌دار، تکنیک‌های محاسبات سریع و تحلیل اشکالات متداول.",
                    category = "علوم تجربی",
                    academyWorld = AcademyWorld.GENERAL_AND_KONKUR.name,
                    gradeLevel = "KONKUR",
                    instructorName = "دکتر بهنام مرادی",
                    rating = 4.8f,
                    reviewCount = 380,
                    priceToman = 1350000L,
                    discountPercent = 20,
                    isEnrolled = false,
                    imageBannerRes = R.drawable.hero_konkur_1786635416518,
                    totalLessons = 25,
                    durationHours = 38,
                    levelTitle = "کنکور تخصصی",
                    tagsCsv = "فیزیک,تجربی,کنکور,تست"
                ),
                CourseEntity(
                    id = "gen_biology_12",
                    title = "زیست‌شناسی پایه دوازدهم و جمع‌بندی کنکور",
                    description = "شکافتن انیمیشن‌های ترکیبی زیست، شکل‌های مهم کتاب درسی، ژنتیک و آزمون‌های شبیه‌ساز.",
                    category = "علوم تجربی",
                    academyWorld = AcademyWorld.GENERAL_AND_KONKUR.name,
                    gradeLevel = "GRADE_12",
                    instructorName = "دکتر سارا احمدی",
                    rating = 4.9f,
                    reviewCount = 490,
                    priceToman = 1500000L,
                    discountPercent = 15,
                    isEnrolled = false,
                    imageBannerRes = R.drawable.hero_konkur_1786635416518,
                    totalLessons = 28,
                    durationHours = 40,
                    levelTitle = "پایه دوازدهم و کنکور",
                    tagsCsv = "زیست,تجربی,ژنتیک,امتحانات نهایی"
                ),
                CourseEntity(
                    id = "gen_grade9_all",
                    title = "جامع دروس پایه نهم (آمادگی آزمون تیزهوشان و نمونه دولتی)",
                    description = "تقویت دروس ریاضی، علوم، فارسی و عربی پایه نهم جهت قبولی در مدارس برتر و هدایت تحصیلی.",
                    category = "متوسطه اول",
                    academyWorld = AcademyWorld.GENERAL_AND_KONKUR.name,
                    gradeLevel = "GRADE_9",
                    instructorName = "تیم اساتید متوسطه توانا",
                    rating = 4.8f,
                    reviewCount = 195,
                    priceToman = 980000L,
                    discountPercent = 30,
                    isEnrolled = false,
                    imageBannerRes = R.drawable.hero_konkur_1786635416518,
                    totalLessons = 22,
                    durationHours = 30,
                    levelTitle = "پایه نهم / تیزهوشان",
                    tagsCsv = "نهم,تیزهوشان,علوم,ریاضی"
                )
            )
            db.courseDao().insertCourses(initialCourses)

            // Seed Lessons
            val initialLessons = listOf(
                LessonEntity(
                    id = "les_py_1",
                    courseId = "prof_python_ai",
                    sectionTitle = "بخش ۱: مبانی پایتون و تفکر الگوریتمی",
                    title = "۱. نصب محیط ساختاریافته و آشنایی با متغیرها",
                    durationMinutes = 22,
                    isCompleted = true,
                    isFreePreview = true,
                    summaryText = "آشنایی با پایتون ۳.۱۲، ساختارهای داده اولیه، متغیرها و اصول برنامه‌نویسی تمیز."
                ),
                LessonEntity(
                    id = "les_py_2",
                    courseId = "prof_python_ai",
                    sectionTitle = "بخش ۱: مبانی پایتون و تفکر الگوریتمی",
                    title = "۲. توابع، حلقه و ساختار شرطی همراه پروژه کوچک",
                    durationMinutes = 28,
                    isCompleted = true,
                    isFreePreview = false,
                    summaryText = "نحوه تعریف توابع، دستورات شرطی و پروژه‌محور ساخت ماشین‌حساب هوشمند."
                ),
                LessonEntity(
                    id = "les_py_3",
                    courseId = "prof_python_ai",
                    sectionTitle = "بخش ۲: هوش مصنوعی و مدل‌های Gemini",
                    title = "۳. اتصال به API هوش مصنوعی Gemini و پردازش متن",
                    durationMinutes = 35,
                    isCompleted = false,
                    isFreePreview = false,
                    summaryText = "تزریق کلید API، ارسال پرامپت، ساخت دستیار پاسخگو با مدل‌های سری Gemini."
                ),
                LessonEntity(
                    id = "les_math_1",
                    courseId = "gen_math_konkur",
                    sectionTitle = "فصل ۱: تابع و حد کنکور",
                    title = "۱. تحلیل دامنه‌ها، ترکیب توابع و تست‌های پرتکرار",
                    durationMinutes = 30,
                    isCompleted = true,
                    isFreePreview = true,
                    summaryText = "روش‌های رد گزینه و حل میان‌بر تست‌های تابع سراسری."
                ),
                LessonEntity(
                    id = "les_math_2",
                    courseId = "gen_math_konkur",
                    sectionTitle = "فصل ۱: تابع و حد کنکور",
                    title = "۲. محاسبه حد و رفع ابهام هم‌ارزی‌های سریع",
                    durationMinutes = 26,
                    isCompleted = false,
                    isFreePreview = false,
                    summaryText = "تکنیک‌های رفع ابهام صفر بر صفر و بی‌نهایت در فرمول‌های حد کنکور."
                )
            )
            db.lessonDao().insertLessons(initialLessons)

            // Seed Initial Skills
            val initialSkills = listOf(
                SkillEntity(
                    id = "s_python",
                    userId = "default_user_1",
                    skillName = "برنامه‌نویسی پایتون (Python)",
                    category = "توسعه نرم‌افزار",
                    levelMax5 = 4,
                    isVerified = true,
                    verifiedBy = "منتور ارشد اکوسیستم توانا"
                ),
                SkillEntity(
                    id = "s_ai",
                    userId = "default_user_1",
                    skillName = "هوش مصنوعی و Gemini API",
                    category = "هوش مصنوعی",
                    levelMax5 = 3,
                    isVerified = true,
                    verifiedBy = "آکادمی تخصصی توانا"
                ),
                SkillEntity(
                    id = "s_math",
                    userId = "default_user_1",
                    skillName = "ریاضیات و تست‌زنی کنکور",
                    category = "آموزش عمومی",
                    levelMax5 = 4,
                    isVerified = true,
                    verifiedBy = "دپارتمان عمومی و کنکور"
                )
            )
            for (sk in initialSkills) db.skillDao().insertSkill(sk)

            // Seed Initial Projects
            val initialProjects = listOf(
                ProjectEntity(
                    id = "p_1",
                    userId = "default_user_1",
                    title = "دستیار هوشمند برنامه‌ریزی تحصیلی با پایتون",
                    description = "طراحی اپلیکیشن CLI با قابلیت اتصال به API مربی هوش مصنوعی و تحلیل ساعات مطالعه روزانه.",
                    category = "پروژه هوش مصنوعی",
                    projectUrl = "https://github.com/tavana-student/ai-study-assistant",
                    score = 96,
                    statusText = "تایید شده - دریافت ۵۰ امتیاز XP",
                    teacherFeedback = "ساختار کد بسیار تمیز است. پیشنهاد می‌شود رابط گرافیکی نیز به آن اضافه گردد."
                )
            )
            for (pr in initialProjects) db.projectDao().insertProject(pr)

            // Seed Mock Exams
            val initialExams = listOf(
                ExamEntity(
                    id = "ex_konkur_1",
                    title = "آزمون شبیه‌ساز جامع کنکور تجربی (مرحله ۱)",
                    gradeLevel = "KONKUR",
                    subjectName = "زیست، شیمی، فیزیک، ریاضی",
                    durationMinutes = 120,
                    questionCount = 60,
                    difficultyLevel = "سخت کنکوری",
                    isAttempted = true,
                    lastScorePercent = 78
                ),
                ExamEntity(
                    id = "ex_konkur_2",
                    title = "آزمون مبحثی ریاضیات کنکور - حد و پیوستگی",
                    gradeLevel = "KONKUR",
                    subjectName = "ریاضیات تخصصی",
                    durationMinutes = 40,
                    questionCount = 20,
                    difficultyLevel = "متوسط تا سخت",
                    isAttempted = false,
                    lastScorePercent = 0
                ),
                ExamEntity(
                    id = "ex_grade9_1",
                    title = "آزمون جامع هماهنگ تیزهوشان نهم",
                    gradeLevel = "GRADE_9",
                    subjectName = "علوم و ریاضی نهم",
                    durationMinutes = 60,
                    questionCount = 30,
                    difficultyLevel = "سطح پیشرفته",
                    isAttempted = false,
                    lastScorePercent = 0
                )
            )
            db.examDao().insertExams(initialExams)

            // Seed Initial AI Chat Welcome
            db.aiChatDao().insertMessage(
                AiChatMessageEntity(
                    senderRole = "AI",
                    messageText = "سلام آرش عزیز! به آکادمی توانا خوش آمدی. من مربی و مشاور هوشمند تحصیلی تو هستم. چطور می‌توانم در یادگیری دوره‌ها یا برنامه‌ریزی کنکور و پروژه‌ها به تو کمک کنم؟"
                )
            )
        }
    }
}
