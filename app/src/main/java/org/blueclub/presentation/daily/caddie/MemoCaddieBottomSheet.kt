package org.blueclub.presentation.daily.caddie

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.blueclub.R
import org.blueclub.databinding.DialogCaddieMemoBinding
import org.blueclub.presentation.base.BindingBottomSheetDialogFragment

class MemoCaddieBottomSheet :
    BindingBottomSheetDialogFragment<DialogCaddieMemoBinding>(R.layout.dialog_caddie_memo) {
    private val viewModel: WorkDetailCaddieViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val behavior = BottomSheetBehavior.from(binding.bottomSheet)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        behavior.isDraggable = false

        initLayout()
        collectData()
    }

    private fun initLayout() {
        binding.ivClose.setOnClickListener {
            dismiss()
        }
    }

    private fun collectData() {
        viewModel.memo.flowWithLifecycle(lifecycle).onEach {

        }.launchIn(lifecycleScope)
    }
}