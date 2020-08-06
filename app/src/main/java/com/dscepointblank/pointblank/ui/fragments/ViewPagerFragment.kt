package com.dscepointblank.pointblank.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dscepointblank.pointblank.R
import com.dscepointblank.pointblank.adapters.ViewPagerAdapter
import com.dscepointblank.pointblank.ui.fragments.onbording.CodingContests
import com.dscepointblank.pointblank.ui.fragments.onbording.Forum
import com.dscepointblank.pointblank.ui.fragments.onbording.WriteUpFragment
import com.dscepointblank.pointblank.utilityClasses.DepthPageTransformer
import kotlinx.android.synthetic.main.fragment_view_pager.view.*

class ViewPagerFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_view_pager, container, false)
        val fragList = arrayListOf<Fragment>(
            Forum(),
            WriteUpFragment(),
            CodingContests()
        )

        val adapter =
            ViewPagerAdapter(fragList, requireActivity().supportFragmentManager, lifecycle)

        val viewPager2 = view.viewPager
        viewPager2.adapter = adapter
        viewPager2.setPageTransformer(DepthPageTransformer())

        return view
    }


}
