package com.mimi.translatereminder.dto

import android.os.Bundle
import com.mimi.translatereminder.base.BaseFragment
import com.mimi.translatereminder.view.learning.fragments.choose.ChooseFragment
import com.mimi.translatereminder.view.learning.fragments.group.GroupFragment
import com.mimi.translatereminder.view.learning.fragments.present.FirstFragment
import com.mimi.translatereminder.view.learning.fragments.typing.TypingFragment

/**
 * Created by Mimi on 13/12/2017.
 *
 */
class Progress(val type: Int, val state: Int, val entityId: Int, private val ids: List<Int> = ArrayList()) {
    companion object {
        val ENTITY_ID = "entityId"
        val TYPE = "type"
        val IDS = "ids"
        val TYPE_PRESENT = 1
        val TYPE_HINT = 2
        val TYPE_CHOOSE_GERMAN = 3
        val TYPE_CHOOSE_TRANSLATION = 4
        val TYPE_GROUP = 5
        val TYPE_TYPING = 6
    }

    fun startFragment(): BaseFragment {
        val fragment = generateFragment()
        val args = Bundle()
        args.putInt(ENTITY_ID, entityId)
        args.putInt(TYPE, type)
        args.putIntArray(IDS, ids.toIntArray())
        fragment.arguments = args
        return fragment
    }

    private fun generateFragment(): BaseFragment
            = when (type) {
        TYPE_PRESENT -> FirstFragment()
        TYPE_CHOOSE_GERMAN -> ChooseFragment()
        TYPE_CHOOSE_TRANSLATION -> ChooseFragment()
        TYPE_HINT -> TypingFragment()
        TYPE_TYPING -> TypingFragment()
        TYPE_GROUP -> GroupFragment()
        else -> throw UnsupportedOperationException("Unknown type: $type")
    }

}