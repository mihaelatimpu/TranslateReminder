package com.mimi.translatereminder.view.addedit

import android.support.annotation.StringRes
import com.mimi.translatereminder.base.BasePresenter
import com.mimi.translatereminder.base.BaseView
import com.mimi.translatereminder.dto.Entity
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
        fun getOriginalText():String
        fun getTranslation():String
        fun refreshSentences(items: List<Entity>)
    }

    interface Presenter : BasePresenter<View> {
        var activity: Activity
        fun editItem(id: Int)
        fun onDeleteSentence(item:Entity)
        fun onEditSentence(id: Int)
        fun onAddSentence()
        fun onAddWord(onFinish: () -> Unit = {})
    }

    interface Activity {
        fun getRepository(): TranslationRepository
        fun refreshTitle(@StringRes title: Int)
        fun showConfirmDialog(title: Int, message: Int, onConfirm: () -> Unit)
        fun showAddSentenceDialog(parentId:Int, onAdded: () -> Unit)
        fun showEditSentenceDialog(parentId:Int, entityId: Int, onFinish: () -> Unit)
    }
}