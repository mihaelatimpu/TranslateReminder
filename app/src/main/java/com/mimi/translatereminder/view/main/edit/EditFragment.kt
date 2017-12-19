package com.mimi.translatereminder.view.main.edit

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import com.mimi.translatereminder.R
import com.mimi.translatereminder.base.BaseFragment
import com.mimi.translatereminder.dto.Entity
import com.mimi.translatereminder.utils.Context
import com.mimi.translatereminder.view.main.ItemsAdapter
import com.mimi.translatereminder.view.main.MoreOptionsDialog
import kotlinx.android.synthetic.main.fragment_edit.*
import org.koin.android.ext.android.inject

/**
 * Created by Mimi on 06/12/2017.
 *
 */

class EditFragment : BaseFragment(), EditContract.View {
    override val contextName = Context.Main

    override val presenter: EditContract.Presenter by inject()
    val adapter by lazy {
        ItemsAdapter(context = activity,
                onClick = { presenter.showDetailsDialog(it.id) },
                createHolder = { EditViewHolder(it) },
                resourceId = R.layout.item_translation)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_edit, container, false)

    override fun init() {
        lstEntities.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        lstEntities.adapter = adapter
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return true
            }

        })
        fab.setOnClickListener { presenter.onAddButtonClicked() }
        mainActionButton.setOnClickListener { presenter.onMainOptionClicked() }
        otherOptionsButton.setOnClickListener { presenter.onOtherOptionsClicked() }
    }

    override fun refreshMainOption(text: Int) {
        mainActionButton.text = getString(text)
    }

    override fun showOtherOptionsDialog(learningItems: Int, reviewItems: Int, wrongItems: Int) {
        val dialog = MoreOptionsDialog.createDialog(reviewItems = reviewItems, learnItems = learningItems, wrongItems = wrongItems,
                onReview = { presenter.onReviewButtonClicked() },
                onLearn = { presenter.onLearnButtonClicked() },
                onWrong = { presenter.onWrongButtonClicked() })
        dialog.show(activity.fragmentManager, "")
    }

    override fun refreshItems(items: List<Entity>) {
        adapter.refreshItems(items)
        if (searchView != null)
            adapter.filter.filter(searchView.query)
    }

    override fun toast(text: String) {
        toast.setText(text)
        toast.show()
    }

    override fun startPresenter() {
        presenter.start()
    }
}