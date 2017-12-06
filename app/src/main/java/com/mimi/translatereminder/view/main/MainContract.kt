package com.mimi.translatereminder.view.main

import android.content.Context
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
        fun startEditActivity(id:Int)
    }

    interface Presenter : BasePresenter<Activity> {
        fun onOptionItemSelected(selectionId: Int)
        fun onNavigationItemSelected(selectionId: Int)
        fun onPlusButtonClicked()
        fun onReturnedFromActivity()
        fun editItem(item: Entity)
        fun deleteItem(item: Entity)
    }

    interface FragmentPresenter<T> : BasePresenter<T>
}