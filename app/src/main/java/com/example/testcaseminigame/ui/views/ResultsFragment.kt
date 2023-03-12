package com.example.testcaseminigame.ui.views

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.testcaseminigame.R
import com.example.testcaseminigame.databinding.DialogDadJokesBinding
import com.example.testcaseminigame.databinding.FragmentResultsBinding
import com.example.testcaseminigame.ui.viewmodels.QuizViewModel
import com.example.testcaseminigame.ui.views.base.BaseFragment
import com.example.testcaseminigame.util.Util
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import com.example.testcaseminigame.util.Util.getBindingErrorString


class ResultsFragment : Fragment(), BaseFragment {

    private var _resultsBinding: FragmentResultsBinding? = null
    private val resultsBinding: FragmentResultsBinding
        get() = checkNotNull(_resultsBinding) {
            getBindingErrorString("results")
        }

    private var _dadJokeDialogBinding: DialogDadJokesBinding? = null
    private val dadJokeDialogBinding: DialogDadJokesBinding
        get() = checkNotNull(_dadJokeDialogBinding) {
            getBindingErrorString("jokes-dialog")
        }

    private lateinit var dadJokeDialog: Dialog
    private val quizViewModel: QuizViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setUpOnBackPressedAdapter()
        _resultsBinding = FragmentResultsBinding.inflate(inflater, container, false)
        _dadJokeDialogBinding = DialogDadJokesBinding.inflate(inflater)
        return resultsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpDialogForAJoke()
        setUpRewardListeners()

    }

    private fun setUpDialogForAJoke() {
        dadJokeDialog = Dialog(requireContext())
        dadJokeDialog.setContentView(dadJokeDialogBinding.root)
        dadJokeDialog.setCancelable(false)
        viewLifecycleOwner.lifecycleScope.launch {
            quizViewModel.savedJoke.collectLatest { dadJoke ->
                dadJokeDialogBinding.tvJoke.text = dadJoke
            }
        }
        dadJokeDialogBinding.btnCancel.setOnClickListener {
            requireActivity().finish()
            dadJokeDialog.dismiss()
        }
        viewLifecycleOwner.lifecycleScope.launch {
            quizViewModel.updateJoke()
        }
    }

    private fun setUpRewardListeners() {
        resultsBinding.btnRewardOne.setOnClickListener {
            Intent(Intent.ACTION_VIEW, Uri.parse(Util.youtubeRewardUrl)).also {
                startActivity(it)
            }
            requireActivity().finish()
        }

        resultsBinding.btnRewardTwo.setOnClickListener {
            dadJokeDialog.show()
        }
    }

    override fun setUpOnBackPressedAdapter() {
        requireActivity().onBackPressedDispatcher.addCallback {
            findNavController().navigate(R.id.welcomeFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _resultsBinding = null
        _dadJokeDialogBinding = null
    }

}