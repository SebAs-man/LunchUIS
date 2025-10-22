package co.edu.uis.lunchuis.common.enums;

/**
 * Represents the status of a combo within the system. The ComboStatus
 * enum defines three possible states:
 * - AVAILABLE: Indicates that the combo is available for purchase.
 * - SOLD_OUT: Indicates that the combo is no longer available due to being sold out.
 * - DISABLE: Indicates that the combo has been disabled and is not available for purchase.
 * This enumeration is used primarily to manage and indicate the availability or
 * state of a combo in the system.
 */
public enum ComboStatus {
    AVAILABLE,
    SOLD_OUT,
    DISABLE
}
