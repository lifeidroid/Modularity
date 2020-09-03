package com.carl.carlLib.manage

import com.blankj.utilcode.util.StringUtils
import java.util.*

/**
 * ==============================================
 * author : carl
 * e-mail : 991579741@qq.com
 * time   : 2019/01/02
 * desc   : 硬件外设管理类
 * version: 1.0
 * ==============================================
 */
class DeviceManager private constructor() {
    private val deviceState = HashMap<String, Boolean>()
    private val listeners: MutableList<DeviceStatusChangedListener> = ArrayList()
    fun addDeviceChangedListener(listener: DeviceStatusChangedListener) {
        listeners.add(listener)
    }

    fun removeDeviceChangedListener(listener: DeviceStatusChangedListener) {
        if (listeners.contains(listener)) {
            listeners.remove(listener)
        }
    }

    /**
     * 获取设备在线状态
     *
     * @param deviceId
     * @return
     */
    fun getOnlineState(deviceId: String): Boolean {
        if (StringUtils.isEmpty(deviceId)) {
            return false
        }
        return if (!deviceState.containsKey(deviceId)) {
            false
        } else deviceState[deviceId]!!
    }

    /**
     * 设置设备在线状态
     *
     * @param deviceId
     * @param isOnline
     */
    fun setOnLineState(deviceId: String, isOnline: Boolean) {
        if (StringUtils.isEmpty(deviceId)) {
            return
        }
        deviceState[deviceId] = isOnline
        for (listener in listeners) {
            listener?.onDeviceStatusChanged(deviceId, isOnline)
        }
    }

    companion object {
        private var instance: DeviceManager? = null
            get() {
                if (field == null) {
                    field = DeviceManager()
                }
                return field
            }
        fun get():DeviceManager{
            return instance!!
        }
    }
}