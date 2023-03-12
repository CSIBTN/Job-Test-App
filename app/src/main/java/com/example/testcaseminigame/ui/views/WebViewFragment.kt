package com.example.testcaseminigame.ui.views

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.testcaseminigame.databinding.FragmentWebViewBinding
import com.example.testcaseminigame.ui.viewmodels.WebViewModel
import com.example.testcaseminigame.ui.views.base.BaseFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import com.example.testcaseminigame.util.Util.getBindingErrorString

class WebViewFragment : Fragment(), BaseFragment {

    private var _webBinding: FragmentWebViewBinding? = null
    private val webBinding: FragmentWebViewBinding
        get() = checkNotNull(_webBinding) {
            getBindingErrorString("web-view")
        }

    private val webViewModel: WebViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _webBinding = FragmentWebViewBinding.inflate(inflater, container, false)
        setUpWebView()
        setUpOnBackPressedAdapter()
        return webBinding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setUpWebView() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                webViewModel.currentLink.collectLatest { newUrl ->
                    val urlToShow = newUrl.ifEmpty { arguments?.getString("link") }
                    webBinding.wvDisplay.loadUrl("$urlToShow")
                }
            }
        }
        webBinding.wvDisplay.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                if (getView() != null)
                    viewLifecycleOwner.lifecycleScope.launch {
                        webViewModel.setUpNewCurrentLink(url.toString())
                    }
            }
        }
        webBinding.wvDisplay.settings.loadWithOverviewMode = true
        webBinding.wvDisplay.settings.javaScriptEnabled = true
    }

    override fun setUpOnBackPressedAdapter() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (webBinding.wvDisplay.canGoBack()) webBinding.wvDisplay.goBack()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _webBinding = null
    }
}