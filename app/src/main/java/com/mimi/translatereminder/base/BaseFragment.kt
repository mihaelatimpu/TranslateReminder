package com.mimi.translatereminder.base

import android.annotation.SuppressLint
import android.widget.Toast
import com.mimi.translatereminder.R
import org.jetbrains.anko.support.v4.indeterminateProgressDialog
import org.jetbrains.anko.support.v4.progressDialog
import org.koin.android.contextaware.ContextAwareFragment

/**
 * Created by Mimi on 08/11/2017.
 *
 */
@SuppressLint("ShowToast")
abstract class BaseFragment : ContextAwareFragment() {

    val toast: Toast by lazy { Toast.makeText(activity, "", Toast.LENGTH_LONG) }
    private val loadingDialog by lazy {
        indeterminateProgressDialog(R.string.loading_data, R.string.please_wait)
    }

    fun showLoadingDialog() {
        loadingDialog.show()
    }

    fun hideLoadingDialog() {
        loadingDialog.dismiss()
    }

    abstract fun startPresenter()

    override fun onResume() {
        super.onResume()
        startPresenter()
    }


}