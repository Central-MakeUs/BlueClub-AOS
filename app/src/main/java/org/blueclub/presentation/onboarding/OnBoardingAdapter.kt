package org.blueclub.presentation.onboarding

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.blueclub.databinding.ItemOnboardingBinding
import org.blueclub.presentation.type.OnBoardingViewType

class OnBoardingAdapter(context: Context) :
    RecyclerView.Adapter<OnBoardingAdapter.OnBoardingViewHolder>() {
    private val inflater by lazy { LayoutInflater.from(context) }
    private val onBoardingList = OnBoardingViewType.values()

    class OnBoardingViewHolder(private val binding: ItemOnboardingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(type: OnBoardingViewType) {
            binding.item = type
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnBoardingViewHolder =
        OnBoardingViewHolder(ItemOnboardingBinding.inflate(inflater, parent, false))

    override fun getItemCount(): Int = onBoardingList.size

    override fun onBindViewHolder(holder: OnBoardingViewHolder, position: Int) {
        holder.bind(onBoardingList[position])
    }
}