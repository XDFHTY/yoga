package com.emphealth.business.customer.vo;

import com.emphealth.business.customer.enums.GenderType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerVo {

	@ApiModelProperty(value = "用户ID")
	private Long id;
	@ApiModelProperty(value = "用户名")
	private String username;
	@ApiModelProperty(value = "昵称")
	private String nickname;
	@ApiModelProperty(value = "真实姓名")
	private String realname;
	@ApiModelProperty(value = "性别")
	private GenderType gender;
	@ApiModelProperty(value = "生日")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate birthday;
	@ApiModelProperty(value = "家庭地址")
	private String address;
	@ApiModelProperty(value = "身份证号")
	private String pid;
	@ApiModelProperty(value = "手机号")
	private String mobile;
	@ApiModelProperty(value = "邮箱")
	private String email;
	@ApiModelProperty(value = "头像")
	private String avatar;
	@ApiModelProperty(value = "全拼")
	private String spell;
	@ApiModelProperty(value = "首字母")
	private String capital;
	@ApiModelProperty(value = "最后登录")
	private LocalDateTime lastLogin;
}
