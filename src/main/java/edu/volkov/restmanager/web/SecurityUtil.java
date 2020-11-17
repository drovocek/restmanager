package edu.volkov.restmanager.web;

public class SecurityUtil {

    private static final int DEFAULT_CALORIES_PER_DAY = 1000;
    private static int id = 0;

    private SecurityUtil() {
    }

    public static int authUserId() {
        return id;
    }

    public static void setAuthUserId(int id) {
        SecurityUtil.id = id;
    }

    public static int authUserCaloriesPerDay() {
        return DEFAULT_CALORIES_PER_DAY;
    }

    public static boolean isAdmin() {
        return id == 1;
    }
}