package indi.all.tol.utils.vali.validator;

import cn.hutool.core.util.ObjectUtil;
import indi.all.tol.utils.vali.annotation.DateTime;
import indi.all.tol.utils.vali.annotation.DateTimeField;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * @Author: Wenhao Huang
 * @Date: 2024/10/30 15:57
 * @Version: 1.0
 * @Description: 自定义验证处理类，swagger3搭配全局异常处理
 */

public class DateTimeValidator implements ConstraintValidator<DateTime, Object> {

    private DateTime dateTime;

    /**
     * 比较两个时间，afterTime是否在beforeTime之后
     *
     * @param beforeTime 较早时间
     * @param afterTime  较晚时间
     * @param identical  是否允许相等
     * @param format     ******时间格式，这里约定两个时间格式相同 ，如果时间格式不同需要分别取出两个时间的时间格式传进来再比较
     * @return 是否成立
     */
    private static boolean compareTime(Object beforeTime, Object afterTime, boolean identical, String format) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        try {
            Date beforDate = simpleDateFormat.parse(simpleDateFormat.format(beforeTime));
            Date afterDate = simpleDateFormat.parse(simpleDateFormat.format(afterTime));
            int i = afterDate.compareTo(beforDate);
            switch (i) {
                case 0:
                    return identical;
                case 1:
                    return Boolean.TRUE.booleanValue();
                case -1:
                    return Boolean.FALSE.booleanValue();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void initialize(DateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        Class<?> valueClass = value.getClass();
        List<Field> fields = getDateTimeAnnotationField(valueClass);
        BeanWrapper beanWrapper = new BeanWrapperImpl(value);
        String description = "";
        for (Field field : fields) {
            // 不为空的验证，为空的使用@NotBlank @NotNull等进行验证
            // 当前属性的值
            Object dateValue = beanWrapper.getPropertyValue(field.getName());
            DateTimeField dateTimeValidator = field.getAnnotation(DateTimeField.class);
            Schema schemaAnnotation = field.getAnnotation(Schema.class);
            if (schemaAnnotation != null) {
                description = schemaAnnotation.description() + ":";
            }

            if (!dateTimeValidator.canNull() & ObjectUtil.isEmpty(dateValue)) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(description + dateTimeValidator.nullMessage())
                        .addConstraintViolation();
                return false;
            }

            if (ObjectUtil.isNotEmpty(dateValue)) {
                String format = dateTimeValidator.format();
                String beforeField = dateTimeValidator.before();
                String afterField = dateTimeValidator.after();
                boolean identicalBefore = dateTimeValidator.identicalBefore();
                boolean identicalAfter = dateTimeValidator.identicalAfter();

                // 验证时间格式
                // if (ObjectUtil.isNotEmpty(format) && !dateFormatValidator(dateValue, format)) {
                // context.disableDefaultConstraintViolation();
                // context.buildConstraintViolationWithTemplate(description + dateTimeValidator.formatMessage())
                // .addConstraintViolation();
                // return false;
                // }

                // 验证前面时间是否成立
                if (ObjectUtil.isNotEmpty(beforeField)) {
                    Object beforeDateValue = beanWrapper.getPropertyValue(beforeField);
                    if (!compareTime(beforeDateValue, dateValue, identicalBefore, format)) {
                        context.disableDefaultConstraintViolation();
                        context.buildConstraintViolationWithTemplate(description + dateTimeValidator.beforeMessage())
                                .addConstraintViolation();
                        return false;
                    }
                }

                // 验证后面时间是否成立
                if (ObjectUtil.isNotEmpty(afterField)) {
                    Object afterDateValue = beanWrapper.getPropertyValue(afterField);
                    if (!compareTime(dateValue, afterDateValue, identicalAfter, format)) {
                        context.disableDefaultConstraintViolation();
                        context.buildConstraintViolationWithTemplate(description + dateTimeValidator.afterMessage())
                                .addConstraintViolation();
                        return false;
                    }
                }

                // 验证当前日期是否为未来或现在时间
                if (dateTimeValidator.future() || dateTimeValidator.futureOrPresent()) {
                    if (!compareTime(new Date(), dateValue, dateTimeValidator.futureOrPresent(), format)) {
                        context.disableDefaultConstraintViolation();
                        context.buildConstraintViolationWithTemplate(description +
                                        (dateTimeValidator.futureOrPresent() ? dateTimeValidator.futureOrPresentMessage() : dateTimeValidator.futureMessage()))
                                .addConstraintViolation();
                        return false;
                    }
                }

                // 验证当前日期是否为过去或现在时间
                if (dateTimeValidator.past() || dateTimeValidator.pastOrPresent()) {
                    if (!compareTime(dateValue, new Date(), dateTimeValidator.pastOrPresent(), format)) {
                        context.disableDefaultConstraintViolation();
                        context.buildConstraintViolationWithTemplate(description +
                                        (dateTimeValidator.pastOrPresent() ? dateTimeValidator.pastOrPresentMessage() : dateTimeValidator.pastMessage()))
                                .addConstraintViolation();
                        return false;
                    }

                }

            }
        }
        return true;
    }

    /**
     * 时间格式校验
     *
     * @param currentDateValue 验证的时间
     * @param format           时间格式
     * @return 是否成立
     */
    private boolean dateFormatValidator(Object currentDateValue, String format) {
        if (currentDateValue.toString().length() != format.length()) {
            return false;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        try {
            simpleDateFormat.parse(currentDateValue.toString());
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 获取有时间校验的字段
     *
     * @param valueClass 获取字段提供的类
     * @return 是否成立
     */
    private List<Field> getDateTimeAnnotationField(Class<?> valueClass) {
        Field[] fields = valueClass.getDeclaredFields();
        if (ObjectUtil.isNotEmpty(fields)) {
            return Arrays.stream(fields).filter(fls -> ObjectUtil.isNotEmpty(fls.getAnnotation(DateTimeField.class)))
                    .collect(toList());
        }
        return null;
    }
}