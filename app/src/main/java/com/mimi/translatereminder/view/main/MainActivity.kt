package com.mimi.translatereminder.view.main

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.Menu
import android.view.MenuItem
import com.mimi.translatereminder.R
import com.mimi.translatereminder.base.BaseActivity
import com.mimi.translatereminder.base.BaseFragment
import com.mimi.translatereminder.utils.replaceFragmentInActivity
import com.mimi.translatereminder.view.addedit.AddEditActivity
import com.mimi.translatereminder.view.learning.LearningActivity
import com.mimi.translatereminder.view.main.about.AboutFragment
import com.mimi.translatereminder.view.main.contact.ContactFragment
import com.mimi.translatereminder.view.main.learning.LearningFragment
import com.mimi.translatereminder.view.main.edit.EditFragment
import com.mimi.translatereminder.view.main.learning.LearningFragmentPresenter
import com.mimi.translatereminder.view.main.settings.SettingsFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import org.jetbrains.anko.alert
import org.koin.android.ext.android.inject


class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener,
        MainContract.Activity {
    companion object {
        val ADD_EDIT_ACTIVITY_CODE = 3424
        val ITEM_ID = "itemId"
        val REQUESTING_PERMISSION = 4545
    }

    private val learningFragment by lazy {
        createLearningFragment(LearningFragmentPresenter.TYPE_LEARNING)
    }
    private val reviewFragment by lazy {
        createLearningFragment(LearningFragmentPresenter.TYPE_REVIEW)
    }
    private val mistakesFragment by lazy {
        createLearningFragment(LearningFragmentPresenter.TYPE_MISTAKES)
    }
    private val aboutFragment by inject<AboutFragment>()
    private val contactFragment by inject<ContactFragment>()
    private val editFragment by inject<EditFragment>()
    private val settingsFragment by inject<SettingsFragment>()

    override fun getContext() = this


    override val presenter: MainContract.Presenter by inject()
    private var onPermissionResult: (Boolean) -> Unit = {}

    private fun createLearningFragment(type: Int): LearningFragment {
        val fragment = LearningFragment()
        fragment.presenter.type = type
        return fragment

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    override fun init() {
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close)

        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
        initPresenters()
        presenter.start()
    }

    private fun initPresenters() {
        presenter.view = this
        learningFragment.presenter.view = learningFragment
        reviewFragment.presenter.view = reviewFragment
        mistakesFragment.presenter.view = mistakesFragment
        aboutFragment.presenter.view = aboutFragment
        contactFragment.presenter.view = contactFragment
        editFragment.presenter.view = editFragment
        settingsFragment.presenter.view = settingsFragment
        presenter.fragments = listOf(
                learningFragment.presenter,
                reviewFragment.presenter,
                mistakesFragment.presenter,
                aboutFragment.presenter,
                contactFragment.presenter,
                editFragment.presenter,
                settingsFragment.presenter
        )
    }

    override fun showFragment(id: Int) {
        val fragment: BaseFragment
        val titleId: Int
        when (id) {
            R.id.nav_learning -> {
                fragment = learningFragment
                titleId = R.string.menu_learning
            }
            R.id.nav_review -> {
                fragment = reviewFragment
                titleId = R.string.menu_review
            }
            R.id.nav_mistakes -> {
                fragment = mistakesFragment
                titleId = R.string.menu_mistakes
            }
            R.id.nav_edit -> {
                fragment = editFragment
                titleId = R.string.edit
            }
            R.id.nav_settings -> {
                fragment = settingsFragment
                titleId = R.string.nav_settings
            }
            R.id.nav_about -> {
                fragment = aboutFragment
                titleId = R.string.about_app
            }
            R.id.nav_contact_developer -> {
                fragment = contactFragment
                titleId = R.string.contact_developer
            }
            else -> throw UnsupportedOperationException("Unknown id: $id")
        }
        replaceFragmentInActivity(fragment, R.id.contentFrame)
        supportActionBar?.title = getString(titleId)

    }

    override fun startLearningActivity() {
        startActivityForResult(Intent(this, LearningActivity::class.java), 43)

    }

    override fun startAddActivity() {
        startActivityForResult(Intent(this, AddEditActivity::class.java), ADD_EDIT_ACTIVITY_CODE)
    }

    override fun startEditActivity(id: Int) {
        val intent = Intent(this, AddEditActivity::class.java)
        intent.putExtra(ITEM_ID, id)
        startActivityForResult(intent, ADD_EDIT_ACTIVITY_CODE)

    }

    override fun showDetailsDialog(entityId: Int) {
        val dialog = DetailsDialog.createDialog(entityId = entityId,
                onEdit = {presenter.editItem(it)},
                onDelete = {presenter.deleteItem(it)},
                onReview = {presenter.reviewItem(it)},
                translationRepository = getRepository())
        dialog.show(fragmentManager,"")
    }


    override fun showConfirmDialog(title: Int, message: Int, onConfirm: () -> Unit) {
        showConfirmDialog(getString(title), getString(message), onConfirm)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        presenter.onOptionItemSelected(item.itemId)
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        presenter.onNavigationItemSelected(item.itemId)
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun checkForPermission(permission: String, title: Int,
                                    description: Int, onPermissionResult: (Boolean) -> Unit) {
        this.onPermissionResult = onPermissionResult
        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
            onPermissionResult(true)
            return
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            alert(message = getString(description), title = getString(title)) {
                positiveButton(R.string.grant_permission) {
                    ActivityCompat.requestPermissions(
                            this@MainActivity,
                            arrayOf(permission), REQUESTING_PERMISSION)
                }
                onCancelled {
                    onPermissionResult(false)
                }
            }.show()
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(arrayOf(permission), REQUESTING_PERMISSION)
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK)
            return
        when (requestCode) {
            ADD_EDIT_ACTIVITY_CODE -> presenter.onReturnedFromActivity()
            REQUESTING_PERMISSION -> onPermissionResult(true)
        }
    }

    override fun toast(text: String) {
        toast.setText(text)
        toast.show()
    }

    override fun toast(text: Int) {
        toast.setText(getText(text))
        toast.show()

    }
}
