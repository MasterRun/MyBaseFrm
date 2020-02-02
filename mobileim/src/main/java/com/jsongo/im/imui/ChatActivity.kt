package com.jsongo.im.imui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.AudioManager
import android.os.Bundle
import android.os.PowerManager
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import cn.jiguang.imui.chatinput.ChatInputView
import cn.jiguang.imui.chatinput.model.FileItem
import cn.jiguang.imui.commons.models.IMessage
import cn.jiguang.imui.commons.models.IUser
import cn.jiguang.imui.messages.MsgListAdapter
import cn.jiguang.imui.messages.MsgListAdapter.HoldersConfig
import cn.jiguang.imui.messages.ViewHolderController
import com.huantansheng.easyphotos.EasyPhotos
import com.huantansheng.easyphotos.models.album.entity.Photo
import com.jsongo.core.arch.BaseActivity
import com.jsongo.core.arch.mvvm.IMvvmView
import com.jsongo.core.constant.CommonDbKeys
import com.jsongo.core.constant.ConstConf
import com.jsongo.core.db.CommonDbOpenHelper
import com.jsongo.core.widget.RxToast
import com.jsongo.im.R
import com.jsongo.im.bean.ChatMessage
import com.jsongo.im.imui.listener.IChatOperationListenerCallback
import com.jsongo.im.imui.listener.internal.CameraCallbackListener
import com.jsongo.im.imui.listener.internal.MenuClickListener
import com.jsongo.im.imui.listener.internal.RecordVoiceListener
import com.jsongo.ui.component.image.preview.ImgPreviewClick
import com.jsongo.ui.util.EasyPhotoGlideEngine
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import kotlinx.android.synthetic.main.activity_chat.*

/**
 * @author ： jsongo
 * @date ： 2020/1/30 15:42
 * @desc : 聊天页面
 */
class ChatActivity : BaseActivity(), IMvvmView {
    companion object {
        const val TAG = "MobileIM_ChatActivity"

        const val selectPhotoRequestCode = 105

        @JvmStatic
        @JvmOverloads
        fun go(activity: Activity, aimUserguid: String, convid: String = "") =
            activity.startActivity(Intent(activity, ChatActivity::class.java).apply {
                putExtra("aimUserguid", aimUserguid)
                putExtra("convid", convid)
            })

    }

    override val translucentWindow: Boolean = false

    override var containerIndex: Int = 0

    override var mainLayoutId: Int = R.layout.activity_chat

    lateinit var viewModel: ChatViewModel

    var mWakeLock: PowerManager.WakeLock? = null
        get() {
            if (field == null) {
                val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager?
                field = powerManager?.newWakeLock(
                    PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK, "$TAG:WakeLock"
                )
            }
            return field
        }

    val inputMethodManager: InputMethodManager? by lazy {
        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    }

    var mSensorManager: SensorManager? = null
    var mSensor: Sensor? = null

    private lateinit var headsetDetectReceiver: HeadsetDetectReceiver

    val mWindow: Window by lazy {
        window
    }

    lateinit var chatView: ChatView
    lateinit var chatInputView: ChatInputView
    lateinit var msgListAdapter: MsgListAdapter<ChatMessage>

    lateinit var eventProxy: EventProxy
    lateinit var chatOpeListenerCallback: IChatOperationListenerCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViewModel()

        initView()

        initData()

        observeLiveData()
    }

    override fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(ChatViewModel::class.java)
        lifecycle.addObserver(viewModel)
    }

    override fun initView() {

        //todo activity返回时，设置标记，让上一activity设置消息已读

//        rlLayoutRoot.fitsSystemWindows = true
        QMUIStatusBarHelper.translucent(this)
        QMUIStatusBarHelper.setStatusBarLightMode(this)

/*        if (Build.VERSION.SDK_INT >= 21) {
            val window = window
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.statusBarColor = Color.TRANSPARENT;
        }*/

/*
        window.setFlags(
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)*/


        topbar.visibility = View.GONE
        smartRefreshLayout.isEnabled = false

        chatView = chat_view

        registerProximitySensorListener()
        chatView.initModule()
        chatInputView = chatView.chatInputView
//        chatView.leftBackImageButton.setOnClickListener { super.onBackPressed() }

        eventProxy = EventProxy(this)
        chatOpeListenerCallback = ChatOpeListenerCallback(this, chatView, viewModel)

        initMsgAdapter()
        headsetDetectReceiver = HeadsetDetectReceiver(msgListAdapter)
        val intentFilter = IntentFilter()
        intentFilter.addAction(Intent.ACTION_HEADSET_PLUG)
        registerReceiver(headsetDetectReceiver, intentFilter)
        setClickListener()
    }

    fun initData() {
        val intent = intent
        if (intent.hasExtra("aimUserguid")) {
            viewModel.aimUserguid = intent.getStringExtra("aimUserguid")
        }
        if (intent.hasExtra("convid")) {
            viewModel.convid = intent.getStringExtra("convid")
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    fun setClickListener() {
        chatView.setOnTouchListener { view, event ->
            eventProxy.onTouchChatView(view, event)
        }
        chatView.setMenuClickListener(
            MenuClickListener(
                chatOpeListenerCallback
            )
        )

        chatView.setRecordVoiceListener(
            RecordVoiceListener(
                chatOpeListenerCallback
            )
        )

        chatView.setOnCameraCallbackListener(
            CameraCallbackListener(
                chatOpeListenerCallback
            )
        )

        chatInputView.inputView.setOnTouchListener { v, event ->
            eventProxy.onTouchInput()
        }

        chatView.selectAlbumBtn.setOnClickListener {
            eventProxy.selectAlbum()
        }
    }

    private fun initMsgAdapter() {
        // Use default layout
        val holdersConfig = HoldersConfig()
        var userguid = CommonDbOpenHelper.getValue(CommonDbKeys.USER_GUID)
        if (userguid == null) {
            userguid = ""
        }
        msgListAdapter = MsgListAdapter(userguid, holdersConfig, IMUIImageLoader(this))
        msgListAdapter.setOnMsgClickListener { message: ChatMessage ->
            eventProxy.clickMsg(message)
        }
        msgListAdapter.setMsgLongClickListener { view, message ->
            eventProxy.longClickMsg(message)
        }
        msgListAdapter.setOnAvatarClickListener { message ->
            eventProxy.clickAvatar(message.fromUser)
        }
        msgListAdapter.setMsgStatusViewClickListener {
            eventProxy.clickMsgStatus(it)
        }
        val pullToRefreshLayout = chatView.ptrLayout
        pullToRefreshLayout.setPtrHandler {
            eventProxy.refresh()
        }
        chatView.setAdapter(msgListAdapter)
        msgListAdapter.layoutManager.scrollToPosition(0)
    }

    override fun observeLiveData() {
        viewModel.conversation.observe(this, Observer {
            chatView.setTitle(it.convName)
        })

        viewModel.pageIndex.observe(this, Observer {
            viewModel.getMessages(it)
        })

        viewModel.newSendMessage.observe(this, Observer {
            msgListAdapter.addToStart(it, true)
        })
        viewModel.updateMessage.observe(this, Observer {
            msgListAdapter.updateMessage(it)
        })

        viewModel.historyMessages.observe(this, Observer {
            chatView.ptrLayout.refreshComplete()
            if (it.isEmpty()) {
                if (viewModel.pageIndex.value != 1) {
                    RxToast.info("没有了...")
                }
            } else {
                msgListAdapter.addToEnd(it)
                if (viewModel.pageIndex.value == 1) {
                    chatView.scrollToBottom()
                } else {
                    RxToast.normal("加载完成")
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (Activity.RESULT_OK == resultCode) {
            if (data != null && requestCode == selectPhotoRequestCode) {
                //获取选择的图片/视频
                val resultPhotos: ArrayList<Photo> =
                    data.getParcelableArrayListExtra(EasyPhotos.RESULT_PHOTOS)
                chatOpeListenerCallback.sendFilesCallback(ArrayList<FileItem>().apply {
                    resultPhotos.forEach {
                        add(FileItem(
                            it.path,
                            it.name,
                            it.size.toString(),
                            it.time.toString()
                        ).apply {
                            // TODO: 2020/2/1 目前全部以图片处理 （暂未考虑视频）
                            type = FileItem.Type.Image
                        })
                    }
                })
//                viewModel.uploadChatFile()
            }
        }
    }

    class EventProxy(private val chatActivity: ChatActivity) :
        ChatViewModel.EventProxy(chatActivity.viewModel) {
        /**
         * 点击选择相册
         */
        fun selectAlbum() = EasyPhotos.createAlbum(
            chatActivity, false,
            EasyPhotoGlideEngine.getInstance()
        ) //参数说明：上下文，是否显示相机按钮，
            // [配置Glide为图片加载引擎](https://github.com/HuanTanSheng/EasyPhotos/wiki/12-%E9%85%8D%E7%BD%AEImageEngine%EF%BC%8C%E6%94%AF%E6%8C%81%E6%89%80%E6%9C%89%E5%9B%BE%E7%89%87%E5%8A%A0%E8%BD%BD%E5%BA%93)
            .setFileProviderAuthority(ConstConf.FILE_PROVIDER_AUTH)
            .setCount(9)
            .start(selectPhotoRequestCode)


        fun onTouchChatView(view: View, motionEvent: MotionEvent): Boolean {
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (chatActivity.chatInputView.menuState == View.VISIBLE) {
                        chatActivity.chatInputView.dismissMenuLayout()
                    }
                    try {
                        val v = chatActivity.currentFocus
                        if (v != null) {
                            chatActivity.inputMethodManager?.hideSoftInputFromWindow(
                                v.windowToken,
                                0
                            )
                            chatActivity.mWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
                            view.clearFocus()
                        }
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                }
                MotionEvent.ACTION_UP -> view.performClick()
                else -> {
                }
            }
            return false
        }

        fun onTouchInput(): Boolean {
            chatActivity.chatView.scrollToBottom()
            return false
        }

        /**
         * 点击消息
         */
        fun clickMsg(message: ChatMessage) {
            if (message.type == IMessage.MessageType.RECEIVE_VIDEO.ordinal
                || message.type == IMessage.MessageType.SEND_VIDEO.ordinal
            ) {
                /*      if (!TextUtils.isEmpty(message.getMediaFilePath())) {
                            Intent intent = new Intent(ChatActivity.this, VideoActivity.class);
                            intent.putExtra(VideoActivity.VIDEO_PATH, message.getMediaFilePath());
                            startActivity(intent);
                        }*/
                RxToast.info("click video")
            } else if (message.type == IMessage.MessageType.RECEIVE_IMAGE.ordinal
                || message.type == IMessage.MessageType.SEND_IMAGE.ordinal
            ) {
                val i: Int = viewModel.imgMsgIdList.indexOf(message.msgId)
                ImgPreviewClick(chatActivity, i, viewModel.imagePathList).start()
            } else {
                RxToast.info("message click")
            }
        }

        /**
         * 长按消息
         */
        fun longClickMsg(message: ChatMessage) {
            RxToast.info("onMessageLongClick : ")
            /*Toast.makeText(getApplicationContext(),
                getApplicationContext().getString(R.string.message_long_click_hint),
                Toast.LENGTH_SHORT).show();
            // do something*/
        }

        /**
         * 点击头像
         */
        fun clickAvatar(fromUser: IUser) {
            //跳转用户信息页面
//            UserDetailActivity.Companion.startThis(fromUser.id)
            RxToast.info("跳转用户信息页面")
        }

        /**
         * 点击消息状态
         */
        fun clickMsgStatus(message: ChatMessage) {
            RxToast.info("onStatusViewClick")
            // message status view click, resend or download here
        }
    }

    class ChatOpeListenerCallback(
        override val activity: FragmentActivity,
        override val chatView: ChatView,
        viewModel: ChatViewModel
    ) : com.jsongo.im.imui.listener.ChatOpeListenerCallback(viewModel)

    //region sensor  screen headset

    //传感器时间监听
    private val sensorEventListener = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        }

        /**
         * 传感器变化回调
         */
        override fun onSensorChanged(event: SensorEvent?) {
            val sensor = mSensor
            if (event != null && sensor != null) {
                val audioManager =
                    getSystemService(Context.AUDIO_SERVICE) as AudioManager
                try {
                    if (audioManager.isBluetoothA2dpOn || audioManager.isWiredHeadsetOn) {
                        return
                    }
                    if (msgListAdapter.getMediaPlayer().isPlaying()) {
                        val distance = event.values[0]
                        if (distance >= sensor.maximumRange) {
                            msgListAdapter.setAudioPlayByEarPhone(0)
                            //息屏
                            mWakeLock?.setReferenceCounted(false)
                            mWakeLock?.release()
                            mWakeLock = null
                        } else {
                            msgListAdapter.setAudioPlayByEarPhone(2)
                            ViewHolderController.getInstance().replayVoice()
                            //亮屏
                            mWakeLock?.acquire()
                        }
                    } else {
                        if (mWakeLock?.isHeld == true) {
                            mWakeLock?.release()
                            mWakeLock = null
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    @SuppressLint("InvalidWakeLockTag")
    private fun registerProximitySensorListener() {
        try {
            mSensorManager =
                getSystemService(Context.SENSOR_SERVICE) as SensorManager
            mSensor = mSensorManager?.getDefaultSensor(Sensor.TYPE_PROXIMITY)

            mSensorManager?.registerListener(
                sensorEventListener,
                mSensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 耳机广播接收器
     */
    private class HeadsetDetectReceiver(private val msgListAdapter: MsgListAdapter<ChatMessage>) :
        BroadcastReceiver() {
        override fun onReceive(
            context: Context,
            intent: Intent
        ) {
            if (intent.action == Intent.ACTION_HEADSET_PLUG) {
                if (intent.hasExtra("state")) {
                    val state = intent.getIntExtra("state", 0)
                    msgListAdapter.setAudioPlayByEarPhone(state)
                }
            }
        }
    }
    //endregion

    override fun onIPageDestroy() {
        unregisterReceiver(headsetDetectReceiver)
        mSensorManager?.unregisterListener(sensorEventListener)
        chatView.dispose()
        super.onIPageDestroy()
    }
}