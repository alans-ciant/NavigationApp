package ciandt.com.navigation.extension

import android.content.Context
import ciandt.com.navigation.view.utils.ResSpans

/**
 * @author "<a href=\'mailto:pmoreira@ciandt.com\'>Pedro Felipe Marques Moreira (CIT)</a>"
 * @since 01/08/2018
 */
inline fun Context.resSpans(options: ResSpans.() -> Unit) =
        ResSpans(this).apply(options)