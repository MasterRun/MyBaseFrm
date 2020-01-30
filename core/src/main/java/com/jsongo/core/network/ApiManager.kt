package com.jsongo.core.network

import com.google.gson.*
import com.jsongo.core.BaseCore
import com.jsongo.core.R
import com.jsongo.core.constant.ConstConf
import com.safframework.log.okhttp.LoggingInterceptor
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.lang.reflect.Type
import java.sql.Timestamp
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.HashMap


/**
 * @author  jsongo
 * @date 2019/3/26 15:54
 */
object ApiManager {

    private val TIMEOUT = BaseCore.context.resources.getInteger(R.integer.network_time_out).toLong()
    private var gson: Gson? = null

    /**
     * OkHttpCLient集合
     * key : isAddAuth（true/false）
     */
    private val okHttpClientPool = HashMap<String, OkHttpClient>()
    /**
     * Retrofit集合
     * key : BaseUrl_OkHttpClientKey
     */
    private val retrofitPool = HashMap<String, Retrofit>()

    /**
     * apiService集合
     * key : apiServiceName_RetrofitKey
     */
    private val apiServicePool = HashMap<String, Any>()

    /**
     * 获取ApiService
     */
    fun <T : Any> createApiService(clazz: Class<T>, isAddAuth: Boolean = true): T {
        val okHttpClientPair = createOkHttpClient(isAddAuth)
        val retrofitPair = createRetrofit(okHttpClientPair)

        val key = "${clazz.name}_${retrofitPair.first}"
        val cachedApiService = apiServicePool[key]
        val apiService: T
        if (cachedApiService == null) {
            apiService = retrofitPair.second.create(clazz)
            apiServicePool[key] = apiService
        } else {
            apiService = cachedApiService as T
        }
        return apiService
    }

    /**
     * 获取APIService，不带身份认证
     */
    fun <T : Any> createApiServiceWithoutAuth(clazz: Class<T>): T {
        return createApiService(clazz, false)
    }

    @Synchronized
    fun createOkHttpClient(isAddAuth: Boolean): Pair<String, OkHttpClient> {
        //key
        val key = isAddAuth.toString()
        //从缓存中获取
        var okHttpClient = okHttpClientPool[key]
        //为空则创建
        if (okHttpClient == null) {
            okHttpClient = OkHttpClient.Builder()
                .connectTimeout(TIMEOUT, TimeUnit.SECONDS)  //连接超时设置
                .readTimeout(TIMEOUT, TimeUnit.SECONDS)  //写入缓存超时10s
                .writeTimeout(TIMEOUT, TimeUnit.SECONDS)  //读取缓存超时10s
                .retryOnConnectionFailure(true)  //失败重连
                .addInterceptor(HeaderInterceptor(isAddAuth))  //添加header
                .addInterceptor(NetCacheInterceptor())  //添加网络缓存
                .addLogIntercepter()  //日志拦截器
                .setCacheFile()  //网络缓存
                .build()
            okHttpClientPool[key] = okHttpClient
        }
        //返回key 和 OkHttpClient对象
        return Pair(key, okHttpClient)
    }

    @Synchronized
    private fun createRetrofit(
        okHttpClientPair: Pair<String, OkHttpClient>,
        baseUrl: String = ServerAddr.SERVER_ADDRESS
    ): Pair<String, Retrofit> {
        //key
        val key = "${baseUrl}_${okHttpClientPair.first}"
        //从缓存中获取
        var retrofit = retrofitPool[key]
        //为空则创建
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(getGson()))
                .client(okHttpClientPair.second)
                .build()
            //放入缓存
            retrofitPool[key] = retrofit
        }
        //返回key 和 Retrofit对象
        return Pair(key, retrofit!!)
    }

    /**
     *  设置缓存文件路径
     */
    private fun OkHttpClient.Builder.setCacheFile(): OkHttpClient.Builder {
        //设置缓存文件
        val cacheFile = File(ConstConf.HTTP_CACHE_DIR)
        //缓存大小为10M
        val cacheSize = 10 * 1024 * 1024L
        val cache = Cache(cacheFile, cacheSize)
        cache(cache)
        return this
    }

    /**
     * 调试模式下加入日志拦截器
     * @param builder
     */
    private fun OkHttpClient.Builder.addLogIntercepter(): OkHttpClient.Builder {
        if (BaseCore.isDebug) {
            addInterceptor(LoggingInterceptor())
        }
        return this
    }


    fun getGson(): Gson {
        if (gson == null) {
            gson = GsonBuilder()
                //.setDateFormat("yyyy-MM-dd HH:mm:ss SSS")
                .registerTypeAdapter(java.util.Date::class.java, DateSerializer())
                .registerTypeAdapter(java.util.Date::class.java, DateDeserializer())
                .create()
        }
        return gson!!
    }

    internal class DateSerializer : JsonSerializer<Date> {
        override fun serialize(
            src: Date,
            typeOfSrc: Type,
            context: JsonSerializationContext
        ): JsonElement {
            return JsonPrimitive(src.getTime())
        }
    }

    internal class DateDeserializer : JsonDeserializer<Date> {
        @Throws(JsonParseException::class)
        override fun deserialize(
            json: JsonElement,
            typeOfT: Type,
            context: JsonDeserializationContext
        ): Date {
            val asLong = json.asLong
            return Timestamp(asLong)
        }
    }


}