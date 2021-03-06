package com.mimi.translatereminder.view.learning.fragments.choose

import com.mimi.translatereminder.base.BasePresenter
import com.mimi.translatereminder.base.BaseView
import com.mimi.translatereminder.view.learning.LearningContract

/**
 * Created by Mimi on 13/12/2017.
 *
 */
interface ChooseContract {
    interface View : BaseView<Presenter> {
        fun refreshText(translation: String, options:List<String>)
        fun changeBackground(answer: ChooseItem)
    }

    interface Presenter : BasePresenter<View>, LearningContract.FragmentPresenter {
        var type:Int
        fun setEntityId(id:Int)
        fun onAnswered(answer: ChooseItem)
    }
}