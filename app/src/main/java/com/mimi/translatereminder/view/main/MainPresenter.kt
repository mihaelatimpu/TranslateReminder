package com.mimi.translatereminder.view.main

import com.mimi.translatereminder.R
import com.mimi.translatereminder.dto.Entity
import com.mimi.translatereminder.utils.FileUtil
import com.mimi.translatereminder.utils.json.ExportUtil
import com.mimi.translatereminder.utils.json.ImportUtil
import com.mimi.translatereminder.view.main.learning.LearningFragmentContract
import com.mimi.translatereminder.view.main.edit.EditContract
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.onComplete

/**
 * Created by Mimi on 06/12/2017.
 *
 */
class MainPresenter(
        private val editPresenter: EditContract.Presenter,
        val learningFragmentPresenter: LearningFragmentContract.Presenter
) : MainContract.Presenter {

    lateinit override var view: MainContract.Activity

    private val exceptionHandler: (Throwable) -> Unit = {
        it.printStackTrace()
        view.hideLoadingDialog()
        view.toast(it.message ?: "Unknown error")
    }

    override fun start() {
        view.showFragment(R.id.nav_review)
        reloadData()
    }

    init {
        editPresenter.mainPresenter = this
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

    override fun onOptionItemSelected(selectionId: Int) {
        when (selectionId) {
            R.id.action_import -> importData()
            R.id.action_export -> exportData()
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
                    view.toast(R.string.deleted)
                    reloadData()
                }
            }
        }
    }

    private fun exportData() {
        view.checkForPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                title = R.string.missing_permission, description = R.string.please_grant_permission) {
            if (!it)
                return@checkForPermission
            view.showLoadingDialog()
            doAsync(exceptionHandler) {
                val exportedJson = ExportUtil(view.getRepository().getAll()).start()
                val exportedFile = FileUtil().writeExportedDataToNewFile(exportedJson)
                onComplete {
                    view.hideLoadingDialog()
                    view.toast(R.string.exported)
                    view.toast(exportedFile?:"")
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
                editPresenter.refreshItems(items)
            }
        }

    }

    override fun onNavigationItemSelected(selectionId: Int) {
        view.showFragment(selectionId)
    }

}