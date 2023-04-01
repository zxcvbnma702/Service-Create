package com.example.servicecreate.ui.home.exhibit

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.base.kxt.addOnBackPressed
import com.example.base.ui.activity.BaseFragment
import com.example.servicecreate.R
import com.example.servicecreate.databinding.FragmentExhibitBinding
import com.example.servicecreate.ui.home.MainListener
import com.example.servicecreate.ui.home.adapter.ExhibitRecyclerAdapter
import com.example.servicecreate.ui.home.adapter.HomeRecyclerAdapter
import com.example.servicecreate.ui.home.home.HomeViewModel
import com.example.servicecreate.ui.toast

class ExhibitFragment(private val l: Long) : BaseFragment<FragmentExhibitBinding>() , MainListener{

    private lateinit var roomAdapter : ExhibitRecyclerAdapter
    private lateinit var deviceAdapter : ExhibitRecyclerAdapter
    internal val mViewModel: ExhibitViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[ExhibitViewModel::class.java]
    }

    override fun FragmentExhibitBinding.initBindingView() {
        binding.viewModel = mViewModel
        mViewModel.mainListener = this@ExhibitFragment

        initData()

        roomAdapter = ExhibitRecyclerAdapter(this@ExhibitFragment)
        deviceAdapter = ExhibitRecyclerAdapter(this@ExhibitFragment)
        exhibitRecyclerview.adapter = roomAdapter

    }

    private fun initData() {
        Log.e("id", l.toString())
    }

}
