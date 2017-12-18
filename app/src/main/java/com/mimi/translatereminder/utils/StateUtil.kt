package com.mimi.translatereminder.utils

import com.mimi.translatereminder.dto.Entity
import java.util.*


/**
 * Created by Mimi on 13/12/2017.
 *
 */
class StateUtil {
    fun increaseState(entity: Entity) {
        when {
            entity.isWrong() -> increaseWrongState(entity)
            entity.isReviewing() -> increaseReviewState(entity)
            entity.isLearning() -> increaseLearningState(entity)
            else -> throw UnsupportedOperationException("Unknown state: ${entity.state}")
        }
    }

    private fun increaseWrongState(entity: Entity) {
        val calendar = Calendar.getInstance()
        when {
            Entity.isWrongState(entity.state + 1) -> entity.state++
            Entity.isLearningState(entity.stateBeforeBeingWrong) -> entity.state = entity.stateBeforeBeingWrong
            else -> entity.state = Entity.firstReviewState
        }
        entity.nextReview = calendar.timeInMillis

    }

    private fun increaseLearningState(entity: Entity) {
        if (Entity.isLearningState(entity.state + 1))
            entity.state++
        else
            entity.state = Entity.firstReviewState
        resetReviewTime(entity)

    }

    private fun increaseReviewState(entity: Entity) {
        if (Entity.isReviewingState(entity.state + 1))
            entity.state++
        resetReviewTime(entity)
    }

    private fun resetReviewTime(entity: Entity) {
        val calendar = Calendar.getInstance()
        when (entity.state) {
            Entity.STATE_LEARNING_4 -> {
                calendar.add(Calendar.DAY_OF_MONTH, 1)
            }
            Entity.STATE_REVIEW_1 -> {
                calendar.add(Calendar.DAY_OF_MONTH, 2)
            }
            Entity.STATE_REVIEW_2 -> {
                calendar.add(Calendar.WEEK_OF_YEAR, 1)
            }
            Entity.STATE_REVIEW_3 -> {
                calendar.add(Calendar.WEEK_OF_YEAR, 1)
            }
            Entity.STATE_REVIEW_4 -> {
                calendar.add(Calendar.MONTH, 1)
            }
            Entity.STATE_REVIEW_5 -> {
                calendar.add(Calendar.MONTH, 2)
            }
            Entity.STATE_REVIEW_6 -> {
                calendar.add(Calendar.MONTH, 6)
            }
        }
        entity.nextReview = calendar.timeInMillis
    }

    fun setWrong(entity: Entity): Entity {
        if (!entity.isWrong())
            entity.stateBeforeBeingWrong = entity.state
        entity.state = Entity.firstMistakeState

        return entity
    }
}
