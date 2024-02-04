package org.blueclub.presentation.auth.setting.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.blueclub.R
import org.blueclub.databinding.DialogTermsOfServiceBinding
import org.blueclub.presentation.WebActivity
import org.blueclub.presentation.WebActivity.Companion.ARG_WEB_VIEW_LINK
import org.blueclub.presentation.auth.setting.AuthSettingFinishActivity
import org.blueclub.presentation.auth.setting.AuthSettingViewModel
import org.blueclub.presentation.base.BindingBottomSheetDialogFragment
import org.blueclub.presentation.type.TosViewType

class TermsOfServiceBottomSheetDialog :
    BindingBottomSheetDialogFragment<DialogTermsOfServiceBinding>(R.layout.dialog_terms_of_service) {
    private val viewModel: AuthSettingViewModel by activityViewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLayout()
        collectData()
    }

    private fun initLayout() {
        binding.btnTos.setOnClickListener {
            viewModel.writeUserDetails(::moveToFinish)
        }
        binding.ivClose.setOnClickListener {
            dismiss()
        }
        binding.ivServiceDetail.setOnClickListener {
            moveToWebPage(SERVICE_LINK)
        }
        binding.ivPrivacyDetail.setOnClickListener {
            moveToWebPage(PRIVACY_LINK)
        }
        checkboxClickEvent()
    }


    private fun collectData() {
        viewModel.selectedTosType.flowWithLifecycle(lifecycle).onEach {
            if (it[TosViewType.AGE] == true && it[TosViewType.PRIVACY] == true
                && it[TosViewType.SERVICE] == true
            ) {
                binding.btnTos.isEnabled = true
                binding.btnTos.setBackgroundColor(requireActivity().getColor(R.color.coolgray_10))
            } else {
                binding.btnTos.isEnabled = false
                binding.btnTos.setBackgroundColor(requireActivity().getColor(R.color.gray_04))
            }
        }.launchIn(lifecycleScope)
    }

    private fun checkboxClickEvent() {
        binding.layoutTosEntire.setOnClickListener {
            val isSelected = viewModel.setWholeTosType()
            binding.ivCheck.isSelected = isSelected
        }
        binding.ivCheckAge.setOnClickListener {
            it.isSelected = !it.isSelected
            viewModel.setTosType(TosViewType.AGE)
        }
        binding.ivCheckEvent.setOnClickListener {
            it.isSelected = !it.isSelected
            viewModel.setTosType(TosViewType.EVENT)
        }
        binding.ivCheckService.setOnClickListener {
            it.isSelected = !it.isSelected
            viewModel.setTosType(TosViewType.SERVICE)
        }
        binding.ivCheckMarketing.setOnClickListener {
            it.isSelected = !it.isSelected
            viewModel.setTosType(TosViewType.MARKETING)
        }
        binding.ivCheckPrivacy.setOnClickListener {
            it.isSelected = !it.isSelected
            viewModel.setTosType(TosViewType.PRIVACY)
        }
    }

    private fun moveToFinish() {
        Intent(requireActivity(), AuthSettingFinishActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }.also {
            startActivity(it)
            dismiss()
        }
    }

    private fun moveToWebPage(link: String) {
        Intent(requireActivity(), WebActivity::class.java).apply {
            putExtra(ARG_WEB_VIEW_LINK, link)
        }.also { startActivity(it) }
    }

    companion object {
        private const val SERVICE_LINK =
            "https://shore-knuckle-69b.notion.site/0905a99459cf470f908018d20f0d8d72?pvs=4"
        private const val PRIVACY_LINK =
            "https://shore-knuckle-69b.notion.site/ded29418f6604ad993b0d664a653c4d7?pvs=4"
    }

}