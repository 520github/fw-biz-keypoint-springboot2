package org.sunso.keypoint.springboot2.common.status;

/**
 * 结果状态码定义接口
 */
public interface ResultStatusEnumInterface {

    /**
     * 获取结果状态码
     * @return
     */
    String getCode();

    /**
     * 获取结果状态码说明
     * @return
     */
    String getMsg();

    static ResultStatusEnumInterface valueOf(String code, String msg) {
        return new ResultStatusEnumInterface() {
            @Override
            public String getCode() {
                return code;
            }

            @Override
            public String getMsg() {
                return msg;
            }
        };
    }
}
