package com.mimi.translatereminder.view.main.contact

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

class ContactFragment : BaseFragment(), ContactContract.View {
    override val contextName = Context.Main

    override val presenter: ContactContract.Presenter by inject()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_contact, container, false)

    override fun init() {
    }

    override fun toast(text: String) {
    }
    override fun startPresenter() {
        presenter.start()
    }
}