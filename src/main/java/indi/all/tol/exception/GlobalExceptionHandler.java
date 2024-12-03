package indi.all.tol.exception;

import io.micrometer.common.util.StringUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * 异常处理
 *
 * @author wangjie
 * @version v1.0
 * @date 2023/7/25
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 数据验证异常
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Result<Object> notValidExceptionHandler(HttpServletRequest req, MethodArgumentNotValidException e) {

        // 获取异常信息
        BindingResult exceptions = e.getBindingResult();
        // 判断异常中是否有错误信息，如果存在就使用异常中的消息，否则使用默认消息
        if (exceptions.hasErrors()) {
            List<ObjectError> errors = exceptions.getAllErrors();
            if (!errors.isEmpty()) {
                Object obj = e.getTarget();
                List<String> msgList = new ArrayList<>();
                for (ObjectError error : errors) {
                    if (error instanceof FieldError) {
                        FieldError fieldError = (FieldError)error;
                        Field[] fields = obj.getClass().getDeclaredFields();
                        boolean bo = true;
                        for (Field field : fields) {
                            if (field.getName().equals(fieldError.getField())) {
                                for (Annotation annotation : field.getDeclaredAnnotations()) {
                                    if (annotation.annotationType().getSimpleName().equals("Schema")) {
                                        bo = false;
                                        Schema apiModelProperty = (Schema)annotation;
                                        if (Objects.isNull(fieldError.getRejectedValue())) {
                                            msgList.add(apiModelProperty.description() + "传值:" + "格式错误");
                                        } else if (StringUtils.isBlank(fieldError.getRejectedValue().toString())) {
                                            msgList.add(apiModelProperty.description() + ":" + fieldError.getDefaultMessage());
                                        } else {
                                            msgList.add(apiModelProperty.description() + "传值:"
                                                + fieldError.getRejectedValue() + "格式错误");
                                        }
                                    }
                                }
                            }
                        }
                        if (bo) {
                            msgList.add(fieldError.getField() + "字段" + fieldError.getRejectedValue() + "格式错误");
                        }
                    } else {
                        msgList.add(error.getDefaultMessage());
                    }

                }
                log.error(req.getRequestURI() + "数据验证失败！原因是：" + msgList.toString(), e);
                return Result.error("数据验证失败！原因是：" + msgList.toString());
            }
        }
        log.error(req.getRequestURI() + "数据验证失败！", e);
        return Result.error("数据验证失败！");
    }

    /**
     * 处理空指针的异常
     */
    @ExceptionHandler(value = NullPointerException.class)
    public Result<Object> exceptionHandler(HttpServletRequest req, NullPointerException e) {
        log.error(req.getRequestURI() + "发生空指针异常！原因是:" + e, e);
        return Result.error("发生空指针异常！");
    }

    /**
     * 处理其他异常
     */
    @ExceptionHandler(value = Exception.class)
    public Result<Object> exceptionHandler(HttpServletRequest req, Exception e) {
        log.error(req.getRequestURI() + "未知异常！原因是:" + e, e);
        return Result.error("未知异常！原因是:" + e);
    }
}
