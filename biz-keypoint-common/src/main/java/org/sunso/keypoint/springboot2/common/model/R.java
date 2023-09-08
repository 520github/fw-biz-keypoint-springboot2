package org.sunso.keypoint.springboot2.common.model;

import lombok.*;
import lombok.experimental.Accessors;
import org.sunso.keypoint.springboot2.common.status.DefaultResultStatusEnum;
import org.sunso.keypoint.springboot2.common.status.ResultStatusEnumInterface;

import java.io.Serializable;

/**
 * 响应信息主体
 *
 * @param <T>
 */
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class R<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	private static String DEFAULT_OK_CODE = DefaultResultStatusEnum.ok.getCode();
	private static String DEFAULT_UNKNOWN_CODE = DefaultResultStatusEnum.unknown.getCode();

	@Getter
	@Setter
	private String code;

	@Getter
	@Setter
	private String msg;

	@Getter
	@Setter
	private T data;

	public static <T> R<T> ok() {
		return restResult(null, okCode(), null);
	}

	public static <T> R<T> ok(T data) {
		return restResult(data, okCode(), null);
	}

	public static <T> R<T> ok(T data, String msg) {
		return restResult(data, okCode(), msg);
	}

	public static <T> R<T> fail() {
		return restResult(null, unknownFailCode(), null);
	}

	public static <T> R<T> fail(String msg) {
		return restResult(null, unknownFailCode(), msg);
	}

	public static <T> R<T> fail(String code, String msg) {
		return restResult(null, code, msg);
	}

	public static <T> R<T> fail(T data) {
		return restResult(data, unknownFailCode(), null);
	}

	public static <T> R<T> fail(T data, String msg) {
		if (msg == null) {
			msg = DefaultResultStatusEnum.unknown.getMsg();
		}
		return restResult(data, unknownFailCode(), msg);
	}

	public static <T> R<T> fail(T data, ResultStatusEnumInterface resultStatus) {
		if (resultStatus == null) {
			return fail(data);
		}
		return restResult(data, resultStatus.getCode(), resultStatus.getMsg());
	}

	public static <T> R<T> fail(ResultStatusEnumInterface resultStatus) {
		if (resultStatus == null) {
			return fail();
		}
		return fail(null, resultStatus);
	}

	public static <T> R<T> restResult(T data, String code, String msg) {
		R<T> apiResult = new R<>();
		apiResult.setCode(code);
		apiResult.setData(data);
		apiResult.setMsg(msg);
		return apiResult;
	}

	private static String okCode() {
		return DEFAULT_OK_CODE;
	}

	private static String unknownFailCode() {
		return DEFAULT_UNKNOWN_CODE;
	}

	public static void setOkCode(String okCode) {
		DEFAULT_OK_CODE = okCode;
	}

	public static void setDefaultUnknownCode(String defaultUnknownCode) {
		DEFAULT_OK_CODE = defaultUnknownCode;
	}

}
