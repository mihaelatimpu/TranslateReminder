package com.mimi.translatereminder.view.learning.fragments.present

import com.mimi.translatereminder.base.BasePresenter
import com.mimi.translatereminder.base.BaseView
import com.mimi.translatereminder.view.learning.LearningContract

/**
 * Created by Mimi on 13/12/2017.
 *
 */
interface FirstContract{
    interface View: BaseView<Presenter>{
        fun refreshText(germanText:String, translation:String)
    }
    interface Presenter:BasePresenter<View>,LearningContract.FragmentPresenter{
        fun setEntityId(id:Int)
        fun onNextButtonPressed()
    }
}