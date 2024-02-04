package org.blueclub.presentation.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.blueclub.data.datasource.BCDataSource
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val localStorage: BCDataSource,
) : ViewModel() {

    val nickname = localStorage.nickname
    val job = localStorage.job
}