package com.mimi.translatereminder.view.learning.fragments.form

import android.text.TextUtils
import com.mimi.translatereminder.dto.Entity
import com.mimi.translatereminder.dto.Entity.Companion.TYPE_SENTENCE
import com.mimi.translatereminder.view.learning.LearningContract
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.onComplete

/**
 * Created by Mimi on 24/02/2018.
 *
 */
class FormPresenter(override var view: FormContract.View,
                    private val masterPresenter: LearningContract.Presenter) : FormContract.Presenter {
    private var optionsList = listOf<OptionWord>()
    private var initialWord: Entity? = null

    private val exceptionHandler: (Throwable) -> Unit = {
        it.printStackTrace()
        view.hideLoadingDialog()
        view.toast(it.message ?: "Unknown error")
    }

    override fun setEntityId(id: Int) {
        view.showLoadingDialog()
        view.refreshSaveButton(false)
        doAsync(exceptionHandler) {
            val word = masterPresenter.repo.selectItemById(id)
                    ?: throw UnsupportedOperationException("Unknown word")
            val options =
                    if (word.type == Entity.TYPE_SENTENCE)
                        TextUtils.split(word.germanWord, " ").map { it.toString() }
                    else
                        word.germanWord.toCharArray().toList().map { it.toString() }

            optionsList = options.map { OptionWord(it, false) }.shuffled()
            initialWord = word
            onComplete {
                view.hideLoadingDialog()
                view.refreshTranslation(word.translation)
                view.refreshOptions(optionsList)
                view.refreshSaveButton(true)
            }
        }
    }

    override fun start() {
        view.init()
    }

    override fun onOptionSelected(word: OptionWord) {
        if (word.selected)
            return
        view.addResult(word)
        view.removeOption(word)
        if (initialWord?.type == TYPE_SENTENCE) {
            masterPresenter.spell(word.word)
        }
    }

    override fun onResultSelected(word: OptionWord) {
        view.addOption(word)
        view.removeResult(word)
    }

    override fun onFinishClicked() {
        view.refreshSaveButton(false)
        val resultList = view.getFinalResult()
        var resultWord = ""
        val space = if (initialWord?.type == TYPE_SENTENCE) " " else ""
        resultList.forEach { resultWord += it.word + space }
        if (resultWord.isNotEmpty() && (initialWord?.type == TYPE_SENTENCE)) {
            resultWord = resultWord.dropLast(1)
        }
        val success = initialWord != null && initialWord?.germanWord == resultWord
        if (success) {
            masterPresenter.spell(resultWord) {
                masterPresenter.onFragmentResult(0, initialWord?.id, success)
            }

        } else {
            masterPresenter.spell(initialWord?.germanWord ?: "")
            view.showIncorrectDialog(initialWord?.translation ?: "", resultWord,
                    initialWord?.germanWord ?: "") {
                masterPresenter.onFragmentResult(0, initialWord?.id, success)

            }
        }
    }

    override fun onVisibleToUser() {
    }

}