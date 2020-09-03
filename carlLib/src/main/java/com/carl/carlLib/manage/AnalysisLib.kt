package com.carl.carlLib.manage

/**
 * ==============================================
 *     author : carl
 *     e-mail : 991579741@qq.com
 *     time   : 2020/04/03
 *     desc   : 定义硬件通用解析格式
 *     version: 1.0
 * ==============================================
 */
open interface AnalysisLib {
    fun decode(address:String, data: String)
}