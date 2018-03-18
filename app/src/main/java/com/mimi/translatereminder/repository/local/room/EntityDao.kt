package com.mimi.translatereminder.repository.local.room

import android.arch.persistence.room.*
import com.mimi.translatereminder.dto.Entity


/**
 * Created by Mimi on 05/12/2017.
 *
 */
@Dao
interface EntityDao:ItemDao {
    companion object {
        const val TABLE = "entity"
        const val id = "id"
        const val state = "state"
        const val type = "type"
        const val parentId = "parentId"
        const val germanWord = "germanWord"
        const val translation = "translation"
        const val dateAdded = "dateAdded"
        const val nextReview = "nextReview"
        const val lastReview = "lastReview"
    }

    @Insert
    override fun insert(entity: Entity):Long

    @Update
    override fun updateEntity(vararg entity: Entity)


    @Query("SELECT * from $TABLE "+
            "ORDER BY $state ASC, $nextReview ASC")
    override fun selectAll(): List<Entity>

    @Query("SELECT * from $TABLE WHERE $id=:id")
    override fun selectItemById(id:Int): List<Entity>

    @Query("SELECT * from $TABLE WHERE $germanWord=:german")
    fun getEntitiesByGermanVersion(german: String): List<Entity>

    @Query("SELECT * from $TABLE WHERE $translation=:translation")
    fun getEntityByTranslation(translation: String): List<Entity>

    @Query("SELECT * from $TABLE WHERE $state=:state ORDER BY $dateAdded LIMIT :limit")
    fun getEntityByState(state:Int, limit:Int): List<Entity>

    @Query("SELECT * FROM $TABLE " +
            "WHERE $id<>:excludeId " +
            "ORDER BY $state DESC, $dateAdded ASC " +
            "LIMIT :limit")
    fun getRandomItems(excludeId:Int, limit:Int):List<Entity>

    @Query("SELECT * FROM $TABLE " +
            "WHERE $state >= ${Entity.firstLearningState} " +
            "AND $state <= ${Entity.lastLearningState} " +
            "ORDER BY $state DESC, $dateAdded ASC " +
            "LIMIT :limit")
    fun getLearningItems(limit:Int):List<Entity>

    @Query("SELECT * FROM $TABLE " +
            "WHERE $state >= ${Entity.firstReviewState} " +
            "AND $state <= ${Entity.lastReviewState} " +
            "AND $nextReview <= :currentTimeInMillis " +
            "ORDER BY $state ASC, $nextReview ASC " +
            "LIMIT :limit")
    fun getReviewItems(limit:Int, currentTimeInMillis:Long):List<Entity>

    @Query("SELECT * FROM $TABLE " +
            "WHERE $state >= ${Entity.firstReviewState} " +
            "AND $state <= ${Entity.lastReviewState} " +
            "ORDER BY $lastReview ASC " +
            "LIMIT :limit")
    fun getOverflow(limit:Int):List<Entity>

    @Query("SELECT * FROM $TABLE " +
            "WHERE $state >= ${Entity.firstMistakeState} " +
            "AND $state <= ${Entity.lastMistakeState} " +
            "ORDER BY $state DESC, $nextReview ASC " +
            "LIMIT :limit")
    fun getMistakenItems(limit:Int):List<Entity>

    @Query("SELECT * from $TABLE " +
            "WHERE $type = ${Entity.TYPE_SENTENCE} " +
            "AND $parentId = :itemId")
    override fun findSentences(itemId:Int):List<Entity>

    @Delete
    override fun delete(vararg entities: Entity)

    @Query("DELETE FROM $TABLE")
    override fun deleteAll()
}