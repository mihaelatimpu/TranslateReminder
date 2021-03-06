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
        view.refreshSaveButton(false)
        view.showLoadingDialog()
        doAsync(exceptionHandler) {
            entity = masterPresenter.repo.selectItemById(id)
            onComplete {
                view.hideLoadingDialog()
                if (entity != null) {
                    if (type == Progress.TYPE_HINT) {
                        view.setHint(hint = entity!!.germanWord)
                    }
                    view.refreshWord(entity!!.translation)
                    view.refreshSaveButton(true)
                }
            }
        }
    }

    override fun onVisibleToUser() {
    }

    override fun onAnswered(answer: String) {
        view.refreshSaveButton(false)
        val myEntity = entity ?: return
        if (answer.toLowerCase().trim() == myEntity.germanWord.toLowerCase().trim()){

            if (type == Progress.TYPE_TYPING) {
                masterPresenter.spell(myEntity.germanWord) {
                    masterPresenter.onFragmentResult(addedScore = 20, entityId = myEntity.id, correct = true)
                }
            }else {
                masterPresenter.onFragmentResult(addedScore = 20, entityId = myEntity.id, correct = true)
            }
        } else{
            masterPresenter.spell(myEntity.germanWord)
            view.showIncorrectDialog(translation = myEntity.translation,
                    answer = answer, correctAnswer = myEntity.germanWord) {
                masterPresenter.onFragmentResult(entityId = myEntity.id, correct = false)
            }
        }
    }

}
