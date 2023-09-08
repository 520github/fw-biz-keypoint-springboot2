package org.sunso.keypoint.springboot2.spring.log;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "biz.log.control")
@RefreshScope
public class LogConfig {
    // 是否关闭特定info日志
    private boolean infoLogClose = false;

    // 是否关闭controller请求日志
    private boolean requestLogClose = false;

    // 请求参数超过指定长度不输出日志
    private int closeRequestLogLength = 5000;
    // 返回结果超过指定长度不输出日志
    private int closeResponseLogLength = 5000;

    // 排除的请求url不输出日志
    private String excludeRequestUrl;

    public boolean isInfoLogClose() {
        return infoLogClose;
    }

    public void setInfoLogClose(boolean infoLogClose) {
        this.infoLogClose = infoLogClose;
    }

    public boolean isRequestLogClose() {
        return requestLogClose;
    }

    public void setRequestLogClose(boolean requestLogClose) {
        this.requestLogClose = requestLogClose;
    }

    public int getCloseRequestLogLength() {
        return closeRequestLogLength;
    }

    public void setCloseRequestLogLength(int closeRequestLogLength) {
        this.closeRequestLogLength = closeRequestLogLength;
    }

    public int getCloseResponseLogLength() {
        return closeResponseLogLength;
    }

    public void setCloseResponseLogLength(int closeResponseLogLength) {
        this.closeResponseLogLength = closeResponseLogLength;
    }

    public String getExcludeRequestUrl() {
        if (excludeRequestUrl == null) {
            return "";
        }
        return excludeRequestUrl;
    }

    public void setExcludeRequestUrl(String excludeRequestUrl) {
        this.excludeRequestUrl = excludeRequestUrl;
    }
}
