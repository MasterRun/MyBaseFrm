package com.jsongo.core_mini.util;

import android.text.TextUtils;
import android.util.Log;

import com.jsongo.core_mini.CoreMini;

import java.util.List;
import java.util.Locale;

import kotlin.text.StringsKt;

/**
 * @author jsongo
 * @date 2019/12/15 14:51
 * @desc logcat 输出工具
 */
public class LogcatUtil {

    /**
     * 控制是否开启日志
     */
    private static boolean openLog = CoreMini.isDebug();

    /**
     * 得到tag  (ClazzName.java)/ThreadName
     *
     * @return
     */
    private static String generateTag() {
        int index = 3;
        String callerClazzName = "";
        Thread thread = Thread.currentThread();
        StackTraceElement[] stackTraces = thread.getStackTrace();
        StackTraceElement stackTraceElement;
        do {
            stackTraceElement = stackTraces[index];
            callerClazzName = stackTraceElement.getClassName();
            callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
            index++;
        } while (TextUtils.equals(LogcatUtil.class.getSimpleName(), callerClazzName));
        String tag = "(%s:%s)/%s";
        String name = thread.getName();
        tag = String.format(Locale.CHINA, tag, stackTraceElement.getFileName(), stackTraceElement.getLineNumber(), name);
        return tag;
    }

    //region verbose级别的日志

    /**
     * 打印 verbose 级别的日志
     *
     * @param msg 日志信息
     */
    public static void v(String msg) {
        v("", msg);
    }


    public static void v_json(String jsonStr) {
        String s = StringUtilKt.formatJsonStr(jsonStr);
        List<String> lines = StringsKt.lines(s);
        for (String line : lines) {
            v(line);
        }
    }

    public static void v(Object object) {
        if (object != null) {
            v_json(object.toString());
        }
    }

    /**
     * 打印 verbose 级别的日志
     *
     * @param tag tag
     * @param msg 日志信息
     */
    public static void v(String tag, String msg) {
        v(tag, msg, null);
    }

    /**
     * 打印 verbose 级别的日志
     *
     * @param msg 日志信息
     * @param tr  throwable对象
     */
    public static void v(String msg, Throwable tr) {
        v("", msg, tr);
    }

    /**
     * 打印 verbose 级别的日志
     *
     * @param tag tag
     * @param msg 日志信息
     * @param tr  throwable对象
     */
    public static void v(String tag, String msg, Throwable tr) {
        if (openLog) {
            if (msg == null) {
                msg = "";
            }
            if (TextUtils.isEmpty(tag)) {
                tag = generateTag();
            }
            if (tr == null) {
                Log.v(tag, msg);
            } else {
                Log.v(tag, msg, tr);
            }
        }
    }
    //endregion

    //region debug级别的日志

    /**
     * 打印
     * debug 级别的日志
     *
     * @param msg 日志信息
     */
    public static void d(String msg) {
        d("", msg);
    }

    public static void d_json(String jsonStr) {
        String s = StringUtilKt.formatJsonStr(jsonStr);
        List<String> lines = StringsKt.lines(s);
        for (String line : lines) {
            d(line);
        }
    }

    public static void d(Object object) {
        if (object != null) {
            d_json(object.toString());
        }
    }

    /**
     * 打印 debug 级别的日志
     *
     * @param tag tag
     * @param msg 日志信息
     */
    public static void d(String tag, String msg) {
        d(tag, msg, null);
    }

    /**
     * 打印 debug 级别的日志
     *
     * @param msg 日志信息
     * @param tr  throwable对象
     */
    public static void d(String msg, Throwable tr) {
        d("", msg, tr);
    }

    /**
     * 打印 debug 级别的日志
     *
     * @param tag tag
     * @param msg 日志信息
     * @param tr  throwable对象
     */
    public static void d(String tag, String msg, Throwable tr) {
        if (openLog) {
            if (msg == null) {
                msg = "";
            }
            if (TextUtils.isEmpty(tag)) {
                tag = generateTag();
            }
            if (tr == null) {
                Log.d(tag, msg);
            } else {
                Log.d(tag, msg, tr);
            }
        }
    }
    //endregion

    //region info级别的日志

    /**
     * 打印 info 级别的日志
     *
     * @param msg 日志信息
     */
    public static void i(String msg) {
        i("", msg);
    }

    public static void i_json(String jsonStr) {
        String s = StringUtilKt.formatJsonStr(jsonStr);
        List<String> lines = StringsKt.lines(s);
        for (String line : lines) {
            i(line);
        }
    }

    public static void i(Object object) {
        if (object != null) {
            i_json(object.toString());
        }
    }

    /**
     * 打印 info 级别的日志
     *
     * @param tag tag
     * @param msg 日志信息
     */
    public static void i(String tag, String msg) {
        i(tag, msg, null);
    }

    /**
     * 打印 info 级别的日志
     *
     * @param msg 日志信息
     * @param tr  throwable对象
     */
    public static void i(String msg, Throwable tr) {
        i("", msg, tr);
    }

    /**
     * 打印 info 级别的日志
     *
     * @param tag tag
     * @param msg 日志信息
     * @param tr  throwable对象
     */
    public static void i(String tag, String msg, Throwable tr) {
        if (openLog) {
            if (msg == null) {
                msg = "";
            }
            if (TextUtils.isEmpty(tag)) {
                tag = generateTag();
            }
            if (tr == null) {
                Log.i(tag, msg);
            } else {
                Log.i(tag, msg, tr);
            }
        }
    }
    //endregion

    //region warn级别的日志

    /**
     * 打印 warn 级别的日志
     *
     * @param msg 日志信息
     */
    public static void w(String msg) {
        w("", msg);
    }

    public static void w_json(String jsonStr) {
        String s = StringUtilKt.formatJsonStr(jsonStr);
        List<String> lines = StringsKt.lines(s);
        for (String line : lines) {
            w(line);
        }
    }

    public static void w(Object object) {
        if (object != null) {
            w_json(object.toString());
        }
    }

    /**
     * 打印 warn 级别的日志
     *
     * @param tag tag
     * @param msg 日志信息
     */
    public static void w(String tag, String msg) {
        w(tag, msg, null);
    }

    /**
     * 打印 warn 级别的日志
     *
     * @param msg 日志信息
     * @param tr  throwable对象
     */
    public static void w(String msg, Throwable tr) {
        w("", msg, tr);
    }

    /**
     * 打印 warn 级别的日志
     *
     * @param tag tag
     * @param msg 日志信息
     * @param tr  throwable对象
     */
    public static void w(String tag, String msg, Throwable tr) {
        if (openLog) {
            if (msg == null) {
                msg = "";
            }
            if (TextUtils.isEmpty(tag)) {
                tag = generateTag();
            }
            if (tr == null) {
                Log.w(tag, msg);
            } else {
                Log.w(tag, msg, tr);
            }
        }
    }
    //endregion

    //region error 级别的日志

    /**
     * 打印 error 级别的日志
     *
     * @param msg 日志信息
     */
    public static void e(String msg) {
        e("", msg);
    }

    public static void e_json(String jsonStr) {
        String s = StringUtilKt.formatJsonStr(jsonStr);
        List<String> lines = StringsKt.lines(s);
        for (String line : lines) {
            e(line);
        }
    }

    public static void e(Object object) {
        if (object != null) {
            e_json(object.toString());
        }
    }

    /**
     * 打印 error 级别的日志
     *
     * @param tag tag
     * @param msg 日志信息
     */
    public static void e(String tag, String msg) {
        e(tag, msg, null);
    }

    /**
     * 打印 error 级别的日志
     *
     * @param msg 日志信息
     * @param tr  throwable对象
     */
    public static void e(String msg, Throwable tr) {
        e("", msg, tr);
    }

    /**
     * 打印 error 级别的日志
     *
     * @param tag tag
     * @param msg 日志信息
     * @param tr  throwable对象
     */
    public static void e(String tag, String msg, Throwable tr) {
        if (openLog) {
            if (msg == null) {
                msg = "";
            }
            if (TextUtils.isEmpty(tag)) {
                tag = generateTag();
            }
            if (tr == null) {
                Log.e(tag, msg);
            } else {
                Log.e(tag, msg, tr);
            }
        }
    }
    //endregion

}
