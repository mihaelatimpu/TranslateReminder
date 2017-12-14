package com.mimi.translatereminder.view.main.edit

import com.mimi.translatereminder.dto.Entity
import com.mimi.translatereminder.view.main.MainContract
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.onComplete


/**
 * Created by Mimi on 06/12/2017.
 *
 */


class EditPresenter : EditContract.Presenter {

    lateinit override var mainPresenter: MainContract.Presenter
    override lateinit var view: EditContract.View

    private val exceptionHandler: (Throwable) -> Unit = {
        it.printStackTrace()
        view.hideLoadingDialog()
        view.toast(it.message ?: "Unknown error")
    }

    override fun isVisible() = view.isVisible()

    override fun showDetailsDialog(entityId: Int) {
        mainPresenter.showDetailsDialog(entityId)
    }


    override fun start() {
        view.init()
        reloadData()
    }

    override fun reloadData() {
        if (!view.isVisible())
            return
        view.showLoadingDialog()
        doAsync(exceptionHandler) {
            val items = mainPresenter.getRepository().getAll()
            onComplete {
                view.hideLoadingDialog()
                view.refreshItems(items)
            }
        }
    }

    override fun onAddButtonClicked() {
        mainPresenter.onAddButtonClicked()
    }
}