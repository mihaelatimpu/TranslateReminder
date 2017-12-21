package com.mimi.translatereminder.view.learning.fragments.choose

import com.mimi.translatereminder.dto.Entity
import com.mimi.translatereminder.dto.Progress
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
                      private val masterPresenter: LearningContract.Presenter, override var type: Int) : ChooseContract.Presenter {
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
                view.hideLoadingDialog()
                displayData(myEntity, otherTexts)
            }
        }
    }

    private fun displayData(myEntity: Entity, otherTexts: List<Entity>) {
        entity = myEntity
        if (type == Progress.TYPE_CHOOSE_GERMAN)
            view.refreshText(myEntity.translation,
                    listOf(myEntity.germanWord,
                            otherTexts[0].germanWord,
                            otherTexts[1].germanWord,
                            otherTexts[2].germanWord))
        else
            view.refreshText(myEntity.germanWord,
                    listOf(myEntity.translation,
                            otherTexts[0].translation,
                            otherTexts[1].translation,
                            otherTexts[2].translation))
    }

    override fun onVisibleToUser() {
    }

    override fun onAnswered(answer: String) {
        if (entity == null)
            return
        val entity = entity!!
        val correctAnswer = if (type == Progress.TYPE_CHOOSE_GERMAN)
            entity.germanWord else entity.translation
        val correct = answer.toLowerCase() == correctAnswer.toLowerCase()
        view.changeBackground(correct, answer)
        view.changeBackground(true, entity.germanWord)
        Timer("").schedule(500) {
            saveAndMoveOn(correct)
        }
        if(type == Progress.TYPE_CHOOSE_GERMAN)
            masterPresenter.spell(entity.germanWord)
    }

    private fun saveAndMoveOn(correct: Boolean) {
        val addedScore = if (correct) 30 else 9
        masterPresenter.onFragmentResult(addedScore = addedScore,
                entityId = entity!!.id, correct = correct)
    }

}
