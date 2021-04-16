package com.emphealth.business.equipment.controller;

import com.emphealth.business.equipment.dto.*;
import com.emphealth.business.equipment.model.Equipment;
import com.emphealth.business.equipment.service.EquipmentService;
import com.emphealth.business.equipment.vo.EquipmentVo;
import com.github.pagehelper.PageInfo;
import com.yoga.core.base.BaseController;
import com.yoga.core.data.ApiResult;
import com.yoga.core.data.ApiResults;
import com.yoga.core.data.CommonPage;
import com.yoga.core.exception.IllegalArgumentException;
import com.yoga.setting.customize.CustomPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin/emphealth/equipment")
@Api(tags = "EMP设备管理")
public class EquipmentController extends BaseController {

    @Autowired
    private EquipmentService equipmentService;

    @ApiIgnore
    @RequiresPermissions("emphealth_equipment")
    @RequestMapping("")
    public String equipments(ModelMap model, CustomPage page, @Valid EquipmentListDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        PageInfo<Equipment> equipments = equipmentService.list(dto.getTid(), dto.getKeyword(), page.getPageIndex(), page.getPageSize());
        model.put("equipments", equipments.getList());
        model.put("page", new CommonPage(equipments));
        model.put("param", dto.wrapAsMap());
        return "/admin/emphealth/equipment/list";
    }


    @ResponseBody
    @RequiresAuthentication
    @GetMapping("/list.json")
    @ApiOperation(value = "设备列表")
    public ApiResults<EquipmentVo> list(CustomPage page, @Valid @ModelAttribute EquipmentListDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        PageInfo<Equipment> equipments = equipmentService.list(dto.getTid(), dto.getKeyword(), page.getPageIndex(), page.getPageSize());
        return new ApiResults<>(equipments, EquipmentVo.class);
    }
    @ResponseBody
    @RequiresAuthentication
    @GetMapping("/get.json")
    @ApiOperation(value = "设备详情")
    public ApiResult<EquipmentVo> get(@Valid @ModelAttribute EquipmentGetDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        Equipment equipment = equipmentService.get(dto.getTid(), dto.getId());
        return new ApiResult<>(equipment, EquipmentVo.class);
    }


    @ResponseBody
    @RequiresRoles("operator")
    @PostMapping("/add.json")
    @ApiOperation(value = "添加设备")
    public ApiResult<Long> add(@Valid @ModelAttribute EquipmentAddDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        long id = equipmentService.add(dto.getTid(), dto.getImei(), dto.getType(), dto.getRemark());
        return new ApiResult<>(id);
    }
    @ResponseBody
    @RequiresPermissions("emphealth_equipment.edit")
    @DeleteMapping("/delete.json")
    @ApiOperation(value = "删除设备怒")
    public ApiResult delete(@Valid @ModelAttribute EquipmentDeleteDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        equipmentService.delete(dto.getTid(), dto.getId());
        return new ApiResult();
    }
    @ResponseBody
    @RequiresPermissions("emphealth_equipment.edit")
    @PostMapping("/update.json")
    @ApiOperation(value = "修改设备")
    public ApiResult update(@Valid @ModelAttribute EquipmentUpdateDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        equipmentService.update(dto.getTid(), dto.getId(), dto.getType(), dto.getRemark());
        return new ApiResult();
    }
}

