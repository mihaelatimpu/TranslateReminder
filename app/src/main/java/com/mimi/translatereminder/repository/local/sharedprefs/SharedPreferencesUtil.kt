package com.mimi.translatereminder.repository.local.sharedprefs

import android.content.Context

/**
 * Created by Mimi on 13/12/2017.
 *
 */
class SharedPreferencesUtil {
    companion object {
        private val PREF_FILENAME = "settingsPreferences"
        private val LEARNING_ITEMS_PER_SESSION = "learningItemsPerSession"
        private val REVIEW_ITEMS_PER_SESSION = "reviewItemsPerSession"
        private val LISTENING_ITEMS_PER_SESSION = "listeningItemsPerSession"
        private val WRONG_ITEMS_PER_SESSION = "wrongItemsPerSession"
        private val SPELL_WORDS = "spellWords"
    }

    fun getLearningItemsPerSession(context: Context): Int {
        val prefs = context.getSharedPreferences(PREF_FILENAME, 0)
        return prefs.getInt(LEARNING_ITEMS_PER_SESSION, 5)
    }

    fun getReviewItemsPerSession(context: Context): Int {
        val prefs = context.getSharedPreferences(PREF_FILENAME, 0)
        return prefs.getInt(REVIEW_ITEMS_PER_SESSION, 15)
    }


    fun getListeningItemsPerSession(context: Context): Int {
        val prefs = context.getSharedPreferences(PREF_FILENAME, 0)
        return prefs.getInt(LISTENING_ITEMS_PER_SESSION, -1)
    }

    fun getWrongItemsPerSession(context: Context): Int {
        val prefs = context.getSharedPreferences(PREF_FILENAME, 0)
        return prefs.getInt(WRONG_ITEMS_PER_SESSION, 3)
    }

    fun setLearningItemsPerSession(context: Context, items: Int) {
        val prefs = context.getSharedPreferences(PREF_FILENAME, 0)
        val editor = prefs.edit()
        editor.putInt(LEARNING_ITEMS_PER_SESSION, items)
        editor.apply()
    }

    fun setReviewItemsPerSession(context: Context, items: Int) {
        val prefs = context.getSharedPreferences(PREF_FILENAME, 0)
        val editor = prefs.edit()
        editor.putInt(REVIEW_ITEMS_PER_SESSION, items)
        editor.apply()
    }

    fun setWrongItemsPerSession(context: Context, items: Int) {
        val prefs = context.getSharedPreferences(PREF_FILENAME, 0)
        val editor = prefs.edit()
        editor.putInt(WRONG_ITEMS_PER_SESSION, items)
        editor.apply()
    }

    fun getSpellWords(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_FILENAME, 0)
        return prefs.getBoolean(SPELL_WORDS, true)
    }

    fun setSpellWords(context: Context, active: Boolean) {
        val prefs = context.getSharedPreferences(PREF_FILENAME, 0)
        val editor = prefs.edit()
        editor.putBoolean(SPELL_WORDS, active)
        editor.apply()
    }

}