package com.mehedi.sleep_tracker.sleeptracker

class SleepNightListener(val clickListener: (nightId: Long) -> Unit) {
    fun onClick(nightId: Long) = clickListener(nightId)

}