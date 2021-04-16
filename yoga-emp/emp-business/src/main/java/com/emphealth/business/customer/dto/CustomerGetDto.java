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
public class CustomerGetDto extends BaseDto {
    @ApiModelProperty(value = "用户ID")
    @NotNull(message = "请输入用户ID")
    private Long id;
}
