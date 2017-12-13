package com.mimi.translatereminder.view.learning

import android.content.Context
import com.mimi.translatereminder.base.BasePresenter
import com.mimi.translatereminder.base.BaseView
import com.mimi.translatereminder.repository.TranslationRepository

/**
 * Created by Mimi on 13/12/2017.
 *
 */
interface LearningContract{
    interface Activity:BaseView<Presenter>{
        fun getRepository(): TranslationRepository
        fun getContext():Context
    }
    interface Presenter:BasePresenter<Activity>{

    }
    interface FragmentPresenter{

    }
}
