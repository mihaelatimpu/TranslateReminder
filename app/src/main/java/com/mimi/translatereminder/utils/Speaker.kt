package com.mimi.translatereminder.utils

import android.content.Context
import android.media.AudioManager
import android.os.Build
import android.speech.tts.TextToSpeech
import java.util.*
import kotlin.collections.HashMap

/**
 * Created by Mimi on 20/12/2017.
 *
 */
class Speaker(context: Context) : TextToSpeech.OnInitListener {
    private val tts: TextToSpeech = TextToSpeech(context, this)
    private var ready = false
    var allowed = true
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts.language = Locale.GERMAN
            ready = true
        } else {
            ready = false
        }
    }

    fun speak(text: String) {
        if (ready && allowed) {
            val hash = HashMap<String, String>()
            hash.put(TextToSpeech.Engine.KEY_PARAM_STREAM,
                    AudioManager.STREAM_NOTIFICATION.toString())

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                tts.speak(text, TextToSpeech.QUEUE_ADD, null, null)
            } else {
                tts.speak(text, TextToSpeech.QUEUE_ADD, hash)
            }

        }
    }
}