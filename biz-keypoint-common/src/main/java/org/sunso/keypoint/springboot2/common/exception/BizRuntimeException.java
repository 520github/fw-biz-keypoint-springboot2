package org.sunso.keypoint.springboot2.common.exception;

import org.sunso.keypoint.springboot2.common.status.ResultStatusEnumInterface;


public class BizRuntimeException extends RuntimeException {

    private ResultStatusEnumInterface resultStatus;

    public BizRuntimeException() {
        super();
    }

    public BizRuntimeException(ResultStatusEnumInterface resultStatus) {
        super(resultStatus.getMsg());
        this.resultStatus = resultStatus;
    }

    public BizRuntimeException(ResultStatusEnumInterface resultStatus, Throwable t) {
        super(resultStatus.getMsg(), t);
        this.resultStatus = resultStatus;
    }

    public BizRuntimeException(String message, Throwable t) {
        super(message, t);
    }

    public ResultStatusEnumInterface getResultStatus() {
        return resultStatus;
    }
}
