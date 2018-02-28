package com.mimi.translatereminder.view.learning

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.view.ViewPager
import com.mimi.translatereminder.R
import com.mimi.translatereminder.base.BaseActivity
import com.mimi.translatereminder.dto.Progress
import com.mimi.translatereminder.utils.Speaker
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
    private val speaker by lazy { Speaker(this) }

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
        speaker.allowed = true
        presenter.start()
    }

    override fun init() {
        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                presenter.onFragmentVisible(position)
                val percentage = position / adapter.count.toFloat()
                val params = indicatorHelper.layoutParams as ConstraintLayout.LayoutParams
                params.guidePercent = percentage
                indicatorHelper.layoutParams = params
            }

        })
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

    override fun spellText(text: String, onFinish: () -> Unit) {
        runOnUiThread{
            speaker.speak(text, onFinish)
        }
    }

    override fun getCurrentFragmentPosition()
            = viewPager.currentItem

    override fun finishActivity() {
        finish()
    }

}
