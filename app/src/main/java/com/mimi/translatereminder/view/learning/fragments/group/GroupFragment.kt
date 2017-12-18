package com.mimi.translatereminder.view.learning.fragments.group

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.mimi.translatereminder.R
import com.mimi.translatereminder.base.BaseFragment
import com.mimi.translatereminder.dto.Progress
import com.mimi.translatereminder.utils.Context
import com.mimi.translatereminder.view.learning.LearningActivity
import kotlinx.android.synthetic.main.fragment_group.*
import org.jetbrains.anko.backgroundColor
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Mimi on 18/12/2017.
 *
 */
class GroupFragment : BaseFragment(), GroupContract.View {

    override val presenter: GroupContract.Presenter by lazy {
        val type = arguments.getInt(Progress.TYPE)
        val ids = arguments.getIntArray(Progress.IDS)
        GroupPresenter(this, (activity as LearningActivity).presenter, type = type, ids = ids.asList())
    }
    private val leftButtons by lazy { listOf(left1, left2, left3, left4) }
    private val rightButtons by lazy { listOf(right1, right2, right3, right4) }
    private var currentSelectedLeft: Button? = null
    private var currentSelectedRight: Button? = null

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_group, container, false)

    override fun init() {
        val list = ArrayList(leftButtons)
        list.addAll(rightButtons)
        list.forEach { it.setOnClickListener { onButtonSelected(it as Button) } }
    }

    override fun toast(text: String) {
        toast.setText(text)
        toast.show()
    }

    override val contextName = Context.Learning

    override fun refreshText(left: List<String>, right: List<String>) {
        setText(left, leftButtons)
        setText(right, rightButtons)
    }

    private fun onButtonSelected(button: Button) {
        if (leftButtons.contains(button)) { // left button selected
            if (currentSelectedLeft != null)
                currentSelectedLeft!!.backgroundColor = getUnselectedColor()
            currentSelectedLeft = if (currentSelectedLeft == button) {
                null
            } else {
                button.setBackgroundColor(getSelectedColor())
                button
            }
        } else if (rightButtons.contains(button)) { // right button selected
            if (currentSelectedRight != null)
                currentSelectedRight!!.backgroundColor = getUnselectedColor()
            currentSelectedRight = if (currentSelectedRight == button) {
                null
            } else {
                button.setBackgroundColor(getSelectedColor())
                button
            }

        }
        if (currentSelectedLeft != null && currentSelectedRight != null) {
            presenter.onSelected(currentSelectedLeft!!.text.toString(),
                    currentSelectedRight!!.text.toString())
            currentSelectedLeft = null
            currentSelectedRight = null
        }
    }

    private fun getUnselectedColor() = ContextCompat.getColor(context, R.color.colorPrimaryDark)
    private fun getSelectedColor() = ContextCompat.getColor(context, R.color.colorAccent)

    private fun setText(text: List<String>, buttons: List<Button>) {
        val list = ArrayList<String>()
        list.addAll(text)
        buttons.forEach {
            if (list.isNotEmpty())
                setText(it, list)
        }
    }


    private fun setText(button: Button, options: ArrayList<String>): ArrayList<String> {
        val position = Random().nextInt(options.size)
        button.text = options[position]
        options.removeAt(position)
        return options
    }

    override fun changeBackground(left: String, right: String, color: Int) {
        leftButtons.filter { it.text == left }
                .forEach { it.setBackgroundColor(ContextCompat.getColor(activity, color)) }
        rightButtons.filter { it.text == right }
                .forEach { it.setBackgroundColor(ContextCompat.getColor(activity, color)) }
    }

    override fun makeButtonsInactive(left: String, right: String) {
        leftButtons.filter { it.text == left }
                .forEach { it.setOnClickListener { } }
        rightButtons.filter { it.text == right }
                .forEach { it.setOnClickListener { } }
    }

    override fun startPresenter() {
        presenter.start()
    }

}
