package com.emphealth.business.measure.bo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class TreadItem {

	@Id
	private Long id;
	@Column(name = "time")
	private LocalDateTime time;
	@Column(name = "record")
	private Double record;
}
