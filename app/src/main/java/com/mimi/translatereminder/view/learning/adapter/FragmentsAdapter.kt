package com.mimi.translatereminder.view.learning.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.mimi.translatereminder.dto.Progress

/**
 * Created by Mimi on 13/12/2017.
 *
 */
class FragmentsAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
    private val items = ArrayList<Progress>()

    fun refreshItems(newItems: List<Progress>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun getItem(position: Int): Fragment {
        return items[position].startFragment()
    }

    override fun getCount() = items.size

}