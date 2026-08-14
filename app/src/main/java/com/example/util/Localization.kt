package com.example.util

object Localization {

    fun getString(key: String, lang: String): String {
        val isEn = lang.lowercase() == "en"
        return when (key) {
            // App & Navigation
            "app_title" -> if (isEn) "TAVANA Education" else "آموزش توانا"
            "app_subtitle" -> if (isEn) "Comprehensive Academy & AI Mentor Ecosystem" else "اکوسیستم جامع آکادمی تخصصی و کنکور با مربی AI"
            "nav_home" -> if (isEn) "Home" else "خانه"
            "nav_professional" -> if (isEn) "Professional" else "تخصصی"
            "nav_general" -> if (isEn) "General & Konkur" else "عمومی و کنکور"
            "nav_ai_mentor" -> if (isEn) "AI Mentor" else "مربی AI"
            "nav_passport" -> if (isEn) "Skill Passport" else "گذرنامه مهارت"
            "nav_exams" -> if (isEn) "Exams" else "آزمون‌ها"
            "nav_teacher" -> if (isEn) "Teacher Panel" else "پنل مدرس"
            "nav_admin" -> if (isEn) "Admin Panel" else "پنل مدیریت"
            "nav_profile" -> if (isEn) "Profile" else "پروفایل"

            // Language & Accessibility
            "language_select" -> if (isEn) "Language / زبان" else "انتخاب زبان / Language"
            "lang_fa" -> if (isEn) "Persian (فارسی)" else "فارسی (RTL)"
            "lang_en" -> if (isEn) "English (LTR)" else "English (انگلیسی)"
            "accessibility_settings" -> if (isEn) "Accessibility Options" else "تنظیمات دسترس‌پذیری معلولین"
            "acc_high_contrast" -> if (isEn) "High Contrast Mode (Low Vision)" else "حالت کنتراست بالا (ویژه کم‌بینایان)"
            "acc_large_text" -> if (isEn) "Large Font Size" else "درشت‌نمایی متون (درشت‌خط)"
            "acc_screen_reader" -> if (isEn) "Voice Reader / TalkBack Narrator" else "گوینده صوتی و متن‌خوان (ویژه نابینایان)"
            "acc_captions" -> if (isEn) "Closed Captions / Visual Cues" else "زیرنویس و علائم بصری (ویژه کم‌شنوایان)"
            "acc_mode_title" -> if (isEn) "Inclusion & Accessibility Suite" else "سامانه ویژه کم‌بینایان، کم‌شنوایان و نابینایان"
            "acc_narrator_active" -> if (isEn) "Voice Narrator is Active" else "گوینده صوتی فعال است"

            // Auth & Registration
            "auth_title" -> if (isEn) "Sign In / Register" else "ورود / ثبت‌نام در توانا"
            "auth_sub" -> if (isEn) "Choose your preferred method to sign in" else "روش دلخواه خود را برای ورود انتخاب کنید"
            "by_phone" -> if (isEn) "Phone Number" else "ثبت‌نام با شماره تلفن"
            "by_email" -> if (isEn) "Email Address" else "ثبت‌نام با ایمیل"
            "phone_number" -> if (isEn) "Mobile Phone Number" else "شماره تلفن همراه"
            "phone_placeholder" -> if (isEn) "e.g. +98 912 345 6789" else "مانند ۰۹۱۲۳۴۵۶۷۸۹"
            "otp_code" -> if (isEn) "SMS Verification Code" else "کد تایید پیامک‌شده"
            "send_otp" -> if (isEn) "Send Verification SMS" else "ارسال کد تایید پیامکی"
            "confirm_otp" -> if (isEn) "Verify & Sign In" else "تایید و ورود به حساب"
            "email_address" -> if (isEn) "Email Address" else "نشانی ایمیل"
            "password" -> if (isEn) "Password" else "کلمه عبور"
            "full_name" -> if (isEn) "Full Name" else "نام و نام خانوادگی"
            "register_btn" -> if (isEn) "Complete Registration" else "تکمیل ثبت‌نام"
            "login_success" -> if (isEn) "Successfully signed in!" else "ثبت‌نام و ورود با موفقیت انجام شد!"

            // Search & Filters
            "search_hint" -> if (isEn) "Search courses, instructors, lessons..." else "جستجو در دوره‌ها، مدرسین و دروس..."
            "skill_level" -> if (isEn) "Skill Level" else "سطح مهارتی"
            "streak" -> if (isEn) "Study Streak" else "زنجیره مطالعه"
            "certificates" -> if (isEn) "Certificates" else "گواهی‌نامه‌ها"
            "days" -> if (isEn) "Days" else "روز"
            "level" -> if (isEn) "Level" else "سطح"
            "free" -> if (isEn) "Free" else "رایگان"
            "toman" -> if (isEn) "Toman" else "تومان"
            "enrolled" -> if (isEn) "Enrolled" else "ثبت‌نام شده"
            "enroll_now" -> if (isEn) "Enroll Now" else "ثبت‌نام در این دوره"

            // Course & Lesson
            "quick_actions" -> if (isEn) "Quick Access Shortcuts" else "دسترسی‌های سریع سیستم"
            "ai_mentor_title" -> if (isEn) "AI Mentor" else "مربی هوش مصنوعی"
            "ai_mentor_sub" -> if (isEn) "Smart Tutoring & Advice" else "رفع اشکال و مشاوره"
            "passport_title" -> if (isEn) "Skill Passport" else "گذرنامه مهارت"
            "exams_title" -> if (isEn) "Question Bank & Exams" else "بانک سوال و آزمون"
            "exams_sub" -> if (isEn) "Konkur Simulator" else "شبیه‌ساز کنکور"
            "caption_label" -> if (isEn) "Subtitles (Deaf Friendly)" else "زیرنویس همزمان (ویژه کم‌شنوایان)"
            "audio_reader_play" -> if (isEn) "Read Lesson Aloud" else "قرائت صوتی درس (ویژه نابینایان)"

            else -> key
        }
    }
}
