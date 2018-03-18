package com.mimi.translatereminder.view.main.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import com.mimi.translatereminder.R
import com.mimi.translatereminder.base.BaseFragment
import com.mimi.translatereminder.utils.Context
import kotlinx.android.synthetic.main.fragment_settings.*
import org.koin.android.ext.android.inject

/**
 * Created by Mimi on 06/12/2017.
 *
 */

class SettingsFragment : BaseFragment(), SettingsContract.View {
    override val contextName = Context.Main

    override val presenter: SettingsContract.Presenter by inject()
    private val reviewItemsList by lazy { resources.getStringArray(R.array.review_items_per_session) }
    private val learnItemsList by lazy { resources.getStringArray(R.array.learn_items_per_session) }
    private val wrongItemsList by lazy { resources.getStringArray(R.array.mistake_items_per_session) }
    private val languageList by lazy { resources.getStringArray(R.array.languages) }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_settings, container, false)

    override fun getContext(): android.content.Context {
        return super.getContext()!!
    }

    override fun toast(text: String) {
        toast.setText(text)
        toast.show()
    }

    override fun toast(string: Int) {
        toast.setText(string)
        toast.show()
    }

    override fun refreshItems(spellWords: Boolean, reviewItems: Int, learningItems: Int, wrongItems: Int, language: String) {
        reviewItemsSpinner.setSelection(reviewItemsList.indexOf(reviewItems.toString()))
        learningItemsSpinner.setSelection(learnItemsList.indexOf(learningItems.toString()))
        wrongItemsSpinner.setSelection(wrongItemsList.indexOf(wrongItems.toString()))
        languageSpinner.setSelection(languageList.indexOf(language))
        spellWordsSwitch.isChecked = spellWords
    }

    override fun init() {
        setOnItemSelected(reviewItemsSpinner) {
            presenter.onReviewChanged(reviewItemsList[it].toInt())
        }
        setOnItemSelected(learningItemsSpinner) {
            presenter.onLearningChanged(learnItemsList[it].toInt())
        }
        setOnItemSelected(wrongItemsSpinner) {
            presenter.onWrongChanged(wrongItemsList[it].toInt())
        }
        setOnItemSelected(languageSpinner) {
            presenter.onLanguageChanged(languageList[it])
        }
        spellWordsSwitch.setOnCheckedChangeListener { _, isChecked ->
            presenter.onSpellWordsChanged(isChecked)
        }
        importButton.setOnClickListener { presenter.importItems() }
        exportButton.setOnClickListener { presenter.exportItems() }

    }

    private fun setOnItemSelected(spinner: Spinner, onSelected: (position: Int) -> Unit) {
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                onSelected(position)
            }

        }
    }

    override fun startPresenter() {
        presenter.start()
    }
}