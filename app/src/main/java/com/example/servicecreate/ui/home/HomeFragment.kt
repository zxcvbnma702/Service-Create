package com.example.servicecreate.ui.home

import android.annotation.SuppressLint
import android.text.method.ScrollingMovementMethod
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.base.kxt.toast
import com.example.base.ui.activity.BaseFragment
import com.example.servicecreate.R
import com.example.servicecreate.ServiceCreateApplication
import com.example.servicecreate.databinding.FragmentHomeBinding
import com.example.servicecreate.ui.home.adapter.HomeRecyclerAdapter
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.*

class HomeFragment : BaseFragment<FragmentHomeBinding>(), HomeListener, SwipeRefreshLayout.OnRefreshListener {

    private lateinit var adapter : HomeRecyclerAdapter
    private val mViewModel: HomeViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(HomeViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun FragmentHomeBinding.initBindingView() {
        binding.viewModel = mViewModel
        mViewModel.homeListener = this@HomeFragment

        adapter = HomeRecyclerAdapter(this@HomeFragment)

        homeRecyclerview.adapter = adapter
        adapter.setData(arrayListOf(0, 1, 2, 3, 4, 6, 5, 2, 8, 9, 7))

        homeUsername.text = "Hello, ${ServiceCreateApplication.appSecret.split(".")}"


        homeTablayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })

        homeSwipeRefresh.apply {
            setColorSchemeColors(
                resources.getColor(R.color.main_color),
                resources.getColor(R.color.main_color_primary_variant),
                resources.getColor(R.color.main_color_primary_secondary)
            )
            setOnRefreshListener(this@HomeFragment)
        }
    }

    override fun onRefresh() {
//        TODO("Not yet implemented")
        ServiceCreateApplication.context.toast("refreshed")
        val job = Job()
        CoroutineScope(job).launch {
            withContext(Dispatchers.Main) {
                delay(3000)
                binding.homeSwipeRefresh.isRefreshing = false
            }
        }
    }

}