package com.example.servicecreate.ui.home.adapter

import com.example.base.ui.activity.BaseAdapter
import com.example.servicecreate.databinding.ItemExhibitRoomCardBinding
import com.example.servicecreate.databinding.ItemHomeRoomCardBinding
import com.example.servicecreate.ui.home.exhibit.ExhibitFragment

/**
 * @author:SunShibo
 * @date:2023-04-01 11:32
 * @feature:
 */
class ExhibitRecyclerAdapter(private val fragment: ExhibitFragment):
    BaseAdapter<Int, ItemExhibitRoomCardBinding>() {
    override fun ItemExhibitRoomCardBinding.onBindViewHolder(bean: Int, position: Int) {

    }
}