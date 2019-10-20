
### @AjsApi

#### 参数说明

- prefix  前缀，不指定则为类名
- methodName 方法名，不指定则为当前注解的方法名

#### 使用说明

- 不支持写在内部类中
- 必须是静态方法，kotlin中配合@JvmStatic实现静态方法
- 可以写在伴生方法中，配合@JvmStatic注解
- kotlin因为要配合@JvmStatic注解，所以不能作为顶层方法或写在普通类中
- 建议新建类，在其中自定义api

### @Page

用于方便配置页面的mainLayoutId 和 containerId

### @Presenter

用于注入MVP 的View 中的presenter

### @Model

用于注入MVP 的Presenter 中的Model


#### 1.0.0.b-SNAPSHOT
2019-10-20
- 注解初版发布 1.0.0.b

##### 2019-10-15
- 将ConfigPage注解更名为Page

#### 1.0.0.a-SNAPSHOT
2019-9-21
- 注解初版发布 1.0.0.a

##### 2019-9-20 
- 初步完成 @AjsApi  @ConfPage  @Presenter  @Model 注解
