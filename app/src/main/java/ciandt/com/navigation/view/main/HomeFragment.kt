package ciandt.com.navigation.view.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ciandt.com.navigation.R

/**
 * @author "<a href=\'mailto:pmoreira@ciandt.com\'>Pedro Felipe Marques Moreira (CIT)</a>"
 * @since 01/09/2018
 */
class HomeFragment : Fragment() {

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

}
