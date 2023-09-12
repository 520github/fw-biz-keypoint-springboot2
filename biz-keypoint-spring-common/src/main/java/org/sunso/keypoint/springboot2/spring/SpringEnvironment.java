package org.sunso.keypoint.springboot2.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.sunso.keypoint.springboot2.spring.log.LogConfig;

@Component
public class SpringEnvironment implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringEnvironment.applicationContext = applicationContext;
    }

    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    public static <T> T getBean(String name, Class<T> clazz) {
        return applicationContext.getBean(name, clazz);
    }

    public static LogConfig getLogConfig() {
        return getBean(LogConfig.class);
    }

    public static String[] getActiveProfiles() {
        return getEnvironment().getActiveProfiles();
    }

    public static Environment getEnvironment() {
        return applicationContext.getEnvironment();
    }

    public static String getProperty(String code) {
        return getEnvironment().getProperty(code);
    }

    public static String getActiveProfile() {
        String activeProfiles[] = getActiveProfiles();
        return activeProfiles==null? null: activeProfiles[0];
    }

    public static boolean isProdEnv() {
        String activeProfile = getActiveProfile();
        if (activeProfile == null) {
            return false;
        }
        if ("prod".equalsIgnoreCase(activeProfile) || "fl".equalsIgnoreCase(activeProfile)) {
            return true;
        }
        return false;
    }

    public static boolean isNotProdEnv() {
        return !isProdEnv();
    }

    public static boolean isDevEnv() {
        String activeProfile = getActiveProfile();
        if (activeProfile == null) {
            return false;
        }
        if (activeProfile.contains("dev")) {
            return true;
        }
        return false;
    }

    public static String getServerPort() {
        return getProperty("server.port");
    }

    public static String getServerLocalIp() {
        return getProperty("localIp");
    }

}
