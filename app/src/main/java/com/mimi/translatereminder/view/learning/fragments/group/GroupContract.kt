package com.mimi.translatereminder.view.learning.fragments.group

import android.support.annotation.ColorRes
import com.mimi.translatereminder.base.BasePresenter
import com.mimi.translatereminder.base.BaseView
import com.mimi.translatereminder.view.learning.LearningContract

/**
 * Created by Mimi on 18/12/2017.
 *
 */
interface GroupContract {
    interface View : BaseView<Presenter> {

        fun refreshLeftItem(left: GroupItem)
        fun refreshRightItem(right: GroupItem)
        fun refreshText(left: List<String>, right: List<String>)
    }

    interface Presenter : BasePresenter<View>, LearningContract.FragmentPresenter {
        var type: Int
        var ids: List<Int>
        fun onLeftSelected(left:GroupItem)
        fun onRightSelected(right:GroupItem)
    }
}
