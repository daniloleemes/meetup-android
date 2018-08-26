package br.com.gdgbrasilia.meetup.app.view.fragments.filter

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.gdgbrasilia.meetup.app.R
import br.com.gdgbrasilia.meetup.app.view.adapters.ViewPagerAdapter
import kotlinx.android.synthetic.main.fragment_filter_menu.*

class FilterMenuFragment : Fragment() {

    interface FilterListener {

        fun onEventoFiltered(filter: FilterMenuEventoFragment.EventoFilter)
        fun onEstabelecimentoFiltered(filter: FilterMenuEstabelecimentoFragment.EstabelecimentoFilter)
        fun onFilterCleared()

    }

    private val viewPagerAdapter by lazy { ViewPagerAdapter(childFragmentManager) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_filter_menu, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPagerAdapter.addFragment(FilterMenuEstabelecimentoFragment(), "Estabelecimento")
        viewPagerAdapter.addFragment(FilterMenuEventoFragment(), "Eventos")
        filterViewPager.adapter = viewPagerAdapter
        filterViewPager.offscreenPageLimit = 2
        filterTabLayout.setupWithViewPager(filterViewPager)
//        filterViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
//            override fun onPageScrollStateChanged(state: Int) {
//
//            }
//
//            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
//
//            }
//
//            override fun onPageSelected(position: Int) {
//                filterViewPager.reMeasureCurrentPage(filterViewPager.currentItem)
//            }
//
//        })

    }

}