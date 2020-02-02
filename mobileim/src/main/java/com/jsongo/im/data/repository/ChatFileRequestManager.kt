package com.jsongo.im.data.repository

import com.jsongo.core.common.CommonCallback
import com.jsongo.core.network.FileRequestManager
import com.jsongo.core.network.IFileRemoteRequest
import com.jsongo.core.network.OkHttpDownloader
import java.io.File

/**
 * @author ： jsongo
 * @date ： 2020/1/30 15:24
 * @desc :
 */
//服务端保存路径的枚举值
enum class ChatFileType(val serverPath: String) {
    Image("/chatfiles/image"),
    Video("/chatfiles/video"),
    Audio("/chatfiles/audio"),
    File("/chatfiles/files")
}

object ChatFileRequestManager : IFileRemoteRequest {

    suspend fun uploadFile(chatFileType: ChatFileType, file: File): String =
        uploadFile(chatFileType.serverPath, file)

    override suspend fun uploadFile(file: File): String =
        FileRequestManager.uploadFile(ChatFileType.File.serverPath, file)

    override suspend fun uploadFile(serverPath: String, file: File): String =
        FileRequestManager.uploadFile(serverPath, file)


    override suspend fun removeUnusedFiles(files: List<String>): Boolean =
        FileRequestManager.removeUnusedFiles(files)

    override suspend fun downloadFile(url: String, savePath: String): File =
        FileRequestManager.downloadFile(url, savePath)

    override fun downloadFile(
        url: String,
        savePath: String,
        progressListener: OkHttpDownloader.ProgressListener,
        callback: CommonCallback<File>
    ) = FileRequestManager.downloadFile(url, savePath, progressListener, callback)
}
