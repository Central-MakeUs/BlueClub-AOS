package org.blueclub.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import org.blueclub.R
import org.blueclub.databinding.ActivityWebviewBinding
import org.blueclub.presentation.base.BindingActivity

class WebActivity : BindingActivity<ActivityWebviewBinding>(R.layout.activity_webview) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initLayout()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initLayout() {
        binding.webView.apply {
            webViewClient = WebViewClient()
            webChromeClient = WebChromeClient()

            settings.apply {
                javaScriptEnabled = true
                javaScriptCanOpenWindowsAutomatically = true
                loadWithOverviewMode = true
                useWideViewPort = true
                domStorageEnabled = true
                setSupportZoom(true)
            }

            intent.getStringExtra(ARG_WEB_VIEW_LINK)?.let { loadUrl(it) }
        }
    }

    companion object {
        const val ARG_WEB_VIEW_LINK = "webviewLink"
    }
}