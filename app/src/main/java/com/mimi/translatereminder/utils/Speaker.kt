package com.mimi.translatereminder.utils

import android.content.Context
import android.media.AudioManager
import android.os.Build
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
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
    private var delayedWord: String? = null
    override fun onInit(status: Int) {
        Log.d("Speaker", "Initialising speaker")
        if (status == TextToSpeech.SUCCESS) {
            Log.d("Speaker", "Speaker ready")
            tts.language = Locale.GERMANY
            tts.setPitch(0.5f)
            ready = true
            if (delayedWord != null)
                speak(delayedWord!!)
        } else {
            Log.d("Speaker", "Speaker not ready")
            ready = false
        }
    }

    fun speak(text: String, onFinish: () -> Unit = {}) {
        Log.d("Speaker", "Speaking: $text")
        if (ready && allowed) {
            Log.d("Speaker", "Speaker allowed and ready")
            val hash = HashMap<String, String>()
            hash.put(TextToSpeech.Engine.KEY_PARAM_STREAM,
                    AudioManager.STREAM_NOTIFICATION.toString())

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                tts.speak(text, TextToSpeech.QUEUE_ADD, null, null)
            } else {
                tts.speak(text, TextToSpeech.QUEUE_ADD, hash)
            }
            tts.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onDone(utteranceId: String?) {
                    onFinish()
                }

                override fun onError(utteranceId: String?) {
                }

                override fun onStart(utteranceId: String?) {
                }

            })
            delayedWord = null

        } else {
            Log.d("Speaker", "Speaker not ready. $text will be spoken later")
            delayedWord = text
        }
    }
}