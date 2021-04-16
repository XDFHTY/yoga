package com.emphealth.business.equipment.dto;

import com.yoga.core.base.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class EquipmentUpdateDto extends BaseDto {
    @ApiModelProperty(value = "设备ID")
    @NotNull(message = "设备ID不能为空")
    private Long id;
    @ApiModelProperty(value = "设备类型")
    private String type;
    @ApiModelProperty(value = "备注信息")
    private String remark;
}
