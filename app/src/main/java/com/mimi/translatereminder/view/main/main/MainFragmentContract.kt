package com.mimi.translatereminder.view.main.main

import android.support.annotation.StringRes
import com.mimi.translatereminder.base.BasePresenter
import com.mimi.translatereminder.base.BaseView
import com.mimi.translatereminder.dto.Entity
import com.mimi.translatereminder.view.main.MainContract
import com.mimi.translatereminder.view.main.MultiselectStateListener

/**
 * Created by Mimi on 06/12/2017.
 *
 */

interface MainFragmentContract {
    interface View : BaseView<Presenter> {
        fun isVisible(): Boolean
        fun refreshItems(items: List<Entity>, showState:Boolean)
        fun refreshMainOption(visible:Boolean, @StringRes text: Int)
        fun showOtherOptionsDialog(learningItems: Int, reviewItems: Int, wrongItems: Int)
        fun changeSelectableState(selectable: Boolean)
        fun getSelectedItems(): List<Entity>
        fun refreshAddButtonVisibility(visible:Boolean)
    }

    interface Presenter : MainContract.FragmentPresenter, BasePresenter<View>, MultiselectStateListener {
        fun showDetailsDialog(entityId: Int)
        fun onAddButtonClicked()
        fun onMainOptionClicked()
        fun onOtherOptionsClicked()
        fun onLearnButtonClicked()
        fun onReviewButtonClicked()
        fun onWrongButtonClicked()
        fun onListeningButtonClicked()
        fun notifyChangeState(selectable: Boolean)
        fun notifyChangeCount(count:Int)
    }
}