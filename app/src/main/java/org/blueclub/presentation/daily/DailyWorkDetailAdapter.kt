package org.blueclub.presentation.daily

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.blueclub.databinding.ItemDailyWorkAmountBinding
import org.blueclub.databinding.ItemDailyWorkCheckboxBinding
import org.blueclub.databinding.ItemDailyWorkMoneyInputBinding
import org.blueclub.databinding.ItemDailyWorkTextInputBinding
import org.blueclub.databinding.ItemDailyWorkTypeBinding
import org.blueclub.presentation.model.DailyWorkDetailOption
import org.blueclub.presentation.type.DailyWorkDetailOptionType
import org.blueclub.util.ItemDiffCallback

class DailyWorkDetailAdapter(
    private val showWorkTypeDialog: () -> Unit,
) : ListAdapter<DailyWorkDetailOption, RecyclerView.ViewHolder>(
    ItemDiffCallback<DailyWorkDetailOption>(
        onContentsTheSame = { old, new -> old == new },
        onItemsTheSame = { old, new -> old.titleRes == new.titleRes }
    )
) {
    private lateinit var inflater: LayoutInflater

    class WorkTypeViewHolder(
        private val binding: ItemDailyWorkTypeBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            showWorkTypeDialog: () -> Unit,
        ) {
            binding.ivSelectWorkType.setOnClickListener { showWorkTypeDialog() }
        }
    }

    class WorkAmountViewHolder(
        private val binding: ItemDailyWorkAmountBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            option: DailyWorkDetailOption,
        ) {
            binding.option = option
        }
    }

    class WorkMoneyInputViewHolder(
        private val binding: ItemDailyWorkMoneyInputBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            option: DailyWorkDetailOption,
        ) {
            binding.option = option
        }
    }

    class WorkTextInputViewHolder(
        private val binding: ItemDailyWorkTextInputBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            option: DailyWorkDetailOption,
        ) {
            binding.option = option
        }
    }

    class WorkCheckBoxViewHolder(
        private val binding: ItemDailyWorkCheckboxBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            option: DailyWorkDetailOption,
        ) {
            binding.option = option
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (!::inflater.isInitialized)
            inflater = LayoutInflater.from(parent.context)
        return when(viewType){
            DailyWorkDetailOptionType.WORK_TYPE.ordinal -> {
                WorkTypeViewHolder(ItemDailyWorkTypeBinding.inflate(inflater, parent, false))
            }
            DailyWorkDetailOptionType.MONEY_INPUT.ordinal -> {
                WorkMoneyInputViewHolder(ItemDailyWorkMoneyInputBinding.inflate(inflater, parent, false))
            }
            DailyWorkDetailOptionType.TEXT_INPUT.ordinal -> {
                WorkTextInputViewHolder(ItemDailyWorkTextInputBinding.inflate(inflater, parent, false))
            }
            DailyWorkDetailOptionType.WORK_AMOUNT.ordinal -> {
                WorkAmountViewHolder(ItemDailyWorkAmountBinding.inflate(inflater, parent,false))
            }
            DailyWorkDetailOptionType.CHECKBOX.ordinal -> {
                WorkCheckBoxViewHolder(ItemDailyWorkCheckboxBinding.inflate(inflater, parent, false))
            }
            else ->{
                throw java.lang.ClassCastException("Unknown ViewType Error")
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is WorkTypeViewHolder ->  holder.bind(showWorkTypeDialog)
            is WorkAmountViewHolder -> holder.bind(currentList[position])
            is WorkMoneyInputViewHolder -> holder.bind(currentList[position])
            is WorkTextInputViewHolder -> holder.bind(currentList[position])
            is WorkCheckBoxViewHolder -> holder.bind(currentList[position])
        }
    }

    override fun getItemViewType(position: Int): Int = currentList[position].optionType.ordinal

}