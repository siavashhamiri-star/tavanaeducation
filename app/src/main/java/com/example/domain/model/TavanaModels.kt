package com.example.domain.model

enum class UserRole(val titleFa: String, val descriptionFa: String) {
    STUDENT("دانش‌آموز / دانشجو", "یادگیری دوره‌ها، ثبت پروژه و شرکت در آزمون‌ها"),
    PROFESSIONAL_STUDENT("دانشجوی تخصصی توانا", "عضو اکوسیستم تخصصی توانا و مسیرهای مهارت‌محور"),
    MENTOR("منتور تحصیلی و شغلی", "راهنمایی دانشجویان و ارزیابی پروژه‌های مهارتی"),
    TEACHER("استاد / مدرس مستقل", "ایجاد و فروش دوره، برگزاری کلاس زنده و کسب درآمد"),
    ADMIN("مدیر ارشد پلتفرم", "مدیریت کاربران، دوره‌ها، پورسانت‌ها و تنظیمات سیستم")
}

enum class AcademyWorld(val titleFa: String, val subtitleFa: String) {
    TAVANA_PROFESSIONAL(
        "آکادمی تخصصی توانا",
        "توسعه مهارت‌های حرفه‌ای، هوش مصنوعی، برنامه‌نویسی، کسب‌وکار و بازاریابی"
    ),
    GENERAL_AND_KONKUR(
        "آموزش عمومی و کنکور",
        "پایه‌های هفتم تا دوازدهم، بانک سوال، آزمون‌های آزمایشی و مشاوره کنکور"
    )
}

enum class SchoolGrade(val titleFa: String, val categoryFa: String) {
    GRADE_7("پایه هفتم", "متوسطه اول"),
    GRADE_8("پایه هشتم", "متوسطه اول"),
    GRADE_9("پایه نهم", "متوسطه اول"),
    GRADE_10("پایه دهم", "متوسطه دوم"),
    GRADE_11("پایه یازدهم", "متوسطه دوم"),
    GRADE_12("پایه دوازدهم", "متوسطه دوم"),
    KONKUR("کنکور سراسری", "آزمون ورودی دانشگاه‌ها")
}

enum class AcademicField(val titleFa: String) {
    MATHEMATICS("ریاضی و فیزیک"),
    EXPERIMENTAL_SCIENCES("علوم تجربی"),
    HUMANITIES("علوم انسانی"),
    TECHNICAL_VOCATIONAL("فنی و حرفه‌ای / کاردانش")
}

data class LearningStep(
    val stepNumber: Int,
    val titleFa: String,
    val descriptionFa: String,
    val isCompleted: Boolean = false,
    val isCurrent: Boolean = false
)

data class LearningPathModel(
    val id: String,
    val titleFa: String,
    val world: AcademyWorld,
    val categoryFa: String,
    val iconName: String,
    val totalCourses: Int,
    val estimatedWeeks: Int,
    val steps: List<LearningStep>
)
