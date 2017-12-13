package com.mimi.translatereminder.view.learning

import com.mimi.translatereminder.utils.LearningFragmentsGenerator
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.onComplete

/**
 * Created by Mimi on 13/12/2017.
 *
 */
class LearningPresenter : LearningContract.Presenter {
    override lateinit var view: LearningContract.Activity
    private val fragmentGen = LearningFragmentsGenerator()
    private val exceptionHandler: (Throwable) -> Unit = {
        it.printStackTrace()
        view.hideLoadingDialog()
        view.toast(it.message ?: "Unknown error")
    }

    override fun start() {
        view.init()
        reloadItems()
    }

    private fun reloadItems() {
        view.showLoadingDialog()
        doAsync(exceptionHandler) {
            val items = view.getRepository().retrieveLearningItems(view.getContext())
            val fragments = fragmentGen.start(items)
            onComplete {
                view.hideLoadingDialog()
                view.toast("FINISH")
            }
        }
    }

}