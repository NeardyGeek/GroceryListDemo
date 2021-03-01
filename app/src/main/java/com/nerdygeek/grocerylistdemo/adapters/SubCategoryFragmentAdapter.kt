package com.nerdygeek.grocerylistdemo.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.nerdygeek.grocerylistdemo.fragments.SubCategoryFragment
import com.nerdygeek.grocerylistdemo.models.SubCategory

class SubCategoryFragmentAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    var mFragments = ArrayList<Fragment>()
    var mTitles = ArrayList<String>()
    override fun getCount(): Int {
        return mFragments.size

    }

    override fun getItem(position: Int): Fragment {
        return mFragments[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mTitles[position]
    }

    fun addFragment(subCategory: SubCategory){
        mFragments.add(SubCategoryFragment.newInstance(subCategory.subId))
        mTitles.add(subCategory.subName)
    }
}