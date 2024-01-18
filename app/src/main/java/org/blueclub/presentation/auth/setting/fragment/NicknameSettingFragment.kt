package org.blueclub.presentation.auth.setting.fragment

import android.os.Bundle
import android.text.InputFilter
import android.view.View
import androidx.fragment.app.activityViewModels
import org.blueclub.R
import org.blueclub.databinding.FragmentNicknameSettingBinding
import org.blueclub.presentation.auth.setting.AuthSettingViewModel
import org.blueclub.presentation.base.BindingFragment
import java.util.regex.Pattern

class NicknameSettingFragment :
    BindingFragment<FragmentNicknameSettingBinding>(R.layout.fragment_nickname_setting) {
    private val viewModel: AuthSettingViewModel by activityViewModels()
    private var dialog = TermsOfServiceBottomSheetDialog()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        initLayout()
    }

    private fun initLayout() {
        val filterAlphaNum = InputFilter { source, _, _, _, _, _ ->
            val ps = Pattern.compile("^[ㄱ-ㅣ가-힣a-zA-Z0-9\\s]+$")
            if (!ps.matcher(source).matches()) {
                ""
            } else source
        }
        binding.etNickname.filters = arrayOf(filterAlphaNum)
        binding.btnNext.setOnClickListener {
            dialog.show(parentFragmentManager, "termsOfService")
        }
    }
}