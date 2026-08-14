package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.entity.CourseEntity
import com.example.data.local.entity.UserEntity
import com.example.domain.model.AcademyWorld
import com.example.ui.TavanaViewModel
import com.example.ui.components.CourseCard
import com.example.ui.components.StatCard
import com.example.ui.components.WorldSwitcherHeader
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: TavanaViewModel,
    user: UserEntity?,
    courses: List<CourseEntity>,
    selectedWorld: AcademyWorld,
    onNavigateToCourse: (CourseEntity) -> Unit,
    onNavigateToAiMentor: () -> Unit,
    onNavigateToSkillPassport: () -> Unit,
    onNavigateToExams: () -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var showAuthModal by remember { mutableStateOf(false) }
    var showAccModal by remember { mutableStateOf(false) }

    val lang by viewModel.selectedLanguage.collectAsState()
    val isHighContrast by viewModel.isHighContrastMode.collectAsState()
    val isLargeText by viewModel.isLargeTextMode.collectAsState()
    val isScreenReader by viewModel.isScreenReaderEnabled.collectAsState()
    val isCaptions by viewModel.isCaptionsEnabled.collectAsState()

    val isEn = lang == "en"

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // Top Header
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = TavanaNavy
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(id = R.drawable.tavana_icon_1786635357393),
                                contentDescription = "Tavana Icon",
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = if (isEn) "TAVANA Education" else "آموزش توانا",
                                    color = Color.White,
                                    fontSize = if (isLargeText) 20.sp else 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = if (isEn) "Academy & AI Mentor" else "اکوسیستم آکادمی و مربی AI",
                                    color = TavanaGold,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            // Accessibility Modal Button
                            IconButton(
                                onClick = { showAccModal = true },
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(TavanaCyanDark.copy(alpha = 0.4f))
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AccessibilityNew,
                                    contentDescription = "Accessibility",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            // Register / Phone & Email Auth Button
                            IconButton(
                                onClick = { showAuthModal = true },
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(TavanaGold.copy(alpha = 0.3f))
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PersonAdd,
                                    contentDescription = "Register",
                                    tint = TavanaGold,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = TavanaCyanDark.copy(alpha = 0.3f),
                                border = androidx.compose.foundation.BorderStroke(1.dp, TavanaCyan)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Bolt,
                                        contentDescription = null,
                                        tint = TavanaGold,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Text(
                                        text = "${user?.xpPoints ?: 1850} XP",
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Search Bar
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = {
                            searchQuery = it
                            viewModel.searchQuery.value = it
                        },
                        placeholder = {
                            Text(
                                if (isEn) "Search courses, instructors, lessons..." else "جستجو در دوره‌ها، مدرسین و دروس...",
                                color = Color.White.copy(alpha = 0.6f),
                                fontSize = 13.sp
                            )
                        },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = TavanaCyan) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = TavanaNavyLight,
                            unfocusedContainerColor = TavanaNavyLight,
                            disabledContainerColor = TavanaNavyLight,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedIndicatorColor = TavanaCyan,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        singleLine = true
                    )
                }
            }
        }

        // Two Main Worlds Switcher
        item {
            WorldSwitcherHeader(
                selectedWorld = selectedWorld,
                onWorldSelected = { viewModel.selectWorld(it) }
            )
        }

        // Stats Bar
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatCard(
                    title = "سطح مهارتی",
                    value = "سطح ${user?.levelNumber ?: 5}",
                    icon = Icons.Default.MilitaryTech,
                    iconColor = TavanaGold,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "زنجیره مطالعه",
                    value = "${user?.streakDays ?: 14} روز",
                    icon = Icons.Default.LocalFireDepartment,
                    iconColor = Color(0xFFEF4444),
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "گواهی‌نامه‌ها",
                    value = "${user?.totalCertificates ?: 2} مدرک",
                    icon = Icons.Default.Verified,
                    iconColor = TavanaEmerald,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Hero Banner
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(160.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(TavanaNavy, TavanaCyanDark)
                        )
                    )
            ) {
                val bannerRes = if (selectedWorld == AcademyWorld.TAVANA_PROFESSIONAL) R.drawable.hero_tavana_1786635401986 else R.drawable.hero_konkur_1786635416518
                Image(
                    painter = painterResource(id = bannerRes),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color.Transparent, TavanaNavy.copy(alpha = 0.9f))
                            )
                        )
                )

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = TavanaGold
                    ) {
                        Text(
                            text = if (selectedWorld == AcademyWorld.TAVANA_PROFESSIONAL) "مسیر تخصصی اشتغال" else "برنامه طلایی کنکور",
                            color = Color.Black,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (selectedWorld == AcademyWorld.TAVANA_PROFESSIONAL) "پروژه محور از صفر تا ورود به بازار کار" else "بانک سوال و آزمون‌های شبیه‌ساز کنکور سراسری",
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Benefactor Inclusive Empowerment & Free Education Banner (Special Needs / Disabled Learners)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF0F172A)
                ),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, TavanaGold)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = TavanaGold.copy(alpha = 0.2f),
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.VolunteerActivism,
                                    contentDescription = null,
                                    tint = TavanaGold,
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = if (isEn) "Inclusive Benefactor Scholarship & Guarantee" else "طرح ولی‌نعمتان - آموزش رایگان و اولویت استخدام",
                                    color = TavanaGold,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                                Text(
                                    text = if (isEn) "100% Free Tuition for Visually & Hearing Impaired Learners" else "بورسیه ۱۰۰٪ رایگان ویژه روشندلان، کم‌شنوایان و توان‌یابان",
                                    color = Color.White.copy(alpha = 0.8f),
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = if (isEn)
                            "Full educational access, voice/caption support, personalized in-person/online mentor assistance, and certified priority hiring in the job market to the full extent of ability."
                        else
                            "ارائه تمامی دوره‌ها به صورت کاملاً رایگان، همراه با پشتیبان و مربی اختصاصی (حضوری/آنلاین)، ابزارهای صوتی و تصویری فراگیر و ثبت رسمی پرونده جهت اولویت اول در جذب و استخدام بازار کار.",
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 11.sp,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { showAccModal = true },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = TavanaCyan),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.AccessibilityNew, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(if (isEn) "Accessibility Suite" else "تنظیمات دسترس‌پذیری", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = {
                                viewModel.speakAccessibilityText("درخواست پشتیبان و مربی اختصاصی حضوری و آنلاین برای شما ثبت گردید.")
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = TavanaEmerald),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.SupportAgent, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(if (isEn) "Request Assistant" else "درخواست همراه و پشتیبان", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Quick Action Shortcuts
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = "دسترسی‌های سریع سیستم",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    QuickActionTile(
                        title = "مربی هوش مصنوعی",
                        subtitle = "رفع اشکال و مشاوره",
                        icon = Icons.Default.AutoAwesome,
                        accentColor = TavanaCyan,
                        onClick = onNavigateToAiMentor,
                        modifier = Modifier.weight(1f)
                    )
                    QuickActionTile(
                        title = "گذرنامه مهارت",
                        subtitle = "Skill Passport",
                        icon = Icons.Default.Badge,
                        accentColor = TavanaGold,
                        onClick = onNavigateToSkillPassport,
                        modifier = Modifier.weight(1f)
                    )
                    QuickActionTile(
                        title = "بانک سوال و آزمون",
                        subtitle = "شبیه‌ساز کنکور",
                        icon = Icons.Default.Assignment,
                        accentColor = TavanaEmerald,
                        onClick = onNavigateToExams,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Courses Title & Grid/List
        item {
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (selectedWorld == AcademyWorld.TAVANA_PROFESSIONAL) "دوره‌های آکادمی تخصصی" else "دوره‌های عمومی و کنکور",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "${courses.size} دوره",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

        if (courses.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isEn) "No courses found." else "هیچ دوره‌ای یافت نشد.",
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                }
            }
        } else {
            items(courses) { course ->
                CourseCard(
                    course = course,
                    onClick = { onNavigateToCourse(course) },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                )
            }
        }
    }

    if (showAccModal) {
        com.example.ui.components.AccessibilityControlModal(
            lang = lang,
            isHighContrast = isHighContrast,
            isLargeText = isLargeText,
            isScreenReader = isScreenReader,
            isCaptions = isCaptions,
            onLanguageChange = { viewModel.setLanguage(it) },
            onToggleHighContrast = { viewModel.toggleHighContrast() },
            onToggleLargeText = { viewModel.toggleLargeText() },
            onToggleScreenReader = { viewModel.toggleScreenReader() },
            onToggleCaptions = { viewModel.toggleCaptions() },
            onDismiss = { showAccModal = false }
        )
    }

    if (showAuthModal) {
        com.example.ui.components.AuthRegistrationModal(
            lang = lang,
            onRegisterPhone = { phone, name -> viewModel.registerUserWithPhone(phone, name) },
            onRegisterEmail = { email, name -> viewModel.registerUserWithEmail(email, name) },
            onDismiss = { showAuthModal = false }
        )
    }
}

@Composable
fun QuickActionTile(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    accentColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() },
        color = MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(accentColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = subtitle,
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}
