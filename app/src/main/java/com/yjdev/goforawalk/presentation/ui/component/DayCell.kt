package com.yjdev.goforawalk.presentation.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.yjdev.goforawalk.R
import java.time.LocalDate

@Composable
fun DayCell(
    day: CalendarDay,
    hasFootstep: Boolean,
    modifier: Modifier = Modifier,
    onClick: (LocalDate) -> Unit = {}
) {
    val isCurrentMonth = day.position == DayPosition.MonthDate
    val isToday = day.date == LocalDate.now()

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .padding(4.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(
                if (isToday) Color(0xFFA47764) else Color.Transparent // 브라운
            )
            .clickable(
                enabled = isCurrentMonth && hasFootstep
            ) {
                onClick(day.date)
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = day.date.dayOfMonth.toString(),
                color = when {
                    !isCurrentMonth ->
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)

                    isToday ->
                        Color.White

                    else ->
                        MaterialTheme.colorScheme.onSurface
                },
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal
            )

            if (hasFootstep) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_bear),
                    contentDescription = "footstep",
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .padding(top = 2.dp)
                        .size(20.dp)
                )
            }
        }
    }
}
