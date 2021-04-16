package com.emphealth.business.customer.model;

import com.emphealth.business.customer.enums.GenderType;
import com.yoga.logging.model.LoginUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.shiro.SecurityUtils;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "emp_customer")
public class Customer implements Serializable, LoginUser {
	private static final long serialVersionUID = 1L;

	@Id
	@KeySql(useGeneratedKeys = true)
	private Long id;
	private String username;
	private String nickname;
	private String realname;
	@Column(name = "gender")
	@Enumerated(EnumType.STRING)
	private GenderType gender;
	private LocalDate birthday;
	private String address;
	private String password;
	@Column(name = "open_id")
	private String openId;
	private String mobile;
	private String email;
	private String avatar;
	private String pid;
	private String spell;
	private String capital;
	@Column(name = "last_login")
	private LocalDateTime lastLogin;
	@Column(name = "add_time")
	private LocalDateTime addTime;

	@Transient
	private Integer recordCount;

	public Customer(String username, String nickname, String openId) {
		this.username = username;
		this.nickname = nickname;
		this.openId = openId;
		this.addTime = LocalDateTime.now();
	}
	public Customer(String username, String nickname, String realname, GenderType gender, LocalDate birthday, String address, String password, String mobile, String email, String avatar, String pid, String spell, String capital) {
		this.username = username;
		this.nickname = nickname;
		this.realname = realname;
		this.gender = gender;
		this.birthday = birthday;
		this.address = address;
		this.password = password;
		this.mobile = mobile;
		this.email = email;
		this.avatar = avatar;
		this.pid = pid;
		this.spell = spell;
		this.capital = capital;
		this.addTime = LocalDateTime.now();
	}

	public Customer(Long id) {
		this.id = id;
		this.lastLogin = LocalDateTime.now();
	}

	@Override
	public String getName() {
		return username;
	}

	@Override
	public Long getTenantId() {
		return 0L;
	}

	public static Customer getLoginUser() {
		return (Customer) SecurityUtils.getSubject().getSession().getAttribute("user");
	}
}
