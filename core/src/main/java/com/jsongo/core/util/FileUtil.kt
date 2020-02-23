package com.jsongo.core.util

import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Base64
import androidx.core.content.FileProvider
import com.jsongo.core.BaseCore
import com.jsongo.core.constant.ConstConf
import java.io.*


/**
 * @author ： jsongo
 * @date ： 19-9-27 下午10:48
 * @desc : 文件工具类
 */

class MyFileProvider : FileProvider()

object FileUtil {
    /**
     * 得到SD卡根目录.
     */
    fun getRootPath(): File? =
        if (sdCardIsAvailable()) {
            Environment.getExternalStorageDirectory() // 取得sdcard文件路径
        } else {
            Environment.getDataDirectory()
        }


    /**
     * 根据文件路径获取文件
     *
     * @param filePath 文件路径
     * @return 文件
     */
    fun getFileByPath(filePath: String?): File? =
        if (filePath.isNullOrEmpty()) null else File(filePath)

    /**
     * 删除文件
     *
     * @param srcFilePath 文件路径
     * @return `true`: 删除成功<br></br>`false`: 删除失败
     */
    fun deleteFile(srcFilePath: String?): Boolean {
        return deleteFile(getFileByPath(srcFilePath))
    }

    /**
     * 删除文件
     *
     * @param file 文件
     * @return `true`: 删除成功<br></br>`false`: 删除失败
     */
    fun deleteFile(file: File?): Boolean {
        return file != null && (!file.exists() || file.isFile && file.delete())
    }

    /**
     * SD卡是否可用.
     */
    fun sdCardIsAvailable(): Boolean =
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            val sd = File(Environment.getExternalStorageDirectory().path)
            sd.canWrite()
        } else {
            false
        }

    fun file2Base64(filePath: String): String {
        var fis: FileInputStream?
        val bos = ByteArrayOutputStream()
        try {
            fis = FileInputStream(filePath)
            val buffer = ByteArray(1024 * 100)
            var count = 0
            while (fis.read(buffer).also({ count = it }) != -1) {
                bos.write(buffer, 0, count)
            }
            fis.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val base64String = Base64.encodeToString(bos.toByteArray(), Base64.DEFAULT)
        return base64String
    }

    /**
     * 保存InputStream流到文件
     */
    fun saveFile(inputStream: InputStream, filePath: String?) {
        try {
            val outputStream: OutputStream = FileOutputStream(File(filePath), false)
            var len: Int
            val buffer = ByteArray(1024)
            while (inputStream.read(buffer).also { len = it } != -1) {
                outputStream.write(buffer, 0, len)
            }
            outputStream.flush()
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}

/**
 * String  扩展方法，获取对应的文件
 */
fun String.toFile(): File? = FileUtil.getFileByPath(this)

/**
 * File  扩展方法，用于获取文件Uri
 */
fun File.getUri(): Uri =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        FileProvider.getUriForFile(BaseCore.context, ConstConf.FILE_PROVIDER_AUTH, this)
    } else {
        Uri.fromFile(this)
    }

