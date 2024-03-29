package com.example.servicecreate.ui.home.adapter

import android.view.View
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import com.bumptech.glide.Glide
import com.example.base.kxt.toast
import com.example.base.activity.BaseAdapter
import com.example.servicecreate.R
import com.example.servicecreate.databinding.ItemAppendDeviceCardBinding
import com.example.servicecreate.logic.network.model.DeviceData
import com.example.servicecreate.ui.dialogCancelInfo
import com.example.servicecreate.ui.dialogTitleInfo
import com.example.servicecreate.ui.home.append.AppendFragment
import com.example.servicecreate.ui.toast
import com.kongzue.dialogx.dialogs.InputDialog
import com.kongzue.dialogx.dialogs.MessageDialog
import com.kongzue.dialogx.dialogs.PopMenu
import com.kongzue.dialogx.interfaces.OnBindView


/**
 * @author:SunShibo
 * @date:2023-03-23 23:24
 * @feature:
 */
class AppendDefaultRecyclerAdapter(
    private val fragment: AppendFragment,
) : BaseAdapter<DeviceData, ItemAppendDeviceCardBinding>() {

    internal var roomList: MutableMap<String, Long> = mutableMapOf("全屋" to 0L)
    private val context = fragment.requireContext()

    override fun ItemAppendDeviceCardBinding.onBindViewHolder(bean: DeviceData, position: Int) {
        itemCardDeviceName.text = bean.name

        when (bean.type) {
            0 -> {
                itemCardDeviceName.setTextColor(
                    fragment.requireContext().getColor(R.color.second_color)
                )
                itemCardAppend.setOnClickListener {
                    InputDialog.show(
                        context.getString(R.string.append_room),
                        context.getString(R.string.append_room_tip),
                        "确定",
                        "取消",
                        " "
                    )
                        .setMaskColor(context.getColor(com.kongzue.dialogx.R.color.black30))
                        .setCancelable(false)
                        .setCancelTextInfo(dialogCancelInfo(context))
                        .setOkButton { dialog, v, inputStr ->
                            if (inputStr.isNullOrBlank() || inputStr.isNullOrEmpty()) {
                                "房间名不能为空".toast()
                            } else {
                                fragment.mViewModel.addRoom(inputStr)
                            }
                            false
                        }
                        .setCancelButton { _, _ ->
                            false
                        }
                }
                Glide.with(context).load(R.drawable.ic_room_10).into(itemCardDeviceImage)
            }
            1 -> {
                itemCardAppend.setOnClickListener {
                    PopMenu.show(roomList.keys.toList())
                        .setRadius(15f)
                        .setOnMenuItemClickListener { dialog, text, index ->
                            fragment.addDevice(bean.id.toString(), roomList[text], text, 1)
                            false
                        }.menuTextInfo = dialogTitleInfo(context)
                }
                Glide.with(context).load(R.drawable.ic_device_air).into(itemCardDeviceImage)
            }
            2 -> {
                itemCardAppend.setOnClickListener {
                    PopMenu.show(roomList.keys.toList())
                        .setRadius(15f)
                        .setOnMenuItemClickListener { dialog, text, index ->
                            fragment.addDevice(bean.id.toString(), roomList[text], text, 2)
                            false
                        }.menuTextInfo = dialogTitleInfo(context)
                }
                Glide.with(context).load(R.drawable.ic_device_lamp).into(itemCardDeviceImage)
            }
            3 -> {
                itemCardAppend.setOnClickListener {
                    PopMenu.show(roomList.keys.toList())
                        .setRadius(15f)
                        .setOnMenuItemClickListener { dialog, text, index ->
                            fragment.addDevice(bean.id.toString(), roomList[text], text, 3)
                            false
                        }.menuTextInfo = dialogTitleInfo(context)
                }
                Glide.with(context).load(R.drawable.ic_device_door_lock).into(itemCardDeviceImage)
            }
            4 -> {
                itemCardAppend.setOnClickListener {
                    PopMenu.show(roomList.keys.toList())
                        .setRadius(15f)
                        .setOnMenuItemClickListener { dialog, text, index ->
                            fragment.addDevice(bean.id.toString(), roomList[text], text, 4)
                            false
                        }.menuTextInfo = dialogTitleInfo(context)
                }
                Glide.with(context).load(R.drawable.ic_device_led).into(itemCardDeviceImage)
            }
            5 -> {
                itemCardAppend.setOnClickListener {
                    PopMenu.show(roomList.keys.toList())
                        .setRadius(15f)
                        .setOnMenuItemClickListener { dialog, text, index ->
                            fragment.addDevice(bean.id.toString(), roomList[text], text, 5)
                            false
                        }.menuTextInfo = dialogTitleInfo(context)
                }
                Glide.with(context).load(R.drawable.ic_device_monitor).into(itemCardDeviceImage)
            }
            6 -> {
                Glide.with(context).load(R.drawable.ic_device_curtain).into(itemCardDeviceImage)
            }
            255->{
                Glide.with(context).load(R.drawable.ic_device_router).into(itemCardDeviceImage)
                itemCardAppend.setOnClickListener {
                    fragment.showMACDialog()
                }
            }
            88 -> Glide.with(context).load(R.drawable.ic_device_pot).into(itemCardDeviceImage)
            89 -> Glide.with(context).load(R.drawable.ic_device_wall).into(itemCardDeviceImage)
            90 -> Glide.with(context).load(R.drawable.ic_device_microwave).into(itemCardDeviceImage)
            91 -> Glide.with(context).load(R.drawable.ic_device_socket).into(itemCardDeviceImage)
        }

        if(bean.isDeleted == 1){
            itemCardAppend.setOnClickListener {

            }
        }
    }
}