package com.mimi.translatereminder.view.main.main.presenters

import com.mimi.translatereminder.R
import com.mimi.translatereminder.dto.Entity
import com.mimi.translatereminder.view.main.MainContract
import com.mimi.translatereminder.view.main.main.MainFragmentContract
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.onComplete
import java.util.*
import kotlin.collections.ArrayList


/**
 * Created by Mimi on 06/12/2017.
 *
 */


abstract class AbstractFragmentPresenter : MainFragmentContract.Presenter {
    companion object {
        const val TYPE_LEARN = 0
        const val TYPE_REVIEW = 1
        const val TYPE_WRONG = 2
    }

    override lateinit var mainPresenter: MainContract.Presenter
    override lateinit var view: MainFragmentContract.View
    private var totalItems = 0
    private var learningItems = 0
    private var reviewItems = 0
    private var wrongItems = 0
    private var mainOptionType = TYPE_LEARN


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
        mainPresenter.unregisterMultiSelectChangeListener(this)
        mainPresenter.registerMultiSelectChangeListener(this)
        reloadData()
    }

    override fun getSelectedItems(): List<Entity> {
        return view.getSelectedItems()
    }

    override fun onMultiSelectChange(selectable: Boolean) {
        view.changeSelectableState(selectable)
    }

    override fun notifyChangeState(selectable: Boolean) {
        mainPresenter.onMultiSelectChange(selectable)
    }

    override fun notifyChangeCount(count: Int) {
        mainPresenter.onSelectCountChanged(count)
    }

    override fun onSelectCountChanged(newCount: Int) {}

    override fun reloadData() {
        view.showLoadingDialog()
        doAsync(exceptionHandler) {
            val items = retrieveItemsFromDB()
            totalItems = items.size
            learningItems = items.filter { it.isLearning() }.size
            wrongItems = items.filter { it.isWrong() }.size
            reviewItems = items.filter {
                it.isReviewing() &&
                        it.nextReview <= Calendar.getInstance().timeInMillis
            }.size
            onComplete {
                view.hideLoadingDialog()
                val displayItems = ArrayList<Entity>()
                displayItems.addAll(items.filter { it.isWrong() })
                displayItems.addAll(items.filter { !it.isWrong() })
                view.refreshItems(displayItems, shouldShowItemsState())
                mainOptionType = getMainOptionType()
                view.refreshMainOption(isMainButtonVisible(), getMainOptionText())
                view.refreshAddButtonVisibility(isAddButtonVisible())
            }
        }
    }

    private fun getMainOptionText(): Int {
        return when (mainOptionType) {
            TYPE_LEARN -> R.string.learn_new_items
            TYPE_REVIEW -> R.string.review_items
            TYPE_WRONG -> R.string.review_wrong_items
            else -> throw UnsupportedOperationException("Unknown type $mainOptionType")
        }
    }

    private fun getMainOptionType(): Int {
        if (wrongItems > 0)
            return TYPE_WRONG
        if (reviewItems > 0)
            return TYPE_REVIEW
        if (learningItems > 0)
            return TYPE_LEARN
        return TYPE_REVIEW
    }

    override fun onLearnButtonClicked() {
        mainPresenter.learnNewWords()
    }

    override fun onReviewButtonClicked() {
        mainPresenter.reviewItems(view.getFilteredItems()
                .filter { it.isReviewing() && it.nextReview < Calendar.getInstance().timeInMillis })
    }

    override fun onWrongButtonClicked() {
        mainPresenter.reviewWrongItems()
    }

    override fun onOtherOptionsClicked() {
        view.showOtherOptionsDialog(totalItems, learningItems,
                reviewItems, wrongItems)
    }

    override fun onListeningButtonClicked() {
        mainPresenter.listenItems(view.getFilteredItems())
    }

    override fun onMainOptionClicked() {
        when (mainOptionType) {
            TYPE_LEARN -> onLearnButtonClicked()
            TYPE_REVIEW -> onReviewButtonClicked()
            TYPE_WRONG -> onWrongButtonClicked()
            else -> throw UnsupportedOperationException("Unknown type $mainOptionType")
        }
    }

    override fun onAddButtonClicked() {
        mainPresenter.onAddButtonClicked()
    }

    abstract fun retrieveItemsFromDB(): List<Entity>
    abstract fun isMainButtonVisible(): Boolean
    abstract fun isAddButtonVisible(): Boolean
    abstract fun shouldShowItemsState(): Boolean

}