package com.mimi.translatereminder.view.main.contact

import com.mimi.translatereminder.base.BasePresenter
import com.mimi.translatereminder.base.BaseView
import com.mimi.translatereminder.view.main.MainContract

/**
 * Created by Mimi on 06/12/2017.
 *
 */

interface ContactContract {
    interface View : BaseView<Presenter>{
        fun isVisible():Boolean
    }
    interface Presenter : MainContract.FragmentPresenter, BasePresenter<View>

}