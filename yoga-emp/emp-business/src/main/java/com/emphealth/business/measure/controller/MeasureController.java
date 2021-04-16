package com.emphealth.business.measure.controller;

import com.emphealth.business.customer.model.Customer;
import com.emphealth.business.customer.service.CustomerService;
import com.emphealth.business.customer.shiro.CustomerRealm;
import com.emphealth.business.equipment.dto.EquipmentStartDto;
import com.emphealth.business.equipment.dto.EquipmentStopDto;
import com.emphealth.business.measure.bo.TreadItem;
import com.emphealth.business.measure.dto.MeasureGetDto;
import com.emphealth.business.measure.dto.MeasureListDto;
import com.emphealth.business.measure.dto.MeasureTrendDto;
import com.emphealth.business.measure.model.HealthRecord;
import com.emphealth.business.measure.service.MeasureService;
import com.emphealth.business.measure.vo.HealthRecordVo;
import com.emphealth.business.measure.vo.TreadItemVo;
import com.github.pagehelper.PageInfo;
import com.yoga.core.base.BaseController;
import com.yoga.core.base.BaseDto;
import com.yoga.core.data.ApiResult;
import com.yoga.core.data.ApiResults;
import com.yoga.core.data.CommonPage;
import com.yoga.core.exception.IllegalArgumentException;
import com.yoga.operator.user.model.User;
import com.yoga.setting.customize.CustomPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin/emphealth/measure")
@Api(tags = "EMP健康检测")
public class MeasureController extends BaseController {

    @Autowired
    private MeasureService measureService;
    @Autowired
    private CustomerService customerService;

    @ApiIgnore
    @RequiresPermissions("emphealth_equipment")
    @RequestMapping("")
    public String records(ModelMap model, CustomPage page, @Valid MeasureListDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        PageInfo<HealthRecord> records = measureService.list(dto.getTid(), null, dto.getBeginDate(), dto.getEndDate(), dto.getKeyword(), page.getPageIndex(), page.getPageSize());
        model.put("records", records.getList());
        model.put("page", new CommonPage(records));
        model.put("param", dto.wrapAsMap());
        return "/admin/emphealth/measure/list";
    }
    @ApiIgnore
    @RequiresPermissions("emphealth_equipment")
    @RequestMapping("/customer")
    public String customerRecords(ModelMap model, CustomPage page, @Valid MeasureListDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        if (dto.getCustomerId() == null) throw new IllegalArgumentException("用户ID不能为空！");
        PageInfo<HealthRecord> records = measureService.list(null, dto.getCustomerId(), dto.getBeginDate(), dto.getEndDate(), dto.getKeyword(), page.getPageIndex(), page.getPageSize());
        model.put("records", records.getList());
        model.put("page", new CommonPage(records));
        model.put("param", dto.wrapAsMap());
        model.put("customer", customerService.get(null, dto.getCustomerId()));
        return "/admin/emphealth/measure/customer";
    }

    @ResponseBody
    @RequiresAuthentication
    @GetMapping("/get.json")
    @ApiOperation(value = "获取测量详情")
    public ApiResult<HealthRecordVo> get(@Valid @ModelAttribute MeasureGetDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        HealthRecord record = measureService.getRecord(dto.getId());
        Subject subject = SecurityUtils.getSubject();
        if (subject.hasRole(CustomerRealm.CustomerRole)) {
            Customer customer = Customer.getLoginUser();
            if (!customer.getId().equals(record.getCustomerId())) throw new UnauthorizedException("只能查询自己的纪录！");
        }
        return new ApiResult<>(record, HealthRecordVo.class);
    }

    @ResponseBody
    @RequiresAuthentication
    @GetMapping("/trend.json")
    @ApiOperation(value = "获取历史数据趋势")
    public ApiResults<TreadItemVo> trend(@Valid @ModelAttribute MeasureTrendDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        Subject subject = SecurityUtils.getSubject();
        if (subject.hasRole(CustomerRealm.CustomerRole)) {
            Customer customer = Customer.getLoginUser();
            dto.setCustomerId(customer.getId());
        }
        if (dto.getCustomerId() == null) throw new IllegalArgumentException("用户ID不能为空！");
        List<TreadItem> items = measureService.listTread(dto.getCustomerId(), dto.getNorm(), dto.getBeginDate(), dto.getEndDate());
        return new ApiResults<>(items, TreadItemVo.class);
    }
    @ResponseBody
    @RequiresRoles(CustomerRealm.CustomerRole)
    @GetMapping("/latest.json")
    @ApiOperation(value = "获取登陆用户最后一次测量数据")
    public ApiResult<HealthRecordVo> latest(@Valid @ModelAttribute BaseDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        Customer customer = Customer.getLoginUser();
        HealthRecord record = measureService.getLatest(customer.getId());
        return new ApiResult<>(record, HealthRecordVo.class);
    }
    @ResponseBody
    @RequiresAuthentication
    @GetMapping("/list.json")
    @ApiOperation(value = "获取测量历史")
    public ApiResults<HealthRecordVo> list(@ModelAttribute CustomPage page, @Valid @ModelAttribute MeasureListDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        Subject subject = SecurityUtils.getSubject();
        if (subject.hasRole(CustomerRealm.CustomerRole)) {
            Customer customer = Customer.getLoginUser();
            dto.setCustomerId(customer.getId());
            dto.setTid(0L);
        }
        PageInfo<HealthRecord> records = measureService.list(dto.getTid(), dto.getCustomerId(), dto.getBeginDate(), dto.getEndDate(), dto.getKeyword(), page.getPageIndex(), page.getPageSize());
        return new ApiResults<>(records, HealthRecordVo.class);
    }
    @ResponseBody
    @GetMapping("/measuring.json")
    @RequiresRoles(CustomerRealm.CustomerRole)
    @ApiOperation(value = "正在测试")
    public ApiResult<HealthRecordVo> measuring(@Valid @ModelAttribute BaseDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        Customer customer = Customer.getLoginUser();
        HealthRecord record = measureService.getFocusRecord(customer.getId());
        if (record == null) return new ApiResult<>(1, "当前未测试！");
        else return new ApiResult<>(record, HealthRecordVo.class);
    }

    @ResponseBody
    @RequiresRoles("operator")
    @PostMapping("/start.json")
    @ApiOperation(value = "开始测量")
    public ApiResult<Long> start(@Valid @ModelAttribute EquipmentStartDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        User user = User.getLoginUser();
        long id = measureService.start(dto.getTid(), dto.getImei(), user.getId(), dto.getCustomerId());
        return new ApiResult<>(id);
    }
    @ResponseBody
    @RequiresRoles("operator")
    @PostMapping("/stop.json")
    @ApiOperation(value = "结束测量")
    public ApiResult stop(@Valid @ModelAttribute EquipmentStopDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        measureService.stop(dto.getId());
        return new ApiResult<>();
    }
}

