package com.example.servicecreate.ui.append.adapter

import com.bumptech.glide.Glide
import com.example.base.ui.activity.BaseAdapter
import com.example.base.util.GlideEngine
import com.example.servicecreate.databinding.FragmentAppendBinding
import com.example.servicecreate.databinding.ItemAppendDeviceCardBinding
import com.example.servicecreate.logic.db.model.AppendItem
import com.example.servicecreate.ui.append.AppendFragment

/**
 * @author:SunShibo
 * @date:2023-03-23 23:24
 * @feature:
 */
class AppendDefaultRecyclerAdapter(private val fragment: AppendFragment) : BaseAdapter<AppendItem, ItemAppendDeviceCardBinding>() {

    override fun ItemAppendDeviceCardBinding.onBindViewHolder(bean: AppendItem, position: Int) {
        Glide.with(fragment.requireContext()).load(bean.imageId).into(itemCardDeviceImage)
        itemCardDeviceName.text = bean.name
    }
}