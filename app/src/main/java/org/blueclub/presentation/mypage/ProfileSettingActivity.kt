package org.blueclub.presentation.mypage

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.blueclub.R
import org.blueclub.data.service.KakaoAuthService
import org.blueclub.data.service.NaverAuthService
import org.blueclub.databinding.ActivityProfileSettingBinding
import org.blueclub.presentation.auth.login.LoginActivity
import org.blueclub.presentation.base.BindingActivity
import org.blueclub.presentation.type.LoginPlatformType
import org.blueclub.util.UiState
import org.blueclub.util.extension.showToast
import javax.inject.Inject

@AndroidEntryPoint
class ProfileSettingActivity :
    BindingActivity<ActivityProfileSettingBinding>(R.layout.activity_profile_setting) {
    @Inject
    lateinit var kakaoSignService: KakaoAuthService

    @Inject
    lateinit var naverSignService: NaverAuthService
    private val viewModel: ProfileSettingViewModel by viewModels()
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initLayout()
        collectData()
    }

    private fun initLayout() {
        binding.ivProfile.clipToOutline = true
        binding.ivCamera.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_MEDIA_IMAGES
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                openGallery()
            } else {
                requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
            }
        }
        binding.tvLogout.setOnClickListener {
            when (viewModel.loginPlatForm) {
                LoginPlatformType.KAKAO -> kakaoSignService.logoutKakao(viewModel::logout)
                LoginPlatformType.NAVER -> naverSignService.logoutNaver(viewModel::logout)
                else -> {}
            }
        }
        binding.tvWithdraw.setOnClickListener {
            when (viewModel.loginPlatForm) {
                LoginPlatformType.KAKAO -> kakaoSignService.deleteAccountKakao(viewModel::deleteAccount)
                LoginPlatformType.NAVER -> naverSignService.deleteAccountNaver(viewModel::deleteAccount)
                else -> {}
            }
        }
        binding.ivBack.setOnClickListener {
            finish()
        }
    }

    private fun collectData() {
        viewModel.logoutUiState.flowWithLifecycle(lifecycle).onEach {
            when (it) {
                is UiState.Success -> {
                    moveToSign()
                }

                else -> {}
            }
        }.launchIn(lifecycleScope)
        viewModel.deleteAccountUiState.flowWithLifecycle(lifecycle).onEach {
            when (it) {
                is UiState.Success -> {
                    this.showToast(getString(R.string.withdraw_success))
                    moveToSign()
                    finish()
                }
                is UiState.Error -> {
                    this.showToast(getString(R.string.withdraw_fail))
                }
                else -> {}
            }
        }.launchIn(lifecycleScope)
    }

    private fun moveToSign() {
        Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }.also {
            startActivity(it)
            finish()
        }
    }


    private val requestPermissionLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted)
                openGallery()
        }

    private val pickImageLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data: Intent? = result.data
                data?.data?.let {
                    imageUri = it
                    binding.ivProfile.setImageURI(imageUri)
                }
            }
        }

    private fun openGallery() {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        pickImageLauncher.launch(gallery)
    }

}