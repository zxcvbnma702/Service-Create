package com.example.servicecreate.ui.append.adapter

import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.base.ui.activity.BaseAdapter
import com.example.base.util.GlideEngine
import com.example.servicecreate.R
import com.example.servicecreate.ServiceCreateApplication
import com.example.servicecreate.databinding.FragmentAppendBinding
import com.example.servicecreate.databinding.ItemAppendDeviceCardBinding
import com.example.servicecreate.logic.db.model.AppendItem
import com.example.servicecreate.ui.append.AppendFragment
import com.example.servicecreate.ui.auth.AuthActivity
import com.example.servicecreate.ui.dialogCancelInfo
import com.example.servicecreate.ui.dialogMessageInfo
import com.example.servicecreate.ui.dialogOkInfo
import com.example.servicecreate.ui.dialogTitleInfo
import com.kongzue.dialogx.dialogs.InputDialog
import com.kongzue.dialogx.dialogs.MessageDialog
import com.kongzue.dialogx.interfaces.OnInputDialogButtonClickListener

/**
 * @author:SunShibo
 * @date:2023-03-23 23:24
 * @feature:
 */
class AppendDefaultRecyclerAdapter(private val fragment: AppendFragment) : BaseAdapter<AppendItem, ItemAppendDeviceCardBinding>() {

    private val context = fragment.requireContext()

    override fun ItemAppendDeviceCardBinding.onBindViewHolder(bean: AppendItem, position: Int) {
        Glide.with(context).load(bean.imageId).into(itemCardDeviceImage)
        itemCardDeviceName.text = bean.name
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

                    .setOkButton { _, _, input ->
                        Toast.makeText(context, input, Toast.LENGTH_SHORT).show()
                        false
                    }
                    .setCancelButton{_, _ ->
                        false
                    }
            }
        }
    }
}