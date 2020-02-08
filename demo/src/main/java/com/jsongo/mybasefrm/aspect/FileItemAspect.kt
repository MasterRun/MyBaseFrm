package com.jsongo.mybasefrm.aspect

import android.os.Handler
import android.view.View
import android.widget.ProgressBar
import cn.jiguang.imui.chatinput.model.FileItem
import cn.jiguang.imui.chatinput.photo.SelectPhotoView
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import kotlin.concurrent.thread

/**
 * @author ： jsongo
 * @date ： 2020/2/2 21:22
 * @desc :
 */
@Aspect
class FileItemAspect {

    @Pointcut("execution(* cn.jiguang.imui.chatinput.model.FileItem.compareTo(..))")
    fun fileItemSortPointcut() {
    }

    @Around("fileItemSortPointcut()")
    fun fileItemSortAspect(joinPoint: ProceedingJoinPoint): Any? {
        joinPoint.args[0] ?: return 0
        return try {
            joinPoint.proceed()
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }

    @Pointcut("execution(* cn.jiguang.imui.chatinput.photo.SelectPhotoView.initData(..))")
    fun selectPhotoViewInitDataPointcut() {
    }

    @Around("selectPhotoViewInitDataPointcut()")
    fun selectPhotoViewInitDataAspect(joinPoint: ProceedingJoinPoint) {
        val selectPhotoView = joinPoint.`this` as SelectPhotoView

        val selectPhotoViewClazz = SelectPhotoView::class.java

        val hasPermissionMethod = selectPhotoViewClazz.getDeclaredMethod("hasPermission")
        hasPermissionMethod.isAccessible = true
        if (hasPermissionMethod.invoke(selectPhotoView) as Boolean) {

            selectPhotoViewClazz.getDeclaredField("mProgressBar").run {
                isAccessible = true
                (get(selectPhotoView) as ProgressBar).visibility = View.VISIBLE
            }

            thread {
                val getPhotos = selectPhotoViewClazz.getDeclaredMethod("getPhotos").run {
                    isAccessible = true
                    invoke(selectPhotoView) as Boolean
                }
                val getVideos = selectPhotoViewClazz.getDeclaredMethod("getVideos").run {
                    isAccessible = true
                    invoke(selectPhotoView) as Boolean
                }
                val handler = selectPhotoViewClazz.getDeclaredField("mMediaHandler").run {
                    isAccessible = true
                    get(selectPhotoView) as Handler
                }

                if (getPhotos && getVideos) {
                    try {
                        selectPhotoViewClazz.getDeclaredField("mFileItems").run {
                            isAccessible = true
                            get(selectPhotoView) as MutableList<FileItem>
                        }.sort()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    handler.sendEmptyMessage(1)
                } else {
                    handler.sendEmptyMessage(0)
                }
            }
        }
    }
}