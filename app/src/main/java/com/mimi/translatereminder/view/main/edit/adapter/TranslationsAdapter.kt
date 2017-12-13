package com.mimi.translatereminder.view.main.edit.adapter

import android.content.Context
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import com.mimi.translatereminder.R
import com.mimi.translatereminder.dto.Entity
import kotlinx.android.synthetic.main.item_translation.view.*

/**
 * Created by Mimi on 06/12/2017.
 *
 */
open class TranslationsAdapter(val context: Context,
                               val editItem: (Entity) -> Unit,
                               val deleteItem: (Entity) -> Unit)
    : RecyclerView.Adapter<TranslationsAdapter.TranslationHolder>(),
        Filterable {

    private val allItems = arrayListOf<Entity>()
    private val filteredItems = arrayListOf<Entity>()
    private val mFilter = ItemsFilter()

    fun refreshTranslations(newTranslations: List<Entity>) {
        allItems.clear()
        allItems.addAll(newTranslations)
        notifyDataSetChanged()
    }

    override fun getFilter() = mFilter

    override fun onBindViewHolder(holder: TranslationHolder, position: Int) {
        holder.bind(filteredItems[position], editItem, deleteItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int)
            = TranslationHolder(LayoutInflater.from(context).
            inflate(R.layout.item_translation, parent, false))


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
            filteredItems.addAll(results?.values as List<Entity>)
            notifyDataSetChanged()
        }

    }

    inner class TranslationHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(entity: Entity, editItem: (Entity) -> Unit, deleteItem: (Entity) -> Unit) {
            itemView.title.text = entity.germanWord
            itemView.subtitle.text = entity.translation
            itemView.overflowButton.setOnClickListener { showPopupMenu(entity, editItem, deleteItem) }
        }


        private fun showPopupMenu(value: Entity, editItem: (Entity) -> Unit, deleteItem: (Entity) -> Unit) {
            val popup = PopupMenu(itemView.context, itemView.overflowButton)
            val inflater = popup.menuInflater
            inflater.inflate(R.menu.menu_edit_delete, popup.menu)
            popup.show()
            popup.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.edit -> editItem(value)
                    R.id.delete -> deleteItem(value)
                }
                false
            }
        }

    }
}
