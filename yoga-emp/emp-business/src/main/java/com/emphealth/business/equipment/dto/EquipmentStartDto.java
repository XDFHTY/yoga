package com.emphealth.business.equipment.dto;

import com.yoga.core.base.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class EquipmentStartDto extends BaseDto {
    @ApiModelProperty(value = "设备编码")
    @NotBlank(message = "设备编码不能为空")
    private String imei;
    @ApiModelProperty(value = "客户ID")
    @NotNull(message = "客户ID不能为空")
    private Long customerId;
}
