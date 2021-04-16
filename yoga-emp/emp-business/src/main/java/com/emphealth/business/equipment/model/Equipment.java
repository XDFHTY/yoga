package com.emphealth.business.equipment.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "emp_equipment")
public class Equipment {

	@Id
	@KeySql(useGeneratedKeys = true)
	private Long id;
	@Column(name = "tenant_id")
	private Long tenantId;
	private String imei;
	private String type;
	private String remark;
	@Column(name = "add_time")
	private LocalDateTime addTime;

	public Equipment(Long tenantId, String imei, String type, String remark) {
		this.tenantId = tenantId;
		this.imei = imei;
		this.type = type;
		this.remark = remark;
		this.addTime = LocalDateTime.now();
	}
}
