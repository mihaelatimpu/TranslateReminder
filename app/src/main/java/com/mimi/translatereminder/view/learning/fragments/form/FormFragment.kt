package com.mimi.translatereminder.view.learning.fragments.form

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mimi.translatereminder.R
import com.mimi.translatereminder.base.BaseFragment
import com.mimi.translatereminder.dto.Progress
import com.mimi.translatereminder.utils.Context
import com.mimi.translatereminder.view.learning.LearningActivity
import com.mimi.translatereminder.view.learning.fragments.form.adapter.FormWordOptionAdapter
import com.mimi.translatereminder.view.learning.fragments.form.adapter.FormWordResultAdapter
import com.xiaofeng.flowlayoutmanager.FlowLayoutManager
import kotlinx.android.synthetic.main.fragment_form.*
import org.jetbrains.anko.support.v4.alert

/**
 * Created by Mimi on 24/02/2018.
 *
 */
class FormFragment : BaseFragment(), FormContract.View {
    override val contextName = Context.Learning
    override val presenter by lazy {
        FormPresenter(this, masterPresenter = (activity as LearningActivity).presenter)
    }
    private val resultAdapter by lazy { FormWordResultAdapter(activity!!, onSelected = { presenter.onResultSelected(it) }) }
    private val optionAdapter by lazy { FormWordOptionAdapter(activity!!, onSelected = { presenter.onOptionSelected(it) }) }

    override fun startPresenter() {
        presenter.start()
    }
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_form, container, false)

    override fun showIncorrectDialog(translation: String, answer: String,
                                     correctAnswer: String, onComplete: () -> Unit) {
        val message = getString(R.string.incorrect_answer_message, answer, correctAnswer)
        alert(title = getString(R.string.incorrect_answer_title), message = message) {
            positiveButton(getString(R.string.ok)) { onComplete() }
        }.show()
    }

    override fun init() {
        val id = arguments?.getInt(Progress.ENTITY_ID, 0)?:0
        if (id != 0)
            presenter.setEntityId(id)
        resultList.layoutManager = getLayoutManager()
        optionsList.layoutManager = getLayoutManager()
        resultList.adapter = resultAdapter
        optionsList.adapter = optionAdapter
        finishButton.setOnClickListener { presenter.onFinishClicked() }
    }
    private fun getLayoutManager():RecyclerView.LayoutManager{
        val flowLayoutManager = FlowLayoutManager()
        flowLayoutManager.isAutoMeasureEnabled = true
        return flowLayoutManager
    }

    override fun refreshTranslation(translation: String) {
        translatedText.text = translation
    }
    override fun refreshOptions(options: List<OptionWord>) {
        optionAdapter.refreshWords(options)
    }

    override fun addOption(word: OptionWord) {
        optionAdapter.addWord(word)
    }

    override fun removeOption(word: OptionWord) {
        optionAdapter.removeWord(word)
    }

    override fun addResult(word: OptionWord) {
        resultAdapter.addWord(word)
    }

    override fun removeResult(word: OptionWord) {
        resultAdapter.removeWord(word)
    }

    override fun toast(text: String) {
        toast.setText(text)
        toast.show()
    }

    override fun getFinalResult() = resultAdapter.getAllWords()

}