package org.sunso.keypoint.springboot2.common.status;

public enum DefaultResultStatusEnum implements ResultStatusEnumInterface {
    ok("100000", "ok"),
    unknown("100010", "unknown error")
    ;

    private String code;
    private String msg;

    DefaultResultStatusEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }
}
