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
interface LearningContract{
    interface Activity:BaseView<Presenter>{
        fun getRepository(): TranslationRepository
        fun getContext():Context
        fun setFragments(items:List<Progress>)
        fun moveToFragment(position:Int)
        fun getCurrentFragmentPosition():Int
        fun finishActivity()
    }
    interface Presenter:BasePresenter<Activity>{
        fun onFragmentResult(addedScore:Int = 0, entityId:Int, correct:Boolean = true)
        fun getRepository():TranslationRepository
    }
    interface FragmentPresenter{

    }
}
