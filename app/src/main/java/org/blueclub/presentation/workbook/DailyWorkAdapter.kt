package org.blueclub.presentation.workbook

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.blueclub.databinding.ItemDailyRecordBinding
import org.blueclub.domain.model.DailyWorkInfo
import org.blueclub.util.ItemDiffCallback
import java.text.DecimalFormat

class DailyWorkAdapter(
    private val moveToDetail: (String) -> Unit,
) : ListAdapter<DailyWorkInfo, RecyclerView.ViewHolder>(
    ItemDiffCallback<DailyWorkInfo>(
        onContentsTheSame = { old, new -> old == new },
        onItemsTheSame = { old, new -> old.date == new.date }
    )
) {
    private lateinit var inflater: LayoutInflater

    class DailyWorkViewHolder(
        private val binding: ItemDailyRecordBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            dailyWorkInfo: DailyWorkInfo,
            moveToDetail: (String) -> Unit,
        ) {
            binding.dailyWorkInfo = dailyWorkInfo
            binding.dailyWorkType = dailyWorkInfo.dailyWorkType
            binding.date = dailyWorkInfo.date.slice(5..9).replace("-", ".")
            binding.ivMoveToDetail.setOnClickListener { moveToDetail(dailyWorkInfo.date) }
            val decimalFormat = DecimalFormat("#,###")
            binding.income = decimalFormat.format(dailyWorkInfo.income ?: 0)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (!::inflater.isInitialized)
            inflater = LayoutInflater.from(parent.context)
        return DailyWorkViewHolder(ItemDailyRecordBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is DailyWorkViewHolder -> holder.bind(currentList[position], moveToDetail)
        }
    }

}