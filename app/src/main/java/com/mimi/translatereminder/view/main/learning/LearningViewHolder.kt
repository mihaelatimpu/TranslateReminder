package com.mimi.translatereminder.view.main.learning

import android.support.v4.content.ContextCompat
import android.view.View
import com.mimi.translatereminder.R
import com.mimi.translatereminder.dto.Entity
import com.mimi.translatereminder.utils.TimeUtils
import com.mimi.translatereminder.view.main.ItemsAdapter
import kotlinx.android.synthetic.main.item_learning.view.*

/**
 * Created by Mimi on 06/12/2017.
 *
 */
class LearningViewHolder(itemView: View) : ItemsAdapter.BaseHolder(itemView) {
    override fun bind(entity: Entity, onClick: (Entity) -> Unit) {
        itemView.translationTitle.text = entity.germanWord
        itemView.setOnClickListener { onClick(entity) }
        when {
            entity.isLearning() -> bindLearningItem(entity)
            entity.isReviewing() -> bindReviewItem(entity)
            else -> bindWrongItem()
        }
        itemView.setOnClickListener { onClick(entity) }
    }

    private fun bindLearningItem(entity: Entity) {
        with(itemView) {
            val progress = listOf(progress_1, progress_2, progress_3, progress_4,
                    progress_5, progress_6, progress_7)
            progressLayout.visibility = View.VISIBLE
            reviewTime.visibility = View.GONE
            with(entity) {
                for (i in 0..6) {
                    progress[i].setBackgroundColor(
                            getColor(Entity.lastLearningState - i, state))
                }
            }
        }

    }

    private fun bindReviewItem(entity: Entity) {
        with(itemView) {
            progressLayout.visibility = View.GONE
            reviewTime.visibility = View.VISIBLE
            reviewTime.text = TimeUtils().getTime(entity.nextReview, context)
        }

    }

    private fun bindWrongItem() {
        with(itemView) {
            progressLayout.visibility = View.GONE
            reviewTime.visibility = View.GONE
        }

    }

    private fun getColor(limit: Int, state: Int): Int {
        val res = if (state <= limit) R.color.grey else R.color.colorPrimaryDark
        return ContextCompat.getColor(itemView.context, res)
    }

}
