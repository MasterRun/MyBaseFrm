

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