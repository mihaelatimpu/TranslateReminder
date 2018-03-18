package com.mimi.translatereminder.view.main.main.presenters

import com.mimi.translatereminder.dto.Entity

/**
 * Created by Mimi on 18/03/2018.
 */

class FavouritesFragmentPresenter : AbstractFragmentPresenter() {
    override fun isMainButtonVisible() = true

    override fun isAddButtonVisible() = false

    override fun shouldShowItemsState() = true

    override fun retrieveItemsFromDB() = mainPresenter.getRepository().getAll()

}