package com.emphealth.business.measure.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HealthRecordVo {

	@ApiModelProperty(value = "纪录ID")
	private Long id;
	@ApiModelProperty(value = "机构ID")
	private Long tenantId;
	@ApiModelProperty(value = "客户ID")
	private Long customerId;
	@ApiModelProperty(value = "收缩压")
	private Integer sbp;
	@ApiModelProperty(value = "舒张压")
	private Integer dbp;
	@ApiModelProperty(value = "血氧")
	private Integer spo2;
	@ApiModelProperty(value = "脉率")
	private Integer ppg;
	@ApiModelProperty(value = "体温")
	private Float temperature;
	@ApiModelProperty(value = "血糖")
	private Float glu;
	@ApiModelProperty(value = "尿酸")
	private Integer purine;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "测量时间")
	private LocalDateTime beginTime;

	@ApiModelProperty(value = "机构")
	private String tenant;
	@ApiModelProperty(value = "客户")
	private String customer;
	@ApiModelProperty(value = "工作人员")
	private String operator;
}
