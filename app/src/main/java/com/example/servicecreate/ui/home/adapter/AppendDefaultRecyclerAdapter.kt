package com.example.servicecreate.ui.home.adapter

import com.bumptech.glide.Glide
import com.example.base.ui.activity.BaseAdapter
import com.example.servicecreate.R
import com.example.servicecreate.databinding.ItemAppendDeviceCardBinding
import com.example.servicecreate.logic.db.model.AppendItem
import com.example.servicecreate.ui.dialogCancelInfo
import com.example.servicecreate.ui.dialogMessageInfo
import com.example.servicecreate.ui.dialogTitleInfo
import com.example.servicecreate.ui.home.append.AppendFragment
import com.example.servicecreate.ui.toast
import com.kongzue.dialogx.dialogs.InputDialog
import com.kongzue.dialogx.dialogs.MessageDialog
import com.kongzue.dialogx.dialogs.PopMenu


/**
 * @author:SunShibo
 * @date:2023-03-23 23:24
 * @feature:
 */
class AppendDefaultRecyclerAdapter(
    private val fragment: AppendFragment,
) : BaseAdapter<AppendItem, ItemAppendDeviceCardBinding>() {

    internal var roomList: MutableMap<String, Long> = mutableMapOf("空房间" to 0L)
    private val context = fragment.requireContext()

    override fun ItemAppendDeviceCardBinding.onBindViewHolder(bean: AppendItem, position: Int) {
        Glide.with(context).load(bean.imageId).into(itemCardDeviceImage)
        itemCardDeviceName.text = bean.name
        itemCardAppend.setOnClickListener {

        }

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
            }
            1 -> {
                itemCardAppend.setOnClickListener {
                    PopMenu.show(roomList.keys.toList())
                        .setRadius(15f)
                        .setOnMenuItemClickListener { dialog, text, index ->
                            addDevice(roomList[text], text, 1)
                            false
                        }.menuTextInfo = dialogTitleInfo(context)
                }
            }
            2 -> {
                itemCardAppend.setOnClickListener {
                    PopMenu.show(roomList.keys.toList())
                        .setRadius(15f)
                        .setOnMenuItemClickListener { dialog, text, index ->
                            addDevice(roomList[text], text, 2)
                            false
                        }.menuTextInfo = dialogTitleInfo(context)
                }
            }
            3 -> {
                itemCardAppend.setOnClickListener {
                    PopMenu.show(roomList.keys.toList())
                        .setRadius(15f)
                        .setOnMenuItemClickListener { dialog, text, index ->
                            addDevice(roomList[text], text, 3)
                            false
                        }.menuTextInfo = dialogTitleInfo(context)
                }
            }
        }
    }

    private fun addDevice(roomId: Long?, text: CharSequence, deviceType: Int) {
        InputDialog.show(
            context.getString(R.string.append_device_to) + " " + text,
            context.getString(R.string.device_name),
            "确定",
            "取消",
            " "
        )
            .setMaskColor(context.getColor(com.kongzue.dialogx.R.color.black30))
            .setCancelable(false)
            .setCancelTextInfo(dialogCancelInfo(context))
            .setOkButton { dialog, v, inputStr ->
                if (inputStr.isNullOrBlank() || inputStr.isNullOrEmpty()) {
                    "设备名不能为空".toast()
                } else {
                    if(roomId == 0L){
                        fragment.mViewModel.addDevice(inputStr, deviceType)
                        return@setOkButton false
                    }else{
                        when {
                            roomId != null -> {
                                // TODO: 添加设备和房间
//                                fragment.mViewModel.addDeviceToRoom(roomId, inputStr, deviceType)
                            }
                            else -> {
                                "未知错误".toast()
                            }
                        }
                    }

                }
                true
            }
            .setCancelButton { _, _ ->
                false
            }
    }
}