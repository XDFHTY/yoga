package com.emphealth.business.customer.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "emp_customer_tenant")
public class CustomerTenant implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "tenant_id")
	private Long tenantId;
	@Id
	@Column(name = "customer_id")
	private Long customerId;
}
