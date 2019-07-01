let myBridge;
let ajs;

$(function () {
    connectWebViewJavascriptBridge(function (bridge) {
        bridge.init(function (message, responseCallback) {
        });
        myBridge = bridge;
        regBridgeMethod();
        regHandler();
        onEnvReady();
    });
});

/**
 * 监听bridge完成
 * @param callback
 */
function connectWebViewJavascriptBridge(callback) {
    if (window.WebViewJavascriptBridge) {
        callback(WebViewJavascriptBridge)
    } else {
        document.addEventListener(
            'WebViewJavascriptBridgeReady', function () {
                callback(WebViewJavascriptBridge)
            }, false);
    }
}

/**
 * 注册原生方法
 */
function regBridgeMethod() {
    var convertFunc = function (callname, params, callback) {
        myBridge.callHandler(callname, params, function (responseData) {
            callback(JSON.parse(responseData))
        })
    };

    ajs = {
        topbar: {
            bgcolor(params, callback) {
                convertFunc('topbar.bgcolor', params, callback)
            },
            hide(params, callback) {
                convertFunc('topbar.hide', params, callback)
            },
            title(params, callback) {
                convertFunc('topbar.title', params, callback)
            },
            statusbar(params, callback) {
                convertFunc('topbar.statusbar', params, callback)
            }
        },
        common: {
            back(callback) {
                convertFunc("common.back", {}, callback)
            },
            messagedialog(params, callback) {
                convertFunc('common.messagedialog', params, callback)
            },
            loading(show, callback) {
                convertFunc("common.loading", {show: show}, callback)
            },
            localpic(path, callback) {
                convertFunc("common.localpic", {path: path}, callback)
            }
        },
        toast: {
            error(text, callback) {
                convertFunc("toast.error", {text: text}, callback)
            },
            warning(text, callback) {
                convertFunc("toast.warning", {text: text}, callback)
            },
            info(text, callback) {
                convertFunc("toast.info", {text: text}, callback)
            },
            normal(text, callback) {
                convertFunc("toast.normal", {text: text}, callback)
            },
            success(text, callback) {
                convertFunc("toast.success", {text: text}, callback)
            },
        }
    };

}