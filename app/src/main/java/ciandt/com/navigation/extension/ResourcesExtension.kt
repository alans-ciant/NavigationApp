package ciandt.com.navigation.extension

import android.content.res.Resources
import android.support.annotation.StringRes
import ciandt.com.navigation.view.utils.SpannableAppendable
import ciandt.com.navigation.view.utils.SpannableStringCreator
import java.util.*

/**
 * @author "<a href=\'mailto:pmoreira@ciandt.com\'>Pedro Felipe Marques Moreira (CIT)</a>"
 * @since 01/08/2018
 */
fun Resources.getSpannable(@StringRes id: Int, vararg spanParts: Pair<Any, Iterable<Any>>): CharSequence {
    val resultCreator = SpannableStringCreator()
    Formatter(
            SpannableAppendable(resultCreator, *spanParts),
            configuration.locale)
            .format(getString(id), *spanParts.map { it.first }.toTypedArray())
    return resultCreator.toSpannableString()
}
fun Resources.getText(@StringRes id: Int, vararg formatArgs: Any?) =
        getSpannable(id, *formatArgs.filterNotNull().map { it to emptyList<Any>() }.toTypedArray())