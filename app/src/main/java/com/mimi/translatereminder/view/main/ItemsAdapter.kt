package com.mimi.translatereminder.view.main

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import com.mimi.translatereminder.R
import com.mimi.translatereminder.dto.Entity

/**
 * Created by Mimi on 06/12/2017.
 *
 */
open class ItemsAdapter<T : ItemsAdapter.BaseHolder>(val context: Context,
                                                     private val onClick: (Entity) -> Unit,
                                                     private val createHolder: (View) -> T,
                                                     private val resourceId: Int)
    : RecyclerView.Adapter<T>(),
        Filterable {

    private val allItems = arrayListOf<Entity>()
    private val filteredItems = arrayListOf<Entity>()
    private val mFilter = ItemsFilter()

    fun refreshItems(newTranslations: List<Entity>) {
        allItems.clear()
        allItems.addAll(newTranslations)
        notifyDataSetChanged()
    }

    override fun getFilter() = mFilter

    override fun onBindViewHolder(holder: T, position: Int) {
        holder.bind(filteredItems[position], onClick)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int)
            = createHolder(LayoutInflater.from(context).
            inflate(resourceId, parent, false))


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
        abstract fun bind(entity: Entity, onClick: (Entity) -> Unit)
    }
}
