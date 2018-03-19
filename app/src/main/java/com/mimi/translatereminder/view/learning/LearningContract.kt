package com.mimi.translatereminder.view.learning

import android.content.Context
import com.mimi.translatereminder.base.BasePresenter
import com.mimi.translatereminder.base.BaseView
import com.mimi.translatereminder.dto.Progress
import com.mimi.translatereminder.repository.TranslationRepository

/**
 * Created by Mimi on 13/12/2017.
 *
 */
interface LearningContract {
    companion object {
        val TYPE_LEARN_NEW_WORDS = 343
        val TYPE_REVIEW_WRONG_WORDS = 4353
        val TYPE_REVIEW_ITEMS = 3234
        val TYPE_LISTENING = 567
    }

    interface Activity : BaseView<Presenter> {
        fun getRepository(): TranslationRepository
        fun getContext(): Context
        fun setFragments(items: List<Progress>)
        fun moveToFragment(position: Int)
        fun getCurrentFragmentPosition(): Int
        fun finishActivity()
        fun spellText(text: String, onFinish: () -> Unit)
        fun spellInNativeLanguage(text: String, onFinish: () -> Unit)
        fun setSpellCheckboxVisibility(visible: Boolean)
    }

    interface Presenter : BasePresenter<Activity> {
        var type: Int
        var itemIds: List<Int>
        val repo: TranslationRepository
        fun onFragmentResult(addedScore: Int = 0, entityId: Int? = null, correct: Boolean = true)
        fun onFragmentVisible(position: Int)
        fun spell(text: String, onFinish: () -> Unit = {})
        fun onSpellNativeChanged(activated: Boolean)
    }

    interface FragmentPresenter {
        fun onVisibleToUser()
    }
}
