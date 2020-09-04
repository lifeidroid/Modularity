package com.carl.carlLib.activity

import android.content.Context
import android.content.Intent
import android.content.res.Resources

/**
 * BaseView
 * @author carl
 */

interface IBaseView {

    fun getAContext(): Context

    fun getResources(): Resources

    fun isShow(): Boolean

    fun finish()

    fun startActivity(intent: Intent)

    fun startActivityForResult(intent: Intent, requestCode: Int)

    fun sendBroadcast(intent: Intent)

    fun dismissDialog()

}
