package com.wisdom.util;

import com.wisdom.context.BaseContext;
import com.wisdom.exception.BusinessException;

public class SecurityUtil {

    private SecurityUtil() {}

    public static Long getCurrentUserId() {
        return BaseContext.getCurrentId();
    }

    public static void checkOwnership(Long resourceOwnerId) {
        Long currentUserId = getCurrentUserId();
        if (currentUserId == null) {
            throw BusinessException.unauthorized();
        }
        if (!currentUserId.equals(resourceOwnerId)) {
            throw BusinessException.forbidden();
        }
    }

    public static boolean isOwner(Long resourceOwnerId) {
        Long currentUserId = getCurrentUserId();
        return currentUserId != null && currentUserId.equals(resourceOwnerId);
    }
}
