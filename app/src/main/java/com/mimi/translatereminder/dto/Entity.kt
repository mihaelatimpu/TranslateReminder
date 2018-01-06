package com.mimi.translatereminder.dto

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.mimi.translatereminder.repository.local.room.TranslationDao
import java.util.*

/**
 * Created by Mimi on 05/12/2017.
 * The base entity of the application
 */
@Entity(tableName = TranslationDao.TABLE)
class Entity(
        @PrimaryKey(autoGenerate = true)
        val id: Int = 0,
        var germanWord: String,
        var translation: String,
        val dateAdded: Long = Calendar.getInstance().timeInMillis,
        var nextReview: Long = Calendar.getInstance().timeInMillis,
        var lastReview: Long = Calendar.getInstance().timeInMillis,
        var reviewCount: Int = 0,
        var mistakesCount: Int = 0,
        var stateBeforeBeingWrong: Int = firstLearningState,
        var state: Int = firstLearningState) {
    fun isLearning() = isLearningState(state)
    fun isWrong() = isWrongState(state)
    fun isReviewing() = isReviewingState(state)

    companion object {
        const val STATE_LEARNING_1 = 1 // learning stage 1
        const val STATE_LEARNING_2 = 2 // learning stage 2
        const val STATE_LEARNING_3 = 3 // learning stage 3
        const val STATE_LEARNING_4 = 4 // learning stage 4
        const val STATE_LEARNING_5 = 5 // learning stage 4
        const val STATE_LEARNING_6 = 6 // learning stage 4
        const val STATE_LEARNING_7 = 7 // learning stage 4
        const val STATE_REVIEW_1 = 10 // review next day
        const val STATE_REVIEW_2 = 11 // review after two days
        const val STATE_REVIEW_3 = 12 // review after a week
        const val STATE_REVIEW_4 = 13 // review after a month
        const val STATE_REVIEW_5 = 14 // review after 2 months
        const val STATE_REVIEW_6 = 15 // review after 6 months
        const val STATE_MISTAKE_1 = 20 // relearn item 1
        const val STATE_MISTAKE_2 = 21 // relearn item 1
        const val STATE_MISTAKE_3 = 22 // relearn item 1

        const val firstReviewState = STATE_REVIEW_1
        const val lastReviewState = STATE_REVIEW_6
        const val firstLearningState = STATE_LEARNING_1
        const val lastLearningState = STATE_LEARNING_7
        const val firstMistakeState = STATE_MISTAKE_1
        const val lastMistakeState = STATE_MISTAKE_3

        fun isLearningState(state: Int) = state in firstLearningState..lastLearningState
        fun isWrongState(state: Int) = state in firstMistakeState..lastMistakeState
        fun isReviewingState(state: Int) = state in firstReviewState..lastReviewState
    }
}
