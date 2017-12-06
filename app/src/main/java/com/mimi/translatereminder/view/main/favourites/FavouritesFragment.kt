package com.mimi.translatereminder.view.main.favourites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mimi.translatereminder.R
import com.mimi.translatereminder.base.BaseFragment
import com.mimi.translatereminder.utils.Context
import org.koin.android.ext.android.inject

/**
 * Created by Mimi on 06/12/2017.
 *
 */

class FavouritesFragment : BaseFragment(), FavouritesContract.View {
    override val contextName = Context.Main

    override val presenter: FavouritesContract.Presenter by inject()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_favourites, container, false)

    override fun init() {
    }

    override fun toast(text: String) {
    }

    override fun startPresenter() {
        presenter.start()
    }
}