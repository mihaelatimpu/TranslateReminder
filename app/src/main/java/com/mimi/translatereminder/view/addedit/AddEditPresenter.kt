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
    private var wordsCounter: Int = 0
    private var editedItem: Entity? = null

    private val exceptionHandler: (Throwable) -> Unit = {
        it.printStackTrace()
        view.hideLoadingDialog()
        view.toast(it.message ?: "Unknown error")
    }

    override fun start() {
        view.init()
    }

    override fun onAddSentence() {
        if (editedItem == null) {
            onAddWord {
                activity.showAddSentenceDialog(editedItem!!.id) {
                    refreshItem(editedItem!!.id)
                }
            }
        } else {
            activity.showAddSentenceDialog(editedItem!!.id) {
                refreshItem(editedItem!!.id)
            }
        }
    }

    override fun onEditSentence(id: Int) {
        activity.showEditSentenceDialog(editedItem!!.id, id) {
            refreshItem(editedItem!!.id)
        }
    }

    override fun onDeleteSentence(item:Entity) {
        activity.showConfirmDialog(R.string.are_you_sure,R.string.you_cannot_restore_data){
            view.showLoadingDialog()
            doAsync(exceptionHandler) {
                val repo = activity.getRepository()
                repo.delete(item)
                onComplete {
                    view.hideLoadingDialog()
                    refreshItem(editedItem!!.id)
                }
            }
        }
    }

    override fun editItem(id: Int) {
        refreshItem(id) {
            activity.refreshTitle(R.string.edit_item)
        }
    }

    override fun onAddWord(onFinish: () -> Unit) {
        view.clearErrors()
        val german = view.getOriginalText()
        val translation = view.getTranslation()
        if (german.isEmpty()) {
            view.showGermanError(R.string.empty_text)
            return
        }
        if (translation.isEmpty()) {
            view.showTranslationError(R.string.empty_text)
            return
        }
        if (editedItem == null)
            addTranslation(Entity(germanWord = german, translation = translation), onFinish)
        else
            saveItem(german, translation, onFinish)
    }

    private fun refreshItem(id: Int, onFinish: () -> Unit = {}) {
        view.showLoadingDialog()
        doAsync(exceptionHandler) {
            val repo = activity.getRepository()
            editedItem = repo.selectItemById(id)
            val sentences = repo.findSentences(editedItem?.id ?: -1)
            onComplete {
                view.hideLoadingDialog()
                if (editedItem != null) {
                    view.refreshText(editedItem!!.germanWord, editedItem!!.translation)
                    onFinish()
                    view.refreshSentences(sentences)
                    activity.refreshTitle(R.string.edit_item)
                }
            }
        }
    }

    private fun saveItem(german: String, translation: String, onFinish: () -> Unit) {
        view.showLoadingDialog()
        doAsync(exceptionHandler) {
            val repo = activity.getRepository()
            val item = editedItem!!
            item.germanWord = german
            item.translation = translation
            repo.saveEntity(item)
            onComplete {
                view.hideLoadingDialog()
                onFinish()
            }
        }
    }

    private fun addTranslation(entity: Entity, onFinish: () -> Unit) {
        view.showLoadingDialog()
        doAsync(exceptionHandler) {
            val repo = activity.getRepository()
            val preExistingWord = repo.findEntityByGermanWord(entity.germanWord).firstOrNull()
            if (preExistingWord == null) {
                repo.addEntity(entity)
                val itemsSoFar = repo.getAll()
                wordsCounter = itemsSoFar.size
            }
            onComplete {
                view.hideLoadingDialog()
                when {
                    (preExistingWord == null) -> onFinish()
                    preExistingWord.germanWord == entity.germanWord ->
                        view.showGermanError(R.string.german_word_already_added)
                }
            }
        }

    }

}
