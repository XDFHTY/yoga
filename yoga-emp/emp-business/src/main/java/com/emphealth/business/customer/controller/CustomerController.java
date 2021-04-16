package com.emphealth.business.customer.controller;

import com.emphealth.business.customer.dto.*;
import com.emphealth.business.customer.model.Customer;
import com.emphealth.business.customer.service.CustomerService;
import com.emphealth.business.customer.shiro.CustomerRealm;
import com.emphealth.business.customer.shiro.CustomerToken;
import com.emphealth.business.customer.vo.CustomerVo;
import com.emphealth.business.customer.vo.SpellVo;
import com.emphealth.business.measure.model.HealthRecord;
import com.emphealth.business.measure.service.MeasureService;
import com.github.pagehelper.PageInfo;
import com.yoga.core.base.BaseController;
import com.yoga.core.base.BaseDto;
import com.yoga.core.data.ApiResult;
import com.yoga.core.data.ApiResults;
import com.yoga.core.data.CommonPage;
import com.yoga.core.exception.IllegalArgumentException;
import com.yoga.core.utils.PinyinUtil;
import com.yoga.setting.customize.CustomPage;
import com.yoga.weixinapp.dto.WexinLoginDto;
import com.yoga.weixinapp.service.WxmpService;
import com.yoga.weixinapp.service.WxmpUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@Controller
@Api(tags = "EMP客户管理")
@RequestMapping("/admin/emphealth/customer")
public class CustomerController extends BaseController {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private MeasureService measureService;
    @Autowired
    private WxmpService wxmpService;
    @Autowired
    private WxmpUserService wxmpUserService;

    @ApiIgnore
    @RequiresPermissions("emphealth_customer")
    @RequestMapping("")
    public String list(ModelMap model, CustomPage page, @Valid CustomerListDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        PageInfo<Customer> customers = customerService.list(dto.getTid(), dto.getName(), dto.getKeyword(), page.getPageIndex(), page.getPageSize());
        model.put("customers", customers.getList());
        model.put("page", new CommonPage(customers));
        model.put("param", dto.wrapAsMap());
        return "/admin/emphealth/customer/list";
    }
    @ApiIgnore
    @RequiresPermissions("emphealth_customer")
    @RequestMapping("/detail")
    public String detail(ModelMap model, CustomPage page, @Valid CustomerDetailDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        Customer customer = customerService.get(null, dto.getId());
        PageInfo<HealthRecord> records = measureService.list(null, dto.getId(), null, null, null, page.getPageIndex(), page.getPageSize());
        model.put("customer", customer);
        model.put("records", records);
        model.put("page", new CommonPage(records));
        model.put("param", dto.wrapAsMap());
        return "/admin/emphealth/customer/detail";
    }

    @ResponseBody
    @RequiresAuthentication
    @GetMapping("/get.json")
    @ApiOperation(value = "获取用户详情")
    public ApiResult<CustomerVo> get(@Valid @ModelAttribute CustomerGetDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        Customer customer = customerService.get(dto.getTid(), dto.getId());
        return new ApiResult<>(customer, CustomerVo.class);
    }
    @ResponseBody
    @RequiresRoles("operator")
    @GetMapping("/list.json")
    @ApiOperation(value = "获取用户列表")
    public ApiResults<CustomerVo> list(@ModelAttribute CustomPage page, @Valid @ModelAttribute CustomerListDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        PageInfo<Customer> customers = customerService.list(dto.isTenantOnly() ? dto.getTid() : null, dto.getName(), dto.getKeyword(), page.getPageIndex(), page.getPageSize());
        return new ApiResults<>(customers, CustomerVo.class);
    }
    @ResponseBody
    @RequiresRoles("operator")
    @PostMapping("/add.json")
    @ApiOperation(value = "新增客户信息")
    public ApiResult<Long> add(@Valid @ModelAttribute CustomerAddDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        long id = customerService.add(dto.getTid(), dto.getUsername(), dto.getRealname(), dto.getGender(), dto.getBirthday(), dto.getAddress(), dto.getMobile(), dto.getPid(), dto.getEmail(), dto.getAvatar(), dto.getSpell(), dto.getCapital());
        return new ApiResult<>(id);
    }
    @ResponseBody
    @RequiresAuthentication
    @PostMapping("/update.json")
    @ApiOperation(value = "修改用户信息")
    public ApiResult update(@Valid @ModelAttribute CustomerUpdateDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isPermitted("emphealth_customer.edit")) {
            if (!subject.hasRole(CustomerRealm.CustomerRole)) throw new UnauthorizedException();
            Customer customer = Customer.getLoginUser();
            dto.setId(customer.getId());
        } else if (dto.getId() == null) {
            throw new IllegalArgumentException("请输入用户ID！");
        }
        customerService.update(dto.getId(), dto.getUsername(), dto.getNickname(), dto.getRealname(), dto.getGender(), dto.getBirthday(), dto.getAddress(), dto.getMobile(), dto.getEmail(), dto.getPid(), dto.getAvatar(), dto.getSpell(), dto.getCapital());
        return new ApiResult();
    }


    @ResponseBody
    @GetMapping("/info.json")
    @RequiresRoles(CustomerRealm.CustomerRole)
    @ApiOperation(value = "获取登录用户详情")
    public ApiResult<CustomerVo> get(@Valid @ModelAttribute BaseDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        Customer customer = Customer.getLoginUser();
        customer = customerService.get(null, customer.getId());
        return new ApiResult<>(customer, CustomerVo.class);
    }

    @ResponseBody
    @ApiOperation("微信用户登录")
    @PostMapping("/weixin/login.json")
    public ApiResult<String> login(@Valid @ModelAttribute WexinLoginDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        Subject subject = (new Subject.Builder()).buildSubject();
        String openid = wxmpUserService.getOpenidByCode(0L, dto.getCode());
        CustomerToken wxToken = new CustomerToken(0L, openid);
        subject.getSession().setTimeout(-1);
        try {
            ThreadContext.bind(subject);
            subject.login(wxToken);
        } catch (AuthenticationException e) {
            return new ApiResult<>(-20, "用户未绑定");
        }
        String token = subject.getSession().getId().toString();
        return new ApiResult<>(token);
    }
    @ResponseBody
    @ApiOperation("微信访问Token")
    @PostMapping("/weixin/token.json")
    public ApiResult<String> token(@Valid @ModelAttribute WexinLoginDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        String token = wxmpService.getToken(0L, false);
        return new ApiResult<>(token);
    }


    @ResponseBody
    @GetMapping("/spell.json")
    @RequiresAuthentication
    @ApiOperation(value = "获取姓名拼音")
    public ApiResult<SpellVo> get(@Valid @ModelAttribute SpellVo dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        SpellVo vo = new SpellVo();
        vo.setName(dto.getName());
        vo.setSpell(PinyinUtil.toPinyin(dto.getName()));
        vo.setCapital(PinyinUtil.toCapital(dto.getName()));
        return new ApiResult<>(vo);
    }
}

