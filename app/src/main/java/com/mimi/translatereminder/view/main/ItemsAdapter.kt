package com.mimi.translatereminder.view.main

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import com.mimi.translatereminder.dto.Entity

/**
 * Created by Mimi on 06/12/2017.
 *
 */
open class ItemsAdapter<T : ItemsAdapter.BaseHolder>(val context: Context,
                                                     private val onClick: (Entity) -> Unit,
                                                     private val createHolder: (View) -> T,
                                                     private val resourceId: Int,
                                                     private val notifyChangeState: (Boolean) -> Unit = {},
                                                     private val notifyChangeCount: (Int) -> Unit = {})
    : RecyclerView.Adapter<T>(),
        Filterable, SelectableInterface {

    private val allItems = arrayListOf<Entity>()
    private val filteredItems = arrayListOf<Entity>()
    private val mFilter = ItemsFilter()
    private val selectedItems = arrayListOf<Entity>()
    private var selectableMode = false
    private var shouldShowState: Boolean = false

    fun refreshItems(newTranslations: List<Entity>, shouldShowState: Boolean) {
        allItems.clear()
        allItems.addAll(newTranslations)
        this.shouldShowState = shouldShowState
        notifyDataSetChanged()
    }

    override fun isInSelectableMode() = selectableMode
    override fun changeSelectedCount(count: Int) {
        notifyChangeCount(count)
    }

    override fun changeState(state: Boolean) {
        if (state == selectableMode) {
            return
        }
        selectableMode = state
        notifyChangeState(selectableMode)
        if (!selectableMode) {
            selectedItems.clear()
            notifyDataSetChanged()
        }
    }

    override fun getSelectedItems() = selectedItems

    override fun getFilter() = mFilter

    override fun onBindViewHolder(holder: T, position: Int) {
        val entity = filteredItems[position]
        holder.bind(entity, onClick, selectedItems.contains(entity), this,
                shouldShowState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = createHolder(LayoutInflater.from(context).inflate(resourceId, parent, false))


    override fun getItemCount() = filteredItems.size

    inner class ItemsFilter : Filter() {

        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val results = FilterResults()
            if (constraint == null)
                results.values = allItems
            else
                results.values = allItems.filter {
                    it.germanWord.toLowerCase().contains(constraint.toString().toLowerCase()) ||
                            it.translation.toLowerCase().contains(constraint.toString().toLowerCase())
                }
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            filteredItems.clear()
            if (results?.values != null)
                filteredItems.addAll(results.values as List<Entity>)
            notifyDataSetChanged()
        }

    }

    abstract class BaseHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(entity: Entity, onClick: (Entity) -> Unit,
                          selected: Boolean, selectableInterface: SelectableInterface,
                          shouldShowState: Boolean)
    }
}

interface SelectableInterface {

    fun changeState(state: Boolean)
    fun changeSelectedCount(count: Int)
    fun getSelectedItems(): ArrayList<Entity>
    fun isInSelectableMode(): Boolean
}
