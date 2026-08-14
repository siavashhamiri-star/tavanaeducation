package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.data.local.entity.ExamEntity
import com.example.ui.TavanaViewModel
import com.example.ui.theme.*

@Composable
fun ExamsScreen(
    exams: List<ExamEntity>,
    viewModel: TavanaViewModel,
    modifier: Modifier = Modifier
) {
    var activeExamToTake by remember { mutableStateOf<ExamEntity?>(null) }
    var selectedOptionIndex by remember { mutableIntStateOf(-1) }
    var showResultDialog by remember { mutableStateOf(false) }

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
                        color = TavanaCyan
                    ) {
                        Text(
                            text = "سامانه آزمون هوشمند",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "بانک سوال و آزمون‌های آنلاین",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "شبیه‌ساز کنکور سراسری، امتحانات نهایی و سنجش مهارتی",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "آزمون‌های دسترس‌پذیر (${exams.size})",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        items(exams) { exam ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = exam.title,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (exam.isAttempted) TavanaEmerald.copy(alpha = 0.15f) else TavanaGold.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = if (exam.isAttempted) "درصد قبلی: ${exam.lastScorePercent}٪" else "جدید",
                                color = if (exam.isAttempted) TavanaEmerald else TavanaGold,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "مباحث: ${exam.subjectName} | سطح: ${exam.difficultyLevel}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${exam.questionCount} سوال • ${exam.durationMinutes} دقیقه",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )

                        Button(
                            onClick = {
                                activeExamToTake = exam
                                selectedOptionIndex = -1
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = TavanaCyan),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("شرکت در آزمون", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }

    // Exam Runner Modal
    activeExamToTake?.let { exam ->
        AlertDialog(
            onDismissRequest = { activeExamToTake = null },
            title = { Text(exam.title, fontWeight = FontWeight.Bold, fontSize = 16.sp) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "سوال ۱ از ${exam.questionCount}:",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "اگر حد تابع f(x) در x->2 برابر با ۵ باشد، حاصل حد f(x)^2 + 3f(x) چقدر است؟",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    val options = listOf("گزینه ۱: ۲۵", "گزینه ۲: ۴۰", "گزینه ۳: ۳۵ (گزینه صحیح)", "گزینه ۴: ۵۰")
                    options.forEachIndexed { index, optionText ->
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedOptionIndex = index },
                            shape = RoundedCornerShape(10.dp),
                            color = if (selectedOptionIndex == index) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                            border = if (selectedOptionIndex == index) androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary) else null
                        ) {
                            Text(
                                text = optionText,
                                fontSize = 13.sp,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val score = if (selectedOptionIndex == 2) 85 else 60
                        viewModel.recordExamResult(exam.id, score)
                        activeExamToTake = null
                        showResultDialog = true
                    },
                    enabled = selectedOptionIndex != -1
                ) {
                    Text("ثبت و دریافت درصد")
                }
            },
            dismissButton = {
                TextButton(onClick = { activeExamToTake = null }) {
                    Text("انصراف")
                }
            }
        )
    }

    if (showResultDialog) {
        AlertDialog(
            onDismissRequest = { showResultDialog = false },
            title = { Text("نتیجه آزمون ثبت گردید!", fontWeight = FontWeight.Bold) },
            text = { Text("کارنامه آزمون آنلاین شما با درصد بالا ثبت شد و ۱۲۰ امتیاز XP به حساب شما افزوده شد!") },
            confirmButton = {
                Button(onClick = { showResultDialog = false }) {
                    Text("متوجه شدم")
                }
            }
        )
    }
}
