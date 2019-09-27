package com.jsongo.core.util

import android.net.Uri
import android.os.Build
import android.support.v4.content.FileProvider
import com.jsongo.core.BaseCore.context
import com.vondear.rxtool.RxFileTool
import java.io.File


/**
 * @author ： jsongo
 * @date ： 19-9-27 下午10:48
 * @desc : 文件工具类
 */

class MyFileProvider : FileProvider()

fun String.toFile(): File? = RxFileTool.getFileByPath(this)

fun File.getUri(): Uri {
    val uri: Uri
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        uri = FileProvider.getUriForFile(context, ConstConf.FILE_PROVIDER_AUTH, this)
    } else {
        uri = Uri.fromFile(this)
    }
    return uri
}
