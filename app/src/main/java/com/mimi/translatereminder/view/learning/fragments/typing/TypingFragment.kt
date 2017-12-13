package com.mimi.translatereminder.view.learning.fragments.typing

import com.mimi.translatereminder.base.BaseFragment
import com.mimi.translatereminder.utils.Context
import com.mimi.translatereminder.view.learning.fragments.first.FirstContract
import com.mimi.translatereminder.view.learning.fragments.first.FirstPresenter

/**
 * Created by Mimi on 13/12/2017.
 *
 */
class TypingFragment: BaseFragment(), TypingContract.View{
    override val presenter: TypingContract.Presenter by lazy { TypingPresenter(this) }

    override fun init() {
    }

    override fun toast(text: String) {
    }

    override val contextName = Context.Learning

    override fun startPresenter() {
        presenter.start()
    }

}