package com.mimi.translatereminder.view.main.settings

import android.content.Context
import android.support.annotation.StringRes
import com.mimi.translatereminder.base.BasePresenter
import com.mimi.translatereminder.base.BaseView
import com.mimi.translatereminder.view.main.MainContract

/**
 * Created by Mimi on 06/12/2017.
 *
 */

interface SettingsContract {
    interface View : BaseView<Presenter> {
        fun isVisible(): Boolean
        fun getContext():Context
        fun toast(@StringRes string:Int)
        fun refreshItems(spellWords: Boolean, reviewItems: Int, learningItems: Int, wrongItems: Int, language: String)
    }

    interface Presenter : MainContract.FragmentPresenter, BasePresenter<View> {
        fun importItems()
        fun exportItems()
        fun onReviewChanged(items: Int)
        fun onLearningChanged(items: Int)
        fun onWrongChanged(items: Int)
        fun onSpellWordsChanged(active: Boolean)
        fun onLanguageChanged(language: String)
    }
}