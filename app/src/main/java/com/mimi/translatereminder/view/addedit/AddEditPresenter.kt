package com.mimi.translatereminder.view.addedit

import com.mimi.translatereminder.R
import com.mimi.translatereminder.dto.Entity
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.onComplete

/**
 * Created by Mimi on 05/12/2017.
 *
 */
class AddEditPresenter : AddEditContract.Presenter {
    override lateinit var view: AddEditContract.View
    override lateinit var activity: AddEditContract.Activity
    var wordsCounter: Int = 0
    var editedItem: Entity? = null

    val exceptionHandler: (Throwable) -> Unit = {
        it.printStackTrace()
        view.hideLoadingDialog()
        view.toast(it.message ?: "Unknown error")
    }

    override fun start() {
        view.init()
    }

    override fun onAddButtonPressed(german: String, translation: String) {
        view.clearErrors()
        if (german.isEmpty()) {
            view.showGermanError(R.string.empty_text)
            return
        }
        if (translation.isEmpty()) {
            view.showTranslationError(R.string.empty_text)
            return
        }
        if (editedItem == null)
            addTranslation(Entity(germanWord = german, translation = translation))
        else
            saveItem(german, translation)
    }

    override fun editItem(id: Int) {
        view.showLoadingDialog()
        doAsync(exceptionHandler) {
            val repo = activity.getRepository()
            editedItem = repo.selectItemById(id)
            onComplete {
                view.hideLoadingDialog()
                if (editedItem != null) {
                    view.refreshText(editedItem!!.germanWord, editedItem!!.translation)
                    view.refreshAddButton(R.string.save)
                    activity.refreshTitle(R.string.edit_item)
                }
            }
        }
    }

    private fun saveItem(german: String, translation: String) {
        view.showLoadingDialog()
        doAsync(exceptionHandler) {
            val repo = activity.getRepository()
            val item = editedItem!!
            item.germanWord = german
            item.translation = translation
            repo.saveEntity(item)
            onComplete {
                view.hideLoadingDialog()
                activity.returnToPreviousActivity()
            }
        }
    }

    private fun addTranslation(entity: Entity) {
        view.showLoadingDialog()
        doAsync(exceptionHandler) {
            val repo = activity.getRepository()
            val preExistingWord = repo.findEntityByGermanWord(entity.germanWord).firstOrNull() ?:
                    repo.findEntityByTranslation(entity.translation).firstOrNull()
            if (preExistingWord == null) {
                repo.addEntity(entity)
                val itemsSoFar = repo.getAll()
                wordsCounter = itemsSoFar.size
            }
            onComplete {
                view.hideLoadingDialog()
                when {
                    (preExistingWord == null) -> activity.returnToPreviousActivity()
                    preExistingWord.germanWord == entity.germanWord ->
                        view.showGermanError(R.string.german_word_already_added)
                    preExistingWord.translation == entity.translation ->
                        view.showTranslationError(R.string.translation_already_added)
                }
            }
        }

    }

}
