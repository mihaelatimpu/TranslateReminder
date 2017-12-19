package com.mimi.translatereminder.utils

import com.mimi.translatereminder.dto.Entity
import org.junit.Test

import org.junit.Assert.*

/**
 * Created by Mimi on 18/12/2017.
 *
 */
class LearningFragmentsGeneratorTest {
    private val testedClass = LearningFragmentsGenerator()

    @Test
    fun eachReviewItemsShouldHaveOnlyOneFragment(){
        val items = listOf(
                Entity(id=1,germanWord = "",translation = "", state = Entity.firstReviewState),
                Entity(id=2,germanWord = "",translation = "", state = Entity.firstReviewState),
                Entity(id=3,germanWord = "",translation = "", state = Entity.firstReviewState),
                Entity(id=4,germanWord = "",translation = "", state = Entity.firstReviewState),
                Entity(id=5,germanWord = "",translation = "", state = Entity.firstReviewState))
        val fragments = testedClass.start(items)
        assertEquals(items.size+1, fragments.size)
    }

    @Test
    fun getReviewFragmentsShouldReturnOneFragmentPerEntity(){
        val item = Entity(id=1,germanWord = "",translation = "", state = Entity.firstReviewState)
        val fragments = testedClass.getReviewFragments(item,item.state)
        assertEquals(1,fragments.size)
    }

    @Test
    fun getWrongFragmentsShouldReturnExactly3FragmentsPerItem(){
        val item = Entity(id=1,germanWord = "",translation = "", state = Entity.firstMistakeState)
        val fragments = testedClass.getWrongFragments(item)
        assertEquals(3,fragments.size)
    }

    @Test
    fun getWrongFragmentsShouldNotReturnTwoFragmentsOfTheSameType(){
        for(i in 0..30)
            checkWrongFragmentTypeFromOneItem()
    }
    private fun checkWrongFragmentTypeFromOneItem(){
        val item = Entity(id=1,germanWord = "",translation = "", state = Entity.firstMistakeState)
        val fragments = testedClass.getWrongFragments(item)
        fragments.forEach {
            val type = it.type
            val sameFragments = fragments.filter { it.type == type }
            assertEquals(1,sameFragments.size)
        }

    }

}