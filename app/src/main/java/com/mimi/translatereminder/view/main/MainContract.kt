package com.mimi.translatereminder.view.main

import android.content.Context
import android.support.annotation.StringRes
import com.mimi.translatereminder.base.BasePresenter
import com.mimi.translatereminder.base.BaseView
import com.mimi.translatereminder.dto.Entity
import com.mimi.translatereminder.repository.TranslationRepository

/**
 * Created by Mimi on 06/12/2017.
 *
 */
interface MainContract {
    interface Activity : BaseView<Presenter> {
        fun showFragment(id: Int)
        fun getContext(): Context
        fun getRepository(): TranslationRepository
        fun startAddActivity()
        fun showConfirmDialog(title: Int, message: Int, onConfirm: () -> Unit)
        fun startEditActivity(id: Int)
        fun checkForPermission(permission: String, @StringRes title: Int,
                               @StringRes description: Int, onPermissionResult: (Boolean) -> Unit)

        fun showDetailsDialog(entityId: Int)
        fun toast(@StringRes text: Int)
        fun startLearningActivity(type: Int, reviewIds: List<Int> = arrayListOf())
    }

    interface Presenter : BasePresenter<Activity>, MultiselectStateListener {
        var fragments: List<FragmentPresenter>
        fun onNavigationItemSelected(selectionId: Int)
        fun getRepository(): TranslationRepository
        fun onAddButtonClicked()
        fun onReturnedFromActivity()
        fun editItem(item: Entity)
        fun deleteItems(items: List<Entity>)
        fun reviewItems(items: List<Entity> = listOf())
        fun listenItems(items: List<Entity>)
        fun resetItems(items: List<Entity>)
        fun showDetailsDialog(entityId: Int)
        fun learnNewWords()
        fun reviewWrongItems()
        fun startListeningActivity()
        fun onDeleteActionSelected()
        fun onCancelActionSelected()
        fun onArchiveActionSelected()
        fun onReviewActionSelected()
        fun onListenActionSelected()
        fun onStarActionSelected()
        fun checkForPermission(permission: String, @StringRes title: Int,
                               @StringRes description: Int, onPermissionResult: (Boolean) -> Unit)

        fun registerMultiSelectChangeListener(listener: MultiselectStateListener)
        fun unregisterMultiSelectChangeListener(listener: MultiselectStateListener)
    }

    interface FragmentPresenter {
        var mainPresenter: MainContract.Presenter
        fun isVisible(): Boolean
        fun reloadData()
    }
}