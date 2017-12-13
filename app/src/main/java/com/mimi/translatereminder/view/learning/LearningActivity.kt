package com.mimi.translatereminder.view.learning

import android.os.Bundle
import com.mimi.translatereminder.R
import com.mimi.translatereminder.base.BaseActivity
import com.mimi.translatereminder.dto.Progress
import com.mimi.translatereminder.view.learning.adapter.FragmentsAdapter
import kotlinx.android.synthetic.main.activity_learning.*
import org.koin.android.ext.android.inject

/**
 *
 */
class LearningActivity : BaseActivity(), LearningContract.Activity {

    override val presenter: LearningContract.Presenter by inject()
    private val adapter by lazy { FragmentsAdapter(supportFragmentManager) }

    override fun getContext() = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learning)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        presenter.view = this
        presenter.start()
    }

    override fun init() {
        viewPager.adapter = adapter
    }


    override fun toast(text: String) {
        toast.setText(text)
        toast.show()
    }

    override fun setFragments(items: List<Progress>) {
        adapter.refreshItems(items)
    }

    override fun moveToFragment(position: Int) {
        viewPager.post {
            viewPager.setCurrentItem(position, true)
        }
    }

    override fun getCurrentFragmentPosition()
            = viewPager.currentItem

    override fun finishActivity() {
        finish()
    }

}
