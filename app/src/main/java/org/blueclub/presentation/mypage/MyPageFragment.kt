package org.blueclub.presentation.mypage

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import org.blueclub.R
import org.blueclub.databinding.FragmentMyPageBinding
import org.blueclub.presentation.base.BindingFragment
import org.blueclub.presentation.home.HomeViewModel

class MyPageFragment : BindingFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {
    private val viewModel: HomeViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initLayout()
    }

    private fun initLayout() {
        binding.ivMoveToSetting.setOnClickListener {
            moveToProfileSetting()
        }
    }

    private fun moveToProfileSetting() {
        startActivity(Intent(requireContext(), ProfileSettingActivity::class.java))
    }

}