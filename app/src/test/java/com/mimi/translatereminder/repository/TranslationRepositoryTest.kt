package com.mimi.translatereminder.repository

import com.mimi.translatereminder.dto.Entity
import junit.framework.Assert
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Created by Mimi on 18/03/2018.
 */

class TranslationRepositoryTest {

    private val archivedDao = ArchivedTestDao(arrayListOf())
    private val itemsDao = EntityTestDao(arrayListOf())

    val testClass = TranslationRepository(itemsDao,archivedDao)

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }



    @Test
    fun archiveItems() {
        val items = arrayListOf(
                Entity(id = 2, germanWord = "G2", type = Entity.TYPE_SENTENCE,translation = "T1", parentId = 0),
                Entity(id = 3, germanWord = "G3", type = Entity.TYPE_WORD,translation = "T1"),
                Entity(id = 4, germanWord = "G4", type = Entity.TYPE_SENTENCE,translation = "T1", parentId = 3),
                Entity(id = 6, germanWord = "G6", type = Entity.TYPE_WORD,translation = "T1"),
                Entity(id = 7, germanWord = "G7", type = Entity.TYPE_SENTENCE,translation = "T1", parentId = 6)
        )
        val itemsToBeArchived = listOf(
                Entity(id = 0, germanWord = "G0", type = Entity.TYPE_WORD,translation = "T1"),
                Entity(id = 1, germanWord = "G1", type = Entity.TYPE_SENTENCE,translation = "T1", parentId = 0),
                Entity(id = 5, germanWord = "G5", type = Entity.TYPE_SENTENCE,translation = "T1", parentId = 3)
        )
        items.addAll(itemsToBeArchived)
        itemsDao.list = items
        archivedDao.list.clear()
        testClass.archiveItems(itemsToBeArchived)
        Assert.assertEquals(2, itemsDao.list.size)
        Assert.assertEquals(6, archivedDao.list.size)
    }
    @Test
    fun unarchiveItems() {
        val items = arrayListOf(
                Entity(id = 2, germanWord = "G2", type = Entity.TYPE_SENTENCE,translation = "T1", parentId = 0),
                Entity(id = 3, germanWord = "G3", type = Entity.TYPE_WORD,translation = "T1"),
                Entity(id = 4, germanWord = "G4", type = Entity.TYPE_SENTENCE,translation = "T1", parentId = 3),
                Entity(id = 6, germanWord = "G6", type = Entity.TYPE_WORD,translation = "T1"),
                Entity(id = 7, germanWord = "G7", type = Entity.TYPE_SENTENCE,translation = "T1", parentId = 6)
        )
        val itemsToBeArchived = listOf(
                Entity(id = 0, germanWord = "G0", type = Entity.TYPE_WORD,translation = "T1"),
                Entity(id = 1, germanWord = "G1", type = Entity.TYPE_SENTENCE,translation = "T1", parentId = 0),
                Entity(id = 5, germanWord = "G5", type = Entity.TYPE_SENTENCE,translation = "T1", parentId = 3)
        )
        items.addAll(itemsToBeArchived)
        archivedDao.list = items
        itemsDao.list.clear()
        testClass.unarchiveItems(itemsToBeArchived)
        Assert.assertEquals(2, archivedDao.list.size)
        Assert.assertEquals(6, itemsDao.list.size)
    }



}