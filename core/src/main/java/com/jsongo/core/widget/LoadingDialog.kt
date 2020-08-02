package com.jsongo.core.widget

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.IntDef
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatImageView
import com.jsongo.core.R
import com.jsongo.core.widget.LoadingDialog.Builder.IconType
import com.jsongo.core_mini.widget.ILoadingDialog
import com.qmuiteam.qmui.skin.QMUISkinHelper
import com.qmuiteam.qmui.skin.QMUISkinManager
import com.qmuiteam.qmui.skin.QMUISkinValueBuilder
import com.qmuiteam.qmui.util.QMUIResHelper
import com.qmuiteam.qmui.widget.QMUILoadingView
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog
import com.qmuiteam.qmui.widget.dialog.QMUITipDialogView
import com.qmuiteam.qmui.widget.textview.QMUISpanTouchFixTextView

/**
 * @author ： jsongo
 * @date ： 2020/8/2 18:16
 * @desc :
 */

class LoadingDialog @JvmOverloads constructor(
    context: Context?,
    themeResId: Int = com.qmuiteam.qmui.R.style.QMUI_TipDialog
) : QMUITipDialog(context!!, themeResId), ILoadingDialog {

    init {
        setCanceledOnTouchOutside(false)
    }

    override fun show(msg: String) {
        setTitle(msg)
        show()
    }

    /**
     * 生成默认的 [LoadingDialog]
     *
     *
     * 提供了一个图标和一行文字的样式, 其中图标有几种类型可选。见 [IconType]
     *
     *
     * @see CustomBuilder
     */
    class Builder(private val mContext: Context) {

        companion object {
            /**
             * 不显示任何icon
             */
            const val ICON_TYPE_NOTHING = 0

            /**
             * 显示 Loading 图标
             */
            const val ICON_TYPE_LOADING = 1

            /**
             * 显示成功图标
             */
            const val ICON_TYPE_SUCCESS = 2

            /**
             * 显示失败图标
             */
            const val ICON_TYPE_FAIL = 3

            /**
             * 显示信息图标
             */
            const val ICON_TYPE_INFO = 4
        }

        @IntDef(
            ICON_TYPE_NOTHING,
            ICON_TYPE_LOADING,
            ICON_TYPE_SUCCESS,
            ICON_TYPE_FAIL,
            ICON_TYPE_INFO
        )
        @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
        annotation class IconType

        @IconType
        private var mCurrentIconType: Int = ICON_TYPE_NOTHING
        private var mTipWord: CharSequence? = null
        private var mSkinManager: QMUISkinManager? = null

        /**
         * 设置 icon 显示的内容
         *
         * @see IconType
         */
        fun setIconType(@IconType iconType: Int): Builder {
            mCurrentIconType = iconType
            return this
        }

        /**
         * 设置显示的文案
         */
        fun setTipWord(tipWord: CharSequence?): Builder {
            mTipWord = tipWord
            return this
        }

        fun setSkinManager(skinManager: QMUISkinManager?): Builder {
            mSkinManager = skinManager
            return this
        }

        /**
         * 创建 Dialog, 但没有弹出来, 如果要弹出来, 请调用返回值的 [Dialog.show] 方法
         *
         * @param cancelable 按系统返回键是否可以取消
         * @return 创建的 Dialog
         */
        @JvmOverloads
        fun create(
            cancelable: Boolean = true,
            style: Int = com.qmuiteam.qmui.R.style.QMUI_TipDialog
        ): LoadingDialog {
            val dialog = LoadingDialog(mContext, style)
            dialog.setCancelable(cancelable)
            dialog.setSkinManager(mSkinManager)
            val dialogContext = dialog.context
            val dialogView = QMUITipDialogView(dialogContext)
            val builder = QMUISkinValueBuilder.acquire()
            if (mCurrentIconType == ICON_TYPE_LOADING) {
                val loadingView = QMUILoadingView(dialogContext)
                loadingView.setColor(
                    QMUIResHelper.getAttrColor(
                        dialogContext,
                        com.qmuiteam.qmui.R.attr.qmui_skin_support_tip_dialog_loading_color
                    )
                )
                loadingView.setSize(
                    QMUIResHelper.getAttrDimen(
                        dialogContext, com.qmuiteam.qmui.R.attr.qmui_tip_dialog_loading_size
                    )
                )
                builder.tintColor(com.qmuiteam.qmui.R.attr.qmui_skin_support_tip_dialog_loading_color)
                QMUISkinHelper.setSkinValue(loadingView, builder)
                dialogView.addView(loadingView, onCreateIconOrLoadingLayoutParams(dialogContext))
            } else if (mCurrentIconType == ICON_TYPE_SUCCESS || mCurrentIconType == ICON_TYPE_FAIL || mCurrentIconType == ICON_TYPE_INFO
            ) {
                val imageView: ImageView = AppCompatImageView(dialogContext)
                builder.clear()
                val drawable: Drawable?
                if (mCurrentIconType == ICON_TYPE_SUCCESS) {
                    drawable = QMUIResHelper.getAttrDrawable(
                        dialogContext,
                        com.qmuiteam.qmui.R.attr.qmui_skin_support_tip_dialog_icon_success_src
                    )
                    builder.src(com.qmuiteam.qmui.R.attr.qmui_skin_support_tip_dialog_icon_success_src)
                } else if (mCurrentIconType == ICON_TYPE_FAIL) {
                    drawable = QMUIResHelper.getAttrDrawable(
                        dialogContext,
                        com.qmuiteam.qmui.R.attr.qmui_skin_support_tip_dialog_icon_error_src
                    )
                    builder.src(com.qmuiteam.qmui.R.attr.qmui_skin_support_tip_dialog_icon_error_src)
                } else {
                    drawable = QMUIResHelper.getAttrDrawable(
                        dialogContext,
                        com.qmuiteam.qmui.R.attr.qmui_skin_support_tip_dialog_icon_info_src
                    )
                    builder.src(com.qmuiteam.qmui.R.attr.qmui_skin_support_tip_dialog_icon_info_src)
                }
                imageView.setImageDrawable(drawable)
                QMUISkinHelper.setSkinValue(imageView, builder)
                dialogView.addView(imageView, onCreateIconOrLoadingLayoutParams(dialogContext))
            }
            if (mTipWord != null && mTipWord!!.length > 0) {
                val tipView: TextView = QMUISpanTouchFixTextView(dialogContext)
                tipView.ellipsize = TextUtils.TruncateAt.END
                tipView.gravity = Gravity.CENTER
                tipView.setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    QMUIResHelper.getAttrDimen(
                        dialogContext,
                        com.qmuiteam.qmui.R.attr.qmui_tip_dialog_text_size
                    )
                        .toFloat()
                )
                tipView.setTextColor(
                    QMUIResHelper.getAttrColor(
                        dialogContext,
                        com.qmuiteam.qmui.R.attr.qmui_skin_support_tip_dialog_text_color
                    )
                )
                tipView.text = mTipWord
                builder.clear()
                builder.textColor(com.qmuiteam.qmui.R.attr.qmui_skin_support_tip_dialog_text_color)
                QMUISkinHelper.setSkinValue(tipView, builder)
                dialogView.addView(
                    tipView,
                    onCreateTextLayoutParams(dialogContext, mCurrentIconType)
                )
            }
            builder.release()
            dialog.setContentView(dialogView)
            return dialog
        }

        protected fun onCreateIconOrLoadingLayoutParams(context: Context?): LinearLayout.LayoutParams {
            return LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        protected fun onCreateTextLayoutParams(
            context: Context?,
            @Builder.IconType iconType: Int
        ): LinearLayout.LayoutParams {
            val lp = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
            )
            if (iconType != ICON_TYPE_NOTHING) {
                lp.topMargin =
                    QMUIResHelper.getAttrDimen(context, R.attr.qmui_tip_dialog_text_margin_top)
            }
            return lp
        }

    }

    /**
     * CustomBuilder with xml layout
     */
    class CustomBuilder(protected val mContext: Context) {
        private var mContentLayoutId = 0
        private var mSkinManager: QMUISkinManager? = null
        fun setSkinManager(skinManager: QMUISkinManager?): CustomBuilder {
            mSkinManager = skinManager
            return this
        }

        fun setContent(@LayoutRes layoutId: Int): CustomBuilder {
            mContentLayoutId = layoutId
            return this
        }

        fun create(): LoadingDialog {
            val dialog = LoadingDialog(mContext)
            dialog.setSkinManager(mSkinManager)
            val dialogContext = dialog.context
            val tipDialogView = QMUITipDialogView(dialogContext)
            LayoutInflater.from(dialogContext).inflate(mContentLayoutId, tipDialogView, true)
            dialog.setContentView(tipDialogView)
            return dialog
        }

    }
}