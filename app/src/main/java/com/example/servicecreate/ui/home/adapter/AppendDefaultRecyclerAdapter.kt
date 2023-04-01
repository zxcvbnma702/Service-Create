package com.example.servicecreate.ui.home.adapter

import com.bumptech.glide.Glide
import com.example.base.kxt.toast
import com.example.base.ui.activity.BaseAdapter
import com.example.servicecreate.R
import com.example.servicecreate.databinding.ItemAppendDeviceCardBinding
import com.example.servicecreate.logic.db.model.AppendItem
import com.example.servicecreate.ui.home.append.AppendFragment
import com.example.servicecreate.ui.dialogCancelInfo
import com.example.servicecreate.ui.toast
import com.kongzue.dialogx.dialogs.InputDialog

/**
 * @author:SunShibo
 * @date:2023-03-23 23:24
 * @feature:
 */
class AppendDefaultRecyclerAdapter(
    private val fragment: AppendFragment,
) : BaseAdapter<AppendItem, ItemAppendDeviceCardBinding>() {

    private val context = fragment.requireContext()

    override fun ItemAppendDeviceCardBinding.onBindViewHolder(bean: AppendItem, position: Int) {
        Glide.with(context).load(bean.imageId).into(itemCardDeviceImage)
        itemCardDeviceName.text = bean.name
        itemCardAppend.setOnClickListener {

        }

        if(bean.type == 0){
            itemCardDeviceName.setTextColor(fragment.requireContext().getColor(R.color.second_color))
            itemCardAppend.setOnClickListener {
                InputDialog.show(context.getString(R.string.append_room),
                    context.getString(R.string.append_room_tip),
                    "确定",
                    "取消",
                    " ")
                    .setMaskColor(context.getColor(com.kongzue.dialogx.R.color.black30))
                    .setCancelable(false)
                    .setCancelTextInfo(dialogCancelInfo(context))
                    .setOkButton { dialog, v, inputStr ->
                        if (inputStr.isNullOrBlank() || inputStr.isNullOrEmpty()) {
                            "房间名不能为空".toast()
                        }
                        else{
                            fragment.mViewModel.addRoom(inputStr)

                        }
                        false
                    }
                    .setCancelButton{_, _ ->
                        false
                    }
            }
        }
    }
}