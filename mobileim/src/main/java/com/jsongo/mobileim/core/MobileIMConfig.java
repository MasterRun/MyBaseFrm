package com.jsongo.mobileim.core;

import android.content.Context;

import androidx.annotation.NonNull;

import com.jsongo.core.BaseCore;
import com.jsongo.mobileim.MobileIM;
import com.jsongo.mobileim.R;
import com.jsongo.mobileim.operator.ChatLoginCallback;
import com.jsongo.mobileim.operator.ChatMessageReceiver;

import net.openmob.mobileimsdk.android.ClientCoreSDK;
import net.openmob.mobileimsdk.android.conf.ConfigEntity;


//基本配置
public class MobileIMConfig {

    private static final String APP_KEY = MobileIM.context.getString(R.string.mobileIMKey);

    public static void init(@NonNull Context context) {
        ConfigEntity.appKey = APP_KEY;
        ConfigEntity.serverIP = MobileIM.context.getString(R.string.im_ip);
        ConfigEntity.serverUDPPort = MobileIM.context.getResources().getInteger(R.integer.im_chat_port);
        ClientCoreSDK.getInstance().init(context);
        ClientCoreSDK.DEBUG = BaseCore.INSTANCE.isDebug() && context.getResources().getBoolean(R.bool.im_debug);

        ClientCoreSDK.getInstance().setChatBaseEvent(new ChatBaseEventImpl(new ChatLoginCallback()));
        ClientCoreSDK.getInstance().setChatTransDataEvent(new ChatTransDataEventImpl(new ChatMessageReceiver()));
        ClientCoreSDK.getInstance().setMessageQoSEvent(new MessageQoSEventImpl());
    }
}