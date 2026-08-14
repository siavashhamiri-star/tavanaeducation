package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.CourseEntity
import com.example.domain.model.AcademyWorld
import com.example.domain.model.UserRole
import com.example.ui.theme.*

@Composable
fun WorldSwitcherHeader(
    selectedWorld: AcademyWorld,
    onWorldSelected: (AcademyWorld) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        shadowElevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AcademyWorld.entries.forEach { world ->
                    val isSelected = selectedWorld == world
                    val bgColor by animateColorAsState(
                        if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                        label = "worldBg"
                    )
                    val textColor by animateColorAsState(
                        if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                        label = "worldText"
                    )

                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .clickable { onWorldSelected(world) },
                        color = bgColor,
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(vertical = 12.dp, horizontal = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = if (world == AcademyWorld.TAVANA_PROFESSIONAL) Icons.Default.Engineering else Icons.Default.School,
                                contentDescription = null,
                                tint = textColor,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = world.titleFa,
                                color = textColor,
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = selectedWorld.subtitleFa,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
            )
        }
    }
}

@Composable
fun CourseCard(
    course: CourseEntity,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .background(TavanaNavy)
            ) {
                if (course.imageBannerRes != 0) {
                    Image(
                        painter = painterResource(id = course.imageBannerRes),
                        contentDescription = course.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.linearGradient(
                                    listOf(TavanaNavy, TavanaCyanDark)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayCircle,
                            contentDescription = null,
                            tint = Color.White.copy(alpha = 0.8f),
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }

                // Category Tag
                Surface(
                    modifier = Modifier
                        .padding(10.dp)
                        .align(Alignment.TopStart),
                    shape = RoundedCornerShape(8.dp),
                    color = TavanaNavy.copy(alpha = 0.85f)
                ) {
                    Text(
                        text = course.category,
                        color = TavanaGold,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                if (course.isEnrolled) {
                    Surface(
                        modifier = Modifier
                            .padding(10.dp)
                            .align(Alignment.TopEnd),
                        shape = RoundedCornerShape(8.dp),
                        color = TavanaEmerald
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "ثبت‌نام شده",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = course.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = course.instructorName,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = TavanaGold,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${course.rating} (${course.reviewCount})",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    if (course.priceToman == 0L) {
                        Text(
                            text = "رایگان",
                            color = TavanaEmerald,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    } else {
                        Text(
                            text = "%,d تومان".format(course.priceToman),
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    icon: ImageVector,
    iconColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(iconColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column {
                Text(
                    text = value,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = title,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}

@Composable
fun RoleSwitcherModal(
    currentRole: UserRole,
    onRoleSelected: (UserRole) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "تغییر نقش کاربر در سیستم",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "نقش دلخواه را انتخاب کنید تا پنل مربوطه (دانشجو، مدرس یا مدیر) فعال گردد:",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )
                Spacer(modifier = Modifier.height(4.dp))
                UserRole.entries.forEach { role ->
                    val isSelected = role == currentRole
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .clickable {
                                onRoleSelected(role)
                                onDismiss()
                            },
                        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                        border = if (isSelected) androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary) else null
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = {
                                    onRoleSelected(role)
                                    onDismiss()
                                }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = role.titleFa,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = role.descriptionFa,
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("انصراف")
            }
        }
    )
}

@Composable
fun AccessibilityControlModal(
    lang: String,
    isHighContrast: Boolean,
    isLargeText: Boolean,
    isScreenReader: Boolean,
    isCaptions: Boolean,
    onLanguageChange: (String) -> Unit,
    onToggleHighContrast: () -> Unit,
    onToggleLargeText: () -> Unit,
    onToggleScreenReader: () -> Unit,
    onToggleCaptions: () -> Unit,
    onDismiss: () -> Unit
) {
    val isEn = lang == "en"
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AccessibilityNew, contentDescription = null, tint = TavanaCyan)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isEn) "Inclusion & Accessibility Suite" else "سامانه ویژه کم‌بینایان، کم‌شنوایان و نابینایان",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = if (isEn) "Configure language and specialized accessibility controls:" else "تنظیمات زبان و ابزارهای کمکی فراگیر:",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )

                // Language Switcher Row
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(if (isEn) "Language / زبان" else "انتخاب زبان برنامه", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            FilterChip(
                                selected = lang == "fa",
                                onClick = { onLanguageChange("fa") },
                                label = { Text("فارسی") }
                            )
                            FilterChip(
                                selected = lang == "en",
                                onClick = { onLanguageChange("en") },
                                label = { Text("English") }
                            )
                        }
                    }
                }

                // High Contrast
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                if (isEn) "High Contrast Mode" else "حالت کنتراست بالا (کم‌بینایان)",
                                fontWeight = FontWeight.Bold, fontSize = 13.sp
                            )
                            Text(
                                if (isEn) "Pure black background with bright yellow/cyan accents" else "پس‌زمینه مشکی خالص با فونت‌های درخشان زرد و فیروزه‌ای",
                                fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurface.copy(0.6f)
                            )
                        }
                        Switch(checked = isHighContrast, onCheckedChange = { onToggleHighContrast() })
                    }
                }

                // Large Text
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                if (isEn) "Large Font Size" else "درشت‌نمایی متون (درشت‌خط)",
                                fontWeight = FontWeight.Bold, fontSize = 13.sp
                            )
                            Text(
                                if (isEn) "Enlarges all headings and readable text" else "افزایش مقیاس فونت‌ها جهت خوانایی آسان‌تر",
                                fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurface.copy(0.6f)
                            )
                        }
                        Switch(checked = isLargeText, onCheckedChange = { onToggleLargeText() })
                    }
                }

                // Screen Reader Voice Narrator
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                if (isEn) "Voice Reader / TalkBack (Blind Users)" else "گوینده صوتی و متن‌خوان (ویژه نابینایان)",
                                fontWeight = FontWeight.Bold, fontSize = 13.sp
                            )
                            Text(
                                if (isEn) "Reads out lesson titles, notes, and mentor responses" else "خوانش صوتی عنوان دروس، نکات آموزشی و پاسخ‌های مربی",
                                fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurface.copy(0.6f)
                            )
                        }
                        Switch(checked = isScreenReader, onCheckedChange = { onToggleScreenReader() })
                    }
                }

                // Closed Captions / Subtitles
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                if (isEn) "Closed Captions (Deaf Users)" else "زیرنویس همزمان (ویژه کم‌شنوایان)",
                                fontWeight = FontWeight.Bold, fontSize = 13.sp
                            )
                            Text(
                                if (isEn) "Shows visual captions and audio cue indicators" else "نمایش کامل متن صحبت‌های مدرس و علائم صوتی بصری",
                                fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurface.copy(0.6f)
                            )
                        }
                        Switch(checked = isCaptions, onCheckedChange = { onToggleCaptions() })
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = onDismiss, colors = ButtonDefaults.buttonColors(containerColor = TavanaCyan)) {
                Text(if (isEn) "Apply Settings" else "اعمال تنظیمات")
            }
        }
    )
}

@Composable
fun AuthRegistrationModal(
    lang: String,
    onRegisterPhone: (phone: String, fullName: String) -> Unit,
    onRegisterEmail: (email: String, fullName: String) -> Unit,
    onDismiss: () -> Unit
) {
    val isEn = lang == "en"
    var selectedAuthType by androidx.compose.runtime.remember { androidx.compose.runtime.mutableIntStateOf(0) } // 0: Phone, 1: Email
    var fullNameInput by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf("") }
    var phoneInput by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf("") }
    var otpInput by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf("") }
    var emailInput by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf("") }
    var passwordInput by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf("") }
    var isOtpSent by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text(
                    text = if (isEn) "Register & Sign In" else "ثبت‌نام و ورود به آکادمی توانا",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = if (isEn) "Register using Mobile Phone or Email" else "ثبت‌نام سریع با شماره تلفن همراه یا نشانی ایمیل",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(0.6f)
                )
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // Auth Method Tabs
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { selectedAuthType = 0 },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedAuthType == 0) TavanaCyan else MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = if (selectedAuthType == 0) Color.White else MaterialTheme.colorScheme.onSurface
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(if (isEn) "Phone" else "موبایل", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = { selectedAuthType = 1 },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedAuthType == 1) TavanaCyan else MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = if (selectedAuthType == 1) Color.White else MaterialTheme.colorScheme.onSurface
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.Email, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(if (isEn) "Email" else "ایمیل", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }

                OutlinedTextField(
                    value = fullNameInput,
                    onValueChange = { fullNameInput = it },
                    label = { Text(if (isEn) "Full Name" else "نام و نام خانوادگی") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                if (selectedAuthType == 0) {
                    // Mobile Registration
                    OutlinedTextField(
                        value = phoneInput,
                        onValueChange = { phoneInput = it },
                        label = { Text(if (isEn) "Mobile Phone Number" else "شماره تلفن همراه") },
                        placeholder = { Text("09123456789") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) }
                    )

                    if (!isOtpSent) {
                        Button(
                            onClick = { isOtpSent = true },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = TavanaNavy),
                            shape = RoundedCornerShape(10.dp),
                            enabled = phoneInput.isNotBlank()
                        ) {
                            Text(if (isEn) "Send SMS Verification Code" else "ارسال کد تایید پیامکی")
                        }
                    } else {
                        Surface(
                            color = TavanaEmerald.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                if (isEn) "SMS Code 4829 sent to $phoneInput" else "کد تایید ۴ رقمی ارسال گردید (کد نمونه: ۴۸۲۹)",
                                fontSize = 11.sp,
                                color = TavanaEmerald,
                                modifier = Modifier.padding(8.dp)
                            )
                        }

                        OutlinedTextField(
                            value = otpInput,
                            onValueChange = { otpInput = it },
                            label = { Text(if (isEn) "SMS Verification Code" else "کد تایید پیامک‌شده") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                    }
                } else {
                    // Email Registration
                    OutlinedTextField(
                        value = emailInput,
                        onValueChange = { emailInput = it },
                        label = { Text(if (isEn) "Email Address" else "نشانی ایمیل") },
                        placeholder = { Text("user@tavana.edu") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) }
                    )

                    OutlinedTextField(
                        value = passwordInput,
                        onValueChange = { passwordInput = it },
                        label = { Text(if (isEn) "Password" else "کلمه عبور") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) }
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (selectedAuthType == 0) {
                        onRegisterPhone(phoneInput, fullNameInput)
                    } else {
                        onRegisterEmail(emailInput, fullNameInput)
                    }
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = TavanaCyan),
                enabled = if (selectedAuthType == 0) (isOtpSent && otpInput.isNotBlank()) else (emailInput.isNotBlank() && passwordInput.isNotBlank())
            ) {
                Text(if (isEn) "Confirm & Sign In" else "تایید و ورود به سیستم")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(if (isEn) "Cancel" else "انصراف")
            }
        }
    )
}

