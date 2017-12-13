package com.mimi.translatereminder.utils

import com.mimi.translatereminder.dto.Entity
import com.mimi.translatereminder.dto.LearningFragmentInfo

/**
 * Created by Mimi on 13/12/2017.
 * This class will generate the fragments information for the learning activity
 */
class LearningFragmentsGenerator {
    fun start(entities: List<Entity>): List<LearningFragmentInfo> {
        return entities.map { getFragment(it) }
    }

    private fun getFragment(entity: Entity): LearningFragmentInfo {
        val type = when (entity.state) {
            Entity.STATE_LEARNING_1 -> LearningFragmentInfo.TYPE_FIRST
            else -> LearningFragmentInfo.TYPE_TYPING
        }
        return LearningFragmentInfo(type = type, entityId = entity.id)
    }
}