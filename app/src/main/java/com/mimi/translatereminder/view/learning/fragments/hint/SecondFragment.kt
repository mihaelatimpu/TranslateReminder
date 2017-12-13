package com.mimi.translatereminder.view.learning.fragments.hint

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
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

class SecondFragment : BaseFragment(), SecondContract.View {

    override val presenter: SecondContract.Presenter by lazy {
        SecondPresenter(this, (activity as LearningActivity).presenter)
    }


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_typing, container, false)

    override fun toast(text: String) {
        toast.setText(text)
        toast.show()
    }

    override val contextName = Context.Learning

    override fun startPresenter() {
        presenter.start()
    }

    override fun refreshText(translation: String) {
        title.text = translation
    }

    override fun setHint(hint: String) {
        subtitle.hint = hint
    }

    override fun init() {
        val id = arguments.getInt(Progress.ENTITY_ID, 0)
        if (id != 0)
            presenter.setEntityId(id)
        fab.setOnClickListener { presenter.onAnswered(subtitle.text.toString()) }
        subtitle.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == EditorInfo.IME_ACTION_DONE)
                fab.callOnClick()
            true
        }
    }

    override fun showIncorrectDialog(translation: String, answer: String, correctAnswer: String, onComplete: () -> Unit) {
        val message = getString(R.string.incorrect_answer_message, answer, correctAnswer)
        alert(title=getString(R.string.incorrect_answer_title), message = message){
            positiveButton(getString(R.string.ok)){onComplete()}
        }.show()
    }


}