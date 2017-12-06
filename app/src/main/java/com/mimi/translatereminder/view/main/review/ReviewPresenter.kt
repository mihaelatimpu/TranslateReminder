package com.mimi.translatereminder.view.main.review

import com.mimi.translatereminder.dto.Entity
import com.mimi.translatereminder.view.main.MainContract


/**
 * Created by Mimi on 06/12/2017.
 *
 */


class ReviewPresenter : ReviewContract.Presenter {
    lateinit override var mainPresenter: MainContract.Presenter
    override lateinit var view: ReviewContract.View

    override fun editItem(id: Entity) {
        mainPresenter.editItem(id)
    }

    override fun deleteItem(id: Entity) {
        mainPresenter.deleteItem(id)
    }


    override fun start() {
        view.init()
    }

    override fun refreshItems(items: List<Entity>) {
        view.refreshItems(items)
    }
}