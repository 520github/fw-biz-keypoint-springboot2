package org.sunso.keypoint.springboot2.biz.keypoint.distribute.lock;

import cn.hutool.core.util.ReUtil;
import cn.hutool.json.JSONObject;

import java.util.List;
import java.util.regex.Pattern;

public class RegUtilsTest {

    public static void main(String[] args) {
        Pattern KEY_GROUP = Pattern.compile("\\$\\{([^}]*)\\}");
        String lockKey = "lock_aspect_${orderId}_${createTime}";
        List<String> matchKeyList = ReUtil.getAllGroups(KEY_GROUP, lockKey);
        List<String> resultList = ReUtil.findAll(KEY_GROUP, lockKey, 0);
        System.out.println("ddd:" + matchKeyList);
        System.out.println("resultList:" + resultList);
        for(String value: resultList) {
            System.out.println("value:" + value);
        }

        JSONObject jsonObject  = new JSONObject();
        jsonObject.putOnce("orderId", "19938");

        JSONObject data = new JSONObject();
        data.putOnce("userId", "kkkk");
        jsonObject.putOnce("data", data);

        String result = jsonObject.getByPath("data.userId", String.class);
        System.out.println("result:" + result);
    }
}
