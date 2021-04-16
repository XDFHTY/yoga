package com.emphealth.business.equipment.dto;

import com.yoga.core.base.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EquipmentListDto extends BaseDto {

    @ApiModelProperty(value = "关键字过滤")
    private String keyword = "";
}
