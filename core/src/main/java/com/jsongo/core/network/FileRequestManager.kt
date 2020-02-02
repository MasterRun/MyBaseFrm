package com.jsongo.core.network

import com.jsongo.core.bean.DataWrapper
import com.jsongo.core.common.CommonCallback
import com.jsongo.core.constant.CommonDbKeys
import com.jsongo.core.db.CommonDbOpenHelper
import com.jsongo.core.util.FileUtil
import com.jsongo.core.util.StringUtil
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.http.*
import java.io.File

/**
 * @author ： jsongo
 * @date ： 2020/1/29 20:53
 * @desc : 文件相关请求
 */

interface FileApiService {

    @POST("file/upload")
    @Multipart
    suspend fun uploadFile(
        @Part("serverPath") serverPath: RequestBody?,
        @Part file: MultipartBody.Part?
    ): DataWrapper<String?>

    @POST("file/removeUnused")
    @FormUrlEncoded
    suspend fun removeUnusedFiles(@Field("files") files: List<String>): DataWrapper<Boolean?>

    @GET
    @Streaming
    fun downloadFile(@Url url: String): ResponseBody
}

interface IFileRemoteRequest {

    @Throws
    suspend fun uploadFile(serverPath: String, file: File): String

    @Throws
    suspend fun uploadFile(file: File): String

    @Throws
    suspend fun removeUnusedFiles(files: List<String>): Boolean

    @Throws
    suspend fun downloadFile(url: String, savePath: String): File

    fun downloadFile(
        url: String,
        savePath: String,
        progressListener: OkHttpDownloader.ProgressListener,
        callback: CommonCallback<File>
    )
}

object FileRequestManager : IFileRemoteRequest {

    override suspend fun uploadFile(serverPath: String, file: File): String =
        checkResult {
            val lastDotIndex = file.name.lastIndexOf(".")
            val suffix = file.name.substring(lastDotIndex + 1)
            val guid = CommonDbOpenHelper.getValue(CommonDbKeys.USER_GUID) ?: StringUtil.genUUID()
            val fileName = System.currentTimeMillis().toString() + guid + "." + suffix
            val requestBody = file.asRequestBody(null)
            //mark  name 参数使用  "file"  对应接口的file参数获取到multipartFile
            val bodyPart = MultipartBody.Part.createFormData("file", fileName, requestBody)

//            MultipartBody.Builder().addFormDataPart()
            val serverPathBody = serverPath.toRequestBody("text/plain".toMediaTypeOrNull())
            ApiManager.createApiService(FileApiService::class.java)
                .uploadFile(serverPathBody, bodyPart)
        }

    override suspend fun uploadFile(file: File): String = uploadFile("/common/files", file)

    override suspend fun removeUnusedFiles(files: List<String>): Boolean =
        checkResult {
            ApiManager.createApiService(FileApiService::class.java).removeUnusedFiles(files)
        }

    override suspend fun downloadFile(url: String, savePath: String): File =
        checkResult {
            val responseBody =
                ApiManager.createApiService(FileApiService::class.java).downloadFile(url)
            val dir = File(savePath)
            if (!dir.exists()) {
                dir.mkdirs()
            }
            val file = File(dir, url.split("/").last())
            FileUtil.saveFile(responseBody.byteStream(), file.absolutePath)
            DataWrapper<File?>(file)
        }

    override fun downloadFile(
        url: String,
        savePath: String,
        progressListener: OkHttpDownloader.ProgressListener,
        callback: CommonCallback<File>
    ) = OkHttpDownloader.download(url, savePath, progressListener, callback)

}