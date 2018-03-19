package com.mimi.translatereminder.view.learning

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.view.ViewPager
import android.view.View
import android.widget.CompoundButton
import com.mimi.translatereminder.R
import com.mimi.translatereminder.base.BaseActivity
import com.mimi.translatereminder.dto.Progress
import com.mimi.translatereminder.utils.Speaker
import com.mimi.translatereminder.view.learning.LearningContract.Companion.TYPE_LEARN_NEW_WORDS
import com.mimi.translatereminder.view.learning.adapter.FragmentsAdapter
import kotlinx.android.synthetic.main.activity_learning.*
import kotlinx.android.synthetic.main.content_main.*
import org.koin.android.ext.android.inject
import java.util.*


/**
 *
 */
class LearningActivity : BaseActivity(), LearningContract.Activity {
    companion object {
        private const val TYPE = "type"
        private const val ITEM_ID = "itemIds"
        fun startActivityIntent(context: Context, type: Int, reviewIds: List<Int>): Intent {
            val intent = Intent(context, LearningActivity::class.java)
            intent.putExtra(TYPE, type)
                intent.putExtra(ITEM_ID, reviewIds.toIntArray())
            return intent
        }
    }

    override val presenter: LearningContract.Presenter by inject()
    private val adapter by lazy { FragmentsAdapter(supportFragmentManager) }
    private val speaker by lazy { Speaker(this, Locale.GERMAN) }
    private val nativeSpeaker by lazy { Speaker(this,  Locale("ro_RO")) }

    override fun getContext() = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learning)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if (intent.hasExtra(TYPE))
            presenter.type = intent.getIntExtra(TYPE, TYPE_LEARN_NEW_WORDS)
        if (intent.hasExtra(ITEM_ID))
            presenter.itemIds = intent.getIntArrayExtra(ITEM_ID).toList()
        presenter.view = this
        speaker.allowed = true
        spellCheckbox.setOnCheckedChangeListener { _, selected -> presenter.onSpellNativeChanged(selected) }
        presenter.start()
    }

    override fun setSpellCheckboxVisibility(visible: Boolean) {
        spellCheckbox.visibility = if(visible) View.VISIBLE else View.GONE
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

    override fun spellInNativeLanguage(text: String, onFinish: () -> Unit) {
        runOnUiThread{
            nativeSpeaker.speak(text, onFinish)
        }
    }

    override fun getCurrentFragmentPosition()
            = viewPager.currentItem

    override fun finishActivity() {
        finish()
    }

}
