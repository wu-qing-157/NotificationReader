package personal.wuqing.notificationreader.reader

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.*

object TextToSpeechReader {
    lateinit var tts: TextToSpeech
    var flush = true
    fun init(context: Context, fail: () -> Unit, success: () -> Unit) {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS && tts.setLanguage(Locale.getDefault()) == TextToSpeech.LANG_AVAILABLE)
                success()
            else
                fail()
        }
    }
    fun speak(s: String) {
        val ss = if (s.length > TextToSpeech.getMaxSpeechInputLength()) s.substring(0, TextToSpeech.getMaxSpeechInputLength()) else s
        tts.speak(ss, if (flush) TextToSpeech.QUEUE_FLUSH else TextToSpeech.QUEUE_ADD, null, System.currentTimeMillis().toString())
    }
}
