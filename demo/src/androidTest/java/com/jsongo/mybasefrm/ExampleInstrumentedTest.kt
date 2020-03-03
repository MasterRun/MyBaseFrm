package com.jsongo.mybasefrm

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().getTargetContext()
//        assertEquals("com.jsongo.mybasefrm", appContext.packageName)y


        val statusBarHeight = QMUIDisplayHelper.getStatusBarHeight(appContext)
        println(statusBarHeight)
    }
}
