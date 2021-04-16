package com.emphealth.business.equipment.service;

import com.emphealth.business.equipment.mapper.EquipmentMapper;
import com.emphealth.business.equipment.model.Equipment;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yoga.core.base.BaseService;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.mybatis.MapperQuery;
import com.yoga.logging.annotation.Logging;
import com.yoga.logging.annotation.LoggingPrimary;
import com.yoga.logging.service.LoggingPrimaryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@LoggingPrimary(module = EquipmentService.ModuleName, name = "设备管理")
public class EquipmentService extends BaseService implements LoggingPrimaryHandler {

    @Autowired
    private EquipmentMapper equipmentMapper;

    public final static String ModuleName = "emphealth_equipment";

    @Override
    public String getPrimaryInfo(Object primaryId) {
        Equipment equipment = equipmentMapper.selectByPrimaryKey(primaryId);
        if (equipment == null) return null;
        return equipment.getImei();
    }

    @Logging(module = ModuleName, description = "添加设备", primaryKeyIndex = -1, argNames = "租户ID，设备编码，设备类型，备注信息")
    public long add(long tenantId, String imei, String type, String remark) {
        if (new MapperQuery<>(Equipment.class)
                .andEqualTo("imei", imei)
                .andEqualTo("tenantId", tenantId)
                .count(equipmentMapper) > 0) throw new BusinessException("该编码的设备已经添加！");
        Equipment equipment = new Equipment(tenantId, imei, type, remark);
        return equipmentMapper.insert(equipment);
    }
    @Logging(module = ModuleName, description = "删除设备", primaryKeyIndex = 1, argNames = "，租户ID")
    public void delete(long tenantId, long id) {
        Equipment saved = equipmentMapper.selectByPrimaryKey(id);
        if (saved == null || saved.getTenantId() != tenantId) throw new BusinessException("设备不存在！");
        equipmentMapper.deleteByPrimaryKey(id);
    }
    @Logging(module = ModuleName, description = "修改设备", primaryKeyIndex = -1, argNames = "租户ID，，设备类型，备注信息")
    public void update(long tenantId, long id, String type, String remark) {
        Equipment saved = equipmentMapper.selectByPrimaryKey(id);
        if (saved == null || saved.getTenantId() != tenantId) throw new BusinessException("设备不存在！");
        saved.setType(type);
        saved.setRemark(remark);
        equipmentMapper.updateByPrimaryKey(saved);
    }

    public PageInfo<Equipment> list(long tenantId, String keyword, int pageIndex, int pageSize) {
        PageHelper.startPage(pageIndex + 1, pageSize);
        List<Equipment> customers = equipmentMapper.list(tenantId, keyword);
        return new PageInfo<>(customers);
    }
    public Equipment get(long tenantId, long id) {
        Equipment equipment = equipmentMapper.selectByPrimaryKey(id);
        if (equipment == null || equipment.getTenantId() != tenantId) throw new BusinessException("设备不存在！");
        return equipment;
    }
    public Equipment get(long tenantId, String imei) {
        Equipment equipment = new MapperQuery<>(Equipment.class)
                .andEqualTo("tenantId", tenantId)
                .andEqualTo("imei", imei)
                .queryFirst(equipmentMapper);
        if (equipment == null) throw new BusinessException("设备不存在！");
        return equipment;
    }
}
