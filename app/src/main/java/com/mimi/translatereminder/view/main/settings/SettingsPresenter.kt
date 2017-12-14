package com.mimi.translatereminder.view.main.settings

import com.mimi.translatereminder.view.main.MainContract
import com.mimi.translatereminder.view.main.contact.ContactContract

/**
 * Created by Mimi on 06/12/2017.
 *
 */


class SettingsPresenter: SettingsContract.Presenter{

    override lateinit var view: SettingsContract.View

    override lateinit var mainPresenter: MainContract.Presenter

    override fun isVisible() = view.isVisible()

    override fun reloadData() {

    }

    override fun start() {

    }

}