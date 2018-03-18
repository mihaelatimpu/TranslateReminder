package com.mimi.translatereminder.view.learning.fragments.typing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mimi.translatereminder.R
import com.mimi.translatereminder.base.BaseFragment
import com.mimi.translatereminder.dto.Progress
import com.mimi.translatereminder.utils.Context
import com.mimi.translatereminder.view.learning.LearningActivity
import kotlinx.android.synthetic.main.fragment_typing.*
import org.jetbrains.anko.support.v4.alert

/**
 * Created by Mimi on 13/12/2017.
 *
 */
class TypingFragment : BaseFragment(), TypingContract.View {

    override val presenter: TypingContract.Presenter by lazy {
        val type = arguments?.getInt(Progress.TYPE)?:0
        TypingPresenter(this, (activity as LearningActivity).presenter, type = type)
    }


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_typing, container, false)

    override fun toast(text: String) {
        toast.setText(text)
        toast.show()
    }

    override fun onVisibleToUser() {
        super.onVisibleToUser()
        presenter.onVisibleToUser()
    }


    override val contextName = Context.Learning

    override fun startPresenter() {
        presenter.start()
    }

    override fun refreshWord(translation: String) {
        subtitle.text = translation
    }

    override fun setHint(hint: String) {
        answer.hint = hint
        hintText.text = hint
        hintText.visibility = View.VISIBLE
    }

    override fun init() {
        val id = arguments?.getInt(Progress.ENTITY_ID, 0)?:0
        if (id != 0)
            presenter.setEntityId(id)
        fab.setOnClickListener { presenter.onAnswered(answer.text.toString()) }
    }

    override fun showIncorrectDialog(translation: String, answer: String,
                                     correctAnswer: String, onComplete: () -> Unit) {
        val message = getString(R.string.incorrect_answer_message, answer, correctAnswer)
        alert(title = getString(R.string.incorrect_answer_title), message = message) {
            positiveButton(getString(R.string.ok)) { onComplete() }
        }.show()
    }


}