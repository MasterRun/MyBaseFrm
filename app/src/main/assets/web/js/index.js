function onEnvReady() {
    ajs.common.localpic("/storage/emulated/0/Pictures/Screenshots/1444875167806.png", function (data) {
        $("#myimg").attr("src", data["path"].toString());
    });
    //设置topbar背景颜色
    ajs.topbar.bgcolor({ color: "#EE7AE9" }, function (data) {
        if (data['result'].toString() == "1") {
            console.log("topbar.bgcolor success")
        } else {
            console.log("topbar.bgcolor failed")
        }
    });

    //隐藏topbar  参数  true/false
    /*ajs.topbar.hide({hide:true}, function (responseData) {
        console.log(typeof responseData)
        console.log(responseData)
    });*/

    //设置topbar文字  参数都是可选
    ajs.topbar.title({
        text: '设置title',
        color: '#E8E8E8',
        size: 22
    }, function (data) {
        if (data['result'].toString() == "1") {
            console.log("topbar.title success")
        } else {
            console.log("topbar.title failed")
        }
    });

    //设置状态栏字体颜色   参数  mode  1:白色  other：黑色
    ajs.topbar.statusbar({ mode: 0 }, function (data) {
        if (data['result'].toString() == "1") {
            console.log("topbar.statusbar success")
        } else {
            console.log("topbar.statusbar failed")
        }
    })
}

function backclick() {
    //返回键
    /*ajs.common.back(function (data) {
        if (data['result'].toString() == "1") {
            console.log("common.back success")
        } else {
            console.log("common.back failed")
        }
    });*/
    ajs.common.back();
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
        action2mode: 0,
        method2: "dialogaction2",
        params2: "params222"
    }, function (data) {
        if (data['result'].toString() == "1") {
            console.log("common.dialog success")
        } else {
            console.log("common.dialog failed")
        }
    })
}

/**
 * toast  参数为text
 */
function toastclick() {
    var toastcallback = function (data) {
        if (data['result'].toString() == "1") {
            console.log("toast success")
        } else {
            console.log("toast failed")
        }
    };
    var type = $("#toasttype").val();
    switch (type) {
        case "2":
            ajs.toast.warning("warning toast", toastcallback);
            break;
        case "3":
            ajs.toast.info("info toast", toastcallback);
            break;
        case "4":
            ajs.toast.normal("normal toast", toastcallback);
            break;
        case "5":
            ajs.toast.success("success toast", toastcallback);
            break;
        default:
            ajs.toast.error("error toast");
    }

}

function loadingclick() {
    /*ajs.loading.show(function (data) {
        if(data['result']=='1'){
            consol  e.log("loading success")
        }else{
            console.log("loading failed")
        }
    });*/
    //只有参入false，才是取消loading
    ajs.loading.cancelable(true)
    ajs.loading.show();
}

function loadurl() {
    // ajs.common.load("https://www.baidu.com")
    ajs.common.load("file:///android_asset/web/index.html")
}
function goactivity() {
    ajs.common.go("com.jsongo.mybasefrm.MainActivity")
}

function enableRefresh(){
    ajs.smartrefresh.enableRefresh(false)
}

function enableLoadmore(){
    ajs.smartrefresh.enableLoadmore(true)
}

function regHandler() {
    WebViewJavascriptBridge.registerHandler("dialogaction1", function (data, responseCallback) {
        console.log("dialogaction1 params:" + JSON.parse(data));
        ajs.toast.normal("action1");
        var responseData = "response from js on dialogaction1";
        responseCallback(responseData);
    });
    WebViewJavascriptBridge.registerHandler("dialogaction2", function (data, responseCallback) {
        console.log("dialogaction2 params:" + data);
        ajs.toast.normal("action2");
        var responseData = "response from js on dialogaction2";
        responseCallback(responseData);
    });
}