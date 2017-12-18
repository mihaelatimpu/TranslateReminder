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
        fun refreshText(translation: String)
        fun showIncorrectDialog(translation: String, answer: String,
                                correctAnswer: String, onComplete: () -> Unit)
    }

    interface Presenter : BasePresenter<View>, LearningContract.FragmentPresenter {
        fun setEntityId(id:Int)
        fun onAnswered(answer: String)
    }
}