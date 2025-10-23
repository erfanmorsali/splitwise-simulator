package com.splitwise.application.statics;

public abstract class Urls {
    // Auth Controller
    public static final String REQUEST_OTP = "request-otp";
    public static final String VERIFY_OTP = "verify-otp";
    public static final String REFRESH = "refresh";

    // Group Controller
    public static final String GROUP = "group";
    public static final String GROUP_ID = "group/{id}";
    public static final String GROUP_INVITE_ACCEPT = "group/{id}/invite/accept";
    public static final String GROUP_INVITE_REJECT = "group/{id}/invite/reject";
}
