package indi.all.tol.utils.vali.example;

import com.fasterxml.jackson.annotation.JsonFormat;
import indi.all.tol.utils.vali.annotation.DateTime;
import indi.all.tol.utils.vali.annotation.DateTimeField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @Author: Wenhao Huang
 * @Date: 2024/12/3 16:52
 * @Version: 1.0
 * @Description:
 */
@Data
@DateTime
public class DateTimeExample {
    @DateTimeField(after = "endDate")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Schema(description = "开始日期")
    private Date startDate;

    @DateTimeField(after = "endTime", format = "HH:mm:ss")
    @JsonFormat(pattern = "HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "开始时间")
    private Date startTime;

    @DateTimeField(before = "startDate", futureOrPresent = true)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Schema(description = "结束日期")
    private Date endDate;

    @DateTimeField(before = "startTime", format = "HH:mm:ss")
    @JsonFormat(pattern = "HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "结束时间")
    private Date endTime;
}