package com.mimi.translatereminder.view.main.edit

import android.view.View
import com.mimi.translatereminder.R
import com.mimi.translatereminder.dto.Entity
import com.mimi.translatereminder.utils.TimeUtils
import com.mimi.translatereminder.view.main.ItemsAdapter
import com.mimi.translatereminder.view.main.SelectableInterface
import kotlinx.android.synthetic.main.item_translation.view.*

/**
 * Created by Mimi on 14/12/2017.
 *
 */
class EditViewHolder(itemView: View) : ItemsAdapter.BaseHolder(itemView) {

    override fun bind(entity: Entity, onClick: (Entity) -> Unit, selected: Boolean,
                      selectableInterface: SelectableInterface) {
        itemView.title.text = entity.germanWord
        itemView.translation.text = entity.translation
        refreshSelected(selectableInterface.getSelectedItems().contains(entity))

        val state = when {
            entity.isLearning() -> itemView.context.getString(R.string.menu_learning)
            entity.isWrong() -> itemView.context.getString(R.string.menu_mistakes)
            entity.isReviewing() -> TimeUtils().getTime(entity.nextReview, itemView.context)
            else -> null
        }
        if (state != null) {
            itemView.subtitle.visibility = View.VISIBLE
            itemView.subtitle.text = state
        } else {
            itemView.subtitle.visibility = View.INVISIBLE

        }
        itemView.setOnClickListener { onClick(entity, onClick, selectableInterface) }

        itemView.setOnLongClickListener {
            selectableInterface.changeState(!selectableInterface.isInSelectableMode())
            if (selectableInterface.isInSelectableMode()) {
                selectableInterface.getSelectedItems().add(entity)
                refreshSelected(true)
                selectableInterface.changeSelectedCount(selectableInterface.getSelectedItems().size)
            }
            return@setOnLongClickListener true
        }
    }

    private fun onClick(entity: Entity, onClick: (Entity) -> Unit,
                        selectableInterface: SelectableInterface) {
        if (selectableInterface.isInSelectableMode()) {
            val alreadyExists = selectableInterface.getSelectedItems().contains(entity)
            refreshSelected(!alreadyExists)
            if (alreadyExists) {
                selectableInterface.getSelectedItems().remove(entity)
            } else {
                selectableInterface.getSelectedItems().add(entity)
            }
            selectableInterface.changeSelectedCount(selectableInterface.getSelectedItems().size)

        } else {
            onClick(entity)
        }


    }

    private fun refreshSelected(selected: Boolean) {
        if (selected) {
            itemView.setBackgroundResource(R.drawable.learning_item_bg_selected)
        } else {
            itemView.setBackgroundResource(R.drawable.learning_item_bg)

        }
    }

}
