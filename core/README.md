

#### 1.0.0.g-SNAPSHOT
2019-10-20
- 发布1.0.0.g
- 发布1.0.0.g2

##### 2019-10-20
- 将strings-conf 改为 values-conf
- 将权限从presenter移除放到启动页中(g2)
- 添加崩溃日志可点击文字复制(g3)

##### 2019-10-19
- 将注解的依赖改为implementation
- 简易封装recyclerview adapter 和点击长按事件
- 拷贝MyQMUITopBarLayout  避免反射

##### 2019-10-17
- 更新saf-log

##### 2019-10-14
- 更改BaseFragment的销毁调用位置

#### 1.0.0.f-SNAPSHOT
2019-10-8
- 发布1.0.0.f

##### 2019-9-28
- 添加fileprovider
- 扫描二维码依赖改为ui组件
- 使用wcdb数据库

##### 2019-9-27 
- 打包开启混淆

##### 2019-9-22
- 更新saf-log日志库的版本
- 添加蓝牙权限

#### 1.0.0.e-SNAPSHOT
2019-9-21
- 发布1.0.0.e
- 配合注解 apt 发布 `annotation:1.0.0.a-SNAPSHOT` `processor:1.0.0.b-SNAPSHOT`

##### 2019-9-20
- 完成apt 注解配置和注入，废弃`com.jsongo.core.annotations`
- ActivityCollector添加finish指定activity的方法，可自定finish SplashActivity等

#### 1.0.0.d-SNAPSHOT
2019-8-10
- 发布1.0.0.d
- 修复Android9闪退问题
- 更换扫描扫描二维码组件，修复部分手机扫描二维码无效

#### 1.0.0.c-SNAPSHOT
2019-8-10
- 发布1.0.0.c
- AndroidManifest.xml 中使用app theme

#### 1.0.0.b-SNAPSHOT
2019-8-7
- 发布1.0.0.b lib的versionCode都重置为1
- 与ajs 1.0.0.b  ui 1.0.0.b一起使用，文件目录结构改变，不兼容旧版
- 去除第三容器，不使用容器时直接在在根布局添加view，使用RelativeLayout作为布局根布局
- 封装QMUIGroupListView，使用实体类配置，用于创建设置项
- IPage中inflate方法更变

#### 1.0.0.a-SNAPSHOT
2019-8-4
- 重新发布版本 1.0.0.a