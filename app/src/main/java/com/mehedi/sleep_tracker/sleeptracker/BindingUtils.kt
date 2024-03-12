package com.mehedi.sleep_tracker.sleeptracker

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.mehedi.sleep_tracker.R
import com.mehedi.sleep_tracker.convertDurationToFormatted
import com.mehedi.sleep_tracker.convertNumericQualityToString
import com.mehedi.sleep_tracker.database.SleepNight

@BindingAdapter("sleepDuration")
fun TextView.setSleepDurationFormatted(night: SleepNight?) {
    night?.let {
        text =
            convertDurationToFormatted(night.startTimeMilli, night.endTimeMilli, context.resources)
    }
}

@BindingAdapter("sleepQuality")
fun TextView.setSleepQuality(night: SleepNight?) {
    night?.let {
        text =
            convertNumericQualityToString(night.sleepQuality, context.resources)
    }
}

@BindingAdapter("sleepImage")
fun ImageView.setSleepImage(night: SleepNight?) {
    night?.let {
        when (night.sleepQuality) {
            0 -> setImageResource(R.drawable.ic_sleep_0)
            1 -> setImageResource(R.drawable.ic_sleep_1)
            2 -> setImageResource(R.drawable.ic_sleep_2)
            3 -> setImageResource(R.drawable.ic_sleep_3)
            4 -> setImageResource(R.drawable.ic_sleep_4)
            5 -> setImageResource(R.drawable.ic_sleep_5)
            else -> setImageResource(R.drawable.ic_sleep_active)
        }
    }
}