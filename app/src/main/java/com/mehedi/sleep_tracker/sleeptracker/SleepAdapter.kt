package com.mehedi.sleep_tracker.sleeptracker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mehedi.sleep_tracker.R
import com.mehedi.sleep_tracker.database.SleepNight
import com.mehedi.sleep_tracker.databinding.ItemHeaderBinding
import com.mehedi.sleep_tracker.databinding.ItemSleepNightGridBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private val ITEM_VIEW_TYPE_HEADER = 0
private val ITEM_VIEW_TYPE_ITEM = 1

class SleepAdapter(private val sleepNightListener: SleepNightListener) :
    ListAdapter<DataItem, RecyclerView.ViewHolder>(SleepNightDiff()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    fun addHeaderAndSubmitList(list: List<SleepNight>?) {
        adapterScope.launch {
            val items = when (list) {
                null -> listOf(DataItem.Header)
                else -> listOf(DataItem.Header) + list.map { DataItem.SleepNightItem(it) }
            }
            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }


    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is DataItem.SleepNightItem -> ITEM_VIEW_TYPE_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> HeaderViewHolder.from(parent)
            ITEM_VIEW_TYPE_ITEM -> SleepViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }

    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val sleepNight = getItem(position)
        when (holder) {
            is SleepViewHolder -> {
                val nightItem = getItem(position) as DataItem.SleepNightItem
                holder.bind(nightItem.sleepNight, sleepNightListener)
            }

            is HeaderViewHolder -> {
                val nightItem = getItem(position) as DataItem.Header
                holder.bind("")
            }
        }
    }

    class HeaderViewHolder private constructor(private val binding: ItemHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            title: String
        ) {

            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): HeaderViewHolder {
                val binding: ItemHeaderBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_header,
                    parent,
                    false
                )
                return HeaderViewHolder(binding)
            }
        }
    }

    class SleepViewHolder private constructor(private val binding: ItemSleepNightGridBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            sleepNight: SleepNight,
            sleepNightListener: SleepNightListener
        ) {
            binding.sleep = sleepNight
            binding.clickListener = sleepNightListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): SleepViewHolder {
                val binding: ItemSleepNightGridBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_sleep_night_grid,
                    parent,
                    false
                )
                return SleepViewHolder(binding)
            }
        }
    }
}


class SleepNightDiff : DiffUtil.ItemCallback<DataItem>() {
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
    }
}

sealed class DataItem {
    abstract val id: Long

    data class SleepNightItem(val sleepNight: SleepNight) : DataItem() {
        override val id = sleepNight.nightId
    }

    data object Header : DataItem() {
        override val id = Long.MIN_VALUE
    }


}

