package org.blueclub.presentation.auth

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class AuthSettingAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    private val settingFragments = listOf(JobSettingFragment())
    override fun getItemCount(): Int = settingFragments.size
    override fun createFragment(position: Int): Fragment {
        return settingFragments[position]
    }

}