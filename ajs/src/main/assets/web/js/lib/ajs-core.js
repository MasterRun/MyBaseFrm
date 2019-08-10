let myBridge;
/**
 * 核心对象
 */
let ajs = {};

$(function () {
    //连接Android webview
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

    //核心对象
    ajs = {
        regHandler(methodname, callback) {
            WebViewJavascriptBridge.registerHandler(methodname, callback);
        },

        //以下api callback参数不需要可以不传

        //顶部标题栏
        topbar: {
        	/**
        	 * 背景颜色  color 是 '#000000'  这样的颜色值 注意！ #后必须是6位
        	 */
            bgcolor(color, callback) {
                convertFunc('topbar.bgcolor', { color: color }, callback)
            },
            /**
             * 隐藏标题栏
             * 参数   true  隐藏
             * 参数   false 显示
             */
            hide(hide, callback) {
                convertFunc('topbar.hide', { hide: hide }, callback)
            },

            /**
             * 标题栏的文字
             * 参数 ：  以下参数为可选
             {
                  text: '设置title', //标题文字
                  color: '#E8E8E8', //标题字体颜色
                  size: 22	      //标题字体大小
              }
             */
            title(params, callback) {
                convertFunc('topbar.title', params, callback)
            },
            /**
             * 设置状态栏
             * 参数   1  状态栏字体为白色
             * 		其余参数  状态栏字体为黑色
             */
            statusbar(mode, callback) {
                convertFunc('topbar.statusbar', { mode: mode }, callback)
            }
        },
        /**
         * 一些通用方法
         */
        common: {
            //模拟Android的返回键
            back(callback) {
                convertFunc("common.back", {}, callback)
            },


            /**
             * 消息弹窗
             *
                {
                    title: '提示', //弹窗标题
                    message: "这是一个提示",//弹窗文本内容
                    // cancelable: false,//点击弹窗外部是否可取消
                    action1: "取消", //弹窗按钮1的文字
                    action1mode: 0, //弹窗按钮1的样式  传值1，字体为红色，默认为0
                    method1: "dialogaction1",//弹窗按钮1的回调方法
                    params1: JSON.stringify({ param1: "aaa", param2: "bbb" }), //弹窗按钮1 的回调方法的参数，确保是字符串
                    action2: "确定",
                    action2mode: 0,
                    method2: "dialogaction2",
                    params2: "params222"
                }
             */
            messagedialog(params, callback) {
                convertFunc('common.messagedialog', params, callback)
            },

            /**
             * 获取可直接加载的Android图片路径
             * @param {String} path Android本地图片路径
             * @param {function} callback  callback 回调的参数中data['path'] 值是可以直接加载的图片url
             */
            localpic(path, callback) {
                convertFunc("common.localpic", { path: path }, callback)
            },

            /**
             * 调用原生图片预览
             * @param {array} urls   图片的url数组   ['url1','url2','url3']
             * @param {int} index  打开时显示的图片下标
             * @param {function} callback  回调
             */
            showpic(urls, index, callback) {
                convertFunc("common.showpic", { urls: JSON.stringify(urls), index: index }, callback)
            },

            /**
             * 跳转Android原生页面
             * @param {String} activity  原生提供的原生页面路径
             * @param {function} callback
             */
            go(activity, callback) {
                convertFunc("common.go", { activity: activity }, callback)
            },

            /**
             * 开启新的页面打开h5页面
             * @param {String} url  h5页面路径
             * @param {*} callback
             */
            load(url, callback) {
                convertFunc("common.load", { url: url }, callback)
            }
        },

        //toast 吐司
        toast: {
            //错误 红色吐司
            error(text, callback) {
                convertFunc("toast.error", { text: text }, callback)
            },
            //警告 黄色吐司
            warning(text, callback) {
                convertFunc("toast.warning", { text: text }, callback)
            },
            //提示 蓝色吐司
            info(text, callback) {
                convertFunc("toast.info", { text: text }, callback)
            },
            //普通 黑色吐司
            normal(text, callback) {
                convertFunc("toast.normal", { text: text }, callback)
            },
            //成功 绿色吐司
            success(text, callback) {
                convertFunc("toast.success", { text: text }, callback)
            },
        },
        //加载的弹窗
        loading: {
            //显示
            show(callback) {
                convertFunc("loading.show", {}, callback)
            },
            //隐藏
            hide(callback) {
                convertFunc("loading.hide", {}, callback)
            },
            //是否可取消    true/false
            cancelable(cancelable, callback) {
                convertFunc("loading.cancelable", { cancelable: cancelable }, callback)
            }
        },
        //原生的刷新
        smartrefresh: {
            //是否启用下拉刷新  默认启用
            enableRefresh(enable, callback) {
                convertFunc("smartrefresh.enableRefresh", { enable: enable }, callback)
            },
            //是否启用加载更多 默认关闭
            enableLoadmore(enable, callback) {
                convertFunc("smartrefresh.enableLoadmore", { enable: enable }, callback)
            },
            /**
             * 设置刷新的颜色
             * @param {String} primaryColor  刷新的背景色
             * @param {String} accentColor  刷新的前景色
             * @param {function} callback
             */
            color(primaryColor, accentColor, callback) {
                convertFunc("smartrefresh.color", { primaryColor: primaryColor, accentColor: accentColor }, callback)
            },
            //设置刷新头样式  参数：smartrefresh.header.xxx
            header(header, callback) {
                convertFunc("smartrefresh.header", { header: header }, callback)
            },
            //设置加载更所样式  参数：smartrefresh.footer.xxx
            footer(footer, callback) {
                convertFunc("smartrefresh.footer", { footer: footer }, callback)
            }
        }
    };


    /**
     * 刷新相关 包括刷新header 和  刷新footer样式
     */
    let smartrefresh = {
        /**
         * 刷新header样式可选
         */
        header: {
            DeliveryHeader: 'DeliveryHeader',//气球
            DropBoxHeader: 'DropBoxHeader',//盒子
            BezierRadarHeader: 'BezierRadarHeader',//贝塞尔雷达 颜色更换异常！ 不建议是使用
            BezierCircleHeader: 'BezierCircleHeader',//贝塞尔圆圈 颜色可改
            FlyRefreshHeader: 'FlyRefreshHeader',// 纸飞机 无效果！
            ClassicsHeader: 'ClassicsHeader',// 经典刷新 颜色可改
            PhoenixHeader: 'PhoenixHeader',// 金色校园 颜色不建议更改
            TaurusHeader: 'TaurusHeader',// 飞机冲上云霄 颜色不建议更改
            FunGameBattleCityHeader: 'FunGameBattleCityHeader',// 战争城市游戏 颜色不建议更改
            FunGameHitBlockHeader: 'FunGameHitBlockHeader',// 打砖块游戏 颜色不建议更改
            WaveSwipeHeader: 'WaveSwipeHeader',// 全屏水波 颜色可更改
            MaterialHeader: 'MaterialHeader',// material 颜色不可更改
            StoreHouseHeader: 'StoreHouseHeader',// .initWithString("loading...") // StoreHouse 颜色可更改 内容只可设置英文
            WaterDropHeader: 'WaterDropHeader',// 水滴 颜色可更改
        },
        /**
         * 刷新footer样式可选
         */
        footer: {
            BallPulseFooter: 'BallPulseFooter',   //球脉冲  颜色可改
            ClassicsFooter: 'ClassicsFooter'//经典加载更多  颜色不可改
        }
    };

}