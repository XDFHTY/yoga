package com.emphealth.business.customer.shiro;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.shiro.authz.AuthorizationInfo;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerPrincipal implements Serializable {
    private long tenantId;
    private long customerId;
    private AuthorizationInfo authorizationInfo = null;
}
