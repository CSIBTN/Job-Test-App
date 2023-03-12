package com.example.testcaseminigame.ui.views

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.testcaseminigame.R
import com.example.testcaseminigame.databinding.FragmentWelcomeBinding
import com.example.testcaseminigame.ui.views.base.BaseFragment
import com.example.testcaseminigame.util.Util
import com.example.testcaseminigame.util.Util.getBindingErrorString

class WelcomeFragment : Fragment(), BaseFragment {
    private var _welcomeBinding: FragmentWelcomeBinding? = null
    private val welcomeBinding: FragmentWelcomeBinding
        get() = checkNotNull(_welcomeBinding) {
            getBindingErrorString("welcome")
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _welcomeBinding = FragmentWelcomeBinding.inflate(inflater, container, false)
        setUpOnBackPressedAdapter()
        return welcomeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpWelcomeView()
    }


    private fun setUpWelcomeView() {
        welcomeBinding.tvAppDescription.text = getAppDescription()
        welcomeBinding.btnStart.setOnClickListener {
            findNavController().navigate(
                R.id.quizQuestionFragment
            )
        }
    }

    private fun getAppDescription(): SpannableString {
        val sString = SpannableString(Util.welcomeAppDescription)
        val colorSpanRed = ForegroundColorSpan(Color.RED)
        sString.setSpan(colorSpanRed, 14, 19, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return sString
    }

    override fun setUpOnBackPressedAdapter() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            requireActivity().finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _welcomeBinding = null
    }
}
