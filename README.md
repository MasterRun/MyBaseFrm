# Android小小小小小框架
个人搭建的Android小小小小小框架 Building...  

还在搭建中，尚无demo，app module中用于个人测试

mvp kotlin retrofit qmui jsbridge 腾讯x5内核 等搭建

activity fragment 注解配置布局，页面多容器可选，简便的状态管理

<b>Android小菜鸟building  不喜勿喷</b>


### todo list
- 启动页  --ok 
- BaseFragment  --ok
- 使用自定义注解  --ok
- 扫码按钮  --ok
- QMUIGroupListView 实现设置页面  -- doing
- 文档错错别字修正
- 增加缓存ajs api
- topbar添加底部灰色线
- 添加ajsfragment
- 将apimanager改为可配置，支持拦截器（头部缓存，token等），支持多url（缓存retrofit），可配置okhttp缓存等
- 使用FloatLayoutHelper 封装QMUIFloatLayout
- 封装网络请求返回处理
- 使用kotlin观察代理
- RecyclerView
- 使用依赖注入框架  aop  aspectj   apt  javassist


### module依赖关系
|模块|被依赖模块|
|---|---|
|app|core ajs ui|
|ajs|core ui|
|ui|core|


### 鸣谢：
- `com.qmuiteam:qmui` 腾讯qmui Android
- 腾讯x5内核
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