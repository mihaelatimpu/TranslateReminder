package com.mimi.translatereminder.view.learning.fragments.choose

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.mimi.translatereminder.R
import com.mimi.translatereminder.base.BaseFragment
import com.mimi.translatereminder.dto.Progress
import com.mimi.translatereminder.dto.Progress.Companion.TYPE
import com.mimi.translatereminder.utils.Context
import com.mimi.translatereminder.view.learning.LearningActivity
import kotlinx.android.synthetic.main.fragment_choose.*
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Mimi on 13/12/2017.
 *
 */
class ChooseFragment : BaseFragment(), ChooseContract.View {

    override val presenter: ChooseContract.Presenter by lazy {
        val type = arguments.getInt(TYPE)
        ChoosePresenter(this, (activity as LearningActivity).presenter, type = type)
    }

    private val optionButtons by lazy { listOf(option1, option2, option3, option4) }


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_choose, container, false)

    override fun toast(text: String) {
        toast.setText(text)
        toast.show()
    }

    override val contextName = Context.Learning

    override fun startPresenter() {
        presenter.start()
    }

    override fun init() {
        val id = arguments.getInt(Progress.ENTITY_ID, 0)
        if (id != 0)
            presenter.setEntityId(id)
        optionButtons.forEach { setOnClickListener(it) }
    }

    private fun setOnClickListener(button: Button) {
        button.setOnClickListener { presenter.onAnswered(button.text.toString()) }

    }

    override fun refreshText(translation: String, options: List<String>) {
        subtitle.text = translation
        var array = ArrayList<String>(options)
        optionButtons.forEach {
            array = setText(it, array)
        }
    }

    override fun changeBackground(green: Boolean, text: String) {
        optionButtons.forEach {
            if (it.text == text) {
                val color = if (green) R.color.green
                else R.color.red
                it.setBackgroundResource(color)
            }
        }
    }


    private fun setText(button: Button, options: ArrayList<String>): ArrayList<String> {
        val position = Random().nextInt(options.size)
        button.text = options[position]
        options.removeAt(position)
        return options
    }


}