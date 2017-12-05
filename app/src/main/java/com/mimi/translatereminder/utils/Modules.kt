package com.mimi.translatereminder.utils

import com.mimi.translatereminder.view.addedit.AddEditContract
import com.mimi.translatereminder.view.addedit.AddEditFragment
import com.mimi.translatereminder.view.addedit.AddEditPresenter
import com.mimi.translatereminder.utils.Context.AddEditTranslation
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
    }
}

/**
 * Module constants
 */
object Context {
    val Translations = "Translations"
    val TranslationDetail = "TranslationDetail"
    val AddEditTranslation = "AddEditTranslation"
}