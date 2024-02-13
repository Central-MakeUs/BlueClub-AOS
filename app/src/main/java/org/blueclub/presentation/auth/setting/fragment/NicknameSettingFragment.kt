package org.blueclub.presentation.auth.setting.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.blueclub.R
import org.blueclub.databinding.FragmentNicknameSettingBinding
import org.blueclub.presentation.auth.setting.AuthSettingViewModel
import org.blueclub.presentation.base.BindingFragment
import org.blueclub.presentation.type.NicknameGuideType

class NicknameSettingFragment :
    BindingFragment<FragmentNicknameSettingBinding>(R.layout.fragment_nickname_setting) {
    private val viewModel: AuthSettingViewModel by activityViewModels()
    private var dialog = TermsOfServiceBottomSheetDialog()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        initLayout()
        collectData()
    }

    private fun initLayout() {
        binding.btnNext.setOnClickListener {
            dialog.show(parentFragmentManager, "termsOfService")
        }
        binding.btnCheckDuplication.setOnClickListener {
            viewModel.checkNicknameDuplication(binding.etNickname.text.toString())
        }
    }

    private fun collectData() {
        viewModel.nickname.flowWithLifecycle(lifecycle).onEach {
            if(it.isNullOrEmpty()){
                viewModel.setNicknameInputGuide(NicknameGuideType.DEFAULT)
            }
            viewModel.setNicknameCorrect(verifyNickname(it ?: ""))
            viewModel.setNicknameAvailable(null)
        }.launchIn(lifecycleScope)
        viewModel.isNicknameCorrect.observe(viewLifecycleOwner) {
            if (it)
                viewModel.setNicknameInputGuide(NicknameGuideType.DEFAULT)
            else
                viewModel.setNicknameInputGuide(NicknameGuideType.INVALID_NICKNAME)
        }
    }

    fun verifyNickname(nickname: String): Boolean =
        nickname.matches(Regex("^[ㄱ-ㅣ가-힣a-zA-Z0-9]{0,10}$"))

}