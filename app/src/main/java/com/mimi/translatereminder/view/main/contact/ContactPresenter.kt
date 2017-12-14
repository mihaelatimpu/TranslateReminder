package com.mimi.translatereminder.view.main.contact

import com.mimi.translatereminder.view.main.MainContract

/**
 * Created by Mimi on 06/12/2017.
 *
 */

class ContactPresenter: ContactContract.Presenter{

    override lateinit var view: ContactContract.View
    override lateinit var mainPresenter: MainContract.Presenter

    override fun isVisible() = view.isVisible()

    override fun reloadData() {

    }

    override fun start() {

    }

}