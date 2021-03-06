package br.com.gdgbrasilia.meetup.app.view.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.gdgbrasilia.meetup.R
import br.com.gdgbrasilia.meetup.app.data.AppConstants
import br.com.gdgbrasilia.meetup.app.util.extensions.loadImg
import kotlinx.android.synthetic.main.fragment_gallery.*

/**
 * Created by danilolemes on 06/03/2018.
 */
class GalleryFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.fragment_gallery, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.getString("path")?.let {
            galleryImage.loadImg(AppConstants.IMAGE_PATH + it)
        }
    }
}