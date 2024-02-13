package org.blueclub.presentation.notice

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.blueclub.data.model.response.ResponseNotice
import org.blueclub.databinding.ItemNoticeBinding
import org.blueclub.util.ItemDiffCallback

class NoticeAdapter: ListAdapter<ResponseNotice.ResponseNoticeData, RecyclerView.ViewHolder>(
    ItemDiffCallback<ResponseNotice.ResponseNoticeData>(
        onContentsTheSame = { old, new -> old == new },
        onItemsTheSame = { old, new -> old.id == new.id }
    )
)  {
    private lateinit var inflater: LayoutInflater

    class NoticeViewHolder(
        private val binding: ItemNoticeBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            noticeData: ResponseNotice.ResponseNoticeData,
        ) {
            binding.noticeData = noticeData
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (!::inflater.isInitialized)
            inflater = LayoutInflater.from(parent.context)
        return NoticeViewHolder(ItemNoticeBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is NoticeViewHolder -> holder.bind(currentList[position])
        }
    }
}