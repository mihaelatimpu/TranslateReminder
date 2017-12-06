package com.mimi.translatereminder.view.main.review

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mimi.translatereminder.R
import com.mimi.translatereminder.base.BaseFragment
import com.mimi.translatereminder.dto.Entity
import com.mimi.translatereminder.utils.Context
import com.mimi.translatereminder.view.main.review.adapter.TranslationsAdapter
import kotlinx.android.synthetic.main.fragment_review.*
import org.koin.android.ext.android.inject

/**
 * Created by Mimi on 06/12/2017.
 *
 */

class ReviewFragment : BaseFragment(), ReviewContract.View {
    override val contextName = Context.Main

    override val presenter: ReviewContract.Presenter by inject()
    val adapter by lazy { TranslationsAdapter(context) }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_review, container, false)

    override fun init() {
        lstEntities.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        lstEntities.adapter = adapter
    }

    override fun refreshItems(items: List<Entity>) {
        adapter.refreshTranslations(items)
    }

    override fun toast(text: String) {
    }
    override fun startPresenter() {
        presenter.start()
    }
}