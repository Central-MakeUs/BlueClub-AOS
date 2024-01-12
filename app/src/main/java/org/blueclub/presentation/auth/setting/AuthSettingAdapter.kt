package org.blueclub.presentation.auth.setting

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import org.blueclub.presentation.auth.setting.fragment.JobSettingFragment

class AuthSettingAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    private val settingFragments = listOf(JobSettingFragment())
    override fun getItemCount(): Int = settingFragments.size
    override fun createFragment(position: Int): Fragment {
        return settingFragments[position]
    }

}