package com.emphealth.business.customer.service;

import com.emphealth.business.customer.enums.GenderType;
import com.emphealth.business.customer.mapper.CustomerMapper;
import com.emphealth.business.customer.mapper.CustomerTenantMapper;
import com.emphealth.business.customer.model.Customer;
import com.emphealth.business.customer.model.CustomerTenant;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yoga.core.base.BaseService;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.mybatis.MapperQuery;
import com.yoga.core.utils.PinyinUtil;
import com.yoga.core.utils.StringUtil;
import com.yoga.logging.annotation.Logging;
import com.yoga.logging.annotation.LoggingPrimary;
import com.yoga.logging.service.LoggingPrimaryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@Service
@LoggingPrimary(module = CustomerService.ModuleName, name = "客户管理")
public class CustomerService extends BaseService implements LoggingPrimaryHandler {

    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private CustomerTenantMapper customerTenantMapper;

    public final static String ModuleName = "emphealth_customer";

    @Override
    public String getPrimaryInfo(Object primaryId) {
        Customer customer = customerMapper.selectByPrimaryKey(primaryId);
        if (customer == null) return null;
        return customer.getNickname();
    }

    @Transactional
    @Logging(module = ModuleName, description = "添加客户", primaryKeyIndex = -1, argNames = "租户ID，用户名，真实姓名，性别，手机号，身份证号，email，头像，拼音，首字母")
    public long add(long tenantId, String username, String realname, GenderType gender, LocalDate birthday, String address, String mobile, String pid, String email, String avatar, String spell, String capital) {
        if (StringUtil.isBlank(realname)) throw new BusinessException("真实姓名不能为空");
        if (StringUtil.isBlank(spell)) spell = PinyinUtil.toPinyin(realname);
        if (StringUtil.isBlank(capital)) capital = PinyinUtil.toCapital(realname);
        if (StringUtil.isNotBlank(pid)) {
            if (new MapperQuery<>(Customer.class)
                    .andEqualTo("pid", pid)
                    .count(customerMapper) > 0) throw new BusinessException("该身份证已经存在！");
        }
        if (StringUtil.isBlank(username)) {
            username = StringUtil.isNotBlank(mobile) ? mobile : "u_" + spell + "_" + System.currentTimeMillis();
        }
        if (new MapperQuery<>(Customer.class)
                .andEqualTo("username", username)
                .count(customerMapper) > 0) throw new BusinessException("用户名已经被占用！");
        if (StringUtil.isBlank(mobile) && StringUtil.isBlank(pid)) throw new BusinessException("手机号和身份证号不能同时为空！");
        String nickname = StringUtil.isNotBlank(realname) ? realname : username;
        Customer customer = new Customer(username, nickname, realname, gender, birthday, address, null, mobile, email, avatar, pid, spell, capital);
        customerMapper.insert(customer);
        CustomerTenant customerTenant = new CustomerTenant(tenantId, customer.getId());
        customerTenantMapper.insert(customerTenant);
        return customer.getId();
    }

    @Transactional
    @Logging(module = ModuleName, description = "修改客户信息", primaryKeyIndex = 0, argNames = "ID，用户名，昵称，真实姓名，性别，手机号，邮箱，身份证号，头像")
    public void update(long id, String username, String nickname, String realname, GenderType gender, LocalDate birthday, String address, String mobile, String email, String pid, String avatar, String spell, String capital) {
        Customer saved = customerMapper.selectByPrimaryKey(id);
        if (saved == null) throw new BusinessException("用户不存在！");
        if (StringUtil.isNotBlank(username) && !username.equals(saved.getUsername())) {
            Customer other = new MapperQuery<>(Customer.class)
                    .andEqualTo("username", username)
                    .queryFirst(customerMapper);
            if (other != null && other.getId() != id) throw new BusinessException("用户名已经被占用！");
            saved.setUsername(username);
        }
        if (nickname != null) saved.setNickname(nickname);
        if (realname != null && !realname.equals(saved.getRealname())) {
            if (StringUtil.isBlank(spell)) spell = PinyinUtil.toPinyin(realname);
            if (StringUtil.isBlank(capital)) capital = PinyinUtil.toCapital(realname);
            saved.setRealname(realname);
        }
        if (gender != null) saved.setGender(gender);
        if (birthday != null) saved.setBirthday(birthday);
        if (gender != null) saved.setGender(gender);
        if (address != null) saved.setAddress(address);
        if (mobile != null) saved.setMobile(mobile);
        if (email != null) saved.setEmail(email);
        if (StringUtil.isNotBlank(pid) && !pid.equals(saved.getPid())) {
            if (new MapperQuery<>(Customer.class)
                    .andEqualTo("pid", pid)
                    .count(customerMapper) > 0) throw new BusinessException("该身份证已经存在！");
            saved.setPid(pid);
        }
        if (avatar != null) saved.setAvatar(avatar);
        if (spell != null) saved.setSpell(spell);
        if (capital != null) saved.setCapital(capital);
        customerMapper.updateByPrimaryKey(saved);
    }
    public PageInfo<Customer> list(Long tenantId, String name, String keyword, int pageIndex, int pageSize) {
        PageHelper.startPage(pageIndex + 1, pageSize);
        List<Customer> customers = customerMapper.list(tenantId, name, keyword);
        return new PageInfo<>(customers);
    }
    public Customer get(Long tenantId, long id) {
        return get(tenantId, id, false);
    }
    public Customer get(Long tenantId, long id, boolean allowNull) {
        Customer customer = customerMapper.get(id);
        if (!allowNull && customer == null) throw new BusinessException("未找到客户信息！");
        if (tenantId != null && !customer.getTenantId().equals(tenantId)) throw new BusinessException("未找到客户信息！");
        return customer;
    }

    public void bindTenant(long id, long tenantId) {
        customerTenantMapper.insertIgnore(id, tenantId);
    }

    private Random random = new Random();
    public Customer login(String openId) {
        Customer customer = new MapperQuery<>(Customer.class)
                .andEqualTo("openId", openId)
                .queryFirst(customerMapper);
        if (customer != null) {
            customerMapper.updateByPrimaryKeySelective(new Customer(customer.getId()));
        } else {
            String username = String.valueOf(System.currentTimeMillis());
            username = username.substring(username.length() - 5);
            username += random.nextInt(1000);
            username = "o_" + username;
            customer = new Customer(username, username, openId);
            customerMapper.insert(customer);
        }
        return customer;
    }
}
