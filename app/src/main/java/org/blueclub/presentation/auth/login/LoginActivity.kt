package org.blueclub.presentation.auth.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.blueclub.R
import org.blueclub.data.service.KakaoAuthService
import org.blueclub.data.service.NaverAuthService
import org.blueclub.databinding.ActivityLoginBinding
import org.blueclub.domain.type.SignType
import org.blueclub.presentation.auth.setting.AuthSettingActivity
import org.blueclub.presentation.base.BindingActivity
import org.blueclub.presentation.home.MainActivity
import org.blueclub.util.UiState
import org.blueclub.util.extension.showToast
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : BindingActivity<ActivityLoginBinding>(R.layout.activity_login) {
    @Inject
    lateinit var kakaoAuthService: KakaoAuthService

    @Inject
    lateinit var naverAuthService: NaverAuthService
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addListeners()
        collectData()
    }

    private fun addListeners() {
        binding.btnLoginKakao.setOnClickListener {
            kakaoAuthService.loginKakao(viewModel::login)
        }
        binding.btnLoginNaver.setOnClickListener {
            naverAuthService.loginNaver(viewModel::login)
        }
    }

    private fun collectData() {
        viewModel.loginUiState.flowWithLifecycle(lifecycle).onEach {
            when (it) {
                is UiState.Success -> {
                    this.showToast("로그인에 성공했습니다.")
                    moveToNext(it.data)
                }
                is UiState.Error -> {
                    // TODO 로그인 실패했을 때 UI
                    this.showToast("로그인에 실패했습니다.")
                }
                else -> {}
            }
        }.launchIn(lifecycleScope)
    }

    private fun moveToNext(signType: SignType) {
        val nextScreen = if (signType == SignType.SIGN_UP) AuthSettingActivity::class.java
        else MainActivity::class.java
        startActivity(Intent(this, nextScreen))
        finish()
    }
}