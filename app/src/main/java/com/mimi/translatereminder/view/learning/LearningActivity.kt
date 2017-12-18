package com.mimi.translatereminder.view.learning

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.mimi.translatereminder.R
import com.mimi.translatereminder.base.BaseActivity
import com.mimi.translatereminder.dto.Progress
import com.mimi.translatereminder.view.learning.LearningContract.Companion.TYPE_LEARN_NEW_WORDS
import com.mimi.translatereminder.view.learning.adapter.FragmentsAdapter
import kotlinx.android.synthetic.main.activity_learning.*
import org.koin.android.ext.android.inject

/**
 *
 */
class LearningActivity : BaseActivity(), LearningContract.Activity {
    companion object {
        private val TYPE = "type"
        private val ITEM_ID = "itemId"
        fun startActivityIntent(context: Context, type: Int, reviewId: Int?): Intent {
            val intent = Intent(context, LearningActivity::class.java)
            intent.putExtra(TYPE, type)
            if (reviewId != null)
                intent.putExtra(ITEM_ID, reviewId)
            return intent
        }
    }

    override val presenter: LearningContract.Presenter by inject()
    private val adapter by lazy { FragmentsAdapter(supportFragmentManager) }

    override fun getContext() = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learning)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if (intent.hasExtra(TYPE))
            presenter.type = intent.getIntExtra(TYPE, TYPE_LEARN_NEW_WORDS)
        if (intent.hasExtra(ITEM_ID))
            presenter.itemId = intent.getIntExtra(ITEM_ID, 0)
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
