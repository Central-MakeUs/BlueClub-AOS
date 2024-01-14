package org.blueclub.presentation.auth.setting.fragment

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.blueclub.databinding.ItemYearSettingBinding

class YearSettingAdapter(
    private val context: Context,
    private val yearList: MutableList<Pair<Int, Boolean>>,
    private val setSelectedYear: (Int) -> Unit,
) : RecyclerView.Adapter<YearSettingAdapter.YearSettingViewHolder>() {
    private val inflater by lazy { LayoutInflater.from(context) }

    class YearSettingViewHolder(
        private val binding: ItemYearSettingBinding,
        private val setSelectedYear: (Int) -> Unit,
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(yearText: Pair<Int, Boolean>, position: Int) {
            binding.year = yearText.first.toString()
            binding.selected = yearText.second
            binding.layoutYear.setOnClickListener {
                setSelectedYear(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YearSettingViewHolder =
        YearSettingViewHolder(
            ItemYearSettingBinding.inflate(inflater, parent, false),
            setSelectedYear
        )

    override fun getItemCount(): Int = yearList.size

    override fun onBindViewHolder(holder: YearSettingViewHolder, position: Int) {
        holder.bind(yearList[position], position)
    }
}