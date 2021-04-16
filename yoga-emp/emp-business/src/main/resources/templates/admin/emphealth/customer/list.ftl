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
            <@crumbItem href="#" name="健管用户" />
        </@crumbRoot>
        <@bodyContent>
            <@panel>
                <@panelHeading "健管用户" />
                <@panelBody>
                    <@inlineForm class="margin-b-15">
                        <@formLabelGroup class="margin-r-15" label="关键字">
                            <@inputText name="keyword" value="${(param.keyword)!}"/>
                        </@formLabelGroup>
                        <@inputSubmit text="搜索" icon="icon icon-search" class="btn btn-success"/>
                    </@inlineForm>
                    <@table>
                        <@thead>
                            <@tr>
                                <@th 15>用户名</@th>
                                <@th 10>姓名</@th>
                                <@th 10>手机号</@th>
                                <@th 15>身份证号</@th>
                                <@th 5 true>体检次数</@th>
                                <@th 15 true>最后登录</@th>
                                <@th 15 true>操作</@th>
                            </@tr>
                        </@thead>
                    <tbody>
                        <#list customers as customer>
                            <@tr>
                                <@td>${customer.username!}</@td>
                                <@td>${customer.realname!}</@td>
                                <@td>${customer.mobile!}</@td>
                                <@td>${customer.pid!}</@td>
                                <@td true>${customer.recordCount!}</@td>
                                <@td true>${(customer.lastLogin?string('yyyy-MM-dd HH:mm:ss'))!}</@td>
                                <@td true>
                                    <@shiro.hasPermission name="emphealth_customer.detail" >
                                        <a href="/admin/emphealth/measure/customer?customerId=${customer.id?c}" class="btn btn-sm btn-primary">
                                            <i class="icon icon-list"></i>
                                            体检历史
                                        </a>
                                    </@shiro.hasPermission>
                                </@td>
                            </@tr>
                        </#list>
                    </tbody>
                    </@table>
                </@panelBody>
                <@panelPageFooter action="/admin/emphealth/customer" />
            </@panel>
        </@bodyContent>
    </@bodyFrame>
</@html>
