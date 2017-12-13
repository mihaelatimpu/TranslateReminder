package com.mimi.translatereminder.view.learning.fragments.present

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mimi.translatereminder.R
import com.mimi.translatereminder.base.BaseFragment
import com.mimi.translatereminder.dto.Progress
import com.mimi.translatereminder.utils.Context.Learning
import com.mimi.translatereminder.view.learning.LearningActivity
import kotlinx.android.synthetic.main.fragment_first.*

/**
 * Created by Mimi on 13/12/2017.
 *
 */
class FirstFragment : BaseFragment(), FirstContract.View {

    override val presenter: FirstContract.Presenter by lazy {
        FirstPresenter(this, masterPresenter = (activity as LearningActivity).presenter) }

    override val contextName = Learning


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_first, container, false)

    override fun init() {
        val id = arguments.getInt(Progress.ENTITY_ID, 0)
        if (id != 0)
            presenter.setEntityId(id)
        fab.setOnClickListener { presenter.onNextButtonPressed() }
    }

    override fun refreshText(germanText: String, translation: String) {
        title.text = germanText
        subtitle.text = translation
    }

    override fun toast(text: String) {
        toast.setText(text)
        toast.show()
    }

    override fun startPresenter() {
        presenter.start()
    }

}
