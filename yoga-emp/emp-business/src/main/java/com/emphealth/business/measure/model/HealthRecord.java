package com.emphealth.business.measure.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "emp_health_record")
public class HealthRecord {

	@Id
	@KeySql(useGeneratedKeys = true)
	private Long id;
	@Column(name = "tenant_id")
	private Long tenantId;
	@Column(name = "customer_id")
	private Long customerId;
	@Column(name = "operator_id")
	private Long operatorId;
	private String imei;
	private Integer sbp;
	private Integer dbp;
	private Integer spo2;
	private Integer ppg;
	private Float temperature;
	private Float glu;
	private Integer purine;
	@Column(name = "begin_time")
	private LocalDateTime beginTime;
	@Column(name = "end_time")
	private LocalDateTime endTime;

	@Transient
	private String customer;
	@Transient
	private String operator;
	@Transient
	private String tenant;

	public HealthRecord(Long tenantId, Long operatorId, Long customerId, String imei) {
		this.tenantId = tenantId;
		this.customerId = customerId;
		this.operatorId = operatorId;
		this.imei = imei;
		this.beginTime = LocalDateTime.now();
		this.endTime = LocalDateTime.now().plusMinutes(30);
	}

	public HealthRecord(Long id, LocalDateTime endTime) {
		this.id = id;
		this.endTime = endTime;
	}
}
