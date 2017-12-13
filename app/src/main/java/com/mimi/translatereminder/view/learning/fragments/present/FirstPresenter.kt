package com.mimi.translatereminder.view.learning.fragments.present

import com.mimi.translatereminder.dto.Entity
import com.mimi.translatereminder.view.learning.LearningContract
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.onComplete

/**
 * Created by Mimi on 13/12/2017.
 *
 */
class FirstPresenter(override var view: FirstContract.View,
                     private val masterPresenter: LearningContract.Presenter)
    : FirstContract.Presenter {
    private var entity: Entity? = null
    private val exceptionHandler: (Throwable) -> Unit = {
        it.printStackTrace()
        view.hideLoadingDialog()
        view.toast(it.message ?: "Unknown error")
    }

    override fun start() {
        view.init()
    }

    override fun setEntityId(id: Int) {
        view.showLoadingDialog()
        doAsync(exceptionHandler) {
            entity = masterPresenter.getRepository().selectItemById(id)
            onComplete {
                view.hideLoadingDialog()
                if (entity != null) {
                    view.refreshText(entity!!.germanWord, entity!!.translation)
                }
            }
        }
    }

    override fun onNextButtonPressed() {
        masterPresenter.onFragmentResult(entityId = entity!!.id)
    }

}
