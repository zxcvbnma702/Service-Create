package com.example.servicecreate.ui.home.adapter

import com.bumptech.glide.Glide
import com.example.base.ui.activity.BaseAdapter
import com.example.servicecreate.R
import com.example.servicecreate.databinding.ItemExhibitRoomCardBinding
import com.example.servicecreate.databinding.ItemHomeRoomCardBinding
import com.example.servicecreate.logic.network.model.DeviceData
import com.example.servicecreate.ui.controller.ControllerActivity
import com.example.servicecreate.ui.home.exhibit.ExhibitFragment
import com.kongzue.dialogx.dialogs.MessageDialog

/**
 * @author:SunShibo
 * @date:2023-04-01 11:32
 * @feature: 展示设备界面adapter
 */
class ExhibitRecyclerAdapter(private val fragment: ExhibitFragment):
    BaseAdapter<DeviceData, ItemExhibitRoomCardBinding>() {

    private val context = fragment.requireContext()

    override fun ItemExhibitRoomCardBinding.onBindViewHolder(bean: DeviceData, position: Int) {
        itemCardRoomName.text = bean.name
        itemCardRoomDescription.text = "上次使用: ${bean.updateTime}"
        when(bean.type){
            1 -> Glide.with(context).load(R.drawable.ic_device_air).into(itemCardRoomImage)
            2 -> Glide.with(context).load(R.drawable.ic_device_lamp).into(itemCardRoomImage)
            3 -> Glide.with(context).load(R.drawable.ic_device_door_lock).into(itemCardRoomImage)
        }
        if(bean.roomList.isNotEmpty()){
            itemCardRoomNumber.text = bean.roomList.first().name
        }else{
            itemCardRoomNumber.text = "默认房间"
        }

        itemCardExhibit.setOnClickListener {
            if(bean.roomList.isNotEmpty()){
                ControllerActivity.startActivity(context, bean.type, bean.id, bean.name, bean.roomList.first().name)
            }else{
                ControllerActivity.startActivity(context, bean.type, bean.id, bean.name, "默认房间")
            }
        }

        itemCardExhibit.setOnLongClickListener {
            if(fragment.l< 100){
                MessageDialog.show(context.getString(R.string.device_delete_title), context.getString(R.string.home_delete_device_content), "确定", "取消")
                    .setMaskColor(context.getColor(com.kongzue.dialogx.R.color.black30))
                    .setCancelable(false)
                    .setOkButton { _, _ ->
                        fragment.mViewModel.deleteDevice(bean.id.toString())
                        false
                    }
                    .setCancelButton{_, _ ->
                        false
                    }
            }

            false
        }

    }
}