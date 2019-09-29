package com.jsongo.ajs.interaction

import android.content.Intent
import android.text.TextUtils
import com.google.gson.reflect.TypeToken
import com.huantansheng.easyphotos.EasyPhotos
import com.jsongo.ajs.helper.AjsCallback
import com.jsongo.ajs.helper.LongCallback
import com.jsongo.ajs.helper.Util
import com.jsongo.ajs.jsbridge.BridgeWebView
import com.jsongo.ajs.webloader.AJsWebLoader
import com.jsongo.core.util.ConstConf
import com.jsongo.ui.util.EasyPhotoGlideEngine
import com.vondear.rxtool.RxFileTool
import kotlin.random.Random


/**
 * @author ： jsongo
 * @date ： 19-9-28 下午3:56
 * @desc : 文件相关
 */
object File {

    @JvmStatic
    fun selectImg(
        jsWebLoader: AJsWebLoader,
        bridgeWebView: BridgeWebView,
        params: Map<String, String>,
        callback: AjsCallback
    ) {
        var requestCode = (params["requestCode"] ?: "0").toInt()
        if (requestCode == 0) {
            requestCode = Random.nextInt()
        }
        val count = (params["count"] ?: "0").toInt()
        val showCamera = TextUtils.equals(params["showCamera"], "1")
        val paths = Util.gson.fromJson<ArrayList<String>>(
            params["selectedPaths"],
            object : TypeToken<ArrayList<String>>() {}.type
        )

        val builder =
            EasyPhotos.createAlbum(jsWebLoader, showCamera, EasyPhotoGlideEngine.getInstance())
                .setCount(count)
                .setFileProviderAuthority(ConstConf.FILE_PROVIDER_AUTH)
        if (!paths.isNullOrEmpty()) {
            builder.setSelectedPhotoPaths(paths)
        }
        builder.start(requestCode)

        jsWebLoader.addLongCallback(requestCode, object : LongCallback<Intent> {
            override fun success(data: Intent?) {
                if (data == null) {
                    failed(data)
                } else {
                    val resultPaths = data.getStringArrayListExtra(EasyPhotos.RESULT_PATHS)
                    callback.success(Pair("paths", resultPaths))
                }
            }

            override fun failed(data: Intent?) {
                callback.failure()
            }
        })
    }

    /**
     * 获取文件的base64
     */
    @JvmStatic
    fun base64(
        jsWebLoader: AJsWebLoader,
        bridgeWebView: BridgeWebView,
        params: Map<String, String>,
        callback: AjsCallback
    ) {
        val path = params["path"] ?: ""
        val file = java.io.File(path)
        if (!file.exists()) {
            callback.failure(message = "file not existed")
        } else {
            val file2Base64 = RxFileTool.file2Base64(path)
            callback.success(Pair("base64", file2Base64))
        }
    }

    /**
     * 删除文件
     */
    @JvmStatic
    fun delete(
        jsWebLoader: AJsWebLoader,
        bridgeWebView: BridgeWebView,
        params: Map<String, String>,
        callback: AjsCallback
    ) {
        val path = params["path"] ?: ""
        val file = java.io.File(path)
        if (!file.exists()) {
            callback.failure(message = "file not existed")
        } else {
            val result = RxFileTool.deleteFile(path)
            if (result) {
                callback.success()
            } else {
                callback.failure(message = "delete failed")
            }
        }
    }


}