package org.blueclub.presentation.card

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.blueclub.R
import org.blueclub.databinding.ActivityWorkCardLoadingBinding
import org.blueclub.presentation.base.BindingActivity

class WorkCardLoadingActivity :
    BindingActivity<ActivityWorkCardLoadingBinding>(R.layout.activity_work_card_loading) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            delay(2000L)
            moveToWorkCard()
        }
    }

    private fun moveToWorkCard() {
        Intent(this, WorkCardActivity::class.java).apply {
            putExtra(ARG_WORK_BOOK_ID, intent.getIntExtra(ARG_WORK_BOOK_ID, -1))
        }.also { startActivity(it) }
        finish()
    }

    companion object{
        val ARG_WORK_BOOK_ID = "workBookId"
    }
}