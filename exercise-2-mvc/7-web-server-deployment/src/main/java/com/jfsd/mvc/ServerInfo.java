package com.jfsd.mvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Model: facts about the server the application is running on.
 */
public class ServerInfo {

    private final String serverName;
    private final String servletApi;
    private final String javaVersion;
    private final String osName;
    private final String contextPath;
    private final String realPath;
    private final LocalDateTime time = LocalDateTime.now();

    public ServerInfo(String serverName, String servletApi, String contextPath, String realPath) {
        this.serverName = serverName;
        this.servletApi = servletApi;
        this.contextPath = contextPath;
        this.realPath = realPath;
        this.javaVersion = System.getProperty("java.version");
        this.osName = System.getProperty("os.name");
    }

    public String getServerName() {
        return serverName;
    }

    public String getServletApi() {
        return servletApi;
    }

    public String getJavaVersion() {
        return javaVersion;
    }

    public String getOsName() {
        return osName;
    }

    public String getContextPath() {
        return contextPath;
    }

    public String getRealPath() {
        return realPath;
    }

    public String getTime() {
        return time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
