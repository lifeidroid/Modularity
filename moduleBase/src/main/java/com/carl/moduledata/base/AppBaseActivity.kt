package com.carl.moduledata.base

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.carl.carlLib.activity.BaseActivity
import com.carl.moduledata.widget.DialogMaker

/**
 * @author carl
 */
abstract class AppBaseActivity : BaseActivity() {
    var dialog: Dialog? = null
    var isFirst = true

    override fun onCreate(savedInstanceState: Bundle?) {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.onCreate(savedInstanceState)
        initNavigationBar()
    }


    /**
     * 初始化状态栏
     */
    private fun initNavigationBar() {
//        ImmersionBar.with(this)
//            .statusBarDarkFont(true)
//            .init()
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
        msg: String,
        btn1Text: String,
        btn2Text: String,
        isCanCancelabel: Boolean,
        callBack: DialogMaker.DialogCallBack?,
        isDismissAfterClickBtn: Boolean,
        tag: Any?
    ): Dialog {
        if (null == dialog || !dialog!!.isShowing) {
            dialog = DialogMaker.showConfirmDialog(
                this,
                msg,
                btn1Text,
                btn2Text,
                isCanCancelabel,
                callBack,
                isDismissAfterClickBtn,
                tag
            )
        }
        return dialog!!
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
        msg: String,
        btn1Text: String,
        isCanCancelabel: Boolean,
        callBack: DialogMaker.DialogCallBack?,
        isDismissAfterClickBtn: Boolean,
        tag: Any?
    ): Dialog {
        if (null == dialog || !dialog!!.isShowing) {
            dialog = DialogMaker.showAlertDialog(
                this,
                msg,
                btn1Text,
                isCanCancelabel,
                callBack,
                isDismissAfterClickBtn,
                tag
            )
        }
        return dialog!!
    }


    /**
     * 等待对话框
     * @param msg String    等待框提示内容
     * @param isCanCancelabel Boolean   点击外部对话框是否隐藏
     * @return Dialog
     */
    fun showWaitDialog(
        msg: String,
        isCanCancelabel: Boolean,
        callBack: DialogMaker.DialogCallBack?,
        tag: Any?
    ): Dialog {
        if (null == dialog || !dialog!!.isShowing) {
            dialog = DialogMaker.showWaitDialog(this, msg, isCanCancelabel, callBack, tag)
        }
        return dialog!!
    }

    /**
     * 关闭对话框
     *
     * @author carl
     */
    fun dismissDialog() {
        if (null != dialog && dialog!!.isShowing) {
            dialog!!.dismiss()
        }
    }

    /**
     * 返回按钮点击事件
     */
    fun backClick(v: View) {
        onBackPressed()
    }

    override fun onDestroy() {
        dismissDialog()
        super.onDestroy()
    }

    /**
     * 可以在这里统一制定跳转动画
     */
    fun startAty(i: Intent) {
        startActivity(i)
    }

    fun startAtyForResult(i: Intent, code: Int) {
        startActivityForResult(i, code)
    }

}