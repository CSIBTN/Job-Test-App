package com.example.testcaseminigame.ui.views

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.testcaseminigame.R
import com.example.testcaseminigame.databinding.ActivityMainBinding
import com.example.testcaseminigame.ui.viewmodels.MainViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        setUpSplashScreen()
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        checkRemoteConfigAndShowAppropriateFragment()
    }

    private fun setUpSplashScreen() {
        val splashScreen = installSplashScreen()
        splashScreen.apply {
            setKeepOnScreenCondition {
                mainViewModel.visible.value
            }
        }
    }

    private fun checkRemoteConfigAndShowAppropriateFragment() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
        val navController = navHostFragment?.findNavController()
        mainViewModel.fetchAndApplyRemoteConfig {
            val linkBundle = Bundle()
            linkBundle.putString("link", mainViewModel.link.value)
            lifecycleScope.launch {
                mainViewModel.savedLink.collectLatest { savedUrl ->
                    if (mainViewModel.status.value || !mainViewModel.isSavedUrlEmpty(savedUrl)) {
                        navController?.navigate(R.id.webViewFragment, linkBundle)
                    }
                }
            }
        }
    }

}