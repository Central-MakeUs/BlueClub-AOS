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
import org.blueclub.util.extension.showToast


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
        binding.btnSave.setOnClickListener { this.showToast(getString(R.string.ready)) }
        binding.btnShare.setOnClickListener { this.showToast(getString(R.string.ready)) }
    }

    private fun collectData() {
        viewModel.cardUiState.flowWithLifecycle(lifecycle).onEach {
            when (it) {
                is UiState.Loading -> {
                    viewModel.getCardInfo()
                }

                is UiState.Success -> {
                    if (it.data.rank.contains("5"))
                        binding.ivCardCoin.setImageResource(R.drawable.img_coin_gold)
                    else if (it.data.rank.contains("30"))
                        binding.ivCardCoin.setImageResource(R.drawable.img_coin_silver)
                    else
                        binding.ivCardCoin.setImageResource(R.drawable.img_coin_bronze)
                }

                else -> {}
            }
        }.launchIn(lifecycleScope)
    }
}