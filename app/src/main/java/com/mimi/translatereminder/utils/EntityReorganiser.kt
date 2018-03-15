package com.mimi.translatereminder.utils

import com.mimi.translatereminder.dto.Entity


/**
 * Created by Mimi on 15/03/2018.
 */
class EntityReorganiser {
    fun reorganiseEntities(initialList: List<Entity>): List<Entity> {
        val sentences = arrayListOf<Entity>()
        var words = arrayListOf<Entity>()
        initialList.forEach {
            if (it.type == Entity.TYPE_SENTENCE) {
                sentences.add(it)
            } else {
                words.add(it)
            }
        }
        val finalList = arrayListOf<Entity>()
        words.sortedByDescending { it.state }.forEach {
            val word = it
            finalList.add(word)
            val children = sentences.filter { it.parentId == word.id }
            finalList.addAll(children)
            sentences.removeAll(children)
        }
        sentences.forEach {
            if (!finalList.contains(it)) {
                finalList.add(it)
            }
        }
        return finalList
    }
}