package com.mimi.translatereminder.view.main.review

import com.mimi.translatereminder.base.BaseView
import com.mimi.translatereminder.dto.Entity
import com.mimi.translatereminder.view.main.MainContract

/**
 * Created by Mimi on 06/12/2017.
 *
 */

interface ReviewContract {
    interface View : BaseView<Presenter> {
        fun refreshItems(items: List<Entity>)
    }

    interface Presenter : MainContract.FragmentPresenter<View>{
        fun refreshItems(items: List<Entity>)
    }
}