package com.emphealth.business.measure.dto;

import com.yoga.core.base.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class MeasureGetDto extends BaseDto {

    @ApiModelProperty(value = "ID")
    @NotNull(message = "ID不能为空")
    private Long id;
}
