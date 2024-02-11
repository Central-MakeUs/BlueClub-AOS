package org.blueclub.presentation.card

import android.os.Bundle
import androidx.activity.viewModels
import org.blueclub.R
import org.blueclub.databinding.ActivityWorkCardBinding
import org.blueclub.presentation.base.BindingActivity


class WorkCardActivity : BindingActivity<ActivityWorkCardBinding>(R.layout.activity_work_card) {
    private val viewModel: WorkCardViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initLayout()

    }

    private fun initLayout(){
        binding.ivBack.setOnClickListener { finish() }
        binding.tvClose.setOnClickListener { finish() }
    }
}