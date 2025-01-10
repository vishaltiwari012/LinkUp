package com.cw.linkedin.notification.auth;

public class UserContextHolder {

    public static final ThreadLocal<Long> currentUser = new ThreadLocal<>();
    public static Long getCurrentUserId() {
        return currentUser.get();
    }

    public static void setCurrentUserId(Long userId) {
        currentUser.set(userId);
    }

    static void clear() {
        currentUser.remove();
    }
}
