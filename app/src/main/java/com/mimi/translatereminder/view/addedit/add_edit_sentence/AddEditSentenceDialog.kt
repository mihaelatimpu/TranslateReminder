package com.mimi.translatereminder.view.addedit.add_edit_sentence

import android.app.ActionBar
import android.app.DialogFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mimi.translatereminder.R
import com.mimi.translatereminder.dto.Entity
import com.mimi.translatereminder.repository.TranslationRepository
import kotlinx.android.synthetic.main.dialog_add_edit_sentence.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.onComplete
import org.jetbrains.anko.toast

/**
 * Created by Mimi on 17/02/2018.
 *
 */
class AddEditSentenceDialog : DialogFragment() {
    companion object {
        val SENTENCE_ID = "sentenceId"
        val PARENT_ID = "parentId"
        fun createDialog(parentId: Int, sentenceId: Int,
                         translationRepository: TranslationRepository, onSaved: () -> Unit): AddEditSentenceDialog {
            val args = Bundle()
            args.putInt(PARENT_ID, parentId)
            args.putInt(SENTENCE_ID, sentenceId)
            val dialog = AddEditSentenceDialog()
            dialog.arguments = args
            dialog.repository = translationRepository
            dialog.onSaved = onSaved
            return dialog
        }
    }

    private val exceptionHandler: (Throwable) -> Unit = {
        it.printStackTrace()
        toast(it.message ?: "Unknown error")
        hideLoadingDialog()
    }
    private lateinit var parent: Entity
    private var sentence: Entity? = null
    private lateinit var repository: TranslationRepository
    private var onSaved: () -> Unit = {}

    private fun showLoadingDialog() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideLoadingDialog() {
        progressBar.visibility = View.GONE
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.90).toInt()
        dialog.window.setLayout(width, ActionBar.LayoutParams.WRAP_CONTENT)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_add_edit_sentence, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        val parentId = arguments.getInt(PARENT_ID)
        val entityId = arguments.getInt(SENTENCE_ID)
        showLoadingDialog()
        doAsync(exceptionHandler) {
            parent = repository.selectItemById(parentId) ?: throw UnsupportedOperationException("Item not found: $entityId")

            sentence = repository.selectItemById(entityId)

            onComplete {
                hideLoadingDialog()
                initView()
            }
        }
    }

    private fun initView() {
        if (sentence == null) {
            title.text = getString(R.string.add_example)
        } else {
            title.text = getString(R.string.edit_example)
            originalSentenceEditText.setText(sentence?.germanWord)
            translationSentenceEditText.setText(sentence?.translation)
        }
        original_word.text = getString(R.string.original_word, parent.germanWord)
        saveButton.setOnClickListener { saveSentence {
            onSaved()
            dismiss()
        } }
    }


    private fun saveSentence(onFinished: () -> Unit = {}) {
        originalSentenceInput.isErrorEnabled = false
        translationSentenceInput.isErrorEnabled = false

        val originalSentence = originalSentenceEditText.text.toString()
        val mainTranslationText = translationSentenceEditText.text.toString()

        if (originalSentence.isEmpty()) {
            originalSentenceInput.isErrorEnabled = true
            originalSentenceInput.error = getString(R.string.empty_text)
            return
        }

        if (mainTranslationText.isEmpty()) {
            originalSentenceInput.isErrorEnabled = true
            originalSentenceInput.error = getString(R.string.empty_text)
            return
        }
        showLoadingDialog()
        doAsync(exceptionHandler) {
            var otherExistingSentence = false
            if (repository.findEntityByGermanWord(originalSentence).any { it.parentId == parent.id }) {
                otherExistingSentence = true
            } else {

                val newSentence: Entity
                if (sentence == null) {
                    newSentence = Entity(germanWord = originalSentence, translation = mainTranslationText,
                            type = Entity.TYPE_SENTENCE, parentId = parent.id)
                } else {
                    newSentence = sentence!!
                    newSentence.germanWord = originalSentence
                    newSentence.translation = mainTranslationText
                }
                if (newSentence.id == 0)
                    repository.addEntity(newSentence)
                else
                    repository.saveEntity(newSentence)

                sentence = newSentence
            }
            onComplete {
                hideLoadingDialog()
                if (otherExistingSentence) {
                    originalSentenceInput.isErrorEnabled = true
                    originalSentenceInput.error = getString(R.string.german_word_already_added)
                } else {
                    onFinished()
                }
            }
        }
    }

}