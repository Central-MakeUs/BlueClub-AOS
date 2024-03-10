package org.blueclub.presentation.mypage

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import org.blueclub.R
import org.blueclub.data.service.KakaoAuthService
import org.blueclub.data.service.NaverAuthService
import org.blueclub.databinding.DialogWithdrawBinding
import org.blueclub.presentation.base.BindingDialogFragment
import org.blueclub.presentation.type.LoginPlatformType
import javax.inject.Inject

@AndroidEntryPoint
class WithdrawDialog : BindingDialogFragment<DialogWithdrawBinding>(R.layout.dialog_withdraw) {
    @Inject
    lateinit var kakaoSignService: KakaoAuthService

    @Inject
    lateinit var naverSignService: NaverAuthService
    private val viewModel: ProfileSettingViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initLayout()
    }

    private fun initLayout() {
        binding.no.setOnClickListener {
            dismiss()
        }
        binding.yes.setOnClickListener {
            when (viewModel.loginPlatForm) {
                LoginPlatformType.KAKAO -> kakaoSignService.deleteAccountKakao(viewModel::deleteAccount)
                LoginPlatformType.NAVER -> naverSignService.deleteAccountNaver(viewModel::deleteAccount)
                else -> {}
            }
            dismiss()
        }
    }
}