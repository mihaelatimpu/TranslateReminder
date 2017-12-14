package com.mimi.translatereminder.view.main.about

import com.mimi.translatereminder.view.main.MainContract

/**
 * Created by Mimi on 06/12/2017.
 *
 */
class AboutPresenter:AboutContract.Presenter{

    override lateinit var view: AboutContract.View

    override lateinit var mainPresenter: MainContract.Presenter

    override fun isVisible() = view.isVisible()

    override fun reloadData() {

    }

    override fun start() {

    }

}