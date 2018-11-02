package personal.wuqing.notificationreader.status

import android.content.Context
import android.graphics.Color
import android.widget.LinearLayout
import android.widget.TextView

fun addStatus(
        context: Context,
        statusLayout: LinearLayout,
        description: String,
        color: Int = Color.RED,
        duration: Long = 3000) {
    val textView = TextView(context)
    textView.text = description
    textView.setTextColor(color)
    statusLayout.addView(textView)
    Thread {
        run {
            Thread.sleep(duration)
            statusLayout.post { statusLayout.removeView(textView) }
        }
    }.start()
}
