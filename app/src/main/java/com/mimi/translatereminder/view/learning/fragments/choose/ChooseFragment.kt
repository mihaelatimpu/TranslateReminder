package com.mimi.translatereminder.view.learning.fragments.choose

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mimi.translatereminder.R
import com.mimi.translatereminder.base.BaseFragment
import com.mimi.translatereminder.dto.Progress
import com.mimi.translatereminder.dto.Progress.Companion.TYPE
import com.mimi.translatereminder.utils.Context
import com.mimi.translatereminder.view.learning.LearningActivity
import kotlinx.android.synthetic.main.fragment_choose.*

/**
 * Created by Mimi on 13/12/2017.
 *
 */
class ChooseFragment : BaseFragment(), ChooseContract.View {

    override val presenter: ChooseContract.Presenter by lazy {
        val type = arguments?.getInt(TYPE)
        ChoosePresenter(this, (activity as LearningActivity).presenter, type = type?:0)
    }

    private val adapter by lazy { ChooseAdapter(context!!, { presenter.onAnswered(it) }) }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_choose, container, false)

    override fun toast(text: String) {
        toast.setText(text)
        toast.show()
    }

    override fun onVisibleToUser() {
        super.onVisibleToUser()
        presenter.onVisibleToUser()
    }

    override val contextName = Context.Learning

    override fun startPresenter() {
        presenter.start()
    }

    override fun init() {
        val id = arguments?.getInt(Progress.ENTITY_ID, 0)?:0
        if (id != 0)
            presenter.setEntityId(id)
        optionsList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        optionsList.adapter = adapter
    }


    override fun refreshText(translation: String, options: List<String>) {
        subtitle.text = translation
        adapter.refreshItems(options)
    }

    override fun changeBackground(answer: ChooseItem) {
        adapter.refreshItem(answer)
    }


}