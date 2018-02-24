package com.mimi.translatereminder.view.addedit

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.mimi.translatereminder.MainApplication
import com.mimi.translatereminder.R
import com.mimi.translatereminder.base.BaseActivity
import com.mimi.translatereminder.repository.TranslationRepository
import com.mimi.translatereminder.utils.replaceFragmentInActivity
import com.mimi.translatereminder.view.addedit.add_edit_sentence.AddEditSentenceDialog
import com.mimi.translatereminder.view.main.MainActivity
import kotlinx.android.synthetic.main.fragment_add_edit.*
import org.koin.android.ext.android.inject

/**
 * Created by Mimi on 05/12/2017.
 *
 */
class AddEditActivity : BaseActivity(), AddEditContract.Activity {

    private val fragment: AddEditFragment by inject()
    private val presenter: AddEditContract.Presenter by inject()

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_done, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item != null && item.itemId == R.id.action_menu_done) {
            presenter.onAddWord{
                returnToPreviousActivity()
            }
        }
        return true
    }

    override fun showAddSentenceDialog(parentId: Int, onAdded: () -> Unit) {
        val dialog = AddEditSentenceDialog.createDialog(parentId,-1,getRepository(),onAdded)
        dialog.show(fragmentManager,"")
    }

    override fun showEditSentenceDialog(parentId: Int, entityId: Int, onFinish: () -> Unit) {
        val dialog = AddEditSentenceDialog.createDialog(parentId,entityId,getRepository(),onFinish)
        dialog.show(fragmentManager,"")

    }

    override fun showConfirmDialog(title: Int, message: Int, onConfirm: () -> Unit) {
        showConfirmDialog(getString(title),getString(message),onConfirm)
    }


    override fun refreshTitle(title: Int) {
        supportActionBar?.setTitle(title)

    }

    private fun returnToPreviousActivity() {
        setResult(Activity.RESULT_OK)
        finish()
    }
}
