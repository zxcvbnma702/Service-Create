package com.example.servicecreate.ui.home.adapter

import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.example.base.ui.activity.BaseAdapter
import com.example.servicecreate.R
import com.example.servicecreate.databinding.ItemHomeRoomCardBinding
import com.example.servicecreate.logic.network.model.RoomData
import com.example.servicecreate.ui.home.home.HomeFragment
import com.kongzue.dialogx.dialogs.MessageDialog

/**
 * @author:SunShibo
 * @date:2023-03-04 22:50
 * @feature:
 */
class HomeRecyclerAdapter(private val fragment: HomeFragment):
    BaseAdapter<RoomData, ItemHomeRoomCardBinding>() {

    private val context = fragment.requireContext()

    override fun ItemHomeRoomCardBinding.onBindViewHolder(bean: RoomData, position: Int) {

        if(bean.id < 100){
            itemCardHome.setOnClickListener {
                fragment.mViewModel.jumpToExhibitPage(bean.id, bean.name)
            }
            when(bean.id){
                1L -> Glide.with(context).load(R.drawable.ic_device_air).into(itemCardRoomImage)
                2L -> Glide.with(context).load(R.drawable.ic_device_lamp).into(itemCardRoomImage)
                3L -> Glide.with(context).load(R.drawable.ic_device_door_lock).into(itemCardRoomImage)
            }
            itemCardRoomDescription.visibility = View.GONE

        }else{
            itemCardHome.setOnClickListener {
                fragment.mViewModel.jumpToExhibitPage(bean.id, bean.name)
            }
            itemCardHome.setOnLongClickListener {
                MessageDialog.show(context.getString(R.string.home_delete_title), context.getString(R.string.home_delete_room_content), "确定", "取消")
                    .setMaskColor(context.getColor(com.kongzue.dialogx.R.color.black30))
                    .setCancelable(false)
                    .setOkButton { _, _ ->
                        Log.e("id", bean.id.toString())
                        fragment.mViewModel.deleteRoom(bean.id.toString())
                        false
                    }
                    .setCancelButton{_, _ ->
                        false
                    }
                false
            }
            /**
             * Load random image
             */
            when (bean.id.shr(22) % 10 ) {
                0L -> {
                    Glide.with(fragment.requireContext()).load(R.drawable.ic_room_0).into(itemCardRoomImage)
                }
                1L -> {
                    Glide.with(fragment.requireContext()).load(R.drawable.ic_room_1).into(itemCardRoomImage)
                }
                2L -> {
                    Glide.with(fragment.requireContext()).load(R.drawable.ic_room_2).into(itemCardRoomImage)
                }
                3L -> {
                    Glide.with(fragment.requireContext()).load(R.drawable.ic_room_3).into(itemCardRoomImage)
                }
                4L -> {
                    Glide.with(fragment.requireContext()).load(R.drawable.ic_room_4).into(itemCardRoomImage)
                }
                5L -> {
                    Glide.with(fragment.requireContext()).load(R.drawable.ic_room_5).into(itemCardRoomImage)
                }
                6L -> {
                    Glide.with(fragment.requireContext()).load(R.drawable.ic_room_6).into(itemCardRoomImage)
                }
                7L -> {
                    Glide.with(fragment.requireContext()).load(R.drawable.ic_room_7).into(itemCardRoomImage)
                }
                8L -> {
                    Glide.with(fragment.requireContext()).load(R.drawable.ic_room_8).into(itemCardRoomImage)
                }
                9L -> {
                    Glide.with(fragment.requireContext()).load(R.drawable.ic_room_9).into(itemCardRoomImage)
                }
            }
        }

        itemCardRoomName.text = bean.name
    }
}