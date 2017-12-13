package com.mimi.translatereminder.utils

import com.mimi.translatereminder.dto.Entity
import java.util.*


/**
 * Created by Mimi on 13/12/2017.
 *
 */
class StateUtil {
    fun increaseState(entity: Entity): Entity {
        val calendar = Calendar.getInstance()
        if (entity.state == Entity.WRONG) {
            if (entity.stateBeforeBeingWrong < Entity.STATE_REVIEW_4)
                entity.stateBeforeBeingWrong++
        }
        if (entity.state < Entity.STATE_REVIEW_4)
            entity.state++
        when {
            entity.state == Entity.STATE_LEARNING_4 -> {
                calendar.add(Calendar.DAY_OF_MONTH, 1)
            }
            entity.state == Entity.STATE_REVIEW_1 -> {
                calendar.add(Calendar.DAY_OF_MONTH, 2)
            }
            entity.state == Entity.STATE_REVIEW_2 -> {
                calendar.add(Calendar.WEEK_OF_YEAR, 1)
            }
            else -> {
                calendar.add(Calendar.MONTH, 1)
            }
        }
        entity.nextReview = calendar.timeInMillis
        return entity
    }

    fun setWrong(entity: Entity): Entity {
        if (entity.state != Entity.WRONG) {
            entity.stateBeforeBeingWrong = entity.state
            entity.state = Entity.WRONG
        }
        return entity
    }
}
