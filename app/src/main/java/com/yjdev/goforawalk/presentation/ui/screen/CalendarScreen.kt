package com.yjdev.goforawalk.presentation.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.yjdev.goforawalk.presentation.ui.component.CalendarMonthHeader
import com.yjdev.goforawalk.presentation.ui.component.DayCell
import com.yjdev.goforawalk.presentation.ui.component.FootstepCard
import com.yjdev.goforawalk.presentation.viewmodel.MainViewModel
import java.time.DayOfWeek
import java.time.LocalDate
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

    val calendarFootsteps = viewModel.calendarFootsteps.collectAsState().value

    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }

    LaunchedEffect(currentMonth) {
        viewModel.loadCalendarFootsteps(
            year = currentMonth.year,
            month = currentMonth.monthValue
        )
    }

    LaunchedEffect(calendarState.firstVisibleMonth) {
        val visibleMonth = calendarState.firstVisibleMonth.yearMonth
        viewModel.loadCalendarFootsteps(
            year = visibleMonth.year,
            month = visibleMonth.monthValue
        )
    }

    val today = LocalDate.now()

    LaunchedEffect(calendarFootsteps) {
        if (selectedDate == null) {
            val todayFootsteps = calendarFootsteps[today]
            if (!todayFootsteps.isNullOrEmpty()) {
                selectedDate = today
            }
        }
    }

    Column {
        HorizontalCalendar(
            state = calendarState,
            monthHeader = { month ->
                CalendarMonthHeader(month)
            },
            dayContent = { day ->
                val footstepsForDay = calendarFootsteps[day.date]
                val hasFootstep = !footstepsForDay.isNullOrEmpty()

                DayCell(
                    day = day,
                    hasFootstep = hasFootstep,
                    onClick = {
                        if (!footstepsForDay.isNullOrEmpty()) {
                            selectedDate = day.date
                        }
                    }
                )
            }

        )
        Spacer(modifier = Modifier.height(20.dp))

        selectedDate?.let { date ->
            val footsteps = calendarFootsteps[date]

            footsteps?.let {
                Spacer(modifier = Modifier.height(16.dp))

                it.forEach { footstep ->
                    FootstepCard(
                        footstep = footstep,
                        onDelete = {
                            viewModel.deleteFootstep(
                                id = footstep.footstepId,
                                onSuccess = {
                                    viewModel.loadCalendarFootsteps(
                                        year = date.year,
                                        month = date.monthValue
                                    )
                                },
                                onError = { }
                            )
                        }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }

    }
}
