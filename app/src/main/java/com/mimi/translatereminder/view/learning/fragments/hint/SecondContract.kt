package com.mimi.translatereminder.view.learning.fragments.hint

import com.mimi.translatereminder.base.BasePresenter
import com.mimi.translatereminder.base.BaseView
import com.mimi.translatereminder.view.learning.LearningContract

/**
 * Created by Mimi on 13/12/2017.
 * This fragment will require the user to input the translation after seeing the hint
 */

interface SecondContract {
    interface View : BaseView<Presenter> {
        fun refreshText(translation: String)
        fun setHint(hint: String)
        fun showIncorrectDialog(translation: String, answer: String,
                                correctAnswer: String, onComplete: () -> Unit)
    }

    interface Presenter : BasePresenter<View>, LearningContract.FragmentPresenter {
        fun setEntityId(id: Int)
        fun onAnswered(answer: String)
    }
}