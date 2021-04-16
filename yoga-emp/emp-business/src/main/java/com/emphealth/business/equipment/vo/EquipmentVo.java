package com.emphealth.business.equipment.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EquipmentVo {

	@ApiModelProperty(value = "设备ID")
	private Long id;
	@ApiModelProperty(value = "设备编码")
	private String imei;
	@ApiModelProperty(value = "设备类型")
	private String type;
	@ApiModelProperty(value = "备注信息")
	private String remark;
}
