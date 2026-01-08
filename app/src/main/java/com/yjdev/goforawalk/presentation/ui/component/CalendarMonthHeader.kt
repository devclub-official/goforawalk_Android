package com.yjdev.goforawalk.presentation.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kizitonwose.calendar.core.CalendarMonth

@Composable
fun CalendarMonthHeader(month: CalendarMonth) {
    val yearMonth = month.yearMonth

    Text(
        text = "${yearMonth.year}년 ${yearMonth.monthValue}월",
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        textAlign = TextAlign.Center
    )
}
