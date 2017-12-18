package com.mimi.translatereminder.view.learning.fragments.choose

import com.mimi.translatereminder.dto.Entity
import com.mimi.translatereminder.view.learning.LearningContract
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.onComplete
import java.util.*
import kotlin.concurrent.schedule

/**
 * Created by Mimi on 13/12/2017.
 *
 */

class ChoosePresenter(override var view: ChooseContract.View,
                      private val masterPresenter: LearningContract.Presenter) : ChooseContract.Presenter {
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
            val myEntity = masterPresenter.getRepository().selectItemById(id)
                    ?: throw UnsupportedOperationException("Could not find entity $id")
            val otherTexts = masterPresenter.getRepository().getRandomItems(myEntity.id, 4)
            onComplete {
                entity = myEntity
                view.hideLoadingDialog()
                view.refreshText(myEntity.translation,
                        listOf(myEntity.germanWord,
                                otherTexts[0].germanWord,
                                otherTexts[1].germanWord,
                                otherTexts[2].germanWord))
            }
        }
    }

    override fun onAnswered(answer: String) {
        if (entity == null)
            return
        val correct = answer.toLowerCase() == entity!!.germanWord.toLowerCase()
        view.changeBackground(correct, answer)
        view.changeBackground(true, entity!!.germanWord)
        Timer("").schedule(500) {
            saveAndMoveOn(correct)
        }
    }

    private fun saveAndMoveOn(correct: Boolean) {
        val addedScore = if (correct) 30 else 9
        masterPresenter.onFragmentResult(addedScore = addedScore,
                entityId = entity!!.id, correct = correct)
    }

}