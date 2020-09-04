package com.carl.carlLib.manage

/**
 * 外设在线状态监听
 */
interface DeviceStatusChangedListener {
    /**
     * 外设在线状态改变
     * @param deviceTag String  设备标签
     * @param isOnLine Boolean  设备在线状态
     */
    fun onDeviceStatusChanged(deviceTag: String, isOnLine: Boolean)
}