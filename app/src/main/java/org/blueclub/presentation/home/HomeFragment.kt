package org.blueclub.presentation.home

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.view.marginLeft
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.blueclub.R
import org.blueclub.databinding.FragmentHomeBinding
import org.blueclub.presentation.base.BindingFragment
import org.blueclub.presentation.workbook.WorkbookFragment
import org.blueclub.util.UiState

@AndroidEntryPoint
class HomeFragment : BindingFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    private val viewModel: HomeViewModel by activityViewModels()
    private lateinit var rootView: View

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        rootView = binding.root

        collectData()
    }

    private fun collectData() {
        viewModel.homeUiState.flowWithLifecycle(lifecycle).onEach {
            when (it) {
                is UiState.Loading -> {
                    viewModel.getMonthlyInfo()
                }

                else -> {}
            }
        }.launchIn(lifecycleScope)
        viewModel.progress.flowWithLifecycle(lifecycle).onEach {
            var leftMargin = getProgressBarSize() * it / 100 - WorkbookFragment.PROGRESS_MARGIN_MIN
            if (leftMargin < WorkbookFragment.PROGRESS_MARGIN_MIN)
                leftMargin = WorkbookFragment.PROGRESS_MARGIN_MIN
            if (leftMargin > getProgressBarSize() - WorkbookFragment.PROGRESS_MARGIN_MAX)
                leftMargin = getProgressBarSize() - WorkbookFragment.PROGRESS_MARGIN_MAX
            var bubbleMargin = leftMargin - WorkbookFragment.BUBBLE_WIDTH
            if (bubbleMargin < WorkbookFragment.PROGRESS_BUBBLE_MARGIN_MIN)
                bubbleMargin = WorkbookFragment.PROGRESS_BUBBLE_MARGIN_MIN
            if (bubbleMargin > getProgressBarSize() - WorkbookFragment.PROGRESS_BUBBLE_MARGIN_MAX)
                bubbleMargin = getProgressBarSize() - WorkbookFragment.PROGRESS_BUBBLE_MARGIN_MAX
            binding.ivBubbleBelow.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                this.marginStart = leftMargin
            }
            binding.tvBubbleAmount.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                this.marginStart = bubbleMargin
            }
            binding.tvBubbleAmount.text = "$it%"
        }.launchIn(lifecycleScope)
    }

    private fun getProgressBarSize(): Int {
        val rect = Rect()
        rootView.getWindowVisibleDisplayFrame(rect)
        val screenWidth = rootView.rootView.width
        return screenWidth - binding.pbHome.marginLeft * 4
    }
}