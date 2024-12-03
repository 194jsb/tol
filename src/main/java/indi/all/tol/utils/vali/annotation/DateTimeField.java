package indi.all.tol.utils.vali.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: Wenhao Huang
 * @Date: 2024/10/30 15:52
 * @Version: 1.0
 * @Description:
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DateTimeField {

    String message() default "错误";

    /**
     * 日期格式错误错误描述
     *
     * @return
     */
    String formatMessage() default "日期格式错误";

    /**
     * 日期先后错误错误描述
     *
     * @return
     */
    String beforeMessage() default "日期先后顺序不正确";

    /**
     * 日期先后错误错误描述
     *
     * @return
     */
    String afterMessage() default "日期先后顺序不正确";

    /**
     * 日期为空错误错误描述
     *
     * @return
     */
    String nullMessage() default "不能为空";

    /**
     * 日期错误错误描述
     *
     * @return
     */
    String futureMessage() default "日期应大于当前时间";

    /**
     * 日期为空错误错误描述
     *
     * @return
     */
    String futureOrPresentMessage() default "日期应大于等于当前时间";

    /**
     * 日期为空错误错误描述
     *
     * @return
     */
    String pastMessage() default "日期应小于当前时间";

    /**
     * 日期为空错误错误描述
     *
     * @return
     */
    String pastOrPresentMessage() default "日期应小于等于当前时间";

    /**
     * 验证日期格式
     *
     * @return
     */
    String format() default "yyyy-MM-dd";

    /**
     * 验证当前日期值是否在该参数日期之前
     *
     * @return
     */
    String before() default "";

    /**
     * 验证当前日期值是否在该参数日期之后
     *
     * @return
     */
    String after() default "";

    /**
     * 验证当前日期值是否为空
     *
     * @return
     */
    boolean canNull() default false;

    /**
     * 验证当前日期是否为未来时间
     *
     * @return
     */
    boolean future() default false;

    /**
     * 验证当前日期是否为未来或现在时间
     *
     * @return
     */
    boolean futureOrPresent() default false;

    /**
     * 验证当前日期是否为过去时间
     *
     * @return
     */
    boolean past() default false;

    /**
     * 验证当前日期是否为过去或现在时间
     *
     * @return
     */
    boolean pastOrPresent() default false;

    /**
     * 是否允许与before时间相同
     *
     * @return
     */
    boolean identicalBefore() default true;

    /**
     * 是否允许与after时间相同
     *
     * @return
     */
    boolean identicalAfter() default true;

    Class<?>[] groups() default {};

}