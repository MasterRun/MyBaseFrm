package com.jsongo.mybasefrm;

import android.support.annotation.Nullable;

import java.io.File;
import java.io.Serializable;

/**
 * author ： jsongo
 * createtime ： 2019/7/21 23:45
 * desc :
 */

public class PrintLog implements Serializable {
    public static final int ACTION_COPY = 8196;
    public static final int ACTION_EXTERNAL_APP_SCAN = 4353;
    public static final int ACTION_ID_MEMORY_CARD_COPY_TO_MOBILE_DEVICE = 8194;
    public static final int ACTION_ID_MEMORY_CARD_COPY_TO_PRINTER = 8193;
    public static final int ACTION_PHOTO_TRANSFER = 8195;
    public static final int ACTION_REPEAT_COPY = 8198;
    public static final int ACTION_SCAN = 8197;
    public static final int PREVIEW_TYPE_DOCUMENT = 2;
    public static final int PREVIEW_TYPE_NOT_SET = 0;
    public static final int PREVIEW_TYPE_PHOTO = 1;
    public static final int PREVIEW_TYPE_WEB = 3;
    public static final int PRINT_SOURCE_BOX = 259;
    public static final int PRINT_SOURCE_CAMERACOPY = 5;
    public static final int PRINT_SOURCE_DOCUMENT = 2;
    public static final int PRINT_SOURCE_DROPBOX = 258;
    public static final int PRINT_SOURCE_EVERNOTE = 256;
    public static final int PRINT_SOURCE_EXTERNAL_APP_DOCUMENT = 4098;
    public static final int PRINT_SOURCE_EXTERNAL_APP_PHOTO = 4097;
    public static final int PRINT_SOURCE_EXTERNAL_APP_WEB = 4100;
    public static final int PRINT_SOURCE_GOOGLEDRIVE = 257;
    public static final int PRINT_SOURCE_MYPOCKET = 261;
    public static final int PRINT_SOURCE_ONEDRIVE = 260;
    public static final int PRINT_SOURCE_PHOTO = 1;
    public static final int PRINT_SOURCE_SCAN_PRINT = 4;
    public static final int PRINT_SOURCE_WEB = 3;
    public String callerPackage;
    public String originalFileExtension;
    public int previewType;
    public int uiRoute;

    @Nullable
    public static String getFileExtension(File paramFile) {
        String paramFileName = paramFile.getName();
        int i = paramFileName.lastIndexOf('.');
        if ((i >= 0) && (i < paramFileName.length())) {
            return paramFileName.substring(i + 1).toLowerCase();
        }
        return null;
    }

    @Nullable
    public static String getFileExtension(String paramString) {
        if (paramString == null) {
            return null;
        }
        return getFileExtension(new File(paramString));
    }
}
