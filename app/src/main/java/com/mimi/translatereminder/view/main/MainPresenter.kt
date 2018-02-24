package com.mimi.translatereminder.view.main

import com.mimi.translatereminder.R
import com.mimi.translatereminder.dto.Entity
import com.mimi.translatereminder.utils.FileUtil
import com.mimi.translatereminder.utils.json.ExportUtil
import com.mimi.translatereminder.utils.json.ImportUtil
import com.mimi.translatereminder.view.learning.LearningContract.Companion.TYPE_LEARN_NEW_WORDS
import com.mimi.translatereminder.view.learning.LearningContract.Companion.TYPE_REVIEW_ITEMS
import com.mimi.translatereminder.view.learning.LearningContract.Companion.TYPE_REVIEW_WRONG_WORDS
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.onComplete

/**
 * Created by Mimi on 06/12/2017.
 *
 */
class MainPresenter : MainContract.Presenter {
    override var fragments: List<MainContract.FragmentPresenter> = listOf()

    lateinit override var view: MainContract.Activity

    private val exceptionHandler: (Throwable) -> Unit = {
        it.printStackTrace()
        view.hideLoadingDialog()
        view.toast(it.message ?: "Unknown error")
    }

    override fun start() {
        fragments.forEach {
            it.mainPresenter = this
        }
        view.showFragment(R.id.nav_edit)
    }


    override fun editItem(item: Entity) {
        val itemId = if(item.type == Entity.TYPE_SENTENCE)  item.parentId else item.id
        view.startEditActivity(itemId)
    }

    override fun reviewItem(item: Entity) {
        view.startLearningActivity(TYPE_REVIEW_ITEMS, item.id)
    }

    override fun getRepository() = view.getRepository()

    override fun deleteItem(item: Entity) {
        view.showConfirmDialog(R.string.are_you_sure,
                R.string.you_cannot_restore_data) {
            view.showLoadingDialog()
            doAsync(exceptionHandler) {
                view.getRepository().delete(item)
                onComplete {
                    view.hideLoadingDialog()
                    reloadData()
                }
            }
        }
    }

    override fun showDetailsDialog(entityId: Int) {
        view.showDetailsDialog(entityId)
    }

    override fun learnNewWords() {
        view.startLearningActivity(type = TYPE_LEARN_NEW_WORDS)
    }

    override fun reviewItems() {
        view.startLearningActivity(type = TYPE_REVIEW_ITEMS)
    }

    override fun reviewWrongItems() {
        view.startLearningActivity(type = TYPE_REVIEW_WRONG_WORDS)

    }

    override fun onAddButtonClicked() {
        view.startAddActivity()
    }

    override fun onReturnedFromActivity() {
        reloadData()
    }

    override fun resetItems(item: Entity) {
        view.showLoadingDialog()
        doAsync(exceptionHandler) {
            item.state = Entity.firstLearningState
            item.nextReview = item.dateAdded
            item.lastReview = item.dateAdded
            getRepository().saveEntity(item)
            onComplete {
                view.hideLoadingDialog()
                reloadData()
            }
        }
    }


    override fun checkForPermission(permission: String, title: Int,
                                    description: Int, onPermissionResult: (Boolean) -> Unit) {
        view.checkForPermission(permission, title, description, onPermissionResult)
    }


    private fun reloadData() {
        fragments.forEach {
            if (it.isVisible())
                it.reloadData()
        }
    }

    override fun onNavigationItemSelected(selectionId: Int) {
        view.showFragment(selectionId)
        // reloadData()
    }

}