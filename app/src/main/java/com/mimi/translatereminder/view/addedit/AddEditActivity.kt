package com.mimi.translatereminder.view.addedit

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.mimi.translatereminder.MainApplication
import com.mimi.translatereminder.R
import com.mimi.translatereminder.repository.TranslationRepository
import com.mimi.translatereminder.utils.replaceFragmentInActivity
import org.koin.android.ext.android.inject

/**
 * Created by Mimi on 05/12/2017.
 *
 */
class AddEditActivity : AppCompatActivity(), AddEditContract.Activity {
    override fun getRepository() = TranslationRepository((application as MainApplication).translationDao)

    private val fragment: AddEditFragment by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(R.string.add_translation)
        fragment.presenter.activity = this
        fragment.presenter.view = fragment
        supportFragmentManager.findFragmentById(R.id.contentFrame) as AddEditFragment?
                ?: fragment.also {
            replaceFragmentInActivity(it, R.id.contentFrame)
        }
    }
}
