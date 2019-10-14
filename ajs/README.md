
<b>ajs api文档在[这里](../doc/ajs.md)</b>

##### 2019-10-14
- 添加vConsole开关
- 完善WebLoaderCard.kt    AJsWebView.kt   AJsWebLoader.kt  的加载监听和错误监听

##### 2019-10-13
- 大改动！！
- 更新腾讯x5内核jar包
- 提取可单独使用ajs api的AJsWebView
- 添加AjsWebViewHost接口，用于在其它的Activity和Fragment中使用ajs api
- 更改所有ajs api参数
- 添加webviewcard 不采用ajswebloader（fragment）实现card

#### 1.0.0.h-SNAPSHOT
2019-10-8
- 发布1.0.0.h

##### 2019-10-4
- 将WebLoader改为fragment
- 添加获取状态栏高度的ajs方法
- AJsWebPage添加状态栏增补高度
- AJsWebPage添加backpress处理
- 更改activity_ajs_webloader.xml布局
- 添加ajswebloader,topbar是否可见参数
- ajs 的js的回调分为 success 和 error 回调方法

##### 2019-9-29
- 添加ajax请求获取本地文件的方法,不建议使用
- 添加ajs 扫描二维码  获取文件base64  删除文件的方法

##### 2019-9-28
- 直接引入com.github.lzyzsd:jsbridge:1.0.4源码
- 去除WebViewJavascriptBridge.js的消息log
- 添加ajs长回调
- 添加ajs选择图片api和文档
- 抽离BaseWebLoader

##### 2019-9-27 
- 打包开启混淆

#### 1.0.0.g-SNAPSHOT
2019-9-21
- 发布1.0.0.g

##### 2019-9-21
- 修复加载h5页面闪退

#### 1.0.0.f-SNAPSHOT
2019-9-21
- 发布1.0.0.f
- 配合注解 apt 发布 `annotation:1.0.0.a-SNAPSHOT` `processor:1.0.0.b-SNAPSHOT`

##### 2019-9-8
- 修改ajs 的 api映射方式
- 使用注解配置自定义ajs api的方式

##### 2019-8-26
- 添加ajs cache相关api，更新文档
- 封装ajscallback用于ajs原生api回调

#### 1.0.0.e-SNAPSHOT
2019-8-14
- 发布1.0.0.e 兼容 core 1.0.0.d  ui 1.0.0.d
- 使用vConcole  添加 js.lib中添加vconsole.min.js  zepto.js
- ajs-core中进行初始化的异常捕获，以防影响后续代码执行
- 将ajs的原生容器更换为第二容器，以防原生与h5页面滑动冲突

#### 1.0.0.d-SNAPSHOT
2019-8-10
- 发布1.0.0.d
- 修复Android9闪退问题
- 更换扫描扫描二维码组件，修复部分手机扫描二维码无效

#### 1.0.0.c-SNAPSHOT
2019-8-10
- 发布1.0.0.c 
- 更改js文件路径
- 与core 1.0.0.c  ui 1.0.0.c一起使用，文件目录结构改变，不兼容旧版

#### 1.0.0.b-SNAPSHOT
2019-8-7
- 发布1.0.0.b
- 与core 1.0.0.b  ui 1.0.0.b一起使用，文件目录结构改变，不兼容旧版

#### 1.0.0.a-SNAPSHOT
2019-8-4
- 重新发布版本