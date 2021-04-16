<#include "/paging.component.ftl">
<#include "/form.component.ftl">
<#include "/table.component.ftl">
<#include "/page.component.ftl">
<#include "/input.component.ftl">
<#include "/modal.component.ftl">
<#setting number_format="##0.##">
<@html>
    <@head includeDate=true includeUploader=true>
        <style>
            td {
                vertical-align: middle!important;
            }
        </style>
    </@head>
    <@bodyFrame>
        <@crumbRoot name="体检业务" icon="icon-user">
            <@crumbItem href="#" name="设备列表" />
        </@crumbRoot>
        <@bodyContent>
            <@panel>
                <@panelHeading "设备列表" />
                <@panelBody>
                    <@inlineForm class="margin-b-15">
                        <@formLabelGroup class="margin-r-15" label="关键字">
                            <@inputText name="keyword" value="${(param.keyword)!}"/>
                        </@formLabelGroup>
                        <@inputSubmit text="搜索" icon="icon icon-search" class="btn btn-success"/>
                        <@rightAction>
                            <@shiro.hasPermission name="emphealth_equipment.edit" >
                                <@inputButton text="添加" icon="icon-plus" class="btn btn-primary" onclick="doAdd();" />
                            </@shiro.hasPermission>
                        </@rightAction>
                    </@inlineForm>
                    <@table>
                        <@thead>
                            <@tr>
                                <@th 5 true>设备ID</@th>
                                <@th 20>设备编码</@th>
                                <@th 10>设备类型</@th>
                                <@th>备注</@th>
                                <@th 15 true>操作</@th>
                            </@tr>
                        </@thead>
                    <tbody>
                        <#list equipments! as equipment>
                            <@tr>
                                <@td true>${(equipment.id?c)!}</@td>
                                <@td>${equipment.imei!}</@td>
                                <@td>${equipment.type!}</@td>
                                <@td>${equipment.remark!}</@td>
                                <@td true>
                                    <@shiro.hasPermission name="emphealth_equipment.edit" >
                                        <a href="javascript:void(0)" class="btn btn-sm btn-primary" onclick="doEdit(${(equipment.id?c)!})">
                                            <i class="icon icon-edit"></i>
                                            编辑
                                        </a>
                                        <a href="javascript:void(0)" class="btn btn-sm btn-danger" onclick="doDelete(${(equipment.id?c)!})">
                                            <i class="icon icon-remove"></i>
                                            删除
                                        </a>
                                    </@shiro.hasPermission>
                                </@td>
                            </@tr>
                        </#list>
                    </tbody>
                    </@table>
                </@panelBody>
                <@panelPageFooter action="/admin/emphealth/equipment" />
            </@panel>
        </@bodyContent>
    </@bodyFrame>


    <@modal title="设备编辑" onOk="doSave">
        <@inputHidden name="id" id="edit_id"/>
        <@formText name="imei" label="设备编码：" />
        <@formText name="type" label="设备类型：" />
        <@formTextArea name="remark" label="备注信息：" />
    </@modal>
    <script>
        function doSave() {
            var id = $("#edit_id").val();
            var json = $("#add_form").serialize();
            $.post(id == 0 ? "/admin/emphealth/equipment/add.json" : "/admin/emphealth/equipment/update.json",
                json,
                function (data) {
                    if (data.code < 0) {
                        alertShow("warning", data.message, 3000);
                    } else {
                        $("#add_modal").modal("hide");
                        window.location.reload();
                    }
                },
                "json"
            );
        }
        function doAdd(parentId) {
            $("#add_form")[0].reset();
            $("#add_form input[name='id']").val(0);
            $("#add_form input[name='imei']").removeAttr("readonly");
            $("#add_modal").modal("show");
        }
        function doEdit(id) {
            $("#add_form")[0].reset();
            $.get(
                "/admin/emphealth/equipment/get.json?id=" + id,
                function (data) {
                    if (parseInt(data.code) < 0) {
                        alertShow("danger", data.message, 3000);
                    } else {
                        console.log(data)
                        $("#add_form input[name='id']").val(id);
                        $("#add_form input[name='imei']").val(data.result.imei);
                        $("#add_form input[name='imei']").attr("readonly", "readonly");
                        $("#add_form input[name='type']").val(data.result.type);
                        $("#add_form textarea[name='remark']").val(data.result.remark);
                        $("#add_modal").modal("show");
                    }
                }
            );
        }
        function doDelete(id) {
            warningModal("确定要删除该设备吗?", function(){
                $.ajax({
                    url: "/admin/emphealth/equipment/delete.json?id=" + id,
                    type: 'DELETE',
                    success: function (data) {
                        if (data.code < 0) {
                            alertShow("danger", data.message, 3000);
                        } else {
                            window.location.reload();
                        }
                    }
                });
            });
        }
    </script>
</@html>
