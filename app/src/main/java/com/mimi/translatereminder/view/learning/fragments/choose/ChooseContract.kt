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
        fun changeBackground(green:Boolean, text:String)
    }

    interface Presenter : BasePresenter<View>, LearningContract.FragmentPresenter {
        fun setEntityId(id:Int)
        fun onAnswered(answer: String)
    }
}