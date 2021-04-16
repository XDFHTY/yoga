package com.emphealth.business.measure.mapper;

import com.emphealth.business.measure.bo.TreadItem;
import com.emphealth.business.measure.model.HealthRecord;
import com.yoga.core.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface HealthRecordMapper extends MyMapper<HealthRecord> {
    List<HealthRecord> list(@Param("tenantId") Long tenantId,
                            @Param("customerId") Long customerId,
                            @Param("beginDate") LocalDate beginDate,
                            @Param("endDate") LocalDate endDate,
                            @Param("keyword") String keyword);
    HealthRecord get(@Param("id") long id);
    List<TreadItem> listTread(@Param("customerId") long customerId,
                              @Param("norm") String norm,
                              @Param("beginDate") LocalDate beginDate,
                              @Param("endDate") LocalDate endDate);
}
