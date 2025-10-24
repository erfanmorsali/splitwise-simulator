package com.splitwise.shared.utils.services;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientIpInfoService {
    protected String mainIP;
    protected String proxyIP;

    public ClientIpInfoService(HttpServletRequest request) {
        this.mainIP = request.getHeader("X-FORWARDED-FOR");
        this.proxyIP = request.getRemoteAddr();
        if (this.mainIP == null || this.mainIP.equals("")) {
            this.mainIP = request.getRemoteAddr();
            this.proxyIP = null;
        } else {
            mainIP = mainIP.split(",")[0];
        }
    }

    public static String getMainIP(HttpServletRequest request) {
        String mainIP = request.getHeader("X-FORWARDED-FOR");
        if (mainIP == null || mainIP.equals("")) {
            mainIP = request.getRemoteAddr();
        } else {
            mainIP = mainIP.split(",")[0];
        }
        return mainIP;
    }
}
