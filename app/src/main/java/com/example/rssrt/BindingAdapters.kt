package com.example.rssrt

import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("html_text_with_links")
fun TextView.setHtmlTextWithLinks(html: String?) {
    setHtmlText(html, true)
}

@BindingAdapter("html_text")
fun TextView.setHtmlText(html: String?) {
    setHtmlText(html, false)
}

private fun TextView.setHtmlText(html: String?, withLinks: Boolean = true) {
    html?.let {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            // Since parsing <img> tags and loading images is out of scope of the task, we just
            // render null drawable instead of images using ImageGetter
            val emptyImageGetter =
                Html.ImageGetter { ContextCompat.getDrawable(context, R.drawable.empty) }

            this.text = Html.fromHtml(it, Html.FROM_HTML_MODE_LEGACY, emptyImageGetter, null)
        } else {
            this.text = Html.fromHtml(it)
        }

        if (withLinks) enableLinks()
    }
}

private fun TextView.enableLinks() {
    this.movementMethod = LinkMovementMethod.getInstance()
}

@BindingAdapter("datetime_text")
fun TextView.setDateText(date: Date?) {
    date?.let {
        text = SimpleDateFormat.getDateTimeInstance().format(date)
    }
}