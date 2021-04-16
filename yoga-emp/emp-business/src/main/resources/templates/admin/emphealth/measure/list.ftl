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
            .norm {
                margin-right: 30px;
            }
        </style>
    </@head>
    <@bodyFrame>
        <@crumbRoot name="体检业务" icon="icon-user">
            <@crumbItem href="#" name="体检记录" />
        </@crumbRoot>
        <@bodyContent>
            <@panel>
                <@panelHeading "体检记录" />
                <@panelBody>
                    <@inlineForm class="margin-b-15">
                        <@formLabelGroup class="margin-r-15" label="开始日期：">
                            <@inputDate name="beginDate" value="${(param.beginDate?string('yyyy-MM-dd'))!}" placeholder="" />
                        </@formLabelGroup>
                        <@formLabelGroup class="margin-r-15" label="结束日期：">
                            <@inputDate name="endDate" value="${(param.endDate?string('yyyy-MM-dd'))!}" placeholder="" />
                        </@formLabelGroup>
                        <@formLabelGroup class="margin-r-15" label="关键字">
                            <@inputText name="keyword" value="${(param.keyword)!}"/>
                        </@formLabelGroup>
                        <@inputSubmit text="搜索" icon="icon icon-search" class="btn btn-success"/>
                    </@inlineForm>
                    <@table>
                        <@thead>
                            <@tr>
                                <@th 5 true>#</@th>
                                <@th 20>检查时间</@th>
                                <@th 10>健管用户</@th>
                                <@th 10>工作人员</@th>
                                <@th>检查结果</@th>
                                <@th 15 true>操作</@th>
                            </@tr>
                        </@thead>
                    <tbody>
                        <#list records! as record>
                            <@tr>
                                <@td true>${(record.id?c)!}</@td>
                                <@td>${record.beginTime?string("yyyy-MM-dd HH:mm")!}</@td>
                                <@td>${record.customer!}</@td>
                                <@td>${record.operator!}</@td>
                                <@td>
                                    <#if record.spo2??>
                                        <span class="norm">血氧：${record.spo2}%</span>
                                    </#if>
                                    <#if record.ppg??>
                                        <span class="norm">脉率：${record.ppg}bpm</span>
                                    </#if>
                                    <#if record.sbp??>
                                        <span class="norm">血压：${record.sbp}/${record.dbp}mmHg</span>
                                    </#if>
                                    <#if record.glu??>
                                        <span class="norm">血糖：${record.glu}mmol/L</span>
                                    </#if>
                                    <#if record.temperature??>
                                        <span class="norm">体温：${record.temperature}ºC</span>
                                    </#if>
                                    <#if record.purine??>
                                        <span class="norm">尿酸：${record.purine}umol/L</span>
                                    </#if>
                                </@td>
                                <@td true>
                                    <@shiro.hasPermission name="emphealth_customer.detail" >
                                        <a href="/admin/emphealth/measure/customer?customerId=${record.customerId?c}" class="btn btn-sm btn-primary">
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
                <@panelPageFooter action="/admin/emphealth/measure" />
            </@panel>
        </@bodyContent>
    </@bodyFrame>
</@html>
