package com.example.servicecreate.ui.home.adapter

import android.view.View
import com.bumptech.glide.Glide
import com.example.base.activity.BaseAdapter
import com.example.servicecreate.R
import com.example.servicecreate.databinding.ItemExhibitRoomCardBinding
import com.example.servicecreate.logic.network.model.DeviceData
import com.example.servicecreate.ui.controller.ControllerActivity
import com.example.servicecreate.ui.home.exhibit.ExhibitFragment
import com.example.servicecreate.ui.toast
import com.kongzue.dialogx.dialogs.MessageDialog
import kotlin.math.absoluteValue

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
        if(bean.roomList.isNullOrEmpty()){
            itemCardRoomNumber.text = "设备id: " + bean.id.toString()  + "    设备状态:"
        }else{
            itemCardRoomNumber.text = "设备id: " + bean.id.toString()  + "    设备状态:" + "\n" + "所属房间: ${bean.roomList.first().name}"
        }

        itemCardRoomSwitch.isChecked = bean.state
        when(bean.type){
            1 -> Glide.with(context).load(R.drawable.ic_device_air).into(itemCardRoomImage)
            2 -> Glide.with(context).load(R.drawable.ic_device_lamp).into(itemCardRoomImage)
            3 -> Glide.with(context).load(R.drawable.ic_device_door_lock).into(itemCardRoomImage)
            4 -> Glide.with(context).load(R.drawable.ic_device_led).into(itemCardRoomImage)
            5 -> {
                itemCardRoomSwitch.visibility = View.GONE
                Glide.with(context).load(R.drawable.ic_device_monitor).into(itemCardRoomImage)
            }
            6 -> {
                Glide.with(context).load(R.drawable.ic_device_curtain).into(itemCardRoomImage)
            }
            255 -> Glide.with(context).load(R.drawable.ic_device_router).into(itemCardRoomImage)
        }
        itemCardExhibit.setOnClickListener {
            if(bean.type == 255){
                "不允许的操作".toast()
            }else{
                if(bean.roomList.isNullOrEmpty()){
                    ControllerActivity.startActivity(context, bean.type, bean.id, bean.name, "")
                }else{
                    ControllerActivity.startActivity(context, bean.type, bean.id, bean.name, bean.roomList.first().name)
                }
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
            }else{
                fragment.roomDeviceSelect(bean.id)
            }

            false
        }

        itemCardRoomSwitch.setOnCheckedChangeListener { _, b ->
            if(b){
                when(bean.type){
                    1 -> fragment.mViewModel.airState(bean.id.toInt().absoluteValue, 1)
                    2 -> fragment.mViewModel.lampState(bean.id.toInt().absoluteValue, 1)
                    3 -> fragment.mViewModel.doorLockState(bean.id.toInt().absoluteValue, 1)
                    4 -> fragment.mViewModel.ledState(bean.id.toInt().absoluteValue, 1)
                }
            }else{
                when(bean.type){
                    1 -> fragment.mViewModel.airState(bean.id.toInt().absoluteValue, 0)
                    2 -> fragment.mViewModel.lampState(bean.id.toInt().absoluteValue, 0)
                    3 -> fragment.mViewModel.doorLockState(bean.id.toInt().absoluteValue, 0)
                    4 -> fragment.mViewModel.ledState(bean.id.toInt().absoluteValue, 0)
                }
            }
        }
    }
}