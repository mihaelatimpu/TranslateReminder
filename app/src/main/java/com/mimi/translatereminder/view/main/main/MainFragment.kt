package com.mimi.translatereminder.view.main.main

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

/**
 * Created by Mimi on 06/12/2017.
 *
 */

class MainFragment : BaseFragment(), MainFragmentContract.View {

    override val contextName = Context.Main

    override lateinit var presenter: MainFragmentContract.Presenter
    val adapter by lazy {
        ItemsAdapter(context = activity!!,
                onClick = { presenter.showDetailsDialog(it.id) },
                createHolder = { EditViewHolder(it) },
                resourceId = R.layout.item_translation,
                notifyChangeCount = { presenter.notifyChangeCount(it) },
                notifyChangeState = { presenter.notifyChangeState(it) })
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

    override fun getSelectedItems() = adapter.getSelectedItems()

    override fun changeSelectableState(selectable: Boolean) {
        adapter.changeState(selectable)
    }

    override fun refreshMainOption(visible: Boolean, text: Int) {
        if (visible) {
            mainActionLayout.visibility = View.VISIBLE
            mainActionButton.text = getString(text)
        } else {
            mainActionLayout.visibility = View.GONE
        }
    }

    override fun showOtherOptionsDialog(learningItems: Int, reviewItems: Int, wrongItems: Int) {
        val dialog = MoreOptionsDialog.createDialog(reviewItems = reviewItems, learnItems = learningItems, wrongItems = wrongItems,
                onReview = { presenter.onReviewButtonClicked() },
                onLearn = { presenter.onLearnButtonClicked() },
                onWrong = { presenter.onWrongButtonClicked() },
                onListening = { presenter.onListeningButtonClicked() })
        dialog.show(activity?.fragmentManager, "")
    }

    override fun refreshItems(items: List<Entity>, showState: Boolean) {
        adapter.refreshItems(items, showState)
        if (searchView != null)
            adapter.filter.filter(searchView.query)
    }

    override fun refreshAddButtonVisibility(visible: Boolean) {
        if(visible){
            fab.visibility = View.VISIBLE
        } else {
            fab.visibility = View.GONE
        }
    }

    override fun toast(text: String) {
        toast.setText(text)
        toast.show()
    }

    override fun startPresenter() {
        presenter.start()
    }
}