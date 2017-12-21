package com.mimi.translatereminder.view.main.edit

import android.view.View
import com.mimi.translatereminder.R
import com.mimi.translatereminder.dto.Entity
import com.mimi.translatereminder.utils.TimeUtils
import com.mimi.translatereminder.view.main.ItemsAdapter
import kotlinx.android.synthetic.main.item_translation.view.*
import java.util.*

/**
 * Created by Mimi on 14/12/2017.
 *
 */
class EditViewHolder(itemView: View) : ItemsAdapter.BaseHolder(itemView) {
    override fun bind(entity: Entity, onClick: (Entity) -> Unit) {
        itemView.title.text = entity.germanWord
        itemView.translation.text = entity.translation
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
        itemView.setOnClickListener { onClick(entity) }
    }

}
