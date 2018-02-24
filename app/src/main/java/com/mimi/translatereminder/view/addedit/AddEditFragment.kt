package com.mimi.translatereminder.view.addedit

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.mimi.translatereminder.R
import com.mimi.translatereminder.base.BaseFragment
import com.mimi.translatereminder.dto.Entity
import com.mimi.translatereminder.utils.Context
import com.mimi.translatereminder.view.main.MainActivity
import kotlinx.android.synthetic.main.fragment_add_edit.*
import org.koin.android.ext.android.inject


/**
 * Created by Mimi on 05/12/2017.
 *
 */
class AddEditFragment : BaseFragment(), AddEditContract.View {
    override val contextName = Context.AddEditTranslation
    override val presenter: AddEditContract.Presenter by inject()
    private val adapter by lazy {  SentenceAdapter(context, delete = { presenter.onDeleteSentence(it) })}


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_add_edit, container, false)

    override fun init() {
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter
        fab.setOnClickListener { presenter.onAddSentence() }
        if (activity.intent != null && activity.intent.hasExtra(MainActivity.ITEM_ID)) {
            presenter.editItem(activity.intent.getIntExtra(MainActivity.ITEM_ID, 0))
        }

    }

    override fun refreshSentences(items: List<Entity>) {
        adapter.refreshSentences(items)
    }

    override fun startPresenter() {
        presenter.start()
    }

    override fun toast(text: String) {
        Toast.makeText(activity, text, Toast.LENGTH_SHORT).show()
    }

    override fun showGermanError(text: Int) {
        germanInput.isErrorEnabled = true
        germanInput.error = getString(text)
        germanInput.requestFocus()
    }

    override fun refreshText(german: String, translation: String) {
        germanEditText.setText(german)
        translationEditText.setText(translation)
    }

    override fun showTranslationError(text: Int) {
        translationInput.isErrorEnabled = true
        translationInput.error = getString(text)
        translationInput.requestFocus()
    }

    override fun clearText() {
        germanEditText.text.clear()
        translationEditText.text.clear()
    }

    override fun clearErrors() {
        germanInput.isErrorEnabled = false
        translationInput.isErrorEnabled = false
        germanInput.clearFocus()
        translationInput.clearFocus()
    }

    override fun getOriginalText() = germanEditText.text.toString()
    override fun getTranslation() = translationEditText.text.toString()


}