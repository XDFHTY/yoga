package com.emphealth.business.customer.dto;

import com.emphealth.business.customer.enums.GenderType;
import com.yoga.core.base.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class CustomerAddDto extends BaseDto {

	@ApiModelProperty(value = "用户名")
	private String username;
	@ApiModelProperty(value = "昵称")
	private String nickname;
	@ApiModelProperty(value = "真实姓名")
	@NotBlank(message = "真实姓名不能为空")
	private String realname;
	@ApiModelProperty(value = "性别")
	@NotNull(message = "性别不能为空")
	@Enumerated(EnumType.STRING)
	private GenderType gender;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@ApiModelProperty(value = "生日")
	@NotNull(message = "生日不能为空")
	private LocalDate birthday;
	@ApiModelProperty(value = "家庭地址")
	private String address;
	@ApiModelProperty(value = "手机号")
	private String mobile;
	@ApiModelProperty(value = "邮箱")
	private String email;
	@ApiModelProperty(value = "身份证")
	@NotBlank(message = "身份证号码不能为空")
	private String pid;
	@ApiModelProperty(value = "头像")
	private String avatar;
	@ApiModelProperty(value = "拼音")
	private String spell;
	@ApiModelProperty(value = "拼音首字母")
	private String capital;
}
