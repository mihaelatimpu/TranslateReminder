package com.mimi.translatereminder.view.learning.fragments.group

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
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
    private val leftAdapter by lazy { GroupAdapter(context, { presenter.onLeftSelected(it) }) }
    private val rightAdapter by lazy { GroupAdapter(context, { presenter.onRightSelected(it) }) }


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_group, container, false)

    override fun init() {
        leftList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        leftList.adapter = leftAdapter
        rightList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rightList.adapter = rightAdapter
    }

    override fun toast(text: String) {
        toast.setText(text)
        toast.show()
    }

    override val contextName = Context.Learning

    override fun refreshText(left: List<String>, right: List<String>) {
        leftAdapter.refreshItems(left)
        rightAdapter.refreshItems(right)
    }

    override fun refreshLeftItem(left: GroupItem) {
        leftAdapter.refreshItem(left)
    }

    override fun refreshRightItem(right: GroupItem) {
        rightAdapter.refreshItem(right)
    }

    override fun onVisibleToUser() {
        super.onVisibleToUser()
        presenter.onVisibleToUser()
    }

    override fun startPresenter() {
        presenter.start()
    }

}
