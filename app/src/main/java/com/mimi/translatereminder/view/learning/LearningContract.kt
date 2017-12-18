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
    }

    interface Presenter : BasePresenter<Activity> {
        var type: Int
        var itemId: Int?
        fun onFragmentResult(addedScore: Int = 0, entityId: Int, correct: Boolean = true)
        fun getRepository(): TranslationRepository
    }

    interface FragmentPresenter {

    }
}