package com.mimi.translatereminder.view.learning.fragments.group

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
 *
 */
class GroupAdapter(val context: Context, private val onSelected: (GroupItem) -> Unit) : RecyclerView.Adapter<GroupViewHolder>() {
    val items = arrayListOf<GroupItem>()
    fun refreshItems(newItems: List<String>) {
        items.clear()
        newItems.forEach { items.add(GroupItem(it, State.Default)) }
        notifyDataSetChanged()
    }

    fun refreshItem(item: GroupItem) {
        notifyItemChanged(items.indexOf(item))
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int) = GroupViewHolder(LayoutInflater.from(context).inflate(R.layout.item_group, parent, false))

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: GroupViewHolder?, position: Int) {
        holder?.bindItem(items[position], onSelected)
    }

}

class GroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bindItem(item: GroupItem, onSelected: (GroupItem) -> Unit) {
        itemView.label.text = item.text
        val color = when (item.state) {
            State.Default -> R.color.colorPrimary
            State.Chosen -> R.color.colorAccent
            State.Correct -> R.color.green
            State.Wrong -> R.color.red
            else -> R.color.grey
        }
        itemView.setOnClickListener {
            if (item.state == State.Default) {
                onSelected(item)
            }
        }
        itemView.setBackgroundColor(ContextCompat.getColor(itemView.context, color))
    }
}

class GroupItem(val text: String, var state: State)
enum class State { Default, Chosen, Wrong, Correct, NotSelectable }