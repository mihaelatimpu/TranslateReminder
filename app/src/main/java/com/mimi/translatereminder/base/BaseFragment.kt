package com.mimi.translatereminder.base

import com.mimi.translatereminder.R
import org.jetbrains.anko.support.v4.progressDialog
import org.koin.android.contextaware.ContextAwareFragment

/**
 * Created by Mimi on 08/11/2017.
 *
 */
abstract class BaseFragment : ContextAwareFragment(){
    private val loadingDialog by lazy {
        progressDialog(R.string.loading_data, R.string.please_wait)
    }

    fun showLoadingDialog() {
        loadingDialog.show()
    }

    fun hideLoadingDialog() {
        loadingDialog.dismiss()
    }


}