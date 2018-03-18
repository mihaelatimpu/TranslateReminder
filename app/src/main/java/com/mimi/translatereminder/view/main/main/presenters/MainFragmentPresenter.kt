package com.mimi.translatereminder.view.main.main.presenters


/**
 * Created by Mimi on 06/12/2017.
 *
 */


class MainFragmentPresenter : AbstractFragmentPresenter() {
    override fun isMainButtonVisible() = true

    override fun isAddButtonVisible() = true

    override fun shouldShowItemsState() = true

    override fun retrieveItemsFromDB() = mainPresenter.getRepository().getAll()
}