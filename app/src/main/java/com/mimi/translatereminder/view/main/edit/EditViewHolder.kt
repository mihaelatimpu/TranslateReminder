package com.mimi.translatereminder.view.main.edit

import android.view.View
import com.mimi.translatereminder.R
import com.mimi.translatereminder.dto.Entity
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
        val state = when{
            entity.isLearning() -> R.string.menu_learning
            entity.isWrong() -> R.string.menu_mistakes
            entity.isReviewing() && entity.nextReview <= Calendar.getInstance().timeInMillis -> R.string.review
            else -> null
        }
        if(state != null){
            itemView.subtitle.visibility = View.VISIBLE
            itemView.subtitle.text = itemView.context.getString(state)
        } else {
            itemView.subtitle.visibility = View.INVISIBLE

        }
        itemView.setOnClickListener { onClick(entity) }
    }

}
