package com.wisdom.util;

import com.wisdom.context.BaseContext;

public class SecurityUtil {

    private SecurityUtil() {}

    public static Long getCurrentUserId() {
        return BaseContext.getCurrentId();
    }

    public static void checkOwnership(Long resourceOwnerId) {
        Long currentUserId = getCurrentUserId();
        if (currentUserId == null) {
            throw new RuntimeException("UNAUTHORIZED");
        }
        if (!currentUserId.equals(resourceOwnerId)) {
            throw new RuntimeException("FORBIDDEN");
        }
    }

    public static boolean isOwner(Long resourceOwnerId) {
        Long currentUserId = getCurrentUserId();
        return currentUserId != null && currentUserId.equals(resourceOwnerId);
    }
}