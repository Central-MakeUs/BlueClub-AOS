package org.blueclub.presentation.mypage

import android.os.Bundle
import androidx.activity.viewModels
import org.blueclub.R
import org.blueclub.databinding.ActivityProfileSettingBinding
import org.blueclub.presentation.base.BindingActivity

class ProfileSettingActivity:BindingActivity<ActivityProfileSettingBinding> (R.layout.activity_profile_setting){
    private val viewModel : ProfileSettingViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
}