package com.mimi.translatereminder.view.main.review

import com.mimi.translatereminder.dto.Entity


/**
 * Created by Mimi on 06/12/2017.
 *
 */


class ReviewPresenter : ReviewContract.Presenter {

    override lateinit var view: ReviewContract.View

    override fun start() {
        view.init()
    }

    override fun refreshItems(items: List<Entity>) {
        view.refreshItems(items)
    }
}