package org.blueclub.presentation.auth.setting

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import org.blueclub.presentation.auth.setting.fragment.JobSettingFragment
import org.blueclub.presentation.auth.setting.fragment.NicknameSettingFragment
import org.blueclub.presentation.auth.setting.fragment.YearSettingFragment

class AuthSettingAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    private val settingFragments = listOf(JobSettingFragment(), YearSettingFragment(), NicknameSettingFragment())
    override fun getItemCount(): Int = settingFragments.size
    override fun createFragment(position: Int): Fragment {
        return settingFragments[position]
    }

}