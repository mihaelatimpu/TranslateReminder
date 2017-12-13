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
import com.mimi.translatereminder.view.main.edit.adapter.TranslationsAdapter
import kotlinx.android.synthetic.main.fragment_review.*
import org.koin.android.ext.android.inject

/**
 * Created by Mimi on 06/12/2017.
 *
 */

class EditFragment : BaseFragment(), EditContract.View {
    override val contextName = Context.Main

    override val presenter: EditContract.Presenter by inject()
    val adapter by lazy {
        TranslationsAdapter(context, { presenter.editItem(it) },
                { presenter.deleteItem(it) })
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_review, container, false)

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
    }

    override fun refreshItems(items: List<Entity>) {
        adapter.refreshTranslations(items)
        adapter.filter.filter(searchView.query)
    }

    override fun toast(text: String) {
    }

    override fun startPresenter() {
        presenter.start()
    }
}