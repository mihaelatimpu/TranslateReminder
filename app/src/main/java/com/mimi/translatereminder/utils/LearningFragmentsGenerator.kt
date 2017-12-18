package com.mimi.translatereminder.utils

import com.mimi.translatereminder.dto.Entity
import com.mimi.translatereminder.dto.Progress
import com.mimi.translatereminder.dto.Progress.Companion.TYPE_CHOOSE_GERMAN
import com.mimi.translatereminder.dto.Progress.Companion.TYPE_CHOOSE_TRANSLATION
import com.mimi.translatereminder.dto.Progress.Companion.TYPE_HINT
import com.mimi.translatereminder.dto.Progress.Companion.TYPE_PRESENT
import com.mimi.translatereminder.dto.Progress.Companion.TYPE_TYPING
import java.util.*

/**
 * Created by Mimi on 13/12/2017.
 * This class will generate the fragments information for the learning activity
 */
class LearningFragmentsGenerator {
    fun start(entities: List<Entity>): List<Progress> {
        val fragments = ArrayList<Progress>()
        entities.forEach {
            if (it.isLearning())
                fragments.addAll(getLearningFragments(it, it.state))
            if (it.isWrong())
                fragments.addAll(getWrongFragments(it, it.state))
            if (it.isReviewing())
                fragments.addAll(getReviewFragments(it, it.state))
        }
        if (entities.all { it.isReviewing() })
            fragments.add(Progress(type = Progress.TYPE_GROUP, ids = entities.map { it.id },
                    state = Entity.firstReviewState, entityId = 0))
        return decideFragmentOrder(fragments)
    }

    private fun decideFragmentOrder(oldList: List<Progress>): List<Progress> {
        val remainingItems = ArrayList<Progress>(oldList)
        val addedItems = ArrayList<Progress>()
        while (remainingItems.isNotEmpty()) {
            val position = Random().nextInt(remainingItems.size)
            val progress = remainingItems[position]
            if (isValid(progress, addedItems, remainingItems)) {
                addedItems.add(progress)
                remainingItems.remove(progress)
            }
        }
        return addedItems
    }

    private fun isValid(progress: Progress, alreadyAdded: List<Progress>, remainingItems: List<Progress>): Boolean {
        if (alreadyAdded.any { it.entityId == progress.entityId && it.state > progress.state })
            return false
        if (remainingItems.any { it.entityId == progress.entityId && it.state < progress.state })
            return false
        return true
    }

    private fun getLearningFragments(entity: Entity, state: Int): List<Progress> {
        val types = ArrayList<Progress>()
        val type = when (state) {
            Entity.STATE_LEARNING_1 -> TYPE_PRESENT
            Entity.STATE_LEARNING_2 -> TYPE_CHOOSE_GERMAN
            Entity.STATE_LEARNING_3 -> TYPE_HINT
            Entity.STATE_LEARNING_4 -> TYPE_TYPING
            else -> throw UnsupportedOperationException("Unknown type: $state")
        }
        types.add(Progress(type = type,
                state = state, entityId = entity.id))
        if (Entity.isLearningState(state + 1))
            types.addAll(getLearningFragments(entity, state + 1))
        return types
    }

    private fun getReviewFragments(entity: Entity, state: Int): List<Progress> {
        val types = ArrayList<Progress>()
        val type = getRandomReviewFragment()
        types.add(Progress(type = type,
                state = state, entityId = entity.id))
        if (Entity.isReviewingState(state + 1))
            types.addAll(getReviewFragments(entity, state + 1))
        return types
    }

    private fun getWrongFragments(entity: Entity, state: Int): List<Progress> {
        val types = ArrayList<Progress>()
        val type = if (state == Entity.firstMistakeState)
            TYPE_PRESENT else getRandomReviewFragment()
        types.add(Progress(type = type,
                state = state, entityId = entity.id))
        if (Entity.isWrongState(state + 1))
            types.addAll(getWrongFragments(entity, state + 1))
        return types
    }

    private fun getRandomReviewFragment(): Int {
        return getRandomFragmentType(listOf(TYPE_CHOOSE_GERMAN, TYPE_CHOOSE_TRANSLATION, TYPE_TYPING))
    }

    private fun getRandomFragmentType(types: List<Int>): Int {
        return types[Random().nextInt(types.size)]
    }


}