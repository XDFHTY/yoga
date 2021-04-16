package com.emphealth.business.customer.dto;

import com.yoga.core.base.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CustomerListDto extends BaseDto {

    @ApiModelProperty(value = "关键字过滤，用户名，姓名")
    private String keyword = "";
    @ApiModelProperty(value = "姓名快速过滤")
    private String name = "";
    @ApiModelProperty(value = "只查询本机构")
    private boolean tenantOnly;
}
