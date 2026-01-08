package com.yjdev.goforawalk.presentation.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.yjdev.goforawalk.presentation.ui.component.CalendarMonthHeader
import com.yjdev.goforawalk.presentation.ui.component.DayCell
import com.yjdev.goforawalk.presentation.viewmodel.MainViewModel
import java.time.DayOfWeek
import java.time.YearMonth

@Composable
fun CalendarScreen(viewModel: MainViewModel) {
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(12) }
    val endMonth = remember { currentMonth.plusMonths(12) }

    val calendarState = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = DayOfWeek.SUNDAY
    )

    Column {
        HorizontalCalendar(
            state = calendarState,
            monthHeader = { month ->
                CalendarMonthHeader(month)
            },
            dayContent = { day ->
                DayCell(day)
            }
        )
    }
}

