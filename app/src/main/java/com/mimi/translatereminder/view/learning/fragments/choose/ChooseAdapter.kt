package com.mimi.translatereminder.view.learning.fragments.choose

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mimi.translatereminder.R
import kotlinx.android.synthetic.main.item_group.view.*

/**
 * Created by Mimi on 15/03/2018.
 */

class ChooseAdapter(val context: Context, private val onSelected: (ChooseItem) -> Unit) : RecyclerView.Adapter<GroupViewHolder>() {
    val items = arrayListOf<ChooseItem>()
    fun refreshItems(newItems: List<String>) {
        items.clear()
        newItems.forEach { items.add(ChooseItem(it, State.Default)) }
        notifyDataSetChanged()
    }

    fun refreshItem(item: ChooseItem) {
        notifyItemChanged(items.indexOf(item))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = GroupViewHolder(LayoutInflater.from(context).inflate(R.layout.item_group, parent, false))

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        holder.bindItem(items[position], onSelected)
    }

}

class GroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bindItem(item: ChooseItem, onSelected: (ChooseItem) -> Unit) {
        itemView.label.text = item.text
        itemView.setOnClickListener {
            onSelected(item)
        }
        val color = when (item.state) {
            State.Default -> R.color.colorPrimary
            State.Correct -> R.color.green
            State.Wrong -> R.color.red
            else -> R.color.grey
        }
        itemView.setBackgroundColor(ContextCompat.getColor(itemView.context, color))
    }
}
class ChooseItem(val text:String,var state:State)

enum class State{Default,Correct,Wrong}