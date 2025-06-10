package com.argo.emspcardaccountservice.domain.enums;

public enum CardStatus {
    CREATED, ASSIGNED, ACTIVATED, DEACTIVATED;

    public static boolean isValidTransition(CardStatus currentStatus, CardStatus newStatus) {
        switch (currentStatus) {
            case CREATED:
                return newStatus == ASSIGNED;
            case ASSIGNED:
                return newStatus == ACTIVATED || newStatus == CREATED;
            case ACTIVATED:
                return newStatus == DEACTIVATED;
            case DEACTIVATED:
                return newStatus == ACTIVATED;
            default:
                return false;
        }
    }
}
