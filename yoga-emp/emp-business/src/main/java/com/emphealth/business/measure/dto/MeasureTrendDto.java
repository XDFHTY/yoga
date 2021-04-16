package com.emphealth.business.measure.dto;

import com.yoga.core.base.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class MeasureTrendDto extends BaseDto {

    @ApiModelProperty(value = "用户ID，健管用户只能查询自己的，该字段为空")
    private Long customerId;
    @ApiModelProperty(value = "开始日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate beginDate;
    @ApiModelProperty(value = "结束日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    @ApiModelProperty(value = "查询指标，比如spo2、glu")
    private String norm;
}
