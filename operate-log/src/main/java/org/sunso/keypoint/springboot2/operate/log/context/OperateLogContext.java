package org.sunso.keypoint.springboot2.operate.log.context;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sunso520
 * @Title:OperateLogContext
 * @Description: <br>
 * @Created on 2024/5/10 09:10
 */
public class OperateLogContext  {

    private static final ThreadLocal<Map<String, Object>> EXTS = new ThreadLocal<>();

    public static void setExt(String key, Object value) {
        if (get() == null) {
            EXTS.set(new HashMap<>());
        }
        get().put(key, value);
    }

    private static Map<String, Object> get() {
        return EXTS.get();
    }

    public static Object getExt(String key) {
        if (get() == null) {
            return null;
        }
        return get().get(key);
    }

    public static void removeEXTS() {
        EXTS.remove();
    }

}
