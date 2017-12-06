package com.mimi.translatereminder.view.addedit

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.mimi.translatereminder.MainApplication
import com.mimi.translatereminder.R
import com.mimi.translatereminder.base.BaseActivity
import com.mimi.translatereminder.repository.TranslationRepository
import com.mimi.translatereminder.utils.replaceFragmentInActivity
import com.mimi.translatereminder.view.main.MainActivity
import org.koin.android.ext.android.inject

/**
 * Created by Mimi on 05/12/2017.
 *
 */
class AddEditActivity : BaseActivity(), AddEditContract.Activity {

    private val fragment: AddEditFragment by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit)
        init()
    }

    private fun init() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(R.string.add_translation)
        fragment.presenter.activity = this
        fragment.presenter.view = fragment
        supportFragmentManager.findFragmentById(R.id.contentFrame) as AddEditFragment?
                ?: fragment.also {
            replaceFragmentInActivity(it, R.id.contentFrame)
        }

    }

    override fun refreshTitle(title: Int) {
        supportActionBar?.setTitle(title)

    }

    override fun returnToPreviousActivity() {
        setResult(Activity.RESULT_OK)
        finish()
    }
}
