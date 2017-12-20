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
    private var fragmentResume = false
    private var fragmentVisible = false
    private var fragmentOnCreated = false
    private val loadingDialog by lazy {
        indeterminateProgressDialog(R.string.loading_data, R.string.please_wait)
    }

    fun showLoadingDialog() {
        loadingDialog.show()
    }

    fun hideLoadingDialog() {
        loadingDialog.dismiss()
    }
    override fun setUserVisibleHint(visible: Boolean) {
        super.setUserVisibleHint(visible)
        if (visible && isResumed){   // only at fragment screen is resumed
            fragmentResume=true
            fragmentVisible=false
            fragmentOnCreated=true
            onVisibleToUser()
        }else  if (visible){        // only at fragment onCreated
            fragmentResume=false
            fragmentVisible=true
            fragmentOnCreated=true
        }
        else if(!visible && fragmentOnCreated){// only when you go out of fragment screen
            fragmentVisible=false
            fragmentResume=false
        }
    }

    abstract fun startPresenter()
    open fun onVisibleToUser(){

    }

    override fun onResume() {
        super.onResume()
        startPresenter()
    }


}