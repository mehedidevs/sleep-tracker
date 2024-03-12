package com.mehedi.sleep_tracker.sleeptracker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mehedi.sleep_tracker.R
import com.mehedi.sleep_tracker.convertLongToDateString
import com.mehedi.sleep_tracker.database.SleepNight
import com.mehedi.sleep_tracker.databinding.ItemSleepNightBinding

class SleepAdapter(private val nights: List<SleepNight>) :
    RecyclerView.Adapter<SleepAdapter.SleepViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SleepViewHolder {
        return SleepViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return nights.size
    }

    override fun onBindViewHolder(holder: SleepViewHolder, position: Int) {
        val sleepNight = nights[position]
        holder.bind(sleepNight)
    }

    class SleepViewHolder private constructor(private val binding: ItemSleepNightBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            sleepNight: SleepNight,
        ) {
            when (sleepNight.sleepQuality) {
                0 -> binding.ivSleepQuality.setImageResource(R.drawable.ic_sleep_0)
                1 -> binding.ivSleepQuality.setImageResource(R.drawable.ic_sleep_1)
                2 -> binding.ivSleepQuality.setImageResource(R.drawable.ic_sleep_2)
                3 -> binding.ivSleepQuality.setImageResource(R.drawable.ic_sleep_3)
                4 -> binding.ivSleepQuality.setImageResource(R.drawable.ic_sleep_4)
                5 -> binding.ivSleepQuality.setImageResource(R.drawable.ic_sleep_5)
            }
            binding.tvEndTime.text = convertLongToDateString(sleepNight.endTimeMilli)
            binding.tvStartTime.text = convertLongToDateString(sleepNight.startTimeMilli)
        }

        companion object {
            fun from(parent: ViewGroup): SleepViewHolder {
                val binding: ItemSleepNightBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_sleep_night,
                    parent,
                    false
                )

                return SleepViewHolder(binding)
            }
        }


    }


}