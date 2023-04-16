package com.lnight.material3clock.core

import com.lnight.material3clock.R

sealed class BottomNavItem(val title: String, val icon: Int, val route: String) {

    object Alarm : BottomNavItem("Alarm", R.drawable.ic_alarm,"alarm")

    object Clock: BottomNavItem("Clock",R.drawable.ic_clock,"clock")

    object Timer: BottomNavItem("Timer",R.drawable.ic_timer,"timer")

    object Stopwatch: BottomNavItem("Stopwatch",R.drawable.ic_stopwatch,"stopwatch")

}