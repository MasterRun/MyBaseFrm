package com.jsongo.core.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.jsongo.core.BaseCore

/**
 * author ： jsongo
 * createtime ： 2019/7/22 17:51
 * desc :
 */
class CommonDbOpenHelper
private constructor(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, VERSION) {

    companion object {

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