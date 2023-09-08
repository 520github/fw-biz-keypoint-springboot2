package org.sunso.keypoint.springboot2.spring.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sunso.keypoint.springboot2.spring.SpringEnvironment;

public class LogStatic {
    private static Logger log = LoggerFactory.getLogger(LogStatic.class);

    public static void info(String msg) {
        info(log, msg);
    }

    public static void info(String msg, Object... arguments) {
        info(log, msg, arguments);
    }

    public static void infoControl(String msg) {
        infoControl(log, msg);
    }

    public static void infoControl(String format, Object... arguments) {
        infoControl(log, format, arguments);
    }

    public static void info(Logger log, String msg) {
        log.info(msg);
    }

    public static void info(Logger log, String format, Object... arguments) {
        log.info(format, arguments);
    }

    public static void infoControl(Logger log, String msg) {
        if (isInfoLogClose()) {
            return;
        }
        log.info(msg);
    }

    public static void infoControl(Logger log, String format, Object... arguments) {
        if (isInfoLogClose()) {
            return;
        }
        log.info(format, arguments);
    }

    public static void error(String msg, Throwable t) {
        error(log, msg, t);
    }

    public static void error(Logger log, String msg, Throwable t) {
        log.error(msg, t);
    }

    public static void error(String format, Object... arguments) {
        error(log, format, arguments);
    }

    public static void error(Logger log, String format, Object... arguments) {
        log.error(format, arguments);
    }

    public static boolean isInfoLogClose() {
        return SpringEnvironment.getLogConfig().isInfoLogClose();
    }

    public static boolean isRequestLogClose() {
        return SpringEnvironment.getLogConfig().isRequestLogClose();
    }

    public static int closeRequestLogLength() {
        return SpringEnvironment.getLogConfig().getCloseRequestLogLength();
    }

    public static int closeResponseLogLength() {
        return SpringEnvironment.getLogConfig().getCloseResponseLogLength();
    }

}
