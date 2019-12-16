package com.jsongo.ajs.lzyzsd_jsbridge;

import android.graphics.Bitmap;
import android.os.Build;
import android.webkit.MimeTypeMap;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.jsongo.ajs.util.ConstValue;
import com.safframework.log.L;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;

/**
 * 如果要自定义WebViewClient必须要集成此类
 * Created by bruce on 10/28/15.
 */
public class BridgeWebViewClient extends WebViewClient {
    private BridgeWebView webView;
    private OnPageFinishListener onPageFinishListener;

    public BridgeWebViewClient(BridgeWebView webView) {
        this.webView = webView;
    }

    public OnPageFinishListener getOnPageFinishListener() {
        return onPageFinishListener;
    }

    public void setOnPageFinishListener(OnPageFinishListener onPageFinishListener) {
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
                String suffix = path.substring(path.lastIndexOf("."));
                suffix = kotlin.text.StringsKt.trim(suffix, '.');
                String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(suffix);
                InputStream open = new FileInputStream(new File(path));
                response = new WebResourceResponse(mimeType, "UTF-8", open);
            } else if (url.startsWith(ConstValue.LocalFilePrefix)) {
                String path = url.replace(ConstValue.LocalFilePrefix, "");
                L.i("本地文件路径：" + path);
                InputStream open = new FileInputStream(new File(path));
                String suffix = path.substring(path.lastIndexOf("."));
                suffix = kotlin.text.StringsKt.trim(suffix, '.');
                String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(suffix);
                response = new WebResourceResponse(mimeType, "UTF-8", open);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //跨域配置
        /*Map<String, String> headers = response.getResponseHeaders();
        if (headers == null) {
            headers = new HashMap<>();
        }
        headers.put("Access-Control-Allow-Origin", "*");*/
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

    protected void onCustomPageFinishd(WebView view, String url) {

    }

    public interface OnPageFinishListener {
        void onPageFinish(WebView view, String url);
    }
}