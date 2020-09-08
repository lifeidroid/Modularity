package com.carl.moduledata.application

import android.app.Application
import android.content.Context
import android.os.Build
import android.os.Environment
import android.os.StrictMode
import androidx.multidex.MultiDex
import cn.carl.communicationLib.JCLib
import cn.carl.communicationLib.ble.BleUUID
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.Utils
import com.carl.carlLib.httpframe.HttpHelper
import com.carl.carlLib.httpframe.XUtilsProcessor
import com.carl.carlLib.manage.CommunicationManager
import com.carl.carlLib.utils.XUtils3ImageLoader
import com.carl.moduledata.R
import com.lzy.imagepicker.ImagePicker
import com.lzy.imagepicker.view.CropImageView
import com.scwang.smartrefresh.header.MaterialHeader
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.*
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.tencent.smtt.sdk.QbSdk
import com.uuzuche.lib_zxing.activity.ZXingLibrary
import es.dmoral.toasty.Toasty
import me.jessyan.autosize.AutoSize
import me.jessyan.autosize.AutoSizeConfig
import me.jessyan.autosize.unit.Subunits
import org.xutils.x
import java.io.File
import java.util.*

/**
 * Application
 */
class App : Application() {
    var appRun = false
    lateinit var imagePicker: ImagePicker

    var isDebug = true
    override fun onCreate() {
        super.onCreate()
        instance = this
        if (isDebug) {
            ARouter.openLog()
            ARouter.openDebug()
        }
        //初始化路由
        ARouter.init(this)
        //打印功能适配9.0方案
        appRun = true
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder.detectFileUriExposure()
        }
        initParams()
        initPlugin()
        MultiDex.install(this);
    }

    private fun initParams() {
    }


    companion object {
        @JvmStatic
        lateinit var instance: App
            private set
    }

    /**
     * 初始化插件
     */
    private fun initPlugin() {
        /***********************************初始化通信类********************************************/
        JCLib.init(this, true, true)
        //初始化通信管理类
        CommunicationManager.get().initObj(this)
//        CommunicationManager.().initObj(this)
        //打开TCPServer
//        JCLib.getInstance().startSocketServer(AppConfig.PORT_PAD)
        //添加低功耗蓝牙读写服务特征集合
        //写特征集合
        val writeUUIDs = ArrayList<BleUUID>()
        writeUUIDs.add(
            BleUUID(
                "0000ffe0-0000-1000-8000-00805f9b34fb",
                "0000ffe1-0000-1000-8000-00805f9b34fb"
            )
        )
        writeUUIDs.add(
            BleUUID(
                "49535343-fe7d-4ae5-8fa9-9fafd205e455",
                "49535343-8841-43f4-a8d4-ecbe34729bb3"
            )
        )
        //读特征集合
        val notifyUUIDs = ArrayList<BleUUID>()
        notifyUUIDs.add(
            BleUUID(
                "0000ffe0-0000-1000-8000-00805f9b34fb",
                "0000ffe1-0000-1000-8000-00805f9b34fb"
            )
        )
        notifyUUIDs.add(
            BleUUID(
                "49535343-fe7d-4ae5-8fa9-9fafd205e455",
                "49535343-1e4d-4bd9-ba61-23c647249616"
            )
        )
        //初始化低功耗蓝牙配置
        JCLib.getInstance().initBLE(writeUUIDs, notifyUUIDs)
        /***********************************初始化AutoSize***********}******************************/
        //屏幕适配
        AutoSize.initCompatMultiProcess(this)
        AutoSizeConfig.getInstance().unitsManager
            .setSupportDP(false)
            .setSupportSP(true)
            .supportSubunits = Subunits.MM

        /***********************************初始化xUtils********************************************/
        x.Ext.init(this)
        //初始化网络访问工具，并指定网络访问框架为xUtils
        HttpHelper.init(XUtilsProcessor(this))
        /***********************************图片加载************************************************/
//        Fresco.initialize(this)
        /***********************************图片选择器**********************************************/
        imagePicker = ImagePicker.getInstance()
        imagePicker.imageLoader =
            XUtils3ImageLoader(R.mipmap.ic_launcher, R.mipmap.ic_launcher)   //设置图片加载器
        imagePicker.isShowCamera = true  //显示拍照按钮
        imagePicker.isCrop = true        //允许裁剪（单选才有效）
        imagePicker.isSaveRectangle = true //是否按矩形区域保存
        imagePicker.isMultiMode = false
        imagePicker.selectLimit = 1    //选中数量限制
        imagePicker.style = CropImageView.Style.RECTANGLE  //裁剪框的形状
        imagePicker.focusWidth = 800   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.focusHeight = 800  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.outPutX = 1000//保存文件的宽度。单位像素
        imagePicker.outPutY = 1000//保存文件的高度。单位像素
        /***********************************工具类集合**********************************************/
        Utils.init(this)
        //配置日志输出
        LogUtils.getConfig().setBorderSwitch(true)
            .setLog2FileSwitch(false)       //设置 log 文件开关
            .setFilePrefix("log").dir =
            (Environment.getExternalStorageDirectory().toString() + File.separator
                    + AppUtils.getAppName())//设置 log 文件存储目录
        /***********************************下拉刷新|上拉加载样式配置*********************************/
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(object : DefaultRefreshHeaderCreator {
            override fun createRefreshHeader(
                context: Context,
                layout: RefreshLayout
            ): RefreshHeader {
                layout.setPrimaryColorsId(
                    android.R.color.transparent,
                    android.R.color.tertiary_text_dark
                )//全局设置主题颜色
                layout.setEnableHeaderTranslationContent(false)
                return MaterialHeader(context)
            }
        })
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(object : DefaultRefreshFooterCreator {
            override fun createRefreshFooter(
                context: Context,
                layout: RefreshLayout
            ): RefreshFooter {
                //指定为经典Footer，默认是 BallPulseFooter
                return ClassicsFooter(context).setDrawableSize(20f)
            }
        })
        /***********************************腾讯X5**************************************************/
        val cb = object : QbSdk.PreInitCallback {
            override fun onViewInitFinished(arg0: Boolean) {}
            override fun onCoreInitFinished() {
            }
        }
        QbSdk.initX5Environment(applicationContext, cb)
        /***********************************Toast**************************************************/
        Toasty.Config.getInstance()
            .setTextSize(22) // 定义字体大小
            .apply()
        /***********************************二维码扫描**********************************************/
        ZXingLibrary.initDisplayOpinion(this)

    }


    /**
     * 退出系统
     */
    fun exit() {
        appRun = false
    }
}