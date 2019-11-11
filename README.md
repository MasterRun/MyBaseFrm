# Android小小小小小框架
个人搭建的Android小小小小小框架 

Building...  尚无demo

demo\app module中用于个人测试

基于`MVVM` `Kotlin` `Retrofit` `RxJava` `APT` `AOP` `javapoet` `QMUI` `JsBridge` `腾讯x5内核` 等搭建

> activity fragment  
  注解+apt 实现 配置布局  
  页面多容器可选  
  简便的状态管理  
  封装设置列表，使用实体类配置  
  封装部分api供js调用原生方法

<b>Android小菜鸟building  不喜勿喷</b>


### todo list

- 添加app首页主体模型
    > 底部导航类型   --  ok
    > 顶部导航类型  
    > 侧滑抽屉类型  

- 登录页
- 首页卡片添加图标和点击事件的设置
- 改造MVVM  --  doing  
- 整合MobileIM即时通讯  --  preparing  
- js与原生交互大量数据传递解决方案（目前只解决原生传递大量数据给js）  -- doing  
- QMUIGroupListView 实现设置页面  -- doing
- 迁移AndroidX
- 考虑使用ARouter解耦       
- 过度绘制及布局优化 -- doing
- 整理混淆规则，尝试开启混淆  --  problem  kotlin-extentions开启混淆导致空指针,组件已开启混淆
- 使用FloatLayoutHelper 封装QMUIFloatLayout
- 封装网络请求返回处理
- 将apimanager改为可配置，支持拦截器（头部缓存，token等），支持多url（缓存retrofit），可配置okhttp缓存等
- 添加flutter混合开发
- 使用依赖注入框架  aop  aspectj   apt  javassist
- topbar添加底部灰色线 --abandon


### complete of todo

- 启动页  --ok 
- BaseFragment  --ok
- 使用自定义注解  --ok
- 扫码按钮  --ok
- 添加vConsole --ok
- 增加缓存ajs api -- ok
- ajs 模块简化原生api编写方法，提供对外自定义api方法 -- ok
    > 考虑使用注解标注api类,使用apt在编译时生成一个类,将所用注解的类放在集合中,在AjsWebloader中注册这些api  -- ok
- SplashActivity点击回到桌面问题（下一跳解决，同时在下一Activity finish SplashActivity）  -- ok
- 使用kotlin观察代理  --  Deprecated  使用MVVM  
- IBaseView 范型调整为P ，尝试解决presenter分离和View的范型问题  --  Deprecated  使用MVVM  
- 注解+apt 实现view的配置 ，解决反射严重耗时问题  -- ok
- fileprovider  --  ok
- easyphoto  提供engine  -- ok
- 使用wcdb  --  ok 
- 提供拍照,选文件图片 base64等ajs api -- ok
- 添加ajsfragment -- ok
- 分离ajs回调,改为 success 和 error 方法分别回调  --  ok
- 抽离ajsWebView 使用ajsWebView 实现ajs api调用  --  ok
- 添加vConsole显示隐藏开关 ajs_config  --  ok
- 添加圆角卡片及扁平的webloader卡片 -- ok
- webview 加载失败使用状态页 https://www.jianshu.com/p/12a011af51c4  --  ok
- 将anno中的PageConfig注解改为Page  --  ok
- 简单封装recyclerview adapter ,点击和长按事件接口  --  ok
- 搜索框  --  ok


### module依赖关系
|模块|被依赖模块|
|---|---|
|annotation|-|
|processor|annotation|
|core|(annotation)|
|ui|core|
|ui-enhance|ui|
|ajs|core ui|
|app/demo|core ajs ui annotation processor|


### 致谢：
- `com.qmuiteam:qmui` 腾讯qmui Android
- 腾讯x5内核
- `com.tencent.wcdb:wcdb-android` 腾讯 wcdb数据库
- `com.github.tamsiree.RxTool`  RxTool,Android开发工具合集
- `com.github.lzyzsd:jsbridge` android js 混合开发jsbridge
- `com.github.HuanTanSheng:EasyPhotos`  Android高仿微信图片选取
- `com.squareup.okhttp3`  okhttp
- `com.squareup.retrofit2` retrofit
- `com.liulishuo.okdownload` 下载库
- `com.github.tbruyelle:rxpermissions` 动态权限申请
- `com.github.bumptech.glide`  glide图片加载
- `com.safframework.log`  使用的日志工具 可配合okhttp拦截器
- `com.scwang.smartrefresh`  强大的刷新
- `com.ycjiang:ImagePreview` 图片预览
- `com.google.zxing`  扫描二维码
- `com.squareup:javapoet` 方便apt创建java文件
- `org.aspectj:aspectjrt` aop aspectjrt
- FlycoBanner_Master 轮播图,引导页