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
            .right {
                text-align: right;
            }
            .avatar {
                width: 128px;
                height: 128px;
                text-align: center;
                margin: 0 auto;
                display: block;
            }
        </style>
    </@head>
    <@bodyFrame>
        <@crumbRoot name="体检业务" icon="icon-user">
            <@crumbItem href="#" name="体检记录" backLevel=1/>
            <@crumbItem href="#" name="个人纪录" />
        </@crumbRoot>
        <@bodyContent>
            <div class="panel panel-primary">
                <div class="panel-heading">
                    <a data-toggle="collapse" data-parent="#accordion" href="#infoPanel" style="width: 100%; color: white">
                        <i class="icon icon-comments-o"></i>
                        用户详情
                    </a>
                </div>
                <div id="infoPanel" class="panel-collapse collapse in">
                    <table class="table table-striped table-bordered">
                        <colgroup width="10%"></colgroup>
                        <colgroup width="10%"></colgroup>
                        <colgroup width="30%"></colgroup>
                        <colgroup width="10%"></colgroup>
                        <colgroup width="30%"></colgroup>
                        <colgroup width="10%"></colgroup>
                        <tbody>
                        <tr>
                            <td colspan="6">
                                <img class="avatar" src="/admin/images/ic_avatar.png">
                            </td>
                        </tr>
                        <tr>
                            <td></td>
                            <td class="right">真实姓名：</td>
                            <td>${(customer.realname)!}</td>
                            <td class="right">性别：</td>
                            <td>${(customer.gender.getName())!}</td>
                            <td></td>
                        </tr>
                        <tr>
                            <td></td>
                            <td class="right">身份证号：</td>
                            <td>${(customer.pid)!}</td>
                            <td class="right">生日：</td>
                            <td>${(customer.birthday?string("yyyy-MM-dd"))!}</td>
                            <td></td>
                        </tr>
                        <tr>
                            <td></td>
                            <td class="right">手机号：</td>
                            <td>${(customer.mobile)!}</td>
                            <td class="right">电子邮箱：</td>
                            <td>${(customer.email)!}</td>
                            <td></td>
                        </tr>
                        <tr>
                            <td></td>
                            <td class="right">地址：</td>
                            <td colspan="3">${(customer.address)!}</td>
                            <td></td>
                        </tr>
                    </table>
                </div>
            </div>
            <@panel class="panel-success">
                <@panelHeading "个人体检纪录" />
                <@panelBody>
                    <@inlineForm class="margin-b-15">
                        <input type="hidden" name="customerId" value="${(param.customerId)!}">
                        <@formLabelGroup class="margin-r-15" label="开始日期：">
                            <@inputDate name="beginDate" value="${(param.beginDate?string('yyyy-MM-dd'))!}" placeholder="" />
                        </@formLabelGroup>
                        <@formLabelGroup class="margin-r-15" label="结束日期：">
                            <@inputDate name="endDate" value="${(param.endDate?string('yyyy-MM-dd'))!}" placeholder="" />
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
                            </@tr>
                        </#list>
                    </tbody>
                    </@table>
                </@panelBody>
                <@panelPageFooter action="/admin/emphealth/equipment" />
            </@panel>
        </@bodyContent>
    </@bodyFrame>
</@html>
