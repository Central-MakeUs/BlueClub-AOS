package org.blueclub.presentation.auth.setting.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import org.blueclub.R
import org.blueclub.databinding.DialogTermsOfServiceBinding
import org.blueclub.presentation.auth.setting.AuthSettingViewModel
import org.blueclub.presentation.base.BindingBottomSheetDialogFragment

class TermsOfServiceBottomSheetDialog :
    BindingBottomSheetDialogFragment<DialogTermsOfServiceBinding>(R.layout.dialog_terms_of_service) {
    private val viewModel: AuthSettingViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}