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
    }
    interface Activity : BaseView<Presenter> {
        fun getRepository(): TranslationRepository
        fun getContext(): Context
        fun setFragments(items: List<Progress>)
        fun moveToFragment(position: Int)
        fun getCurrentFragmentPosition(): Int
        fun finishActivity()
        fun spellText(text:String)
    }

    interface Presenter : BasePresenter<Activity> {
        var type: Int
        var itemId: Int?
        val repo:TranslationRepository
        fun onFragmentResult(addedScore: Int = 0, entityId: Int? = null, correct: Boolean = true)
        fun onFragmentVisible(position:Int)
        fun spell(text:String)
    }

    interface FragmentPresenter {
        fun onVisibleToUser()
    }
}
