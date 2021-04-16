package com.emphealth.business.equipment.dto;

import com.yoga.core.base.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class EquipmentAddDto extends BaseDto {
    @ApiModelProperty(value = "设备编码")
    @NotBlank(message = "设备编码不能为空")
    private String imei;
    @ApiModelProperty(value = "设备类型")
    private String type;
    @ApiModelProperty(value = "备注信息")
    private String remark;
}
