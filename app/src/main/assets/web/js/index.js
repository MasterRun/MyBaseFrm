
ajs_config.open_vconsole = true;

function onEnvReady() {
    ajs.common.localpic("/storage/emulated/0/ADM/1606142160956.jpg", function (data) {
        $("#myimg").attr("src", data["path"].toString());
    });
    //设置topbar背景颜色
    ajs.topbar.bgcolor("#EE7AE9", function (data) {
        console.log("topbar.bgcolor success")
    }, function (msg, data) {
        console.log("topbar.bgcolor failed")
    });

    //使用原始方法，设置topbar背景颜色
    /*    myBridge.callHandler('topbar.bgcolor', { color: "#EE7AE9" }, function (responseData) {
            var data = JSON.parse(responseData)
            if (data['result'].toString() == "1") {
                console.log("topbar.bgcolor success")
            } else {
                console.log("topbar.bgcolor failed")
            }
        })*/

    //隐藏topbar  参数  true/false
    /*ajs.topbar.hide(true, function (result) {
        console.log(typeof result)
        console.log(result)
    });*/

    //设置topbar文字  参数都是可选
    ajs.topbar.title({
        text: '设置title',
        color: '#E8E8E8',
        size: 22
    }, function (data) {
        console.log("topbar.title success")
    }, function (message, data) {
        console.log("topbar.title failed")
    });

    //设置状态栏字体颜色   参数    1:白色   其他参数 ：黑色
    ajs.topbar.statusbar(0, function (data) {
        console.log("topbar.statusbar success")
    }, function (msg, data) {
        console.log("topbar.statusbar failed")
    })

}

function backclick() {
    //返回键
    /*ajs.common.back(function (data) {
        console.log("common.back success")
    }, function (msg, data) {
        console.log("common.back failed")
    });*/
    ajs.common.back();

}

function statusbarHeight() {
    ajs.topbar.statusbarHeight(function (data) {
        ajs.toast.info("状态栏高度:" + data['height'])
    }, function (msg, data) {
        console.log("error:" + msg)
    })
}

function dialogclick() {

    //消息弹窗 参数都是可选
    ajs.common.messagedialog({
        title: '提示',
        message: "这是一个提示",
        // cancelable: false,
        action1: "取消",
        method1: "dialogaction1",
        params1: JSON.stringify({ param1: "aaa", param2: "bbb" }),
        action2: "确定",
        action2mode: 1,
        method2: "dialogaction2",
        params2: "params222"
    }, function (data) {
        console.log("common.dialog success")
    }, function (msg, data) {
        console.log("common.dialog failed")
    });

}

/**
 * toast  参数为text
 */
function toastclick() {
    var toastSuccess = function (data) {
        console.log("toast success")
    };
    var toastError = function (msg, data) {
        console.log("toast failed")
    };
    var type = $("#toasttype").val();
    switch (type) {
        case "2":
            ajs.toast.warning("warning toast", toastSuccess, toastError);
            break;
        case "3":
            ajs.toast.info("info toast", toastSuccess, toastError);
            break;
        case "4":
            ajs.toast.normal("normal toast", toastSuccess, toastError);
            break;
        case "5":
            ajs.toast.success("success toast", toastSuccess);
            break;
        default:
            ajs.toast.error("error toast");
    }

}

function loadingclick() {
    /*ajs.loading.show(function (data) {
        console.log("loading success")
    }, function (msg, data) {
        console.log("loading failed")
    });*/
    ajs.loading.cancelable(true)
    ajs.loading.show();
}

function loadurl() {
    // ajs.common.load({url:"https://www.baidu.com"})
    console.log("load url")
    ajs.common.load({
        url: "file:///android_asset/web/index.html"
    }, function (data) {
        console.log("success" + data)
    }, function (msg, data) {
        console.log("failed" + msg)
    }
    )
    console.log("load url finish")
}
function goactivity() {
    ajs.common.go("com.jsongo.app.ui.mypage.MyPageActivity")
}

let isEnableRefresh = true

function enableRefresh() {
    isEnableRefresh = !isEnableRefresh
    ajs.smartrefresh.enableRefresh(isEnableRefresh)
}

let isEnableLoadmore = false

function enableLoadmore() {
    isEnableLoadmore = !isEnableLoadmore
    ajs.smartrefresh.enableLoadmore(isEnableLoadmore)
}

function refreshColor() {
    ajs.smartrefresh.color('#db8fb1', '#000000')
}

function refreshHeader() {
    ajs.smartrefresh.header(smartrefresh.header.MaterialHeader)
}

function refreshFooter() {
    ajs.smartrefresh.footer(smartrefresh.footer.BallPulseFooter)
}

function showpic() {
    var urls = [
        "http://img5.imgtn.bdimg.com/it/u=3300305952,1328708913&fm=26&gp=0.jpg",
        "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=508387608,2848974022&fm=27&gp=0.jpg",
        "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3893146502,314297687&fm=27&gp=0.jpg",
    ]
    ajs.common.showpic(urls, 0)
}

function putCache() {
    ajs.cache.put("h5Cache", "这是我的缓存内容");
}

function getCache() {
    ajs.cache.get("h5Cache", function (data) {
        ajs.toast.normal(data["value"])
    })
}

function scan() {
    ajs.common.scan(107, function (data) {
        console.log(data)
        ajs.common.load({
            url: data["data"]
        })
    })
}

function selectimg() {
    ajs.file.selectImg({
        count: 4,
        showCamera: 1,
        requestCode: 302,
        selectedPaths: JSON.stringify(["/storage/emulated/0/ADM/face1.jpg", "/storage/emulated/0/ADM/face2.jpg"])
    }, function (data) {
        console.log(data["paths"])
    })
}

function requestFile() {
    $.ajax({
        // url: "http://android_file/storage/emulated/0/ADM/adbd_downcc.apk",
        // url: "http://android_file/storage/emulated/0/ADM/hello.apk",
        url: "http://android_file/storage/emulated/0/ADM/face1.jpg",
        method: 'post',
        success(data) {
            console.log(typeof (data))
            console.log(data.length)
            // $.ajax({
            //     url: "http://submit_file",
            //     method: 'post',
            //     //提交文件测试
            // })
        }
    })
}

function filebase64() {
    //    ajs.file.base64("/sdcard/ADM/face1.jpg", function (data) {
    ajs.file.base64("/sdcard/ADM/PrinterShare-11.22.8.apk", function (data) {
        console.log("get file base64 success")
    }, function (msg, data) {
        console.log("get file base64 error,message:" + msg + " ,base64:" + data["base64"].length)
    })
}

//这这里将js方法注册给原生调用
function regHandler() {

    //原始注册方法
    /*    WebViewJavascriptBridge.registerHandler("dialogaction1", function (data, responseCallback) {
            console.log("dialogaction1 params:" + JSON.parse(data));
            ajs.toast.normal("action1");
            var responseData = "response from js on dialogaction1";
            responseCallback(responseData);
        });*/

    //注册方法
    ajs.regHandler("dialogaction1", dialogaction1);
    ajs.regHandler("dialogaction2", dialogaction2);
}

function dialogaction1(data, responseCallback) {
    console.log("dialogaction1 params:" + JSON.stringify(data));
    ajs.toast.normal("action1");
    var responseData = "response from js on dialogaction1";
    responseCallback(responseData);
}

function dialogaction2(data, responseCallback) {
    console.log("dialogaction2 params:" + JSON.stringify(data));
    ajs.toast.normal("action2");
    var responseData = "response from js on dialogaction2";
    responseCallback(responseData);
}