package com.mimi.translatereminder.utils

import com.mimi.translatereminder.view.addedit.AddEditContract
import com.mimi.translatereminder.view.addedit.AddEditFragment
import com.mimi.translatereminder.view.addedit.AddEditPresenter
import com.mimi.translatereminder.utils.Context.AddEditTranslation
import com.mimi.translatereminder.utils.Context.Main
import com.mimi.translatereminder.view.main.MainContract
import com.mimi.translatereminder.view.main.MainPresenter
import com.mimi.translatereminder.view.main.about.AboutContract
import com.mimi.translatereminder.view.main.about.AboutFragment
import com.mimi.translatereminder.view.main.about.AboutPresenter
import com.mimi.translatereminder.view.main.contact.ContactContract
import com.mimi.translatereminder.view.main.contact.ContactFragment
import com.mimi.translatereminder.view.main.contact.ContactPresenter
import com.mimi.translatereminder.view.main.favourites.FavouritesContract
import com.mimi.translatereminder.view.main.favourites.FavouritesFragment
import com.mimi.translatereminder.view.main.favourites.FavouritesPresenter
import com.mimi.translatereminder.view.main.review.ReviewContract
import com.mimi.translatereminder.view.main.review.ReviewFragment
import com.mimi.translatereminder.view.main.review.ReviewPresenter
import com.mimi.translatereminder.view.main.settings.SettingsContract
import com.mimi.translatereminder.view.main.settings.SettingsFragment
import com.mimi.translatereminder.view.main.settings.SettingsPresenter
import org.koin.android.module.AndroidModule

/**
 * Created by Mimi on 05/12/2017.
 *
 */

fun appModules() = listOf(ViewModule())
class ViewModule : AndroidModule() {
    override fun context() = applicationContext {
        context(AddEditTranslation) {
            provide { AddEditFragment() }
            provide { AddEditPresenter() } bind AddEditContract.Presenter::class
        }
        context(Main){
            provide { AboutFragment() } bind AboutContract.View::class
            provide { AboutPresenter() } bind AboutContract.Presenter::class

            provide { ContactFragment() } bind ContactContract.View::class
            provide { ContactPresenter() } bind ContactContract.Presenter::class

            provide { FavouritesFragment() } bind FavouritesContract.View::class
            provide { FavouritesPresenter() } bind FavouritesContract.Presenter::class

            provide { ReviewFragment() } bind ReviewContract.View::class
            provide { ReviewPresenter() } bind ReviewContract.Presenter::class

            provide { SettingsFragment() } bind SettingsContract.View::class
            provide { SettingsPresenter() } bind SettingsContract.Presenter::class

            provide { MainPresenter(get(),get()) } bind MainContract.Presenter::class
        }
    }
}

/**
 * Module constants
 */

object Context {
    val Main = "Main"
    val TranslationDetail = "TranslationDetail"
    val AddEditTranslation = "AddEditTranslation"
}