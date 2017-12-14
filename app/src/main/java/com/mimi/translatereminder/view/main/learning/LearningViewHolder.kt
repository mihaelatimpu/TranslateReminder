package com.mimi.translatereminder.view.main.learning

import android.support.v4.content.ContextCompat
import android.view.View
import com.mimi.translatereminder.R
import com.mimi.translatereminder.dto.Entity
import com.mimi.translatereminder.view.main.ItemsAdapter
import kotlinx.android.synthetic.main.item_learning.view.*

/**
 * Created by Mimi on 06/12/2017.
 *
 */
class LearningViewHolder(itemView: View) : ItemsAdapter.BaseHolder(itemView) {
    override fun bind(entity: Entity, onClick: (Entity) -> Unit) {
        with(itemView){
            val progress = listOf(progress_1,progress_2,progress_3,progress_4)
            with(entity){
                title.text = germanWord
                if(state <= Entity.STATE_LEARNING_4){
                    for(i in 0..3){
                        progress[i].setBackgroundColor(
                                getColor(Entity.STATE_LEARNING_4 - i,state))
                    }
                    progress.forEach {
                        it.visibility = View.VISIBLE
                    }
                } else {
                    progress.forEach {
                        it.visibility = View.INVISIBLE
                    }

                }
            }
        }
        itemView.setOnClickListener { onClick(entity) }
    }
    private fun getColor(limit:Int, state:Int):Int{
        val res = if(state<=limit) R.color.grey else R.color.colorPrimaryDark
        return ContextCompat.getColor(itemView.context, res)
    }

}
