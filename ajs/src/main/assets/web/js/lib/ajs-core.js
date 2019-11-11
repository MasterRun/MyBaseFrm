let myBridge;
/**
 * 核心对象
 */
let ajs = {};
let ajs_config = {
    //是否开启vConsole
    open_vconsole: true,
    long_data_code: "9999",
    //长数据分段传输的默认一次长度
    default_part_data_length: 1250000  //125w
};

$(function () {
    if (ajs_config.open_vconsole) {
        try {
            /**
            * 使用vConsole
            */
            window.vConsole = new window.VConsole({
                defaultPlugins: ['system', 'network', 'element', 'storage'], // 可以在此设定要默认加载的面板
                maxLogNumber: 1000,
                // disableLogScrolling: true,
                onReady: function () {
                    console.log('vConsole is ready.');
                },
                onClearLog: function () {
                    console.log('on clearLog');
                }
            });
        } catch (e) {
            console.log(eval)
        }
    }

    //连接Android webview
    connectWebViewJavascriptBridge(function (bridge) {
        bridge.init(function (message, responseCallback) {
        });

        myBridge = bridge;
        regBridgeMethod();
        try {
            regHandler();
        } catch (error) {
            console.log(error)
        }
        try {
            onEnvReady();
        } catch (error) {
            console.log(error)
        }
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
 * 接收长数据
 * @param  data  提示数据
 * @param  success 成功回调
 * @param  error 错误回调
 */
function receiveLongData(dataKey, completeData, length, startIndex, success, error) {
    //获取长数据
    if (startIndex < length) {
        ajs.longDataTransfer.get({
            key: dataKey,
            startIndex: startIndex,
            length: ajs_config.default_part_data_length
        }, function (data) {
            completeData += data["partData"]
            //继续去下一部分数据
            receiveLongData(dataKey, completeData, length, startIndex + ajs_config.default_part_data_length, success, error)
        }, function (msg, data) {
            if (error != undefined) {
                error(msg, data)
            } else {
                console.log("get partData error: dataKey=" + dataKey + ", length=" + length + ", startIndex=" + startIndex)
            }
        })
    } else {
        //长数据传输已完成
        var finalData = JSON.parse(completeData);
        if (finalData['result'] == ajs_config.long_data_code) {
            if (error != undefined) {
                error("完整数据不能使用 result: " + ajs_config.long_data_code + " 标识！", finalData)
            } else {
                console.log("完整数据不能使用 result: " + ajs_config.long_data_code + " 标识！ " + JSON.stringify(data))
            }
        } else {
            //传输完成
            ajs.longDataTransfer.complete(dataKey, function (data) {
                onReceiveData(finalData, success, error)
            }, function (msg, data) {
                if (error != undefined) {
                    error(msg, data)
                } else {
                    console.log("传输数据完成回调失败！ " + JSON.stringify(data))
                }
            })
        }
    }
}

/**
 * 接收到数据
 * @param  data  JSON数据
 */
function onReceiveData(data, success, error) {

    if (data['result'] == "1") {
        if (success != undefined) {
            success(data)
        }
    } else if (data['result'] == ajs_config.long_data_code) {
        var key = data["dataKey"];
        var length = parseInt(data["length"]);
        //进行长数据传输
        receiveLongData(key, "", length, 0, success, error)
    } else {
        if (error != undefined) {
            error(data['message'], data)
        } else {
            console.log("error occur: message:" + data['message'])
        }
    }
}

/**
 * 注册原生方法
 */
function regBridgeMethod() {
    //核心对象
    ajs = {
        //调用原生方法
        callHandler(callname, params, success, error) {
            myBridge.callHandler(callname, params, function (responseData) {
                var data = JSON.parse(responseData)
                onReceiveData(data, success, error)
            })
        },
        //注册方法给原生调用
        regHandler(methodname, callback) {
            WebViewJavascriptBridge.registerHandler(methodname,  callback);
        },

        //以下api success,error为成功回调和错误回调不需要可以不传

        /**
         * 缓存
         */
        cache: {
            put(key, value, success, error) {
                ajs.callHandler("cache.put", { key: key, value: value }, success, error)
            },
            get(key, success, error) {
                ajs.callHandler("cache.get", { key: key }, success, error)
            }
        },

        /**
         * 一些通用方法
         */
        common: {
            //模拟Android的返回键
            back(success, error) {
                ajs.callHandler("common.back", {}, success, error)
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
            messagedialog(params, success, error) {
                ajs.callHandler('common.messagedialog', params, success, error)
            },

            /**
             * 获取可直接加载的Android图片路径
             * @param {String} path Android本地图片路径
             * @param {function} callback  callback 回调的参数中data['path'] 值是可以直接加载的图片url
             */
            localpic(path, success, error) {
                ajs.callHandler("common.localpic", { path: path }, success, error)
            },

            /**
             * 调用原生图片预览
             * @param {array} urls   图片的url数组   ['url1','url2','url3']
             * @param {int} index  打开时显示的图片下标
             * @param {function} callback  回调
             */
            showpic(urls, index, success, error) {
                ajs.callHandler("common.showpic", { urls: JSON.stringify(urls), index: index }, success, error)
            },

            /**
             * 跳转Android原生页面
             * @param {String} activity  原生提供的原生页面路径
             * @param {function} callback
             */
            go(activity, success, error) {
                ajs.callHandler("common.go", { activity: activity }, success, error)
            },

            /**
             * 开启新的页面打开h5页面
             * @param {url:"",bgColor:"#ffffff",fixHeight:true} url  h5页面路径
             * @param {*} callback
             */
            load(params, success, error) {
                ajs.callHandler("common.load", params, success, error)
            },

            /**
             * 开启新的页面打开h5页面
             * @param {String} url  h5页面路径
             * @param {*} callback
             */
            scan(requestCode, success, error) {
                ajs.callHandler("common.scan", { requestCode: requestCode }, success, error)
            }
        },

        //文件相关操作
        file: {
            //选择图片
            selectImg(params, success, error) {
                ajs.callHandler("file.selectImg", params, success, error)
            },
            //获取文件base64
            base64(path, success, error) {
                ajs.callHandler("file.base64", { path: path }, success, error)
            },
            //删除文件
            delete(path, success, error) {
                ajs.callHandler("file.delete", { path: path }, success, error)
            },
        },

        //加载的弹窗
        loading: {
            //显示
            show(success, error) {
                ajs.callHandler("loading.show", {}, success, error)
            },
            //隐藏
            hide(success, error) {
                ajs.callHandler("loading.hide", {}, success, error)
            },
            //是否可取消    true/false
            cancelable(cancelable, success, error) {
                ajs.callHandler("loading.cancelable", { cancelable: cancelable }, success, error)
            }
        },

        //长数据传输
        longDataTransfer: {
            //获取长数据的部分
            get(params, success, error) {
                ajs.callHandler("longDataTransfer.get", params, success, error)
            },
            //传输完成
            complete(key, success, error) {
                ajs.callHandler("longDataTransfer.complete", { key: key }, success, error)
            }
        },

        //原生的刷新
        smartrefresh: {
            //是否启用下拉刷新  默认启用
            enableRefresh(enable, success, error) {
                ajs.callHandler("smartrefresh.enableRefresh", { enable: enable }, success, error)
            },
            //是否启用加载更多 默认关闭
            enableLoadmore(enable, success, error) {
                ajs.callHandler("smartrefresh.enableLoadmore", { enable: enable }, success, error)
            },
            /**
             * 设置刷新的颜色
             * @param {String} primaryColor  刷新的背景色
             * @param {String} accentColor  刷新的前景色
             * @param {function} callback
             */
            color(primaryColor, accentColor, success, error) {
                ajs.callHandler("smartrefresh.color", { primaryColor: primaryColor, accentColor: accentColor }, success, error)
            },
            //设置刷新头样式  参数：smartrefresh.header.xxx
            header(header, success, error) {
                ajs.callHandler("smartrefresh.header", { header: header }, success, error)
            },
            //设置加载更所样式  参数：smartrefresh.footer.xxx
            footer(footer, success, error) {
                ajs.callHandler("smartrefresh.footer", { footer: footer }, success, error)
            }
        },

        //toast 吐司
        toast: {
            //错误 红色吐司
            error(text, success, error) {
                ajs.callHandler("toast.error", { text: text }, success, error)
            },
            //警告 黄色吐司
            warning(text, success, error) {
                ajs.callHandler("toast.warning", { text: text }, success, error)
            },
            //提示 蓝色吐司
            info(text, success, error) {
                ajs.callHandler("toast.info", { text: text }, success, error)
            },
            //普通 黑色吐司
            normal(text, success, error) {
                ajs.callHandler("toast.normal", { text: text }, success, error)
            },
            //成功 绿色吐司
            success(text, success, error) {
                ajs.callHandler("toast.success", { text: text }, success, error)
            },
        },

        //顶部标题栏
        topbar: {
            /**
             * 背景颜色  color 是 '#000000'  这样的颜色值 注意！ #后必须是6位
             */
            bgcolor(color, success, error) {
                ajs.callHandler('topbar.bgcolor', { color: color }, success, error)
            },
            /**
             * 隐藏标题栏
             * 参数   true  隐藏
             * 参数   false 显示
             */
            hide(hide, success, error) {
                ajs.callHandler('topbar.hide', { hide: hide }, success, error)
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
            title(params, success, error) {
                ajs.callHandler('topbar.title', params, success, error)
            },
            /**
             * 设置状态栏
             * 参数   1  状态栏字体为白色
             * 		其余参数  状态栏字体为黑色
             */
            statusbar(mode, success, error) {
                ajs.callHandler('topbar.statusbar', { mode: mode }, success, error)
            },
            /**
            * 获取状态栏高度
            */
            statusbarHeight(success, error) {
                ajs.callHandler('topbar.statusbarHeight', {}, success, error)
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
