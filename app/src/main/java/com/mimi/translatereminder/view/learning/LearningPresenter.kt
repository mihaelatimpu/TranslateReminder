package com.mimi.translatereminder.view.learning

import com.mimi.translatereminder.dto.Progress
import com.mimi.translatereminder.utils.LearningFragmentsGenerator
import com.mimi.translatereminder.utils.StateUtil
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.onComplete
import org.jetbrains.anko.runOnUiThread

/**
 * Created by Mimi on 13/12/2017.
 *
 */
class LearningPresenter : LearningContract.Presenter {
    override lateinit var view: LearningContract.Activity
    private val fragmentGen = LearningFragmentsGenerator()
    private val fragments = ArrayList<Progress>()
    private val stateUtil = StateUtil()
    private val exceptionHandler: (Throwable) -> Unit = {
        it.printStackTrace()
        view.hideLoadingDialog()
        view.toast(it.message ?: "Unknown error")
    }

    override fun start() {
        view.init()
        reloadItems()
    }

    override fun getRepository() = view.getRepository()

    private fun reloadItems() {
        view.showLoadingDialog()
        doAsync(exceptionHandler) {
            val items = view.getRepository().retrieveLearningItems(view.getContext())
            fragments.clear()
            fragments.addAll(fragmentGen.start(items))
            onComplete {
                view.hideLoadingDialog()
                view.setFragments(items = fragments)
                view.moveToFragment(0)
            }
        }
    }

    override fun onFragmentResult(addedScore: Int, entityId: Int, correct: Boolean) {
        view.getContext().runOnUiThread {
            view.showLoadingDialog()
            doAsync(exceptionHandler) {
                val item = view.getRepository().selectItemById(entityId)
                if (item != null) {
                    if (correct)
                        stateUtil.increaseState(item)
                    else
                        stateUtil.setWrong(item)
                    view.getRepository().saveEntity(item)
                }
                onComplete {
                    view.hideLoadingDialog()
                    moveToNextFragment()
                }
            }
        }
    }

    private fun moveToNextFragment() {
        val currentPosition = view.getCurrentFragmentPosition()
        if (currentPosition + 1 < fragments.size)
            view.moveToFragment(currentPosition + 1)
        else
            view.finishActivity()
    }


}