package com.carl.testb

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.carl.carlLib.activity.BaseActivity
import com.carl.moduledata.const.PageURL
import com.carl.moduledata.model.LoginModel
import kotlinx.android.synthetic.main.aty_testb.*


@Route(path = PageURL.ACTIVITY_URL_TESTB, extras = 5)
class TestBActivity : BaseActivity() {

    @Autowired
    @JvmField
    var name: String = ""

    @Autowired
    @JvmField
    var age = 0

    @Autowired
    @JvmField
    var student:LoginModel? = null

    override fun getLayoutId(): Int {
        return R.layout.aty_testb
    }

    override fun initVariables() {
    }

    override fun initViews(savedInstanceState: Bundle?) {
        tvContent.text = "页面Bname:$name   age:$age 学生：${student?.name}  ${student?.age}"
    }

    override fun loadData() {
    }

}
