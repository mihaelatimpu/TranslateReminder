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
import android.view.MenuItem
import com.mimi.translatereminder.R
import com.mimi.translatereminder.base.BaseActivity
import com.mimi.translatereminder.base.BaseFragment
import com.mimi.translatereminder.utils.replaceFragmentInActivity
import com.mimi.translatereminder.view.addedit.AddEditActivity
import com.mimi.translatereminder.view.learning.LearningActivity
import com.mimi.translatereminder.view.main.about.AboutFragment
import com.mimi.translatereminder.view.main.contact.ContactFragment
import com.mimi.translatereminder.view.main.main.MainFragment
import com.mimi.translatereminder.view.main.main.presenters.ArchivedFragmentPresenter
import com.mimi.translatereminder.view.main.main.presenters.FavouritesFragmentPresenter
import com.mimi.translatereminder.view.main.main.presenters.MainFragmentPresenter
import com.mimi.translatereminder.view.main.settings.SettingsFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import org.jetbrains.anko.alert
import org.koin.android.ext.android.inject


class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener,
        MainContract.Activity, MultiselectStateListener {
    companion object {
        const val ADD_EDIT_ACTIVITY_CODE = 3424
        const val LEARNING_ACTIVITY_CODE = 766
        const val ITEM_ID = "itemIds"
        const val REQUESTING_PERMISSION = 4545
        const val FRAGMENT_TYPE_MAIN = 1
        const val FRAGMENT_TYPE_FAVOURITES = 2
        const val FRAGMENT_TYPE_ARCHIVED = 3
    }

    private val mainFragment by lazy {
        createItemsFragment(FRAGMENT_TYPE_MAIN)
    }

    private val archivedFragment by lazy {
        createItemsFragment(FRAGMENT_TYPE_ARCHIVED)
    }

    private val favouritesFragment by lazy {
        createItemsFragment(FRAGMENT_TYPE_FAVOURITES)
    }
    private val aboutFragment by inject<AboutFragment>()
    private val contactFragment by inject<ContactFragment>()
    private val settingsFragment by inject<SettingsFragment>()

    override fun getContext() = this


    override val presenter: MainContract.Presenter by inject()
    private var onPermissionResult: (Boolean) -> Unit = {}

    private fun createItemsFragment(type: Int): MainFragment {
        val fragment = MainFragment()
        fragment.presenter = when (type) {
            FRAGMENT_TYPE_MAIN -> MainFragmentPresenter()
            FRAGMENT_TYPE_ARCHIVED -> ArchivedFragmentPresenter()
            FRAGMENT_TYPE_FAVOURITES -> FavouritesFragmentPresenter()
            else -> throw UnsupportedOperationException("Unknown type: $type")
        }
        return fragment

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.cancel_action -> presenter.onCancelActionSelected()
            R.id.delete_action -> presenter.onDeleteActionSelected()
            R.id.archive_action -> presenter.onArchiveActionSelected()
            R.id.review_action -> presenter.onReviewActionSelected()
            R.id.listen_action -> presenter.onListenActionSelected()
            R.id.star_action -> presenter.onStarActionSelected()
            else -> return false
        }
        return true
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
        presenter.registerMultiSelectChangeListener(this)
    }

    private fun initPresenters() {
        presenter.view = this
        archivedFragment.presenter.view = archivedFragment
        mainFragment.presenter.view = mainFragment
        favouritesFragment.presenter.view = favouritesFragment
        aboutFragment.presenter.view = aboutFragment
        contactFragment.presenter.view = contactFragment
        settingsFragment.presenter.view = settingsFragment
        presenter.fragments = listOf(
                archivedFragment.presenter,
                mainFragment.presenter,
                favouritesFragment.presenter,
                aboutFragment.presenter,
                contactFragment.presenter,
                settingsFragment.presenter
        )
    }

    override fun showFragment(id: Int) {
        val fragment: BaseFragment
        val titleId: Int
        when (id) {
            R.id.nav_main -> {
                fragment = mainFragment
                titleId = R.string.menu_main
            }
            R.id.nav_archive -> {
                fragment = archivedFragment
                titleId = R.string.menu_archive
            }
            R.id.nav_favourites -> {
                fragment = favouritesFragment
                titleId = R.string.menu_favourites
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

    override fun startLearningActivity(type: Int, reviewIds: List<Int>) {
        startActivityForResult(LearningActivity.startActivityIntent(this, type, reviewIds)
                , LEARNING_ACTIVITY_CODE)
    }

    override fun startAddActivity() {
        startActivityForResult(Intent(this, AddEditActivity::class.java), ADD_EDIT_ACTIVITY_CODE)
    }

    override fun onMultiSelectChange(selectable: Boolean) {
        if (selectable) {
            showMultiselectActionMenu()
        } else {
            hideMultiselectActionMenu()
        }
    }

    private fun showMultiselectActionMenu() {
        toolbar.menu.clear()
        toolbar.inflateMenu(R.menu.action_mode_menu)
        toolbar.tag = "normalToolbar"
    }

    private fun hideMultiselectActionMenu() {
        toolbar.menu.clear()
        supportActionBar?.title = getString(R.string.edit)
    }

    override fun onSelectCountChanged(newCount: Int) {
        toolbar.title = getString(R.string.items_selected, newCount)
    }

    override fun startEditActivity(id: Int) {
        val intent = Intent(this, AddEditActivity::class.java)
        intent.putExtra(ITEM_ID, id)
        startActivityForResult(intent, ADD_EDIT_ACTIVITY_CODE)

    }

    override fun showDetailsDialog(entityId: Int) {
        val dialog = DetailsDialog.createDialog(entityId = entityId,
                onEdit = { presenter.editItem(it) },
                onDelete = { presenter.deleteItems(listOf(it)) },
                onReview = { presenter.reviewItems(listOf(it)) },
                onReset = { presenter.resetItems(listOf(it)) },
                translationRepository = getRepository())
        dialog.show(fragmentManager, "")
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
            LEARNING_ACTIVITY_CODE -> presenter.onReturnedFromActivity()
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
