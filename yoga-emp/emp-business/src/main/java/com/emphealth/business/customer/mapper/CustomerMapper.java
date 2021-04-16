package com.emphealth.business.customer.mapper;

import com.emphealth.business.customer.model.Customer;
import com.yoga.core.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CustomerMapper extends MyMapper<Customer> {
    List<Customer> list(@Param("tenantId") Long tenantId,
                        @Param("name") String name,
                        @Param("keyword") String keyword);
    Customer get(@Param("id") long id);
}
