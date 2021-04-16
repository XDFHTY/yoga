package com.emphealth.business.customer.dto;

import com.yoga.core.base.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class CustomerDetailDto extends BaseDto {
    @ApiModelProperty(value = "品类ID")
    @NotNull(message = "请输入用户ID")
    private Long id;
}
