package org.sunso.keypoint.springboot2.spring.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseLog {

    protected Logger log = LoggerFactory.getLogger(this.getClass());

    public void logInfo(String msg) {
        LogStatic.info(log, msg);
    }

    public void logInfo(String format, Object... arguments) {
        LogStatic.info(log, format, arguments);
    }

    public void logInfoControl(String msg) {
        LogStatic.infoControl(log, msg);
    }

    public void logInfoControl(String format, Object... arguments) {
        LogStatic.infoControl(log, format, arguments);
    }

    public void logError(String msg, Throwable t) {
        LogStatic.error(log, msg, t);
    }

    public void logError(String format, Object... arguments) {
        LogStatic.error(log, format, arguments);
    }

    public boolean isInfoLogClose() {
        return LogStatic.isInfoLogClose();
    }

    public boolean isRequestLogClose() {
        return LogStatic.isRequestLogClose();
    }

    public int closeRequestLogLength() {
        return LogStatic.closeRequestLogLength();
    }

    public int closeResponseLogLength() {
        return LogStatic.closeResponseLogLength();
    }
}
