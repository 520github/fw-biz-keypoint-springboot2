package org.sunso.keypoint.springboot2.biz.keypoint.distribute.lock.handler;

/**
 * @author sunso520
 * @Title:MokService
 * @Description: <br>
 * @Created on 2024/3/5 11:10
 */
public class MockService {

    public static String getString(String value) {
        return "return:"+value;
    }

    public long getLong(long id) {
        return id;
    }
}
