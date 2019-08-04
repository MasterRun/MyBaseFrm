let myBridge;
let ajs = {};
let smartrefresh = {
    header: {
        DeliveryHeader: 'DeliveryHeader',
        DropBoxHeader: 'DropBoxHeader',
        BezierRadarHeader: 'BezierRadarHeader',
        BezierCircleHeader: 'BezierCircleHeader',
        FlyRefreshHeader: 'FlyRefreshHeader',
        ClassicsHeader: 'ClassicsHeader',
        PhoenixHeader: 'PhoenixHeader',
        TaurusHeader: 'TaurusHeader',
        FunGameBattleCityHeader: 'FunGameBattleCityHeader',
        FunGameHitBlockHeader: 'FunGameHitBlockHeader',
        WaveSwipeHeader: 'WaveSwipeHeader',
        MaterialHeader: 'MaterialHeader',
        StoreHouseHeader: 'StoreHouseHeader',
        WaterDropHeader: 'WaterDropHeader',
    },
    footer: {
        BallPulseFooter: 'BallPulseFooter',
        ClassicsFooter: 'ClassicsFooter'
    }
};

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
            if (callback != undefined) {
                callback(JSON.parse(responseData))
            }
        })
    };


    ajs = {
        topbar: {
            bgcolor(color, callback) {
                convertFunc('topbar.bgcolor', { color: color }, callback)
            },
            hide(hide, callback) {
                convertFunc('topbar.hide', { hide: hide }, callback)
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
            localpic(path, callback) {
                convertFunc("common.localpic", { path: path }, callback)
            },
            showpic(urls, index, callback) {
                convertFunc("common.showpic", { urls: JSON.stringify(urls), index: index }, callback)
            },
            go(activity, callback) {
                convertFunc("common.go", { activity: activity }, callback)
            },
            load(url, callback) {
                convertFunc("common.load", { url: url }, callback)
            }
        },
        toast: {
            error(text, callback) {
                convertFunc("toast.error", { text: text }, callback)
            },
            warning(text, callback) {
                convertFunc("toast.warning", { text: text }, callback)
            },
            info(text, callback) {
                convertFunc("toast.info", { text: text }, callback)
            },
            normal(text, callback) {
                convertFunc("toast.normal", { text: text }, callback)
            },
            success(text, callback) {
                convertFunc("toast.success", { text: text }, callback)
            },
        },
        loading: {
            show(callback) {
                convertFunc("loading.show", {}, callback)
            },
            hide(callback) {
                convertFunc("loading.hide", {}, callback)
            },
            cancelable(cancelable, callback) {
                convertFunc("loading.cancelable", { cancelable: cancelable }, callback)
            }
        },
        smartrefresh: {
            enableRefresh(enable, callback) {
                convertFunc("smartrefresh.enableRefresh", { enable: enable }, callback)
            },
            enableLoadmore(enable, callback) {
                convertFunc("smartrefresh.enableLoadmore", { enable: enable }, callback)
            },
            color(primaryColor, accentColor, callback) {
                convertFunc("smartrefresh.color", { primaryColor: primaryColor, accentColor: accentColor }, callback)
            },
            header(header, callback) {
                convertFunc("smartrefresh.header", { header: header }, callback)
            },
            footer(footer, callback) {
                convertFunc("smartrefresh.footer", { footer: footer }, callback)
            }
        }
    };

}