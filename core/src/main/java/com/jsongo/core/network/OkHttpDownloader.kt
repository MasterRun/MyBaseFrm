package com.jsongo.core.network

import com.jsongo.core.common.CommonCallback
import com.jsongo.core.util.FileUtil
import okhttp3.*
import okio.Buffer
import okio.BufferedSource
import okio.ForwardingSource
import okio.buffer
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

/**
 * @author ： jsongo
 * @date ： 2020/1/30 13:24
 * @desc : okhttp 带进度下载
 */
object OkHttpDownloader {
    /*
     * 回调接口
     */
    interface ProgressListener {
        /**
         * @param bytesRead     已经读取的字节数
         * @param contentLength 响应总长度
         * @param done          是否读取完毕
         */
        fun update(bytesRead: Long, contentLength: Long, done: Boolean)
    }

    internal class ProgressResponseBody(
        private val responseBody: ResponseBody,
        private val progressListener: ProgressListener?
    ) : ResponseBody() {

        private var bufferedSource: BufferedSource? = null
        override fun contentType(): MediaType? {
            return responseBody.contentType()
        }

        override fun contentLength(): Long {
            return responseBody.contentLength()
        }

        override fun source(): BufferedSource {
            if (bufferedSource == null) {
                bufferedSource = object : ForwardingSource(responseBody.source()) {
                    var totalBytesRead = 0L

                    @Throws(IOException::class)
                    override fun read(
                        sink: Buffer,
                        byteCount: Long
                    ): Long {
                        val bytesRead = super.read(sink, byteCount)
                        totalBytesRead += if (bytesRead != -1L) bytesRead else 0 //不断统计当前下载好的数据
                        //接口回调
                        progressListener?.update(
                            totalBytesRead,
                            responseBody.contentLength(),
                            bytesRead == -1L
                        )
                        return bytesRead
                    }
                }.buffer()
            }
            return bufferedSource!!
        }

    }

    /**
     * 下载
     */
    @JvmStatic
    fun download(
        url: String,
        savePath: String,
        progressListener: ProgressListener? = null,
        callBack: CommonCallback<File>? = null
    ) {
        val client = ApiManager.createOkHttpClient(true).second
        client.newBuilder()
            .addNetworkInterceptor { chain: Interceptor.Chain ->
                chain.proceed(chain.request()).apply {
                    newBuilder().apply {
                        val body = body
                        if (body != null) {
                            //使用自定义的body
                            body(ProgressResponseBody(body, progressListener))
                        }
                    }.build()
                }
            }.build()
        val request = Request.Builder().url(url).get().build()
        val call = client.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                callBack?.failed(-1, e.message ?: "", e)
            }

            @Throws(IOException::class)
            override fun onResponse(
                call: Call,
                response: Response
            ) {
                val body = response.body
                if (body == null) {
                    callBack?.failed(-1, "下载失败", null)
                    return
                }
                val inputStream = body.byteStream()
                FileUtil.saveFile(inputStream, savePath)
                callBack?.success(File(savePath))
            }
        })
    }

    @JvmStatic
    fun download(
        url: String,
        savePath: String,
        progressListener: ProgressListener? = null
    ): File? {
//        val startTime = System.currentTimeMillis()
//        LogcatUtil.e("startTime=$startTime")
        val okHttpClient = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .addHeader("Connection", "close")
            .build()
        val call = okHttpClient.newCall(request)
        var inputStream: InputStream? = null
        val buf = ByteArray(2048)
        var len = 0
        var fos: FileOutputStream? = null
        val file = File(savePath)
        // 储存下载文件的目录
        val parentDir = file.parentFile
        if (parentDir?.exists() == false) {
            parentDir.mkdirs()
        }
        try {
            val response = call.execute()
            val body = response.body ?: return null
            inputStream = body.byteStream()
            val total = body.contentLength()
            fos = FileOutputStream(file)
            var sum: Long = 0
            while (inputStream.read(buf).also { len = it } != -1) {
                fos.write(buf, 0, len)
                sum += len.toLong()
                progressListener?.update(sum, total, sum == total)
            }
            fos.flush()
//            LogcatUtil.e("download success")
//            LogcatUtil.e("totalTime=" + (System.currentTimeMillis() - startTime))
            return file
        } catch (e: Exception) {
            e.printStackTrace()
//            LogcatUtil.e("download failed : " + e.message)
        } finally {
            try {
                inputStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            try {
                fos?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return null
    }
}