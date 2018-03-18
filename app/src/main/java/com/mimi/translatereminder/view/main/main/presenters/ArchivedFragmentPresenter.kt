package com.mimi.translatereminder.view.main.main.presenters

import com.mimi.translatereminder.dto.Entity

/**
 * Created by Mimi on 18/03/2018.
 */

class ArchivedFragmentPresenter : AbstractFragmentPresenter() {
    override fun isMainButtonVisible() = false

    override fun isAddButtonVisible() = false

    override fun shouldShowItemsState() = false

    override fun retrieveItemsFromDB() = mainPresenter.getRepository().getArchivedItems()
}