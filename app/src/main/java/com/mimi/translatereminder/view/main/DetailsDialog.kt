package com.mimi.translatereminder.view.main

import android.app.ActionBar
import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.mimi.translatereminder.R
import com.mimi.translatereminder.dto.Entity
import com.mimi.translatereminder.repository.TranslationRepository
import com.mimi.translatereminder.utils.TimeUtils
import kotlinx.android.synthetic.main.dialog_item_details.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.onComplete
import org.jetbrains.anko.toast


/**
 * Created by Mimi on 14/12/2017.
 *
 */
class DetailsDialog : DialogFragment() {
    companion object {
        val ENTITY_ID = "entityId"
        fun createDialog(entityId: Int, translationRepository: TranslationRepository,
                         onEdit: (Entity) -> Unit, onDelete: (Entity) -> Unit,
                         onReview: (Entity) -> Unit): DetailsDialog {
            val args = Bundle()
            args.putInt(ENTITY_ID, entityId)
            val dialog = DetailsDialog()
            dialog.arguments = args
            dialog.repository = translationRepository
            dialog.onEdit = onEdit
            dialog.onDelete = onDelete
            dialog.onReview = onReview
            return dialog
        }
    }


    private val exceptionHandler: (Throwable) -> Unit = {
        it.printStackTrace()
        toast(it.message ?: "Unknown error")
    }
    private var entity: Entity? = null
    private lateinit var repository: TranslationRepository
    private var onEdit: (Entity) -> Unit = {}
    private var onDelete: (Entity) -> Unit = {}
    private var onReview: (Entity) -> Unit = {}
    private val timeUtils = TimeUtils()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        return dialog
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.90).toInt()
        dialog.window.setLayout(width, ActionBar.LayoutParams.WRAP_CONTENT)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_item_details, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        val entityId = arguments.getInt(ENTITY_ID)
        doAsync(exceptionHandler) {
            entity = repository.selectItemById(entityId) ?:
                    throw UnsupportedOperationException("Item not found: $entityId")
            onComplete {
                initView()
            }
        }
    }

    private fun initView() {
        with(entity!!) {
            germanTerm.text = germanWord
            translationText.text = translation
            stateText.text = getString(
                    when (state) {
                        in Entity.STATE_LEARNING_1..Entity.STATE_LEARNING_4 -> R.string.menu_learning
                        in Entity.STATE_REVIEW_1..Entity.STATE_REVIEW_4 -> R.string.menu_review
                        Entity.STATE_MISTAKE -> R.string.mistake
                        else -> R.string.unknown
                    }
            )
            dateAddedText.text = getString(R.string.date_added, timeUtils.getTime(dateAdded,activity))
            reviewDate.text = getString(R.string.next_review, timeUtils.getTime(nextReview,activity))
            reviewCountText.text = getString(R.string.review_count, reviewCount)
            accuracy.text = getString(R.string.accuracy, getAccuracy())
        }
        editButton.setOnClickListener {
            dismiss()
            onEdit(entity!!)
        }
        deleteButton.setOnClickListener {
            dismiss()
            onDelete(entity!!)
        }
        reviewButton.setOnClickListener {
            dismiss()
            onReview(entity!!)
        }
    }
    private fun getAccuracy():Int{
        val entity = entity!!
        return if(entity.mistakesCount == 0 || entity.reviewCount == 0)
            100
        else
            (entity.mistakesCount / entity.reviewCount)*100
    }

}