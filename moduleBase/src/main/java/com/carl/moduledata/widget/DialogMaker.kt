package com.carl.moduledata.widget

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.blankj.utilcode.util.StringUtils
import com.carl.carlLib.widget.NoDoubleClickListener
import com.carl.moduledata.R
import org.json.JSONException

/**
 * 对app的所有对话框进行管理
 *
 * @author carl
 */
object DialogMaker {
    private val res = arrayOfNulls<String>(4)

    interface DialogCallBack {
        /**
         * 对话框按钮点击回调
         *
         * @param position 点击Button的索引.
         * @param tag
         */
        @Throws(JSONException::class)
        fun onButtonClicked(dialog: Dialog, position: Int, tag: Any?)

        /**
         * 当对话框消失的时候回调
         *
         * @param tag
         */
        fun onCancelDialog(dialog: Dialog, tag: Any?)
    }

    /**
     * 展示确认对话框
     * @param msg String    内容
     * @param btn1Text String   第一个按钮的文本
     * @param btn2Text String   第二个按钮的文本
     * @param isCanCancelabel Boolean   点击外部是否可以消失
     * @param callBack DialogCallBack?  对话框点击事件返回
     * @param isDismissAfterClickBtn Boolean    点击按钮后对话框是否消失
     * @return Dialog
     */
    fun showConfirmDialog(
        context: Context,
        msg: String,
        btn1Text: String,
        btn2Text: String,
        isCanCancelabel: Boolean,
        callBack: DialogCallBack?,
        isDismissAfterClickBtn: Boolean,
        tag: Any?
    ): Dialog {
        var dialog = Dialog(context, R.style.DialogNoTitleStyleTranslucentBg)
        dialog.setContentView(R.layout.dlg_comform)
        dialog.findViewById<TextView>(R.id.tvContent).text = msg
        dialog.findViewById<TextView>(R.id.tvSure).text = btn1Text
        dialog.findViewById<TextView>(R.id.tvCancel).text = btn2Text
        dialog.findViewById<TextView>(R.id.tvSure)
            .setOnClickListener(object : NoDoubleClickListener() {
                override fun onNoDoubleClick(v: View?) {
                    callBack?.onButtonClicked(dialog, 0, tag)
                    if (isDismissAfterClickBtn) {
                        dialog.dismiss()
                    }
                }
            })
        dialog.findViewById<TextView>(R.id.tvCancel)
            .setOnClickListener(object : NoDoubleClickListener() {
                override fun onNoDoubleClick(v: View?) {
                    callBack?.onButtonClicked(dialog, 1, tag)
                    if (isDismissAfterClickBtn) {
                        dialog.dismiss()
                    }
                }
            })
        dialog.setCanceledOnTouchOutside(isCanCancelabel)
        dialog.show()
        return dialog
    }

    /**
     * 展示单个按钮警告对话框
     * @param msg String    警告内容
     * @param btn1Text String   按钮文本
     * @param isCanCancelabel Boolean   点击外部是否隐藏对话框
     * @param callBack DialogCallBack?  点击事件
     * @param isDismissAfterClickBtn Boolean    点击按钮后是否隐藏
     * @return Dialog
     */
    fun showAlertDialog(
        context: Context,
        msg: String,
        btn1Text: String,
        isCanCancelabel: Boolean,
        callBack: DialogCallBack?,
        isDismissAfterClickBtn: Boolean,
        tag: Any?
    ): Dialog {
        var dialog = Dialog(context, R.style.DialogNoTitleStyleTranslucentBg)
        dialog.setContentView(R.layout.dlg_comform)
        dialog.findViewById<TextView>(R.id.tvContent).text = msg
        dialog.findViewById<TextView>(R.id.tvSure).text = btn1Text
        dialog.findViewById<TextView>(R.id.tvCancel).visibility = View.GONE
        dialog.findViewById<TextView>(R.id.tvSure)
            .setOnClickListener(object : NoDoubleClickListener() {
                override fun onNoDoubleClick(v: View?) {
                    callBack?.onButtonClicked(dialog, 0, tag)
                    if (isDismissAfterClickBtn) {
                        dialog.dismiss()
                    }
                }
            })
        dialog.setCanceledOnTouchOutside(isCanCancelabel)
        dialog.show()
        return dialog
    }


    /**
     * 等待对话框
     * @param msg String    等待框提示内容
     * @param isCanCancelabel Boolean   点击外部对话框是否隐藏
     * @return Dialog
     */
    fun showWaitDialog(
        context: Context,
        msg: String,
        isCanCancelabel: Boolean,
        callBack: DialogCallBack?,
        tag: Any?
    ): Dialog {
        val dialog = Dialog(context, R.style.DialogNoTitleRoundCornerStyle)
        dialog.ownerActivity?.let {
            it as Activity
        }
        val contentView =
            LayoutInflater.from(context).inflate(R.layout.dlg_wait_common_layout, null)
        val contentTv = contentView.findViewById(R.id.dialog_content_tv) as TextView
        if (!StringUtils.isEmpty(msg)) {
            contentTv.text = msg
        }
        dialog.setOnCancelListener { dialog ->
            callBack?.onCancelDialog(dialog as Dialog, tag)
        }
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(isCanCancelabel)
        dialog.setContentView(contentView)
        dialog.show()
        return dialog
    }
}