package com.argo.emspcardaccountservice.domain.enums;

public enum AccountStatus {
    CREATED, ACTIVATED, DEACTIVATED;
    public static boolean isValidTransition(AccountStatus currentStatus, AccountStatus newStatus) {
        switch (currentStatus) {
            case CREATED:
                return newStatus == ACTIVATED;
            case ACTIVATED:
                return newStatus == DEACTIVATED;
            case DEACTIVATED:
                return newStatus == ACTIVATED; // 允许从DEACTIVATED重新激活
            default:
                return false; // 默认不允许其他状态转换
        }
    }
}
