package com.jsongo.core.db

import android.content.Context
import com.jsongo.core.BaseCore
import com.jsongo.core.R
import com.tencent.wcdb.database.SQLiteDatabase
import com.tencent.wcdb.database.SQLiteOpenHelper

/**
 * author ： jsongo
 * createtime ： 2019/7/22 17:51
 * desc : 键值存储的common_table，充当本地数据缓存
 */
class CommonDbOpenHelper
private constructor(context: Context) :
    SQLiteOpenHelper(context, DB_NAME, PASSWORD, null, VERSION, null) {

    companion object {

        val PASSWORD = BaseCore.context.getString(R.string.common_db_pwd).toByteArray()
        const val VERSION = 1
        const val DB_NAME = "common"

        val instance: CommonDbOpenHelper? = null
            get() {
                if (field == null) {
                    synchronized(CommonDbOpenHelper::class.java) {
                        if (field == null) {
                            return CommonDbOpenHelper(BaseCore.context)
                        }
                    }
                }
                return field
            }

        val readableDatabase = instance?.readableDatabase
        val writableDatabase = instance?.writableDatabase

        @JvmStatic
        fun setKeyValue(key: String, value: String) {
            writableDatabase?.apply {
                val sql =
                    if (null == getValue(key)) {
                        "insert into common_table(`value`,`key`) values(?,?)"
                    } else {
                        "update common_table set `value`=? where `key`=?"
                    }
                execSQL(sql, arrayOf(value, key))
            }
        }

        @JvmStatic
        fun getValue(key: String): String? {
            var value: String? = null
            readableDatabase?.apply {
                val cursor = rawQuery("select * from common_table where `key`=?", arrayOf(key))
                cursor?.let { c ->
                    while (c.moveToNext()) {
                        value = c.getString(1)
                    }
                }
                cursor?.close()
            }
            return value
        }

        /**
         * 获取所有键值对
         */
        @JvmStatic
        fun getAllDatas(): Map<String, String> {
            val datas = HashMap<String, String>()
            readableDatabase?.apply {
                val cursor = rawQuery("select * from common_table ", arrayOf())
                cursor?.apply {
                    while (moveToNext()) {
                        val key = getString(0)
                        val value = getString(1)
                        datas[key] = value
                    }
                }
                cursor?.close()
            }
            return datas
        }

        /**
         * 删除指定的key
         */
        @JvmStatic
        fun deleteKey(key: String) {
            writableDatabase?.apply {
                execSQL("delete from common_table where `key`=?", arrayOf(key))
            }
        }
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.apply {
            val common_table = "create table if not exists common_table(" +
                    "`key` text not null primary key," +
                    "`value` text  " +
                    ")"
            execSQL(common_table)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }
}