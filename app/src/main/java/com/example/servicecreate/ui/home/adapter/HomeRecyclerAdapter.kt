package com.example.servicecreate.ui.home.adapter

import com.bumptech.glide.Glide
import com.example.base.ui.activity.BaseAdapter
import com.example.servicecreate.R
import com.example.servicecreate.databinding.ItemHomeRoomCardBinding
import com.example.servicecreate.ui.controller.ControllerActivity
import com.example.servicecreate.ui.home.HomeFragment

/**
 * @author:SunShibo
 * @date:2023-03-04 22:50
 * @feature:
 */
class HomeRecyclerAdapter(private val fragment: HomeFragment):
    BaseAdapter<Int, ItemHomeRoomCardBinding>() {

    override fun ItemHomeRoomCardBinding.onBindViewHolder(bean: Int, position: Int) {
        itemCardHome.setOnClickListener {
            ControllerActivity.startActivity(fragment.requireContext(), 2)
        }
        when (bean) {
            0 -> {
                Glide.with(fragment.requireContext()).load(R.drawable.ic_device_door_lock).into(itemCardRoomImage)
            }
            1 -> {
                Glide.with(fragment.requireContext()).load(R.drawable.ic_device_air).into(itemCardRoomImage)
            }
            else -> {
                Glide.with(fragment.requireContext()).load(R.drawable.ic_device_lamp).into(itemCardRoomImage)
            }
        }

    }
}