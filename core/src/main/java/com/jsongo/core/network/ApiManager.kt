package com.jsongo.core.network

import com.google.gson.*
import com.jsongo.core.BaseCore
import com.jsongo.core.R
import com.jsongo.core.util.ConstConf
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
    private var mOkHttpClient: OkHttpClient? = null
    private var mRetrofit: Retrofit? = null

    private val apiServiceCache = HashMap<String, Any>()

    fun <T : Any> createApiService(clazz: Class<T>): T {
        if (mRetrofit == null) {
            initRetrofit()
        }
        val cachedApiService = apiServiceCache[clazz.name]
        val apiService: T
        if (cachedApiService == null) {
            apiService = mRetrofit!!.create(clazz)
            apiServiceCache[clazz.name] = apiService
        } else {
            apiService = cachedApiService as T
        }
        return apiService
    }

    init {
        initOkhttp()
        initRetrofit()
    }

    private fun initOkhttp() {
        mOkHttpClient = OkHttpClient.Builder()
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)  //连接超时设置
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)  //写入缓存超时10s
            .writeTimeout(TIMEOUT, TimeUnit.SECONDS)  //读取缓存超时10s
            .retryOnConnectionFailure(true)  //失败重连
            .addInterceptor(HeaderInterceptor())  //添加header
            .addInterceptor(NetCacheInterceptor())  //添加网络缓存
            .addLogIntercepter()  //日志拦截器
            .setCacheFile()  //网络缓存
            .build()
    }

    private fun initRetrofit() {
        if (mOkHttpClient == null) {
            initOkhttp()
        }
        mRetrofit = Retrofit.Builder()
            .baseUrl(ServerAddr.SERVER_ADDRESS)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(getGson()))
            .client(mOkHttpClient!!)
            .build()

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