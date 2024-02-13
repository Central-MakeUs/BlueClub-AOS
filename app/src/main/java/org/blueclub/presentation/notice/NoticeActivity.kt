package org.blueclub.presentation.notice

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.blueclub.R
import org.blueclub.databinding.ActivityNoticeBinding
import org.blueclub.presentation.base.BindingActivity
import org.blueclub.util.UiState

@AndroidEntryPoint
class NoticeActivity :
    BindingActivity<ActivityNoticeBinding>(R.layout.activity_notice) {
    private val viewModel: NoticeViewModel by viewModels()
    private lateinit var noticeAdapter: NoticeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initLayout()
        collectData()
    }

    private fun initLayout() {
        noticeAdapter = NoticeAdapter()
        binding.ivBack.setOnClickListener {
            finish()
        }
        binding.rvNotice.apply {
            itemAnimator = null
            adapter = noticeAdapter
        }
    }

    private fun collectData() {
        viewModel.noticeUiState.flowWithLifecycle(lifecycle).onEach {
            when (it) {
                is UiState.Loading -> {
                    viewModel.getNoticeData()
                }

                is UiState.Success -> {
                    if (it.data.isEmpty()) {
                        binding.layoutZeroNotification.visibility = View.VISIBLE
                    }
                    noticeAdapter.submitList(it.data)
                }

                is UiState.Error -> {
                    binding.layoutZeroNotification.visibility = View.VISIBLE
                }

                else -> {}
            }
        }.launchIn(lifecycleScope)
    }
}