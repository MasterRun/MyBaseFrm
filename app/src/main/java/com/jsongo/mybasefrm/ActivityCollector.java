package com.jsongo.mybasefrm;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jsongo
 * @date 2018/8/16 8:38
 */
public class ActivityCollector {
    private static List<BaseActivity> activities = new ArrayList<>();

    public static List<BaseActivity> getActivities() {
        return activities;
    }

    public static BaseActivity getTopActivity() {
        return activities.get(activities.size() - 1);
    }

    public static void addActivity(BaseActivity activity) {
        activities.add(activity);
    }

    public static void removeActivity(BaseActivity activity) {
        activities.remove(activity);
    }

    public static void finishAll() {
        for (BaseActivity activity : activities) {
            activity.finish();
        }
        activities.clear();
    }
}
