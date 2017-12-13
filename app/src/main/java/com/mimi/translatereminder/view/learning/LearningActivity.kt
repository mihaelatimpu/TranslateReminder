package com.mimi.translatereminder.view.learning

import android.os.Bundle
import com.mimi.translatereminder.R
import com.mimi.translatereminder.base.BaseActivity
import org.koin.android.ext.android.inject

/**
 *
 */
class LearningActivity : BaseActivity(), LearningContract.Activity {
    override val presenter: LearningContract.Presenter by inject()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learning)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        presenter.view = this
        presenter.start()
    }

    override fun init() {
    }

    override fun toast(text: String) {
        toast.setText(text)
        toast.show()
    }

    override fun getContext() = this


}
