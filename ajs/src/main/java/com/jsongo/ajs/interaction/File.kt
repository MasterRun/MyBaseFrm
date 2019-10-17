package com.jsongo.ajs.interaction

import android.content.Intent
import android.text.TextUtils
import com.google.gson.reflect.TypeToken
import com.huantansheng.easyphotos.Builder.AlbumBuilder
import com.huantansheng.easyphotos.EasyPhotos
import com.jsongo.ajs.helper.AjsCallback
import com.jsongo.ajs.helper.AjsWebViewHost
import com.jsongo.ajs.helper.LongCallback
import com.jsongo.ajs.helper.Util
import com.jsongo.ajs.widget.AJsWebView
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
        ajsWebViewHost: AjsWebViewHost,
        aJsWebView: AJsWebView,
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

        val builder: AlbumBuilder
        val hostFragment = ajsWebViewHost.hostFragment
        if (hostFragment != null) {
            builder = EasyPhotos.createAlbum(
                ajsWebViewHost.hostFragment,
                showCamera,
                EasyPhotoGlideEngine.getInstance()
            )
        } else {
            if (ajsWebViewHost.hostActivity == null) {
                callback.failure()
                return
            }
            builder = EasyPhotos.createAlbum(
                ajsWebViewHost.hostActivity,
                showCamera,
                EasyPhotoGlideEngine.getInstance()
            )
        }
        builder.setCount(count)
            .setFileProviderAuthority(ConstConf.FILE_PROVIDER_AUTH)
        if (!paths.isNullOrEmpty()) {
            builder.setSelectedPhotoPaths(paths)
        }
        builder.start(requestCode)

        ajsWebViewHost.addLongCallback(requestCode, object : LongCallback<Intent> {
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
        ajsWebViewHost: AjsWebViewHost,
        aJsWebView: AJsWebView,
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
        ajsWebViewHost: AjsWebViewHost,
        aJsWebView: AJsWebView,
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