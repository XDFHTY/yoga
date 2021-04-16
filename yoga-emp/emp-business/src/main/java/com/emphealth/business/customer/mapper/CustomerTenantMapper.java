package com.emphealth.business.customer.mapper;

import com.emphealth.business.customer.model.CustomerTenant;
import com.yoga.core.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CustomerTenantMapper extends MyMapper<CustomerTenant> {
    void insertIgnore(@Param("customerId") long customerId,
                      @Param("tenantId") long tenantId);
}
