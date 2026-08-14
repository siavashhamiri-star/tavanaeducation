package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.TavanaViewModel
import com.example.ui.components.StatCard
import com.example.ui.theme.*

@Composable
fun AdminDashboardScreen(
    viewModel: TavanaViewModel,
    modifier: Modifier = Modifier
) {
    var platformCommissionPercent by remember { mutableFloatStateOf(20f) }

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
                Column(modifier = Modifier.padding(20.dp)) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFFEF4444)
                    ) {
                        Text(
                            text = "مدیریت ارشد پلتفرم",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "پنل مدیریت سیستم توانا",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "کنترل کاربران، استادان، تایید دوره‌ها، پورسانت‌ها و گزارش‌های مالی",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }
        }

        // Platform Wide System Stats
        item {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    StatCard(
                        title = "کل کاربران فعال",
                        value = "۱۲,۴۵۰ نفر",
                        icon = Icons.Default.People,
                        iconColor = TavanaCyan,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "تعداد مدرسین",
                        value = "۱۴۸ استاد",
                        icon = Icons.Default.School,
                        iconColor = TavanaGold,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    StatCard(
                        title = "دوره‌های منتشرشده",
                        value = "۳۲۰ دوره",
                        icon = Icons.Default.LibraryBooks,
                        iconColor = TavanaEmerald,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "تراکنش‌های موفق",
                        value = "۴,۸۹۰ عدد",
                        icon = Icons.Default.ReceiptLong,
                        iconColor = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Commission Rate Configurator
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "تنظیم نرخ پورسانت پلتفرم از فروش مدرسین",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "درصد فعلی سهم پلتفرم: ${platformCommissionPercent.toInt()}٪ (سهم استاد: ${100 - platformCommissionPercent.toInt()}٪)",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Slider(
                        value = platformCommissionPercent,
                        onValueChange = { platformCommissionPercent = it },
                        valueRange = 10f..40f,
                        steps = 5,
                        colors = SliderDefaults.colors(
                            thumbColor = TavanaCyan,
                            activeTrackColor = TavanaCyan
                        )
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("۱۰٪", fontSize = 10.sp)
                        Text("۲۰٪ (پیشنہادی)", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Text("۴۰٪", fontSize = 10.sp)
                    }
                }
            }
        }

        // Pending Approval Queue
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "درخواست‌های در انتظار تایید مدیریت",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))

            val pendingList = listOf(
                "درخواست تایید رزومه استاد دکتر علیرضا نجفی (دپارتمان هوش مصنوعی)",
                "دوره جدید: «آموزش جامع امنیت شبکه و سیسکو» جهت بررسی کیفیت ویدیوها",
                "پروژه ثبت‌شده جدید در گذرنامه مهارت توسط دانشجو آرش محمدی"
            )

            pendingList.forEach { itemText ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = itemText,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.weight(1f)
                        )
                        Button(
                            onClick = { },
                            colors = ButtonDefaults.buttonColors(containerColor = TavanaEmerald),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("تایید و فعال‌سازی", fontSize = 10.sp)
                        }
                    }
                }
            }
        }
    }
}
