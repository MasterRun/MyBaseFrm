package com.jsongo.ajs.jsbridge;

import android.graphics.Bitmap;
import android.os.Build;

import com.github.lzyzsd.jsbridge.Message;
import com.jsongo.ajs.helper.ConstValue;
import com.safframework.log.L;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;

/**
 * @author jsongo
 * @date 2019/5/9 18:17
 * @desc modify from jsbridge replace by tencent x5 core
 */
public class BridgeWebViewClient extends WebViewClient {
    private BridgeWebView webView;
    private onPageFinishListener onPageFinishListener;

    public BridgeWebViewClient(BridgeWebView webView) {
        this.webView = webView;
    }

    public BridgeWebViewClient.onPageFinishListener getOnPageFinishListener() {
        return onPageFinishListener;
    }

    public void setOnPageFinishListener(BridgeWebViewClient.onPageFinishListener onPageFinishListener) {
        this.onPageFinishListener = onPageFinishListener;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        try {
            url = URLDecoder.decode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (url.startsWith(BridgeUtil.YY_RETURN_DATA)) { // 如果是返回数据
            webView.handlerReturnData(url);
            return true;
        } else if (url.startsWith(BridgeUtil.YY_OVERRIDE_SCHEMA)) { //
            webView.flushMessageQueue();
            return true;
        } else {
            return this.onCustomShouldOverrideUrlLoading(url) ? true : super.shouldOverrideUrlLoading(view, url);
        }
    }

    // 增加shouldOverrideUrlLoading在api》=24时
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            String url = request.getUrl().toString();
            try {
                url = URLDecoder.decode(url, "UTF-8");
            } catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
            }
            if (url.startsWith(BridgeUtil.YY_RETURN_DATA)) { // 如果是返回数据
                webView.handlerReturnData(url);
                return true;
            } else if (url.startsWith(BridgeUtil.YY_OVERRIDE_SCHEMA)) { //
                webView.flushMessageQueue();
                return true;
            } else {
                return this.onCustomShouldOverrideUrlLoading(url) ? true : super.shouldOverrideUrlLoading(view, request);
            }
        } else {
            return super.shouldOverrideUrlLoading(view, request);
        }
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView webView, String url) {
        WebResourceResponse response = null;
        response = super.shouldInterceptRequest(webView, url);
        try {
            ArrayList<String> jsList = ConstValue.INSTANCE.getJsList();
            for (String s : jsList) {
                if (url.contains(s)) {
                    InputStream open = webView.getContext().getResources().getAssets().open(ConstValue.jsBasePath + "/" + s);
                    response = new WebResourceResponse("text/javascript", "UTF-8", open);
                    break;
                }
            }
            //过滤本地图片请求
            if (url.startsWith(ConstValue.LocalPicPrefix)) {
                String path = url.replace(ConstValue.LocalPicPrefix, "");
                L.i("本地图片路径：" + path);
                if (url.endsWith(".png")) {
                    InputStream open = new FileInputStream(new File(path));
                    response = new WebResourceResponse("image/png", "UTF-8", open);
                }
                if (url.endsWith(".jpg")) {
                    InputStream open = new FileInputStream(new File(path));
                    response = new WebResourceResponse("image/jpg", "UTF-8", open);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);

        if (BridgeWebView.toLoadJs != null) {
            BridgeUtil.webViewLoadLocalJs(view, BridgeWebView.toLoadJs);
        }

        //
        if (webView.getStartupMessage() != null) {
            for (Message m : webView.getStartupMessage()) {
                webView.dispatchMessage(m);
            }
            webView.setStartupMessage(null);
        }

        if (onPageFinishListener != null) {
            onPageFinishListener.onPageFinish(view, url);
        }
    }


    protected boolean onCustomShouldOverrideUrlLoading(String url) {
        return false;
    }

    public interface onPageFinishListener {
        void onPageFinish(WebView view, String url);
    }
}