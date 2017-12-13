package com.mimi.translatereminder.dto

import android.os.Bundle
import com.mimi.translatereminder.base.BaseFragment
import com.mimi.translatereminder.view.learning.fragments.first.FirstFragment
import com.mimi.translatereminder.view.learning.fragments.typing.TypingFragment

/**
 * Created by Mimi on 13/12/2017.
 *
 */
class LearningFragmentInfo(private val type: Int, private val entityId: Int) {
    companion object {
        val ENTITY_ID = "entityId"
        val TYPE_FIRST = 1
        val TYPE_TYPING = 2
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
        TYPE_FIRST -> FirstFragment()
        TYPE_TYPING -> TypingFragment()
        else -> throw UnsupportedOperationException("Unknown type: $type")
    }

}