package org.sunso.keypoint.springboot2.operate.log.data;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author sunso520
 * @Title:OperateLogData
 * @Description: <br>
 * @Created on 2024/5/8 09:59
 */
@Data
public class OperateLogData {
    /**
     * 用户id
     */
    private String userId;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 用户类型
     */
    private String userType;
    /**
     * 用户所拥有的角色
     */
    private String userRoles;
    /**
     * 操作模块名称
     */
    private String moduleName;
    /**
     * 操作功能名称
     */
    private String functionName;
    /**
     * 请求方法类型
     */
    private String requestMethod;
    /**
     * 请求地址
     */
    private String requestUrl;

    /**
     * 用户 IP
     */
    private String userIp;

    /**
     * 浏览器 UserAgent
     */
    private String userAgent;
    /**
     * 客户端类型
     */
    private String clientType;
    /**
     * 客户端版本
     */
    private String clientVersion;
    /**
     * Java 方法的参数
     */
    private String javaMethodName;
    /**
     * Java 方法名
     */
    private String javaMethodArgs;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 执行时长，单位：毫秒
     */
    private Long duration;

    /**
     * 结果码
     */
    private String resultCode;

    /**
     * 结果提示
     */
    private String resultMsg;

    /**
     * 结果数据
     */
    private String resultData;

    /**
     * 操作前数据
     */
    private String beforeData;

    /**
     * 操作后数据
     */
    private String afterData;

    /**
     * 变化数据
     */
    private String changeData;


    public static OperateLogData newInstance() {
        return new OperateLogData();
    }

}
