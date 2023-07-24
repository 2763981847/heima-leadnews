package com.heima.model.common.dtos;

import com.alibaba.fastjson.JSON;
import com.heima.model.common.enums.AppHttpCodeEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通用的结果返回类
 *
 * @param <T>
 */
@Data
public class ResponseResult<T> implements Serializable {

    private String host;

    private Integer code;

    private String errorMessage;

    private T data;

    public ResponseResult() {
        this.code = 200;
    }

    public ResponseResult(Integer code, T data) {
        this.code = code;
        this.data = data;
    }

    public ResponseResult(Integer code, String msg, T data) {
        this.code = code;
        this.errorMessage = msg;
        this.data = data;
    }

    public ResponseResult(Integer code, String msg) {
        this.code = code;
        this.errorMessage = msg;
    }

    public static ResponseResult<?> errorResult(int code, String msg) {
        ResponseResult<?> result = new ResponseResult<>();
        return result.error(code, msg);
    }

    public static ResponseResult<?> okResult(int code, String msg) {
        ResponseResult<?> result = new ResponseResult<>();
        return result.ok(code, null, msg);
    }

    public static <T> ResponseResult<T> okResult(T data) {
        ResponseResult<T> result = (ResponseResult<T>) setAppHttpCodeEnum(AppHttpCodeEnum.SUCCESS, AppHttpCodeEnum.SUCCESS.getErrorMessage());
        result.setData(data);
        return result;
    }

    public static ResponseResult<?> errorResult(AppHttpCodeEnum enums) {
        return setAppHttpCodeEnum(enums, enums.getErrorMessage());
    }

    public static ResponseResult<?> errorResult(AppHttpCodeEnum enums, String errorMessage) {
        return setAppHttpCodeEnum(enums, errorMessage);
    }

    public static ResponseResult<?> setAppHttpCodeEnum(AppHttpCodeEnum enums) {
        return okResult(enums.getCode(), enums.getErrorMessage());
    }

    private static ResponseResult<?> setAppHttpCodeEnum(AppHttpCodeEnum enums, String errorMessage) {
        return okResult(enums.getCode(), errorMessage);
    }

    public ResponseResult<?> error(Integer code, String msg) {
        this.code = code;
        this.errorMessage = msg;
        return this;
    }

    public ResponseResult<T> ok(Integer code, T data) {
        this.code = code;
        this.data = data;
        return this;
    }

    public ResponseResult<T> ok(Integer code, T data, String msg) {
        this.code = code;
        this.data = data;
        this.errorMessage = msg;
        return this;
    }

    public ResponseResult<T> ok(T data) {
        this.data = data;
        return this;
    }

}
