package com.mimi.translatereminder.utils

import com.mimi.translatereminder.dto.Entity
import com.mimi.translatereminder.dto.Progress
import com.mimi.translatereminder.dto.Progress.Companion.TYPE_CHOOSE_GERMAN
import com.mimi.translatereminder.dto.Progress.Companion.TYPE_CHOOSE_TRANSLATION
import com.mimi.translatereminder.dto.Progress.Companion.TYPE_FORM
import com.mimi.translatereminder.dto.Progress.Companion.TYPE_HINT
import com.mimi.translatereminder.dto.Progress.Companion.TYPE_PRESENT
import com.mimi.translatereminder.dto.Progress.Companion.TYPE_TYPING
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Mimi on 13/12/2017.
 * This class will generate the fragments information for the learning activity
 */
class LearningFragmentsGenerator {
    fun generateListeningFragments(entities: List<Entity>): List<Progress> {
        return entities.map {
            Progress(type = TYPE_PRESENT,state = Entity.firstLearningState,entityId = it.id,entity = it)
        }
    }
    fun start(entities: List<Entity>): List<Progress> {
        val fragments = ArrayList<Progress>()
        entities.forEach {
            if (it.isLearning())
                fragments.addAll(getLearningFragments(it, it.state))
            if (it.isWrong())
                fragments.addAll(getWrongFragments(it))
            if (it.isReviewing())
                fragments.addAll(getReviewFragments(it, it.state))
        }
        if (entities.all { it.isReviewing() })
            fragments.add(Progress(type = Progress.TYPE_GROUP, ids = entities.map { it.id },
                    state = Entity.firstReviewState, entityId = 0, entity = null))
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
        if (alreadyAdded.filter { it.entityId == progress.entityId }.any { it.state > progress.state })
            return false
        if (remainingItems.filter { it.entityId == progress.entityId }.any { it.state < progress.state })
            return false
        return true
    }

    private fun getLearningFragments(entity: Entity, state: Int): List<Progress> {
        val types = ArrayList<Progress>()
        val type = getLearningFragmentType(entity, state)
        types.add(Progress(type = type,
                state = state, entityId = entity.id, entity = entity))
        if (Entity.isLearningState(state + 1))
            types.addAll(getLearningFragments(entity, state + 1))
        return types
    }

    private fun getLearningFragmentType(entity: Entity, state: Int): Int {
        if (state == Entity.STATE_LEARNING_1)
            return TYPE_PRESENT
        if (entity.type == Entity.TYPE_SENTENCE)
            return getRandomFragmentType(getRandomSentenceReviewFragment())
        return when (state) {
            Entity.STATE_LEARNING_2 -> TYPE_HINT
            in Entity.STATE_LEARNING_3 until Entity.lastLearningState ->
                getRandomFragmentType(getRandomReviewFragment())
            Entity.lastLearningState -> TYPE_TYPING
            else -> throw UnsupportedOperationException("Unknown type: $state")
        }
    }

    fun getReviewFragments(entity: Entity, state: Int): List<Progress> {
        val types = ArrayList<Progress>()
        val type = when (entity.type) {
            Entity.TYPE_SENTENCE -> getRandomFragmentType(getRandomSentenceReviewFragment())
            Entity.TYPE_WORD -> getRandomFragmentType(getRandomReviewFragment())
            else -> throw UnsupportedOperationException("Unknown type: ${entity.type}")
        }
        types.add(Progress(type = type,
                state = state, entityId = entity.id, entity = entity))
        return types

    }

    fun getWrongFragments(entity: Entity): List<Progress> {
        val types = ArrayList<Progress>()
        types.add(Progress(type = TYPE_PRESENT,
                state = Entity.firstMistakeState, entityId = entity.id, entity = entity))

        val fragmentTypes = when (entity.type) {
            Entity.TYPE_SENTENCE -> getRandomSentenceReviewFragment()
            Entity.TYPE_WORD -> getRandomReviewFragment()
            else -> throw UnsupportedOperationException("Unknown type: ${entity.type}")
        }
        for (i in Entity.firstMistakeState + 1..Entity.lastMistakeState) {
            val type = getRandomFragmentType(fragmentTypes)
            types.add(Progress(type = type,
                    state = i, entityId = entity.id, entity = entity))
            if (entity.type != Entity.TYPE_SENTENCE)
                fragmentTypes.remove(type)
        }
        return types
    }

    private fun getRandomReviewFragment(): ArrayList<Int> {
        return arrayListOf(TYPE_CHOOSE_GERMAN, TYPE_CHOOSE_TRANSLATION, TYPE_TYPING, TYPE_FORM)
    }
    private fun getRandomSentenceReviewFragment(): ArrayList<Int> {
        return arrayListOf(TYPE_CHOOSE_GERMAN, TYPE_CHOOSE_TRANSLATION, TYPE_FORM, TYPE_FORM)
    }

    private fun getRandomFragmentType(types: List<Int>): Int {
        return types[Random().nextInt(types.size)]
    }


}