package com.mimi.translatereminder.view.main.settings

import com.mimi.translatereminder.R
import com.mimi.translatereminder.repository.local.sharedprefs.SharedPreferencesUtil
import com.mimi.translatereminder.utils.FileUtil
import com.mimi.translatereminder.utils.json.ExportUtil
import com.mimi.translatereminder.utils.json.ImportUtil
import com.mimi.translatereminder.view.main.MainContract
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.onComplete

/**
 * Created by Mimi on 06/12/2017.
 *
 */


class SettingsPresenter : SettingsContract.Presenter {

    override lateinit var view: SettingsContract.View

    override lateinit var mainPresenter: MainContract.Presenter
    private val exceptionHandler: (Throwable) -> Unit = {
        it.printStackTrace()
        view.hideLoadingDialog()
        view.toast(it.message ?: "Unknown error")
    }

    override fun isVisible() = view.isVisible()
    private var reviewItems = 20
    private var learningItems = 3
    private var wrongItems = 5
    private var language = "English"
    private var spellWords = true
    private val sharedPrefs = SharedPreferencesUtil()

    override fun reloadData() {
        view.showLoadingDialog()
        doAsync(exceptionHandler) {
            reviewItems = sharedPrefs.getReviewItemsPerSession(view.getContext())
            learningItems = sharedPrefs.getLearningItemsPerSession(view.getContext())
            wrongItems = sharedPrefs.getWrongItemsPerSession(view.getContext())
            spellWords = sharedPrefs.getSpellWords(view.getContext())
            onComplete {
                view.hideLoadingDialog()
                if (view.isVisible())
                    view.refreshItems(spellWords, reviewItems,
                            learningItems, wrongItems, language)
            }
        }
    }

    override fun start() {
        view.init()
        reloadData()
    }

    override fun exportItems() {
        mainPresenter.checkForPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                title = R.string.missing_permission,
                description = R.string.please_grant_permission) {
            if (!it)
                return@checkForPermission
            doAsync(resultAnswer = view.getContext().getString(R.string.exported), code = {
                val exportedJson = ExportUtil(mainPresenter.getRepository().getAll()).start()
                FileUtil().writeExportedDataToNewFile(exportedJson)
                val exportedArchivedJson = ExportUtil(mainPresenter.getRepository().getArchivedItems()).start()
                FileUtil().writeExportedDataToNewFile(exportedJson, FileUtil.EXPORT_ARCHIVED_FILENAME)
            })
        }
    }

    override fun importItems() {
        doAsync(resultAnswer = view.getContext().getString(R.string.imported), code = {
            val string = FileUtil().readTextFromImportResources(view.getContext())
            ImportUtil(string, mainPresenter.getRepository()).start()
        })
    }

    override fun onLanguageChanged(language: String) {

    }

    override fun onLearningChanged(items: Int) {
        doAsync(resultAnswer = view.getContext().getString(R.string.saved), code = {
            sharedPrefs.setLearningItemsPerSession(items = items, context = view.getContext())
            learningItems = items
        })
    }

    override fun onReviewChanged(items: Int) {
        doAsync(resultAnswer = view.getContext().getString(R.string.saved), code = {
            sharedPrefs.setReviewItemsPerSession(items = items, context = view.getContext())
            reviewItems = items
        })
    }

    override fun onWrongChanged(items: Int) {
        doAsync(resultAnswer = view.getContext().getString(R.string.saved), code = {
            sharedPrefs.setWrongItemsPerSession(items = items, context = view.getContext())
            wrongItems = items
        })
    }

    override fun onSpellWordsChanged(active: Boolean) {
        doAsync(resultAnswer = view.getContext().getString(R.string.saved), code = {
            sharedPrefs.setSpellWords(active = active, context = view.getContext())
            spellWords = active
        })
    }

    private fun doAsync(code: () -> Unit, resultAnswer: String? = null) {
        view.showLoadingDialog()
        doAsync(exceptionHandler) {
            code()
            onComplete {
                view.hideLoadingDialog()
                if (view.isVisible())
                    view.refreshItems(spellWords, reviewItems,
                            learningItems, wrongItems, language)
                if (resultAnswer != null)
                    view.toast(resultAnswer)
            }
        }
    }

}