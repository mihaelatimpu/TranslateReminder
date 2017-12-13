package com.mimi.translatereminder.view.learning.fragments.first

import com.mimi.translatereminder.base.BaseFragment
import com.mimi.translatereminder.utils.Context.Learning

/**
 * Created by Mimi on 13/12/2017.
 *
 */
class FirstFragment: BaseFragment(), FirstContract.View{
    override val presenter: FirstContract.Presenter by lazy { FirstPresenter(this) }

    override fun init() {
    }

    override fun toast(text: String) {
    }

    override val contextName = Learning

    override fun startPresenter() {
        presenter.start()
    }

}
