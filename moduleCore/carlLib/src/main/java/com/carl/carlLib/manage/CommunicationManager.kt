package com.carl.carlLib.manage

import android.content.Context
import cn.carl.communicationLib.JCLib
import cn.carl.communicationLib.callback.JCLibBleCallBack
import cn.carl.communicationLib.callback.JCLibCallBack
/**
 * ==============================================
 *     author : carl
 *     e-mail : 991579741@qq.com
 *     time   : 2020/04/03
 *     desc   : 外设通讯管理类
 *     version: 1.0
 * ==============================================
 */
class CommunicationManager private constructor() {
    private val notifyMap = HashMap<String, ArrayList<AnalysisLib>>()
    private var context: Context? = null

    init {
        notifyMap.clear()
        //TODO 添加设备默认解析类
    }

    /**
     * 添加通知
     * @param type String
     * @param notify AnalysisLib
     */
    fun addNotify(type: String, notify: AnalysisLib) {
        if (notifyMap.containsKey(type)) {
            notifyMap[type]!!.add(notify)
        } else {
            notifyMap[type] = ArrayList<AnalysisLib>()
            notifyMap[type]!!.add(notify)
        }
    }

    /**
     * 取消通知
     *
     * @param type
     * @param notify
     */
    fun removeNotify(type: String?, notify: AnalysisLib?) {
        if (null == notify) {
            return
        }
        if (notifyMap.containsKey(type)) {
            if (notifyMap[type]!!.contains(notify)) {
                notifyMap[type]!!.remove(notify)
            }
        }
    }

    companion object {
        private var instance: CommunicationManager? = null
            get() {
                if (field == null) {
                        field = CommunicationManager()
                }
                return field
            }
        fun get():CommunicationManager{
            return instance!!
        }
    }


    /**
     * 初始化通信
     */

    fun initObj(context: Context) {
        this.context = context
        JCLib.getInstance().addJCLibBleCallBack(object : JCLibBleCallBack {
            override fun onClientAdd(address: String?, deviceName: String?) {
            }

            override fun onConnectFail(address: String?) {
            }

            override fun onMessageRecived(
                isDeal: Boolean,
                address: String?,
                alias: String?,
                tag: String?,
                data: String?
            ) {
            }

            override fun onClientRemove(address: String?, alias: String?) {
            }

        })

        JCLib.getInstance().addJCLibCallBack(object : JCLibCallBack {
            override fun onClientAdd(address: String?) {
            }

            override fun onConnectFail(address: String?) {
            }

            override fun onMessageRecived(
                isDeal: Boolean,
                address: String?,
                alias: String?,
                tag: String?,
                data: String?
            ) {
            }

            override fun onClientRemove(address: String?, alias: String?) {
            }

        })
    }
}