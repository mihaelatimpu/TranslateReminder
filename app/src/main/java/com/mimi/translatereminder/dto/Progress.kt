package com.mimi.translatereminder.dto

import android.os.Bundle
import com.mimi.translatereminder.base.BaseFragment
import com.mimi.translatereminder.view.learning.fragments.present.FirstFragment
import com.mimi.translatereminder.view.learning.fragments.hint.SecondFragment
import com.mimi.translatereminder.view.learning.fragments.choose.ChooseFragment
import com.mimi.translatereminder.view.learning.fragments.typing.TypingFragment

/**
 * Created by Mimi on 13/12/2017.
 *
 */
class Progress(val type: Int, val state:Int, val entityId: Int) {
    companion object {
        val ENTITY_ID = "entityId"
        val TYPE_PRESENT = 1
        val TYPE_HINT = 2
        val TYPE_CHOOSE = 3
        val TYPE_TYPING = 4
    }

    fun startFragment(): BaseFragment {
        val fragment = generateFragment()
        val args = Bundle()
        args.putInt(ENTITY_ID, entityId)
        fragment.arguments = args
        return fragment
    }

    private fun generateFragment(): BaseFragment
            = when (type) {
        TYPE_PRESENT -> FirstFragment()
        TYPE_HINT -> SecondFragment()
        TYPE_CHOOSE -> ChooseFragment()
        TYPE_TYPING -> TypingFragment()
        else -> throw UnsupportedOperationException("Unknown type: $type")
    }

}