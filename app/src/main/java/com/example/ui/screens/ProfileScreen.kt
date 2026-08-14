package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.UserEntity
import com.example.domain.model.UserRole
import com.example.ui.TavanaViewModel
import com.example.ui.components.RoleSwitcherModal
import com.example.ui.theme.*

@Composable
fun ProfileScreen(
    user: UserEntity?,
    viewModel: TavanaViewModel,
    modifier: Modifier = Modifier
) {
    var showRoleModal by remember { mutableStateOf(false) }
    var showAuthModal by remember { mutableStateOf(false) }

    val lang by viewModel.selectedLanguage.collectAsState()
    val isHighContrast by viewModel.isHighContrastMode.collectAsState()
    val isLargeText by viewModel.isLargeTextMode.collectAsState()
    val isScreenReader by viewModel.isScreenReaderEnabled.collectAsState()
    val isCaptions by viewModel.isCaptionsEnabled.collectAsState()

    val isEn = lang == "en"

    val currentRole = try {
        UserRole.valueOf(user?.roleName ?: "STUDENT")
    } catch (e: Exception) {
        UserRole.STUDENT
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // Top User Header
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = TavanaNavy
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .clip(CircleShape)
                            .background(TavanaCyanDark),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = (user?.fullName ?: "آرش").take(1),
                            color = Color.White,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = user?.fullName ?: "آرش محمدی",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = user?.email ?: "arash@tavana.edu",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = TavanaGold.copy(alpha = 0.2f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, TavanaGold)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Badge, contentDescription = null, tint = TavanaGold, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (isEn) "Role: ${currentRole.name}" else "نقش فعال: ${currentRole.titleFa}",
                                    color = TavanaGold,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = TavanaCyan.copy(alpha = 0.2f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, TavanaCyan),
                            modifier = Modifier.clickable { showAuthModal = true }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Phone, contentDescription = null, tint = TavanaCyan, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (isEn) "Register/Auth" else "ثبت‌نام با تلفن/ایمیل",
                                    color = TavanaCyan,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        // Language & Accessibility Section
        item {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = if (isEn) "Language & Accessibility Suite" else "تنظیمات زبان و دسترس‌پذیری معلولین",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(10.dp))

                // Language Selector Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Language, contentDescription = null, tint = TavanaCyan)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(if (isEn) "Language / زبان" else "زبان برنامه", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            FilterChip(
                                selected = lang == "fa",
                                onClick = { viewModel.setLanguage("fa") },
                                label = { Text("فارسی") }
                            )
                            FilterChip(
                                selected = lang == "en",
                                onClick = { viewModel.setLanguage("en") },
                                label = { Text("English") }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Accessibility Switches
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Visibility, contentDescription = null, tint = TavanaGold)
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(if (isEn) "High Contrast (Low Vision)" else "حالت کنتراست بالا (ویژه کم‌بینایان)", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                    Text(if (isEn) "Pure black & electric colors" else "پس‌زمینه کاملا تیره با فونت درخشان", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurface.copy(0.6f))
                                }
                            }
                            Switch(checked = isHighContrast, onCheckedChange = { viewModel.toggleHighContrast() })
                        }

                        Divider(color = MaterialTheme.colorScheme.onSurface.copy(0.1f))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.FormatSize, contentDescription = null, tint = TavanaCyan)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(if (isEn) "Large Font Scale" else "درشت‌نمایی متون (درشت‌خط)", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                            Switch(checked = isLargeText, onCheckedChange = { viewModel.toggleLargeText() })
                        }

                        Divider(color = MaterialTheme.colorScheme.onSurface.copy(0.1f))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.RecordVoiceOver, contentDescription = null, tint = TavanaEmerald)
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(if (isEn) "Voice Narrator (Blind Users)" else "گوینده صوتی و متن‌خوان (ویژه نابینایان)", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                    Text(if (isEn) "Reads out lesson titles & mentor texts" else "خوانش صوتی محتوای آموزشی برای نابینایان", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurface.copy(0.6f))
                                }
                            }
                            Switch(checked = isScreenReader, onCheckedChange = { viewModel.toggleScreenReader() })
                        }

                        Divider(color = MaterialTheme.colorScheme.onSurface.copy(0.1f))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.ClosedCaption, contentDescription = null, tint = TavanaCyan)
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(if (isEn) "Closed Captions (Deaf Users)" else "زیرنویس همزمان (ویژه کم‌شنوایان)", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                    Text(if (isEn) "Captions for video & audio lessons" else "نمایش کامل متن صحبت‌های مدرس", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurface.copy(0.6f))
                                }
                            }
                            Switch(checked = isCaptions, onCheckedChange = { viewModel.toggleCaptions() })
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = if (isEn) "User Roles & System Settings" else "تنظیمات کاربری و نقش‌ها",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(10.dp))

                // Role Switcher Tile
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { showRoleModal = true },
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.SwitchAccount, contentDescription = null, tint = TavanaCyan)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(if (isEn) "Switch User Role" else "تغییر نقش کاربری", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text(if (isEn) "Student, Teacher, Mentor or Admin" else "تغییر به پنل دانشجو، استاد، منتور یا مدیر", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                            }
                        }
                        Icon(Icons.Default.ChevronRight, contentDescription = null, modifier = Modifier.size(18.dp))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Philosophy Box
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = TavanaNavyLight)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = if (isEn) "💡 Tavana Growth Cycle" else "💡 فلسفه و چرخه رشد در توانا",
                            color = TavanaGold,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "LEARN → UNDERSTAND → PRACTICE → BUILD → PROVE → GROW → WORK → TEACH",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 18.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = if (isEn)
                                "Tavana is an integrated ecosystem for learning, practicing, building projects, proving skill mastery, and inclusive accessibility."
                            else
                                "پلتفرم آموزشی توانا صرفاً یک سایت ویدیویی نیست؛ بلکه اکوسیستم رشد متقابل یادگیری، تمرین، ساخت پروژه، اثبات مهارتی و دسترسی همگانی برای معلولین است.",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 12.sp,
                            lineHeight = 20.sp
                        )
                    }
                }
            }
        }
    }

    if (showRoleModal) {
        RoleSwitcherModal(
            currentRole = currentRole,
            onRoleSelected = { viewModel.switchRole(it) },
            onDismiss = { showRoleModal = false }
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
