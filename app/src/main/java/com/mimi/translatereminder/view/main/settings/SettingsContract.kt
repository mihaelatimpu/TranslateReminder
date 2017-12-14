package com.mimi.translatereminder.view.main.settings

import com.mimi.translatereminder.base.BasePresenter
import com.mimi.translatereminder.base.BaseView
import com.mimi.translatereminder.view.main.MainContract

/**
 * Created by Mimi on 06/12/2017.
 *
 */

interface SettingsContract {
    interface View : BaseView<Presenter>{
        fun isVisible():Boolean
    }

    interface Presenter : MainContract.FragmentPresenter, BasePresenter<View>
}