package com.mimi.translatereminder.view.addedit

import com.mimi.translatereminder.R
import com.mimi.translatereminder.dto.Translation
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

    override fun start() {
        view.init()
    }

    override fun addTranslation(german: String, translation: String) {
        view.clearErrors()
        if (german.isEmpty()) {
            view.showGermanError(R.string.empty_text)
            return
        }
        if (translation.isEmpty()) {
            view.showTranslationError(R.string.empty_text)
            return
        }
        addTranslation(Translation(germanWord = german, translation = translation))
    }

    private fun addTranslation(translation: Translation) {
        view.showLoadingDialog()
        doAsync(exceptionHandler = {
            it.printStackTrace()
            view.hideLoadingDialog()
            view.toast(it.message ?: "Unknown error")
        }) {
            activity.getRepository().addTranslation(translation)
            val itemsSoFar = activity.getRepository().getAll()
            wordsCounter = itemsSoFar.size
            onComplete {
                view.hideLoadingDialog()
                view.clearText()
                view.toast("So far: $wordsCounter")
            }
        }

    }

}
