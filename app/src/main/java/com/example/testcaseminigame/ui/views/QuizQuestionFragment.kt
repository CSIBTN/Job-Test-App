package com.example.testcaseminigame.ui.views

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.testcaseminigame.R
import com.example.testcaseminigame.databinding.DialogQuizBinding
import com.example.testcaseminigame.databinding.FragmentQuizBinding
import com.example.testcaseminigame.ui.viewmodels.QuizViewModel
import com.example.testcaseminigame.ui.views.base.BaseFragment
import com.example.testcaseminigame.util.Util
import com.example.testcaseminigame.util.Util.Status
import com.example.testcaseminigame.util.Util.getBindingErrorString
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.random.Random

class QuizQuestionFragment : Fragment(), BaseFragment {

    private var _quizBinding: FragmentQuizBinding? = null
    private val quizBinding: FragmentQuizBinding
        get() = checkNotNull(_quizBinding) {
            getBindingErrorString("quiz")
        }

    private var _quizDialogBinding: DialogQuizBinding? = null
    private val quizDialogBinding: DialogQuizBinding
        get() = checkNotNull(_quizDialogBinding) {
            getBindingErrorString("quiz-dialog")
        }

    private lateinit var dialog: Dialog
    private val quizViewModel: QuizViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        setUpOnBackPressedAdapter()
        _quizBinding = FragmentQuizBinding.inflate(inflater, container, false)
        _quizDialogBinding = DialogQuizBinding.inflate(inflater)
        createDialog(quizDialogBinding)
        return quizBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViewForCurrentQuestion()
        setUpAnswerListeners()
    }

    private fun createDialog(quizDialogBinding: DialogQuizBinding) {
        dialog = Dialog(requireContext())
        dialog.setContentView(quizDialogBinding.root)
        dialog.setCancelable(false)
    }

    private fun setUpViewForCurrentQuestion() {
        viewLifecycleOwner.lifecycleScope.launch {
            quizViewModel.setDefaultCurrentQuestion()
            quizViewModel.currentQuestion.collectLatest { currentQuestion ->
                updateUI(currentQuestion)
            }
        }
    }

    private fun updateUI(currentQuestionID: Int) {
        if (currentQuestionID == 6) {
            findNavController().navigate(
                R.id.resultsFragment
            )
        } else {
            val currentQuestion = quizViewModel.getQuestionById(currentQuestionID)
            quizBinding.ivQuestionImage.setImageResource(currentQuestion.imageDrawable)
            quizBinding.tvQuestionField.text = currentQuestion.questionText
            when (currentQuestionID) {
                1, 2, 4, 5 -> {
                    if (currentQuestionID == 4) {
                        quizBinding.tvFirstAnswer.visibility = View.VISIBLE
                        quizBinding.tvSecondAnswer.visibility = View.VISIBLE
                        quizBinding.tvThirdAnswer.visibility = View.VISIBLE
                        quizBinding.tvFourthAnswer.visibility = View.VISIBLE
                        quizBinding.etEnteredAnswer.visibility = View.GONE
                        quizBinding.btnAnsweredButton.visibility = View.GONE
                    }
                    quizBinding.tvFirstAnswer.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.answer_background_color
                        )
                    )
                    quizBinding.tvSecondAnswer.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.answer_background_color
                        )
                    )
                    quizBinding.tvThirdAnswer.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.answer_background_color
                        )
                    )
                    quizBinding.tvFourthAnswer.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.answer_background_color
                        )
                    )

                    quizBinding.tvFirstAnswer.text = currentQuestion.possibleAnswers[0]
                    quizBinding.tvSecondAnswer.text = currentQuestion.possibleAnswers[1]
                    quizBinding.tvThirdAnswer.text = currentQuestion.possibleAnswers[2]
                    quizBinding.tvFourthAnswer.text = currentQuestion.possibleAnswers[3]
                    if (currentQuestion.possibleAnswers.size == 5) {
                        quizBinding.tvFifthAnswer.visibility = View.VISIBLE
                        quizBinding.tvFifthAnswer.text = currentQuestion.possibleAnswers[4]
                    }
                }
                3 -> {
                    quizBinding.tvFirstAnswer.visibility = View.GONE
                    quizBinding.tvSecondAnswer.visibility = View.GONE
                    quizBinding.tvThirdAnswer.visibility = View.GONE
                    quizBinding.tvFourthAnswer.visibility = View.GONE
                    quizBinding.tvFifthAnswer.visibility = View.GONE
                    quizBinding.etEnteredAnswer.visibility = View.VISIBLE
                    quizBinding.btnAnsweredButton.visibility = View.VISIBLE
                }
            }
        }
    }


    private fun setUpAnswerListeners() {
        quizBinding.tvFirstAnswer.setOnClickListener {
            setAnswerPickedListenerForAnswerId(1, it)
        }
        quizBinding.tvSecondAnswer.setOnClickListener {
            setAnswerPickedListenerForAnswerId(2, it)
        }
        quizBinding.tvThirdAnswer.setOnClickListener {
            setAnswerPickedListenerForAnswerId(3, it)
        }
        quizBinding.tvFourthAnswer.setOnClickListener {
            setAnswerPickedListenerForAnswerId(4, it)
        }
        quizBinding.tvFifthAnswer.setOnClickListener {
            setAnswerPickedListenerForAnswerId(5, it)
        }
        quizBinding.btnAnsweredButton.setOnClickListener {
            val valueEntered = quizBinding.etEnteredAnswer.text.toString()
            if (quizViewModel.checkIfTheRightAnswer(value = valueEntered)) {
                showDialog(Status.SUCCESS)
            } else {
                showDialog(Status.FAILURE)
            }
        }
    }

    private fun setAnswerPickedListenerForAnswerId(answerId: Int, textView: View) {
        if (quizViewModel.checkIfTheRightAnswer(answerId)) {
            textView.setBackgroundColor(Color.GREEN)
            showDialog(Status.SUCCESS)
        } else {
            textView.setBackgroundColor(Color.RED)
            showDialog(Status.FAILURE)
        }
    }

    private fun showDialog(status: Status) {
        when (status) {
            Status.FAILURE -> {
                quizDialogBinding.tvStatusMessage.text =
                    Util.failureQuoteList[Random.nextInt(0, Util.failureQuoteList.size)]
                quizDialogBinding.root.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.md_theme_light_error
                    )
                )
                quizDialogBinding.btnCancel.setOnClickListener {
                    findNavController().navigate(R.id.welcomeFragment)
                    dialog.dismiss()
                }
            }
            Status.SUCCESS -> {
                quizDialogBinding.root.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.success
                    )
                )
                quizDialogBinding.tvStatusMessage.text =
                    Util.successQuoteList[Random.nextInt(0, Util.successQuoteList.size)]
                quizDialogBinding.btnCancel.setOnClickListener {
                    quizViewModel.updateCurrentQuestion()
                    dialog.dismiss()
                }
            }
        }
        dialog.show()
    }


    override fun setUpOnBackPressedAdapter() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigate(R.id.welcomeFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _quizBinding = null
        _quizDialogBinding = null
    }

}