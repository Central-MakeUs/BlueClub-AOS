package org.blueclub.presentation.card

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.blueclub.R
import org.blueclub.databinding.ActivityWorkCardBinding
import org.blueclub.presentation.base.BindingActivity
import org.blueclub.util.UiState
import org.blueclub.util.extension.showToast
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date


@AndroidEntryPoint
class WorkCardActivity : BindingActivity<ActivityWorkCardBinding>(R.layout.activity_work_card) {
    private val viewModel: WorkCardViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val workId = intent.getIntExtra(WorkCardLoadingActivity.ARG_WORK_BOOK_ID, -1)
        viewModel.setWorkId(workId)
        initLayout()
        collectData()
    }

    private fun initLayout() {
        binding.ivBack.setOnClickListener { finish() }
        binding.tvClose.setOnClickListener { finish() }
        binding.btnSave.setOnClickListener { viewSave(binding.workCard) }
        binding.btnShare.setOnClickListener { shareCard() }
    }

    private fun collectData() {
        viewModel.cardUiState.flowWithLifecycle(lifecycle).onEach {
            when (it) {
                is UiState.Loading -> {
                    viewModel.getCardInfo()
                }

                is UiState.Success -> {
                    if (it.data.rank.contains("5"))
                        binding.ivCardCoin.setImageResource(R.drawable.img_coin_gold)
                    else if (it.data.rank.contains("30"))
                        binding.ivCardCoin.setImageResource(R.drawable.img_coin_silver)
                    else {
                        viewModel.setHighRank(false)
                        binding.ivCardCoin.setImageResource(R.drawable.img_coin_bronze)
                    }
                }

                else -> {}
            }
        }.launchIn(lifecycleScope)
    }

    private fun getViewBitmap(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(
            view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    private fun getSaveFilePathName(): String {
        val folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            .toString()
        val fileName = SimpleDateFormat("yyyyMMddHHmmss").format(Date())
        return "$folder/$fileName.jpg"
    }

    private fun bitmapFileSave(bitmap: Bitmap, path: String) {
        runCatching {
            val fos = FileOutputStream(File(path))
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.flush()
            fos.close()
        }.onSuccess {
            this.sendBroadcast(
                Intent(
                    Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                    Uri.parse("file://" + Environment.getExternalStorageDirectory())
                )
            )
            this.showToast("갤러리에 저장되었습니다.")
        }.onFailure {
            Timber.e(it.message)
        }
    }

    private fun viewSave(view: View) {
        val bitmap = getViewBitmap(view)
        val filePath = getSaveFilePathName()
        bitmapFileSave(bitmap, filePath)
    }

    private fun shareCard() {
        val bitmapPath = MediaStore.Images.Media.insertImage(
            this.contentResolver,
            getViewBitmap(binding.workCard),
            "자랑하기",
            null
        )
        val bitmapUri = Uri.parse(bitmapPath)
        val chooserTitle = "내 근무기록 자랑하기"
        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_STREAM, bitmapUri)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.type = "image/jpeg"
        startActivity(Intent.createChooser(intent, chooserTitle))
    }
}