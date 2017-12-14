package com.mimi.translatereminder.view.main.edit

import android.view.View
import com.mimi.translatereminder.dto.Entity
import com.mimi.translatereminder.view.main.ItemsAdapter
import kotlinx.android.synthetic.main.item_translation.view.*

/**
 * Created by Mimi on 14/12/2017.
 *
 */
class EditViewHolder(itemView: View) : ItemsAdapter.BaseHolder(itemView) {
    override fun bind(entity: Entity, onClick: (Entity) -> Unit) {
        itemView.title.text = entity.germanWord
        itemView.subtitle.text = entity.translation
        itemView.setOnClickListener { onClick(entity) }
    }

}
