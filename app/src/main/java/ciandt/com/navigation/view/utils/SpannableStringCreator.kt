package ciandt.com.navigation.view.utils

import android.content.Context
import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
import android.text.TextUtils.concat
import ciandt.com.navigation.extension.resSpans

/**
 * @author "<a href=\'mailto:pmoreira@ciandt.com\'>Pedro Felipe Marques Moreira (CIT)</a>"
 * @since 01/08/2018
 */
class SpannableStringCreator {
    private val parts = ArrayList<CharSequence>()
    private var length = 0
    private val spanMap: MutableMap<IntRange, Iterable<Any>> = HashMap()

    fun appendSpace(newText: CharSequence) = append(" ").append(newText)

    fun appendSpace(newText: CharSequence, spans: Iterable<Any>) = append(" ").append(newText, spans)

    fun appendLnNotBlank(newText: CharSequence, spans: Iterable<Any>) = applyIf({ !newText.isBlank() }) { appendLn(newText, spans) }

    fun appendLn(newText: CharSequence, spans: Iterable<Any>) = append("\n").append(newText, spans)

    fun appendBold(stringId: Int, context: Context) = apply {
        appendBold(context.getString(stringId), context)
    }

    fun appendBold(newText: CharSequence, context: Context) = apply {
        val end = newText.length
        parts.add(newText)
        spanMap[(length..length + end)] = context.resSpans {typeface(Typeface.BOLD)}
        length += end
    }

    fun append(newText: CharSequence, spans: Iterable<Any>) = apply {
        val end = newText.length
        parts.add(newText)
        spanMap[(length..length + end)] = spans
        length += end
    }

    fun append(newText: CharSequence) = apply {
        parts.add(newText)
        length += newText.length
    }

    inline fun applyIf(predicate: () -> Boolean, action: SpannableStringCreator.() -> SpannableStringCreator) = if (predicate()) action() else this

    fun toSpannableString() = SpannableString(concat(*parts.toTypedArray())).apply {
        spanMap.forEach {
            val range = it.key
            it.value.forEach {
                setSpan(it, range.start, range.endInclusive, SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }
    }
}