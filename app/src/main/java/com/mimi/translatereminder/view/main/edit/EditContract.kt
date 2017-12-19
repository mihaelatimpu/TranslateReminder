package com.mimi.translatereminder.view.main.edit

import android.support.annotation.StringRes
import com.mimi.translatereminder.base.BasePresenter
import com.mimi.translatereminder.base.BaseView
import com.mimi.translatereminder.dto.Entity
import com.mimi.translatereminder.view.main.MainContract

/**
 * Created by Mimi on 06/12/2017.
 *
 */

interface EditContract {
    interface View : BaseView<Presenter> {
        fun isVisible():Boolean
        fun refreshItems(items: List<Entity>)
        fun refreshMainOption(@StringRes text:Int)
        fun showOtherOptionsDialog(learningItems:Int, reviewItems:Int,wrongItems:Int)
    }

    interface Presenter : MainContract.FragmentPresenter, BasePresenter<View> {
        fun showDetailsDialog(entityId: Int)
        fun onAddButtonClicked()
        fun onMainOptionClicked()
        fun onOtherOptionsClicked()
        fun onLearnButtonClicked()
        fun onReviewButtonClicked()
        fun onWrongButtonClicked()
    }
}