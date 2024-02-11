package org.blueclub.presentation.mypage

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import org.blueclub.R
import org.blueclub.databinding.FragmentMyPageBinding
import org.blueclub.presentation.WebActivity
import org.blueclub.presentation.alert.AlertSettingActivity
import org.blueclub.presentation.base.BindingFragment
import org.blueclub.presentation.home.HomeViewModel
import org.blueclub.presentation.notice.NoticeActivity

class MyPageFragment : BindingFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {
    private val viewModel: HomeViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        initLayout()
    }

    private fun initLayout() {
        binding.ivMoveToSetting.setOnClickListener {
            moveToProfileSetting()
        }
        binding.ivArrowTos.setOnClickListener {
            moveToWebPage(SERVICE_LINK)
        }
        binding.ivArrowPrivacy.setOnClickListener {
            moveToWebPage(PRIVACY_LINK)
        }
        binding.ivArrowNotice.setOnClickListener {
            moveToNotice()
        }
        binding.ivArrowAlarm.setOnClickListener {
            moveToAlertSetting()
        }
        binding.ivNotice.setOnClickListener {
            moveToNotice()
        }
    }

    private fun moveToProfileSetting() {
        startActivity(Intent(requireContext(), ProfileSettingActivity::class.java))
    }

    private fun moveToNotice() {
        startActivity(Intent(requireContext(), NoticeActivity::class.java))
    }

    private fun moveToAlertSetting() {
        startActivity(Intent(requireContext(), AlertSettingActivity::class.java))
    }

    private fun moveToWebPage(link: String) {
        Intent(requireActivity(), WebActivity::class.java).apply {
            putExtra(WebActivity.ARG_WEB_VIEW_LINK, link)
        }.also { startActivity(it) }
    }

    companion object {
        private const val SERVICE_LINK =
            "https://shore-knuckle-69b.notion.site/0905a99459cf470f908018d20f0d8d72?pvs=4"
        private const val PRIVACY_LINK =
            "https://shore-knuckle-69b.notion.site/ded29418f6604ad993b0d664a653c4d7?pvs=4"
    }

}