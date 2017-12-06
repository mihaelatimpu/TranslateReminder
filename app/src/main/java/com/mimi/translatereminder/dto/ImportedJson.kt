package com.mimi.translatereminder.dto

/**
 * Created by Mimi on 06/12/2017.
 *
 */
class ImportedJson(val words: List<Word>)

class Word(private val german: String, private val translated: String){
    fun toEntity()
    = Entity(germanWord = german, translation = translated)
}