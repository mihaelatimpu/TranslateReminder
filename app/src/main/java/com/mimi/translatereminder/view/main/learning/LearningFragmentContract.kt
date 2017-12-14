package com.mimi.translatereminder.view.main.learning

import com.mimi.translatereminder.base.BasePresenter
import com.mimi.translatereminder.base.BaseView
import com.mimi.translatereminder.dto.Entity
import com.mimi.translatereminder.view.learning.LearningPresenter
import com.mimi.translatereminder.view.main.MainContract

/**
 * Created by Mimi on 06/12/2017.
 *
 */

interface LearningFragmentContract {
    interface View : BaseView<Presenter> {
        fun isVisible(): Boolean
        fun refreshItems(items: List<Entity>)
    }

    interface Presenter : MainContract.FragmentPresenter, BasePresenter<View> {
        var type: Int
        fun startLearning()
        fun showDetailsDialog(entityId: Int)
    }
}