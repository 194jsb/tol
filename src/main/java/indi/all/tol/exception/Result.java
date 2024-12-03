package indi.all.tol.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "接口返回对象")
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 成功标志
     */
    @Schema(description = "成功标志")
    private boolean success = true;

    /**
     * 返回处理消息
     */
    @Schema(description = "返回处理消息")
    private String message = "";

    /**
     * 返回代码
     */
    @Schema(description = "返回代码")
    private String code = "0";

    /**
     * 返回数据对象 data
     */
    @Schema(description = "返回数据对象")
    private T result;

    /**
     * 时间戳
     */
    @Schema(description = "时间戳")
    private long timestamp = System.currentTimeMillis();
    @JsonIgnore
    private String onlTable;

    public Result() {
    }

    public Result(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static <T> Result<T> ok() {
        Result<T> r = new Result<T>();
        r.setSuccess(true);
        r.setCode(CommonCodeEnum.COMMON_CODE_200.getCode());
        return r;
    }

    public static <T> Result<T> ok(String msg) {
        Result<T> r = new Result<T>();
        r.setSuccess(true);
        r.setCode(CommonCodeEnum.COMMON_CODE_200.getCode());
        r.setResult((T) msg);
        r.setMessage(msg);
        return r;
    }

    public static <T> Result<T> ok(T data) {
        Result<T> r = new Result<T>();
        r.setSuccess(true);
        r.setCode(CommonCodeEnum.COMMON_CODE_200.getCode());
        r.setResult(data);
        return r;
    }

    public static <T> Result<T> ok(String msg, T data) {
        Result<T> r = new Result<T>();
        r.setSuccess(true);
        r.setCode(CommonCodeEnum.COMMON_CODE_200.getCode());
        r.setMessage(msg);
        r.setResult(data);
        return r;
    }

    public static <T> Result<T> error(String msg, T data) {
        Result<T> r = new Result<T>();
        r.setSuccess(false);
        r.setCode(CommonCodeEnum.COMMON_CODE_500.getCode());
        r.setMessage(msg);
        r.setResult(data);
        return r;
    }

    public static <T> Result<T> error(String msg) {
        return error(CommonCodeEnum.COMMON_CODE_500.getCode(), msg);
    }

    public static <T> Result<T> error(String code, String msg) {
        Result<T> r = new Result<T>();
        r.setCode(code);
        r.setMessage(msg);
        r.setSuccess(false);
        return r;
    }

    public Result<T> success(String message) {
        this.message = message;
        this.code = CommonCodeEnum.COMMON_CODE_200.getCode();
        this.success = true;
        return this;
    }

    public Result<T> error500(String message) {
        this.message = message;
        this.code = CommonCodeEnum.COMMON_CODE_500.getCode();
        this.success = false;
        return this;
    }

}