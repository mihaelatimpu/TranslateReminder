package com.mimi.translatereminder.view.learning.fragments.form

import com.mimi.translatereminder.base.BasePresenter
import com.mimi.translatereminder.base.BaseView
import com.mimi.translatereminder.repository.TranslationRepository
import com.mimi.translatereminder.view.learning.LearningContract

/**
 * Created by Mimi on 24/02/2018.
 *
 */
interface FormContract {
    interface Presenter : BasePresenter<View>, LearningContract.FragmentPresenter {
        fun setEntityId(id:Int)
        fun onOptionSelected(word: OptionWord)
        fun onResultSelected(word: OptionWord)
        fun onFinishClicked()
    }

    interface View : BaseView<Presenter> {
        fun refreshTranslation(translation:String)
        fun refreshOptions(options: List<OptionWord>)
        fun addOption(word: OptionWord)
        fun removeOption(word: OptionWord)
        fun showIncorrectDialog(translation: String, answer: String,
                                                                  correctAnswer: String, onComplete: () -> Unit)
        fun addResult(word: OptionWord)
        fun removeResult(word: OptionWord)
        fun getFinalResult():List<OptionWord>
    }
}