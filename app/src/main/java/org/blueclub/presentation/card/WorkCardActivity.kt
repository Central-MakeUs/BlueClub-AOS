package org.blueclub.presentation.card

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.blueclub.R
import org.blueclub.databinding.ActivityWorkCardBinding
import org.blueclub.presentation.base.BindingActivity
import org.blueclub.util.UiState


@AndroidEntryPoint
class WorkCardActivity : BindingActivity<ActivityWorkCardBinding>(R.layout.activity_work_card) {
    private val viewModel: WorkCardViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val workId = intent.getIntExtra(WorkCardLoadingActivity.ARG_WORK_BOOK_ID, -1)
        viewModel.setWorkId(workId)
        initLayout()
        collectData()
    }

    private fun initLayout() {
        binding.ivBack.setOnClickListener { finish() }
        binding.tvClose.setOnClickListener { finish() }
    }

    private fun collectData() {
        viewModel.cardUiState.flowWithLifecycle(lifecycle).onEach {
            when (it) {
                is UiState.Loading -> {
                    viewModel.getCardInfo()
                }

                else -> {}
            }
        }.launchIn(lifecycleScope)
    }
}