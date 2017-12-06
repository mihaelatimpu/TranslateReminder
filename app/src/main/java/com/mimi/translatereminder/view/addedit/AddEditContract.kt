package com.mimi.translatereminder.view.addedit

import android.support.annotation.StringRes
import com.mimi.translatereminder.base.BasePresenter
import com.mimi.translatereminder.base.BaseView
import com.mimi.translatereminder.repository.TranslationRepository

/**
 * Created by Mimi on 05/12/2017.
 *
 */
interface AddEditContract {
    interface View : BaseView<Presenter> {
        fun showGermanError(@StringRes text: Int)
        fun showTranslationError(@StringRes text: Int)
        fun clearErrors()
        fun clearText()
        fun refreshText(german: String, translation: String)
        fun refreshAddButton(@StringRes text: Int)
    }

    interface Presenter : BasePresenter<View> {
        var activity: Activity
        fun onAddButtonPressed(german: String, translation: String)
        fun editItem(id: Int)
    }

    interface Activity {
        fun getRepository(): TranslationRepository
        fun returnToPreviousActivity()
        fun refreshTitle(@StringRes title: Int)
    }
}