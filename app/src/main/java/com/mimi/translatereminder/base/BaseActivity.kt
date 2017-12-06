package com.mimi.translatereminder.base

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.mimi.translatereminder.MainApplication
import com.mimi.translatereminder.R
import com.mimi.translatereminder.repository.TranslationRepository
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.progressDialog
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.yesButton

/**
 * Created by Mimi on 06/12/2017.
 *
 */
@SuppressLint("ShowToast")
abstract class BaseActivity : AppCompatActivity() {

    val toast: Toast by lazy { Toast.makeText(this, "", Toast.LENGTH_LONG) }
    private val loadingDialog by lazy {
        progressDialog(R.string.loading_data, R.string.please_wait)
    }

    fun getRepository() = TranslationRepository((application as MainApplication).translationDao)

    fun showLoadingDialog() {
        loadingDialog.show()
    }

    fun hideLoadingDialog() {
        loadingDialog.dismiss()
    }

     fun showConfirmDialog(title: String, message: String, onConfirm: () -> Unit) {
        alert(title = title, message = message) {
            yesButton { onConfirm() }
            noButton { }
        }.show()
    }

}