package com.example.data.repository

import com.example.BuildConfig
import com.example.domain.model.UserRole
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class GeminiMentorRepository {

    suspend fun sendPromptToMentor(
        userPrompt: String,
        userRole: UserRole,
        selectedWorldTitle: String,
        gradeLevelFa: String,
        historyMessages: List<Pair<String, String>> = emptyList()
    ): String = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Exception) {
            ""
        }

        val systemPrompt = """
            شما مربی و مشاور هوشمند آموزشی «تواتا - TAVANA Education» هستید.
            نام شما: مربی هوشمند توانا (Afarina AI Mentor).
            اطلاعات دانش‌آموز / کاربر:
            - نقش: ${userRole.titleFa}
            - دنیای آموزشی فعلی: $selectedWorldTitle
            - مقطع تحصیلی / سطح: $gradeLevelFa
            
            دستورالعمل‌ها:
            1. به زبان فارسی روان، محترمانه، انگیزشی و صمیمی پاسخ دهید.
            2. مانند یک چت‌بات عمومی رفتار نکنید؛ دقیقاً به عنوان یک مربی آموزشی ارشد توانا با توجه به مسیر یادگیری و نقاط ضعف/قوت کاربر راهنمایی کنید.
            3. پاسخ‌ها را با لایوت تمیز، بالت‌پوینت‌های مرتب و گام‌های عملی روشن ارائه دهید.
            4. اگر کاربر سوال تست کنکور یا برنامه‌نویسی مطرح کرد، پاسخ همراه با توضیح گام‌به‌گام و مثال باشد.
        """.trimIndent()

        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            // Generates rich structured offline smart mentor responses when API key is not supplied
            return@withContext generateOfflineMentorResponse(userPrompt, selectedWorldTitle)
        }

        try {
            val url = URL("https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey")
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8")
            conn.connectTimeout = 30000
            conn.readTimeout = 30000
            conn.doOutput = true

            val rootJson = JSONObject()

            // System Instruction
            val sysInstructionObj = JSONObject()
            val sysPartsArray = JSONArray()
            sysPartsArray.put(JSONObject().put("text", systemPrompt))
            sysInstructionObj.put("parts", sysPartsArray)
            rootJson.put("systemInstruction", sysInstructionObj)

            // Contents
            val contentsArray = JSONArray()
            for ((role, text) in historyMessages.takeLast(6)) {
                val contentObj = JSONObject()
                val roleStr = if (role == "USER") "user" else "model"
                contentObj.put("role", roleStr)
                val parts = JSONArray()
                parts.put(JSONObject().put("text", text))
                contentObj.put("parts", parts)
                contentsArray.put(contentObj)
            }

            // Current user turn
            val currentTurn = JSONObject()
            currentTurn.put("role", "user")
            val currentParts = JSONArray()
            currentParts.put(JSONObject().put("text", userPrompt))
            currentTurn.put("parts", currentParts)
            contentsArray.put(currentTurn)

            rootJson.put("contents", contentsArray)

            // Write output
            val os = conn.outputStream
            val writer = OutputStreamWriter(os, "UTF-8")
            writer.write(rootJson.toString())
            writer.flush()
            writer.close()

            val responseCode = conn.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val reader = BufferedReader(InputStreamReader(conn.inputStream, "UTF-8"))
                val responseStr = reader.readText()
                reader.close()

                val resJson = JSONObject(responseStr)
                val candidates = resJson.optJSONArray("candidates")
                if (candidates != null && candidates.length() > 0) {
                    val cand = candidates.getJSONObject(0)
                    val contentObj = cand.optJSONObject("content")
                    val partsArr = contentObj?.optJSONArray("parts")
                    if (partsArr != null && partsArr.length() > 0) {
                        return@withContext partsArr.getJSONObject(0).optString("text", "پاسخی دریافت نشد.")
                    }
                }
                "مربی هوشمند توانا در حال حاضر پاسخی تولید نکرد."
            } else {
                generateOfflineMentorResponse(userPrompt, selectedWorldTitle)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            generateOfflineMentorResponse(userPrompt, selectedWorldTitle)
        }
    }

    private fun generateOfflineMentorResponse(prompt: String, worldTitle: String): String {
        return when {
            prompt.contains("برنامه") || prompt.contains("مطالعه") -> """
                🎯 **برنامه پیشنهادی اختصاصی مربی توانا**
                
                با توجه به حضور شما در **$worldTitle**، این برنامه ۴ مرحله‌ای برای شما تنظیم شده است:
                
                1️⃣ **گام اول (بازیابی و سنجش):** روزانه ۳۰ دقیقه بررسی فرمول‌ها و مفاهیم کلیدی فصل.
                2️⃣ **گام دوم (حل مسئله و تست):** حل ۱۵ تست یا مسئله سناریومحور با تحلیل خطاهایتان.
                3️⃣ **گام سوم (پروژه عملی / تمرین تثبیت):** پیاده‌سازی یک پروژه کوچک جهت ثبت در گذرنامه مهارت.
                4️⃣ **گام چهارم (رفع اشکال هوشمند):** سوالات باقی‌مانده را مستقیم برای من بنویسید تا گام‌به‌گام تحلیل کنیم!
            """.trimIndent()

            prompt.contains("پروژه") || prompt.contains("پایتون") || prompt.contains("برنامه‌نویسی") -> """
                💻 **راهنمایی پروژه تخصصی توانا**
                
                برای اتمام موفق پروژه برنامه‌نویسی و ثبت آن در گذرنامه مهارت (Skill Passport):
                - **معماری:** کد خود را ماژولار و بر اساس اصول Clean Code بنویسید.
                - **پوشش خطا:** حتماً Exception Handling مناسب اضافه کنید.
                - **مستندسازی:** یک فایل README استاندارد به زبان فارسی شامل نحوه اجرا آماده کنید.
                - **ثبت در گذرنامه:** لینک گیت‌هاب را در داشبورد ثبت کنید تا سهم امتیاز XP شما محاسبه شود!
            """.trimIndent()

            prompt.contains("کنکور") || prompt.contains("تست") || prompt.contains("اشکال") -> """
                📚 **تحلیل نکتوی کنکور و رفع اشکال**
                
                در سوالات گزینه‌ای آزمون‌های سراسری:
                - ابتدا صورت سوال و کلمات کلیدی (مانند «همواره»، «به‌جز»، «نادرست») را نشانه‌گذاری کنید.
                - گزینه‌های کاملاً غلط را رد کنید تا شانس پاسخ صحیح به بالای ۵۰٪ برسد.
                - اگر نقطه ضعفی در این مبحث دارید، دوره بانک سوال اختصاصی پایه‌تان را در بخش آزمون‌ها مرور کنید.
            """.trimIndent()

            else -> """
                👋 **درود! من مربی و مشاور هوشمند شما در آکادمی توانا هستم.**
                
                من می‌توانم به شما در مواردی مثل:
                • طراحی برنامه درسی و راهبرد یادگیری
                • رفع اشکال دروس عمومی، کنکور و دوره‌های تخصصی
                • پیشنهاد پروژه برای ارتقای گذرنامه مهارت (Skill Passport)
                • تحلیل کارنامه و آزمون‌های آزمایشی
                
                کمک کنم. لطفاً سوال یا موضوع مد نظرتان را دقیق‌تر مطرح فرمایید!
            """.trimIndent()
        }
    }
}
