package com.mimi.translatereminder.view.main.learning

import com.mimi.translatereminder.base.BaseView
import com.mimi.translatereminder.view.main.MainContract

/**
 * Created by Mimi on 06/12/2017.
 *
 */

interface LearningFragmentContract {
    interface View : BaseView<Presenter>
    interface Presenter : MainContract.FragmentPresenter<View>
}