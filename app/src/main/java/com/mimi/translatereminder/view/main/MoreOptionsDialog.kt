package com.mimi.translatereminder.view.main

import android.app.ActionBar
import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.mimi.translatereminder.R
import kotlinx.android.synthetic.main.dialog_more_options.*
import org.jetbrains.anko.textColor

/**
 * Created by Mimi on 19/12/2017.
 *
 */
class MoreOptionsDialog : DialogFragment() {
    companion object {
        val REVIEW_ITEMS = "reviewItems"
        val LEARN_ITEMS = "learnItems"
        val WRONG_ITEMS = "wrongItems"
        val TOTAL_ITEMS = "totalItems"
        fun createDialog(totalItems:Int,
                         reviewItems: Int, learnItems: Int, wrongItems: Int, onReview: () -> Unit, onLearn: () -> Unit,
                         onWrong: () -> Unit, onListening: () -> Unit): MoreOptionsDialog {
            val args = Bundle()
            args.putInt(TOTAL_ITEMS, totalItems)
            args.putInt(REVIEW_ITEMS, reviewItems)
            args.putInt(LEARN_ITEMS, learnItems)
            args.putInt(WRONG_ITEMS, wrongItems)
            val dialog = MoreOptionsDialog()
            dialog.arguments = args
            dialog.onReview = onReview
            dialog.onLearn = onLearn
            dialog.onWrong = onWrong
            dialog.onListening = onListening
            return dialog
        }
    }

    private var onReview: () -> Unit = {}
    private var onLearn: () -> Unit = {}
    private var onWrong: () -> Unit = {}
    private var onListening: () -> Unit = {}

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
        return inflater.inflate(R.layout.dialog_more_options, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        val grey = ContextCompat.getColor(activity, R.color.grey)
        val blue = ContextCompat.getColor(activity, R.color.colorPrimary)
        title.text = getString(R.string.added_items, arguments.getInt(TOTAL_ITEMS, 0))
        initLearnItems(grey, blue)
        initReviewItems()
        initWrongItems(grey, blue)
        initListeningItems()
    }

    private fun initLearnItems(grey: Int, blue: Int) {
        val learnItems = arguments.getInt(LEARN_ITEMS, 0)

        if (learnItems == 0) {
            learnNewWords.text = getString(R.string.learn_new_items)
            learnNewWords.textColor = grey
            learnNewWordsButton.setColorFilter(grey, android.graphics.PorterDuff.Mode.SRC_IN)
        } else {
            learnNewWords.textColor = blue
            learnNewWordsButton.setColorFilter(blue, android.graphics.PorterDuff.Mode.SRC_IN)
            learnNewWords.text = getString(R.string.learn_new_items_details, learnItems)
            learnNewWords.setOnClickListener { learnNewItems() }
            learnNewWordsButton.setOnClickListener { learnNewItems() }
        }

    }

    private fun initReviewItems() {
        val reviewItems = arguments.getInt(REVIEW_ITEMS, 0)
        if (reviewItems == 0) {
            reviewWords.text = getString(R.string.review_items)
        } else {
            reviewWords.text = getString(R.string.review_items_details, reviewItems)
        }
        reviewWords.setOnClickListener { reviewItems() }
        reviewWordsButton.setOnClickListener { reviewItems() }

    }

    private fun initWrongItems(grey: Int, blue: Int) {
        val wrongItems = arguments.getInt(WRONG_ITEMS, 0)
        if (wrongItems == 0) {
            wrongWordsdText.text = getString(R.string.review_wrong_items)
            wrongWordsdText.textColor = grey
            wrongWordsButton.setColorFilter(grey, android.graphics.PorterDuff.Mode.SRC_IN)
        } else {
            wrongWordsdText.textColor = blue
            wrongWordsButton.setColorFilter(blue, android.graphics.PorterDuff.Mode.SRC_IN)
            wrongWordsdText.text = getString(R.string.review_wrong_items_details, wrongItems)
            wrongWordsdText.setOnClickListener { wrongItems() }
            wrongWordsButton.setOnClickListener { wrongItems() }
        }

    }

    private fun initListeningItems() {
        listeningImage.setOnClickListener {
            onListening()
            dismiss()
        }
        listeningWordsText.setOnClickListener {
            onListening()
            dismiss()
        }
    }

    private fun wrongItems() {
        onWrong()
        dismiss()
    }

    private fun learnNewItems() {
        onLearn()
        dismiss()
    }

    private fun reviewItems() {
        onReview()
        dismiss()
    }
}