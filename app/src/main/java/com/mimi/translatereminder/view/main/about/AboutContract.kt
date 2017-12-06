package com.mimi.translatereminder.view.main.about

import com.mimi.translatereminder.base.BasePresenter
import com.mimi.translatereminder.base.BaseView
import com.mimi.translatereminder.view.main.MainContract

/**
 * Created by Mimi on 06/12/2017.
 *
 */
interface AboutContract {
    interface View : BaseView<Presenter>
    interface Presenter : MainContract.FragmentPresenter<View>
}