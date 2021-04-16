package com.emphealth.business.customer.shiro;

import lombok.Getter;
import lombok.Setter;
import org.apache.shiro.authc.UsernamePasswordToken;

public class CustomerToken extends UsernamePasswordToken {

    @Getter
    @Setter
    private long tenantId;

    public CustomerToken(long tenantId, String openId) {
        super(openId, openId);
        this.tenantId = tenantId;
    }
}
