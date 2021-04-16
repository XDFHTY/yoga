package com.emphealth.business.equipment.mapper;

import com.emphealth.business.equipment.model.Equipment;
import com.yoga.core.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface EquipmentMapper extends MyMapper<Equipment> {
    List<Equipment> list(@Param("tenantId") long tenantId,
                         @Param("keyword") String keyword);
}
