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
import org.blueclub.presentation.auth.setting.AuthSettingActivity
import org.blueclub.presentation.base.BindingActivity
import org.blueclub.util.UiState
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
                    moveToSetting()
                }
                is UiState.Error -> {

                }
                else -> {}
            }
        }.launchIn(lifecycleScope)
    }

    private fun moveToSetting() {
        startActivity(Intent(this, AuthSettingActivity::class.java))
        finish()
    }
}