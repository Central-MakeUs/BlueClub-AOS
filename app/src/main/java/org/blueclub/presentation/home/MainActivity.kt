package org.blueclub.presentation.home

import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import org.blueclub.R
import org.blueclub.databinding.ActivityMainBinding
import org.blueclub.presentation.base.BindingActivity

@AndroidEntryPoint
class MainActivity : BindingActivity<ActivityMainBinding>(R.layout.activity_main) {
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initLayout()
    }

    private fun initLayout(){
        binding.bnvMain.itemIconTintList = null
        val navController =
            supportFragmentManager.findFragmentById(R.id.fcv_main)?.findNavController()
        navController?.let {
            binding.bnvMain.setupWithNavController(it)
        }
    }
}