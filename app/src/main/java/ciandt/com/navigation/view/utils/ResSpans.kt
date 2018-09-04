package ciandt.com.navigation.view.utils

import android.content.Context
import android.support.annotation.ColorRes
import android.support.annotation.DimenRes
import android.support.annotation.DrawableRes
import android.support.annotation.StyleRes
import android.support.v4.content.ContextCompat
import android.support.v7.content.res.AppCompatResources
import android.text.TextPaint
import android.text.style.*
import android.view.View

/**
 * @author "<a href=\'mailto:pmoreira@ciandt.com\'>Pedro Felipe Marques Moreira (CIT)</a>"
 * @since 01/08/2018
 */
class ResSpans(val context: Context) : Iterable<Any> {
    val spans = ArrayList<Any>()

    override fun iterator() = spans.iterator()

    fun appearance(@StyleRes id: Int) =
            spans.add(TextAppearanceSpan(context, id))

    fun size(@DimenRes id: Int) =
            spans.add(AbsoluteSizeSpan(context.resources.getDimension(id).toInt()))

    fun color(@ColorRes id: Int) =
            spans.add(ForegroundColorSpan(ContextCompat.getColor(context, id)))

    fun icon(@DrawableRes id: Int, size: Int) =
            spans.add(ImageSpan(AppCompatResources.getDrawable(context, id)!!.apply {
                setBounds(0, 0, size, size)
            }))

    fun sansSerifMedium() = typeface("sans-serif-medium")
    fun sansSerifRegular() = typeface("sans-serif-regular")

    fun typeface(family: String) = spans.add(TypefaceSpan(family))
    fun typeface(style: Int) = spans.add(StyleSpan(style))

    fun click(action: () -> Unit) = spans.add(clickableSpan(action))

    fun custom(span: Any) = spans.add(span)
}

fun clickableSpan(action: () -> Unit) = object : ClickableSpan() {
    override fun onClick(view: View) = action()

    override fun updateDrawState(ds: TextPaint?) {
        super.updateDrawState(ds)
        ds?.isUnderlineText = false
    }
}