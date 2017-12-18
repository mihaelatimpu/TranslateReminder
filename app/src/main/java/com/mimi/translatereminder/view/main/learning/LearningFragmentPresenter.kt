package com.mimi.translatereminder.view.main.learning

import com.mimi.translatereminder.view.main.MainContract
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.onComplete

/**
 * Created by Mimi on 06/12/2017.
 *
 */


class LearningFragmentPresenter : LearningFragmentContract.Presenter {
    companion object {
        val TYPE_LEARNING = 0
        val TYPE_REVIEW = 1
        val TYPE_MISTAKES = 2
    }

    override fun isVisible() = view.isVisible()

    override lateinit var mainPresenter: MainContract.Presenter

    override lateinit var view: LearningFragmentContract.View

    override var type: Int = TYPE_LEARNING

    private val exceptionHandler: (Throwable) -> Unit = {
        it.printStackTrace()
        view.hideLoadingDialog()
        view.toast(it.message ?: "Unknown error")
    }

    override fun start() {
        view.init()
        reloadData()
    }

    override fun showDetailsDialog(entityId: Int) {
        mainPresenter.showDetailsDialog(entityId)
    }

    override fun reloadData() {
        view.showLoadingDialog()
        doAsync(exceptionHandler) {
            val repo = mainPresenter.getRepository()
            val items = when (type) {
                TYPE_LEARNING -> repo.getLearningItems(50)
                TYPE_MISTAKES -> repo.getMistakenItems(50)
                TYPE_REVIEW -> repo.getReviewItems(50)
                else -> throw UnsupportedOperationException("Unknown type: $type")
            }
            onComplete {
                view.hideLoadingDialog()
                if (view.isVisible())
                    view.refreshItems(items)
            }
        }
    }

    override fun startLearning() {
        when (type) {
            TYPE_LEARNING -> mainPresenter.learnNewWords()
            TYPE_MISTAKES -> mainPresenter.reviewWrongItems()
            TYPE_REVIEW -> mainPresenter.reviewItems()
        }
    }

}