package br.com.firstsoft.opentheater.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.firstsoft.opentheater.R
import br.com.firstsoft.opentheater.application.AppApplication
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_gallery.*

/**
 * Created by danilolemes on 06/03/2018.
 */
class GalleryFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater!!.inflate(R.layout.fragment_gallery, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments.getString("path")?.let {
            Glide.with(galleryImage).load(AppApplication.IMAGE_PATH + it).into(galleryImage)
        }

    }


}