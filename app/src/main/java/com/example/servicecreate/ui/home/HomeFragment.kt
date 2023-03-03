package com.example.servicecreate.ui.home

import com.example.base.ui.activity.BaseFragment
import com.example.servicecreate.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayout

class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    override fun FragmentHomeBinding.initBindingView() {

        homeTablayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
    }

}