package br.com.firstsoft.opentheater.adapter

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

/**
 * Created by danilolemes on 01/03/2018.
 */
class ViewPagerAdapter(fragmentManager: FragmentManager, private val context: Context) : FragmentStatePagerAdapter(fragmentManager) {

    private val mFragmentList = mutableListOf<Fragment>()
    private val mFragmentTitleList = mutableListOf<String>()

    override fun getItem(position: Int): Fragment = mFragmentList[position]

    override fun getCount(): Int  = mFragmentList.size

    override fun getPageTitle(position: Int): CharSequence = mFragmentTitleList[position]

    fun addFragment(fragment: Fragment, title: String) {
        mFragmentList.add(fragment)
        mFragmentTitleList.add(title)
        this.notifyDataSetChanged()
    }


}