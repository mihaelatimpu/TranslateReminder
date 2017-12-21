package com.mimi.translatereminder.view.learning.fragments.typing

import com.mimi.translatereminder.dto.Entity
import com.mimi.translatereminder.dto.Progress
import com.mimi.translatereminder.view.learning.LearningContract
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.onComplete

/**
 * Created by Mimi on 13/12/2017.
 *
 */

class TypingPresenter(override var view: TypingContract.View,
                      private val masterPresenter: LearningContract.Presenter,
                      override val type: Int) : TypingContract.Presenter {
    private val exceptionHandler: (Throwable) -> Unit = {
        it.printStackTrace()
        view.hideLoadingDialog()
        view.toast(it.message ?: "Unknown error")
    }
    var entity: Entity? = null

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
                    if (type == Progress.TYPE_HINT) {
                        view.setHint(hint = entity!!.germanWord)
                    }
                    view.refreshWord(entity!!.translation)
                }
            }
        }
    }

    override fun onVisibleToUser() {
    }

    override fun onAnswered(answer: String) {
        val myEntity = entity ?: return
        if (type == Progress.TYPE_TYPING)
            masterPresenter.spell(myEntity.germanWord)
        if (answer.toLowerCase() == myEntity.germanWord.toLowerCase())
            masterPresenter.onFragmentResult(addedScore = 20, entityId = myEntity.id, correct = true)
        else
            view.showIncorrectDialog(translation = myEntity.translation,
                    answer = answer, correctAnswer = myEntity.germanWord) {
                masterPresenter.onFragmentResult(entityId = myEntity.id, correct = false)
            }
    }

}
