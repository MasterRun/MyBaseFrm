package com.jsongo.mybasefrm.ui.demo

import androidx.lifecycle.MutableLiveData
import com.jsongo.ajs.webloader.AJsWebPage
import com.jsongo.core.arch.mvvm.stateful.StatefulViewModel
import com.jsongo.core.bean.toErrorDataWrapper
import com.jsongo.mybasefrm.data.repository.HttpRequestManager
import com.jsongo.mybasefrm.databinding.ActivityDemoBinding
import kotlinx.coroutines.launch

/**
 * author ： jsongo
 * createtime ： 2019/7/23 13:52
 * desc :
 */
class DemoViewModel : StatefulViewModel() {

    val txtContent = MutableLiveData<String>()

    val testDbCount = MutableLiveData<Int>()

    val changeFragmentTimes = MutableLiveData<Int>()

    fun getAuthtypes() {
        mainScope.launch {
            try {
//                val data = HttpRequestManager.getAuthtypes().getAsJsonObject("data").toString()
                txtContent.value = "mock value";//data
            } catch (e: Exception) {
                errorLiverData.value = e.message.toErrorDataWrapper()
            }
        }
    }

    fun getDaliyDank() {
        mainScope.launch {
            try {
//                val category = HttpRequestManager.getDailyGank().data.getAsJsonArray("category").toString()
//                txtContent.value = category
            } catch (e: Exception) {
                errorLiverData.value = e.message.toErrorDataWrapper()
            }
        }
    }

    abstract class EventProxy(
        protected val demoViewModel: DemoViewModel,
        protected val activityDemoBinding: ActivityDemoBinding
    ) {

        fun goJsWebloader() {
            val webPath = "file:///android_asset/web/index.html"
            AJsWebPage.load(webPath)
        }

        fun loadBaidu() {
            AJsWebPage.load("https://www.baidu.com")
        }

        fun crash() {
            val a = 0
            println(2 / a)
        }

        abstract fun goMyPage()

        abstract fun choosePhoto()

        abstract fun goActivity2()

        fun goDemo1() {
            AJsWebPage.load("file:///android_asset/web/demo1/index.html", false)
        }

        fun clickTestDb() {
            val times = demoViewModel.testDbCount.value ?: 0
            demoViewModel.testDbCount.value = times + 1
        }

        abstract fun changeFragment()
    }
}