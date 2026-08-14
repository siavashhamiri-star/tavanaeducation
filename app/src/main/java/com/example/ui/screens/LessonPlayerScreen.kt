package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.LessonEntity
import com.example.ui.TavanaViewModel
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonPlayerScreen(
    lesson: LessonEntity?,
    viewModel: TavanaViewModel,
    onBackClick: () -> Unit,
    onOpenAiMentorWithPrompt: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (lesson == null) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("ویدئویی انتخاب نشده است.")
        }
        return
    }

    var isPlaying by remember { mutableStateOf(true) }
    var sliderPosition by remember { mutableFloatStateOf(0.35f) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = lesson.title, fontSize = 15.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowForward, contentDescription = "بازگشت")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = TavanaNavy,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Simulated Video Player Container
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp)
                    .background(Color.Black)
            ) {
                // Video Screen Canvas Simulation
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IconButton(
                        onClick = { isPlaying = !isPlaying },
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(TavanaCyan.copy(alpha = 0.8f))
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (isPlaying) "در حال پخش کیفیت FHD (1080p)..." else "ویدیو متوقف شد",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 12.sp
                    )
                }

                // Bottom Timeline Controls
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .background(Color.Black.copy(alpha = 0.7f))
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Slider(
                        value = sliderPosition,
                        onValueChange = { sliderPosition = it },
                        colors = SliderDefaults.colors(
                            thumbColor = TavanaCyan,
                            activeTrackColor = TavanaCyan,
                            inactiveTrackColor = Color.White.copy(alpha = 0.3f)
                        )
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "08:24 / ${lesson.durationMinutes}:00",
                            color = Color.White,
                            fontSize = 11.sp
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Speed, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("1.25x", color = Color.White, fontSize = 11.sp)
                            Spacer(modifier = Modifier.width(12.dp))
                            Icon(Icons.Default.Fullscreen, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                        }
                    }
                }
            }

            // Lesson Details and AI Doubt Solver Tab
            val lang by viewModel.selectedLanguage.collectAsState()
            val isCaptions by viewModel.isCaptionsEnabled.collectAsState()
            val isScreenReader by viewModel.isScreenReaderEnabled.collectAsState()
            val isEn = lang == "en"

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Closed Captions Bar for Deaf/Hard-of-hearing users
                if (isCaptions) {
                    item {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp),
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFF1E293B),
                            border = androidx.compose.foundation.BorderStroke(1.dp, TavanaCyan)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.ClosedCaption, contentDescription = null, tint = TavanaCyan, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (isEn) "LIVE CAPTIONS & AUDIO CUES (Deaf-Friendly)" else "زیرنویس همزمان و علائم بصری (ویژه کم‌شنوایان)",
                                        color = TavanaCyan,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = if (isEn)
                                        "[Audio: Instructor speaks with background tone] \"In this section of ${lesson.title}, we analyze key architectural principles step by step.\""
                                    else
                                        "[صدا: لحن گویای مدرس] «در این بخش از درس ${lesson.title}، تکنیک‌های اصلی و نکات تست‌زنی را گام‌به‌گام تحلیل می‌کنیم.»",
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    lineHeight = 18.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }

                // TalkBack Screen Reader Button for Blind users
                if (isScreenReader) {
                    item {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp)
                                .clickable {
                                    viewModel.speakAccessibilityText("درس: ${lesson.title}. بخش: ${lesson.sectionTitle}. خلاصه: ${lesson.summaryText}")
                                },
                            shape = RoundedCornerShape(12.dp),
                            color = TavanaEmerald.copy(alpha = 0.2f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, TavanaEmerald)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.RecordVoiceOver, contentDescription = null, tint = TavanaEmerald, modifier = Modifier.size(24.dp))
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = if (isEn) "Read Lesson Aloud (Blind / Voice Narrator)" else "قرائت صوتی و گوینده متن درس (ویژه نابینایان)",
                                        color = TavanaEmerald,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                    Text(
                                        text = if (isEn) "Tap to listen to full lesson summary" else "برای لمس و شنیدن خلاصه صوتی درس اینجا کلیک کنید",
                                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                                        fontSize = 10.sp
                                    )
                                }
                            }
                        }
                    }
                }

                item {
                    Text(
                        text = lesson.sectionTitle,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = lesson.title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                // AI Mentor Instant Helper Box
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = TavanaNavyLight)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = TavanaGold,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "رفع اشکال هوشمند این جلسه با مربی AI",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "آیا در مفهوم این جلسه اشکالی دارید یا می‌خواهید تست تمرینی بسازید؟",
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(
                                onClick = {
                                    onOpenAiMentorWithPrompt("مربی عزیز، درباره مبحث «${lesson.title}» برام کدهای نمونه و ۳ تست همراه پاسخ کلیدی بساز.")
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = TavanaCyan),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("پرسش از مربی هوشمند", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                // Lesson Summary Notes and Downloadable Pamphlet for Special Needs / Offline Learners
                item {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (isEn) "Lesson Pamphlet & Summary Notes:" else "خلاصه نکات و جزوه متنی این جلسه:",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = lesson.summaryText.ifBlank { "در این جلسه مفاهیم پایه، نکات کنکوری و تست‌های کاربردی پوشش داده شده است." },
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                        lineHeight = 22.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Downloadable Pamphlet Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.MenuBook, contentDescription = null, tint = TavanaCyan)
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = if (isEn) "Download PDF Pamphlet & Braille / Text Guide" else "دانلود جزوه متنی کامل (مناسب کم‌شنوایان و روشندلان)",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                    Text(
                                        text = if (isEn) "100% Free text materials & study guides" else "جزوه کامل متنی و قابل چاپ ۱۰۰٪ رایگان جهت مطالعه آسان",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurface.copy(0.6f)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Button(
                                    onClick = {
                                        viewModel.speakAccessibilityText("دانلود جزوه متنی جلسه با موفقیت انجام شد.")
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = TavanaNavy),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(if (isEn) "PDF Pamphlet" else "دانلود جزوه PDF", fontSize = 11.sp)
                                }

                                OutlinedButton(
                                    onClick = {
                                        viewModel.speakAccessibilityText("سفارش کتابچه متنی رایگان برای شما ثبت شد.")
                                    },
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(Icons.Default.VolunteerActivism, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(if (isEn) "Request Print" else "سفارش چاپی رایگان", fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
