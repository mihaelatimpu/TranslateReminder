package com.mimi.translatereminder.view.learning.fragments.typing

import com.mimi.translatereminder.base.BasePresenter
import com.mimi.translatereminder.base.BaseView
import com.mimi.translatereminder.view.learning.LearningContract

/**
 * Created by Mimi on 13/12/2017.
 *
 */
interface TypingContract {
    interface View : BaseView<Presenter> {
        fun refreshWord(translation: String)
        fun setHint(hint: String)
        fun showIncorrectDialog(translation: String, answer: String,
                                correctAnswer: String, onComplete: () -> Unit)
        fun refreshSaveButton(active:Boolean)
    }

    interface Presenter : BasePresenter<View>, LearningContract.FragmentPresenter {
        val type:Int
        fun setEntityId(id:Int)
        fun onAnswered(answer: String)
    }
}