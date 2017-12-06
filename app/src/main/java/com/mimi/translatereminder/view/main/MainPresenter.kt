package com.mimi.translatereminder.view.main

import com.mimi.translatereminder.R
import com.mimi.translatereminder.dto.Entity
import com.mimi.translatereminder.utils.FileUtil
import com.mimi.translatereminder.utils.json.ImportUtil
import com.mimi.translatereminder.view.main.favourites.FavouritesContract
import com.mimi.translatereminder.view.main.review.ReviewContract
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.onComplete

/**
 * Created by Mimi on 06/12/2017.
 *
 */
class MainPresenter(
        val reviewPresenter: ReviewContract.Presenter,
        val favouritesPresenter: FavouritesContract.Presenter
) : MainContract.Presenter {
    init {
        reviewPresenter.mainPresenter = this
    }

    override fun editItem(item: Entity) {
        view.startEditActivity(item.id)
    }

    override fun deleteItem(item: Entity) {
        view.showConfirmDialog(R.string.are_you_sure, R.string.you_cannot_restore_data) {
            view.showLoadingDialog()
            doAsync(exceptionHandler) {
                view.getRepository().delete(item)
                onComplete {
                    view.hideLoadingDialog()
                    view.toast("Deleted")
                    reloadData()
                }
            }
        }
    }

    lateinit override var view: MainContract.Activity

    val exceptionHandler: (Throwable) -> Unit = {
        it.printStackTrace()
        view.hideLoadingDialog()
        view.toast(it.message ?: "Unknown error")
    }

    override fun start() {
        view.showFragment(R.id.nav_review)
        reloadData()
    }

    override fun onOptionItemSelected(selectionId: Int) {
        when (selectionId) {
            R.id.action_import -> importData()
            R.id.action_delete_all -> deleteAllData()
        }
    }

    override fun onPlusButtonClicked() {
        view.startAddActivity()
    }

    override fun onReturnedFromActivity() {
        reloadData()
    }

    private fun deleteAllData() {
        view.showConfirmDialog(R.string.are_you_sure, R.string.you_cannot_restore_data) {

            view.showLoadingDialog()
            doAsync(exceptionHandler) {
                view.getRepository().deleteAll()
                onComplete {
                    view.hideLoadingDialog()
                    view.toast("Deleted:")
                    reloadData()
                }
            }
        }
    }

    private fun importData() {
        view.showLoadingDialog()
        doAsync(exceptionHandler) {
            val string = FileUtil().readTextFromImportResources(view.getContext())
            ImportUtil(string, view.getRepository()).start()
            val items = view.getRepository().getAll()
            onComplete {
                view.hideLoadingDialog()
                view.toast("Loaded: ${items.size}")
                reloadData()
            }
        }
    }

    private fun reloadData() {
        view.showLoadingDialog()
        doAsync(exceptionHandler) {
            val items = view.getRepository().getAll()
            onComplete {
                view.hideLoadingDialog()
                reviewPresenter.refreshItems(items)
            }
        }

    }

    override fun onNavigationItemSelected(selectionId: Int) {
        view.showFragment(selectionId)
    }

}