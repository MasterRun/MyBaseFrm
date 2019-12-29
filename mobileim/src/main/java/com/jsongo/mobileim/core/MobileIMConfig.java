package com.jsongo.mobileim.core;

import android.content.Context;

import androidx.annotation.NonNull;

import com.jsongo.core.util.LogcatUtil;
import com.jsongo.mobileim.MobileIM;
import com.jsongo.mobileim.R;
import com.jsongo.mobileim.operator.ChatLoginCallback;
import com.jsongo.mobileim.operator.ChatMessageReceiver;
import com.jsongo.mobileim.operator.SendCallback;

import net.openmob.mobileimsdk.android.ClientCoreSDK;
import net.openmob.mobileimsdk.android.conf.ConfigEntity;
import net.openmob.mobileimsdk.android.core.LocalUDPDataSender;


//基本配置
public class MobileIMConfig {

    private static final String APP_KEY = MobileIM.context.getString(R.string.mobileIMKey);

    public static void init(@NonNull Context context) {
        ConfigEntity.appKey = APP_KEY;
        ConfigEntity.serverIP = MobileIM.context.getString(R.string.im_ip);
        ConfigEntity.serverUDPPort = MobileIM.context.getResources().getInteger(R.integer.im_chat_port);
        ClientCoreSDK.getInstance().init(context);

        ClientCoreSDK.getInstance().setChatBaseEvent(new ChatBaseEventImpl(new ChatLoginCallback()));
        ClientCoreSDK.getInstance().setChatTransDataEvent(new ChatTransDataEventImpl(new ChatMessageReceiver()));
        ClientCoreSDK.getInstance().setMessageQoSEvent(new MessageQoSEventImpl());
    }

    /**
     * 登陆IM
     *
     * @param chatId
     * @param token
     * @param callback 回调标识登陆的数据发送是否成功
     */
    public static void loginIM(String chatId, String token, SendCallback callback) {
        new LocalUDPDataSender.SendLoginDataAsync(MobileIM.context, chatId, token) {
            @Override
            protected void fireAfterSendLogin(int code) {
                if (code == 0) {
                    LogcatUtil.e("登录数据发送成功！, serverip:" + ConfigEntity.serverIP);
                    callback.onSuccess();
                } else {
                    LogcatUtil.e("登录数据发送失败。错误码是：" + code + "！, serverip:" + ConfigEntity.serverIP);
                    callback.onFailed();
                }
            }
        }.execute();
    }

}