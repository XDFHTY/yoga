package com.emphealth.business.measure.model;

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
@Table(name = "emp_raw_record")
public class RawRecord {

	@Id
	@KeySql(useGeneratedKeys = true)
	private Long id;
	private String imei;
	private LocalDateTime datetime;
	private String param;
	@Column(name = "add_time")
	private LocalDateTime addTime;
	private Boolean used;

	public RawRecord(String imei, LocalDateTime datetime, String param) {
		this.imei = imei;
		this.datetime = datetime;
		this.param = param;
		this.addTime = LocalDateTime.now();
		this.used = false;
	}
}
