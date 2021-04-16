package com.emphealth.business.measure.service;

import com.emphealth.business.customer.service.CustomerService;
import com.emphealth.business.equipment.service.EquipmentService;
import com.emphealth.business.measure.bo.TreadItem;
import com.emphealth.business.measure.mapper.HealthRecordMapper;
import com.emphealth.business.measure.mapper.RawRecordMapper;
import com.emphealth.business.measure.model.HealthRecord;
import com.emphealth.business.measure.model.RawRecord;
import com.emphealth.netty.HTDMessage;
import com.emphealth.netty.HTDMessageListener;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yoga.core.base.BaseService;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.mybatis.MapperQuery;
import com.yoga.core.utils.NumberUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MeasureService extends BaseService implements HTDMessageListener {

    @Autowired
    private RawRecordMapper rawRecordMapper;
    @Autowired
    private HealthRecordMapper healthRecordMapper;
    @Autowired
    private EquipmentService equipmentService;
    @Autowired
    private CustomerService customerService;

    public final static String ModuleName = "emphealth_measure";
    private final static String Key_EquipmentBindKey = "emp.bind.";
    private final static String Key_CustomerBindKey = "emp.customer.";

    public PageInfo<HealthRecord> list(Long tenantId, Long customerId, LocalDate beginDate, LocalDate endDate, String keyword, int pageIndex, int pageSize) {
        PageHelper.startPage(pageIndex + 1, pageSize);
        List<HealthRecord> records = healthRecordMapper.list(tenantId, customerId, beginDate, endDate, keyword);
        return new PageInfo<>(records);
    }
    public HealthRecord getRecord(long id) {
        HealthRecord record = healthRecordMapper.get(id);
        if (record == null) throw new BusinessException("未找到对应的纪录！");
        return record;
    }
    public HealthRecord getLatest(long customerId) {
        PageHelper.startPage(1, 1);
        List<HealthRecord> records = healthRecordMapper.list(null, customerId, null, null, null);
        if (records.isEmpty()) throw new BusinessException("未找到测量纪录！");
        return records.get(0);
    }
    public HealthRecord getFocusRecord(long customerId) {
        String key = Key_CustomerBindKey + customerId;
        Long recordId = redisOperator.get(key, Long.class);
        if (recordId == null) return null;
        return healthRecordMapper.get(recordId);
    }
    public List<TreadItem> listTread(long customerId, String norm, LocalDate beginDate, LocalDate endDate) {
        return healthRecordMapper.listTread(customerId, norm, beginDate, endDate);
    }

    private void addSpo2(HealthRecord healthRecord, String param) {
        String[] parts = param.split("_");
        if (parts.length != 2) throw new BusinessException("无效的SPO2数据格式！");
        Integer spo2 = NumberUtil.optInt(parts[0]);
        Integer ppg = NumberUtil.optInt(parts[1]);
        if (spo2 == null || ppg == null) throw new BusinessException("无效的SPO2数据格式！");
        healthRecord.setSpo2(spo2);
        healthRecord.setPpg(ppg);
        healthRecordMapper.updateByPrimaryKey(healthRecord);
    }
    private void addNibp(HealthRecord healthRecord, String param) {
        String[] parts = param.split("_");
        if (parts.length != 3) throw new BusinessException("无效的NIBP数据格式！");
        Integer sbp = NumberUtil.optInt(parts[0]);
        Integer dbp = NumberUtil.optInt(parts[1]);
        Integer ppg = NumberUtil.optInt(parts[2]);
        if (sbp == null || dbp == null || ppg == null) throw new BusinessException("无效的NIBP数据格式！");
        healthRecord.setSbp(sbp);
        healthRecord.setDbp(dbp);
        healthRecord.setPpg(ppg);
        healthRecordMapper.updateByPrimaryKey(healthRecord);
    }
    private void addTemp(HealthRecord healthRecord, String param) {
        Integer temp = NumberUtil.optInt(param);
        if (temp == null) throw new BusinessException("无效的TEMP数据格式！");
        healthRecord.setTemperature(temp / 10f);
        healthRecordMapper.updateByPrimaryKey(healthRecord);
    }
    private void addGlu(HealthRecord healthRecord, String param) {
        Integer temp = NumberUtil.optInt(param);
        if (temp == null) throw new BusinessException("无效的GLU数据格式！");
        healthRecord.setGlu(temp / 10f);
        healthRecordMapper.updateByPrimaryKey(healthRecord);
    }
    private void addUa(HealthRecord healthRecord, String param) {
        Integer temp = NumberUtil.optInt(param);
        if (temp == null) throw new BusinessException("无效的UA数据格式！");
        healthRecord.setPurine(temp);
        healthRecordMapper.updateByPrimaryKey(healthRecord);
    }

    private void finishImeiTask(String imei) {
        String key = Key_EquipmentBindKey + imei;
        Long measureId = redisOperator.get(key, Long.class);
        if (measureId == null) return;
        HealthRecord record = healthRecordMapper.selectByPrimaryKey(measureId);
        if (record == null) return;
        HealthRecord updated = new HealthRecord(record.getId(), LocalDateTime.now());
        healthRecordMapper.updateByPrimaryKeySelective(updated);
        key = Key_CustomerBindKey + record.getCustomerId();
        redisOperator.remove(key);
    }

    @Transactional
    public long start(long tenantId, String imei, long operatorId, long customerId) {
        finishImeiTask(imei);
        equipmentService.get(tenantId, imei);
        HealthRecord healthRecord = new HealthRecord(tenantId, operatorId, customerId, imei);
        healthRecordMapper.insert(healthRecord);
        customerService.bindTenant(customerId, tenantId);
        long measureId = healthRecord.getId();
        String key = Key_EquipmentBindKey + imei;
        redisOperator.set(key, measureId);
        key = Key_CustomerBindKey + customerId;
        redisOperator.set(key, measureId, 1800);
        return healthRecord.getId();
    }
    @Transactional
    public void stop(long id) {
        HealthRecord record = healthRecordMapper.selectByPrimaryKey(id);
        if (record == null) return;
        HealthRecord updated = new HealthRecord(record.getId(), LocalDateTime.now());
        healthRecordMapper.updateByPrimaryKeySelective(updated);
        String key = Key_CustomerBindKey + record.getCustomerId();
        redisOperator.remove(key);
        key = Key_EquipmentBindKey + record.getImei();
        redisOperator.remove(key);
    }

    @Override
    public void onMessage(HTDMessage message) {
        String time = message.getTime() == null ? "<null>" : message.getTime().toString();
        logger.info("onMessage: " + message.getImei() + " : " + time + " : " + message.getCommand() + " : " + message.getParam());
        try {
            addRecord(message);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }
    private void addRecord(HTDMessage message) {
        if (message.getTime() == null) return;
        if (message.getParam() == null) return;
        RawRecord rawRecord = new RawRecord(message.getImei(), message.getTime(), message.getParam());
        LocalDateTime beginTime = message.getTime().minusSeconds(30);
        LocalDateTime endTime = message.getTime().plusSeconds(30);
        HealthRecord healthRecord = new MapperQuery<>(HealthRecord.class)
                .andEqualTo("imei", message.getImei())
                .andLessThanOrEqualTo("beginTime", beginTime)
                .andGreaterThanOrEqualTo("endTime", endTime)
                .orderBy("beginTime", true)
                .queryFirst(healthRecordMapper);
        if (healthRecord != null) {
            rawRecord.setUsed(true);
            switch (message.getCommand()) {
                case "SPO2":
                    addSpo2(healthRecord, message.getParam());
                    break;
                case "NIBP":
                    addNibp(healthRecord, message.getParam());
                    break;
                case "TEMP":
                    addTemp(healthRecord, message.getParam());
                    break;
                case "GLU":
                    addGlu(healthRecord, message.getParam());
                    break;
                case "UA":
                    addUa(healthRecord, message.getParam());
                    break;
            }
        }
        rawRecordMapper.insert(rawRecord);
        healthRecordMapper.updateByPrimaryKey(healthRecord);
    }
}
