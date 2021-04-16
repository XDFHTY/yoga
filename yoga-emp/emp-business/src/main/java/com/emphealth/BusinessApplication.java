package com.emphealth;

import com.yoga.admin.AdminApplication;
import com.yoga.core.CoreApplication;
import com.yoga.logging.LoggingApplication;
import com.yoga.operator.OperatorApplication;
import com.yoga.resource.ResourceApplication;
import com.yoga.setting.SettingApplication;
import com.yoga.tenant.TenantApplication;
import com.yoga.utility.UtilityApplication;
import com.yoga.weixinapp.WeixinappApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class BusinessApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(
                CoreApplication.class,
                LoggingApplication.class,
                OperatorApplication.class,
                ResourceApplication.class,
                SettingApplication.class,
                TenantApplication.class,
                UtilityApplication.class,

                AdminApplication.class,
                WeixinappApplication.class,
                BusinessApplication.class
        )
                .web(WebApplicationType.SERVLET)
                .run(args);
    }

}
