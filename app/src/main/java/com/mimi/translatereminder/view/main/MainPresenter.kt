package com.mimi.translatereminder.view.main

import com.mimi.translatereminder.R
import com.mimi.translatereminder.dto.Entity
import com.mimi.translatereminder.view.learning.LearningContract.Companion.TYPE_LEARN_NEW_WORDS
import com.mimi.translatereminder.view.learning.LearningContract.Companion.TYPE_LISTENING
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

    override lateinit var view: MainContract.Activity

    private var multiSelectableMode = false
    private val selectableListeners = arrayListOf<MultiselectStateListener>()

    private val exceptionHandler: (Throwable) -> Unit = {
        it.printStackTrace()
        view.hideLoadingDialog()
        view.toast(it.message ?: "Unknown error")
    }

    override fun registerMultiSelectChangeListener(listener: MultiselectStateListener) {
        selectableListeners.add(listener)
        listener.onMultiSelectChange(multiSelectableMode)
    }

    override fun unregisterMultiSelectChangeListener(listener: MultiselectStateListener) {
        selectableListeners.remove(listener)
    }

    override fun start() {
        fragments.forEach {
            it.mainPresenter = this
        }
        view.showFragment(R.id.nav_edit)
    }

    override fun getSelectedItems(): List<Entity> {
        val array = arrayListOf<Entity>()
        selectableListeners.forEach { array.addAll(it.getSelectedItems()) }
        return array
    }

    override fun onMultiSelectChange(selectable: Boolean) {
        if (multiSelectableMode == selectable) {
            return
        }
        multiSelectableMode = selectable
        selectableListeners.forEach {
            it.onMultiSelectChange(multiSelectableMode)
        }
    }

    override fun onSelectCountChanged(newCount: Int) {
        selectableListeners.forEach { it.onSelectCountChanged(newCount) }
    }

    override fun editItem(item: Entity) {
        val itemId = if (item.type == Entity.TYPE_SENTENCE) item.parentId else item.id
        view.startEditActivity(itemId)
    }

    override fun reviewItems(items: List<Entity>) {
        view.startLearningActivity(TYPE_REVIEW_ITEMS, items.map { it.id })

    }

    override fun getRepository() = view.getRepository()

    override fun deleteItems(items: List<Entity>) {
        view.showConfirmDialog(R.string.are_you_sure,
                R.string.you_cannot_restore_data) {
            view.showLoadingDialog()
            doAsync(exceptionHandler) {
                items.forEach {
                    view.getRepository().delete(it)
                }
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


    override fun reviewWrongItems() {
        view.startLearningActivity(type = TYPE_REVIEW_WRONG_WORDS)
    }

    override fun listenItems(items: List<Entity>) {
        view.startLearningActivity(type = TYPE_LISTENING, reviewIds = items.map { it.id })
    }

    override fun startListeningActivity() {
        view.startLearningActivity(type = TYPE_LISTENING)
    }

    override fun onAddButtonClicked() {
        view.startAddActivity()
    }

    override fun onReturnedFromActivity() {
        reloadData()
    }

    override fun resetItems(items: List<Entity>) {
        view.showLoadingDialog()
        doAsync(exceptionHandler) {
            items.forEach {
                it.state = Entity.firstLearningState
                it.nextReview = it.dateAdded
                it.lastReview = it.dateAdded
                getRepository().saveEntity(it)
            }
            onComplete {
                view.hideLoadingDialog()
                reloadData()
            }
        }
    }

    override fun onCancelActionSelected() {
        onMultiSelectChange(false)
    }

    override fun onDeleteActionSelected() {
        view.showConfirmDialog(R.string.are_you_sure,
                R.string.you_cannot_restore_data) {
            view.toast("Deleting items")
        }
    }

    override fun onArchiveActionSelected() {
        view.toast("Not yet..")
    }

    override fun onListenActionSelected() {
        listenItems(getSelectedItems())
    }

    override fun onReviewActionSelected() {
        reviewItems(getSelectedItems())

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

interface MultiselectStateListener {
    fun onMultiSelectChange(selectable: Boolean)
    fun onSelectCountChanged(newCount: Int)
    fun getSelectedItems(): List<Entity> = listOf()
}