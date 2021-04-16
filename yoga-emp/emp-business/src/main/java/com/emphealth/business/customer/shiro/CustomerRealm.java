package com.emphealth.business.customer.shiro;

import com.emphealth.business.customer.model.Customer;
import com.emphealth.business.customer.service.CustomerService;
import com.yoga.logging.service.LoggingService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component("empHealthCustomerRealm")
public class CustomerRealm extends AuthorizingRealm {

    public final static String CustomerRole = "Emphealth.Customer";

    @Lazy
    @Autowired
    private CustomerService customerService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        Object principal = super.getAvailablePrincipal(principalCollection);
        if (principal instanceof CustomerPrincipal) return ((CustomerPrincipal)principal).getAuthorizationInfo();
        else return null;
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof CustomerToken;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        CustomerToken token = (CustomerToken) authenticationToken;
        Customer customer = customerService.login(token.getUsername());
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        Set<String> roles = new HashSet<>();
        roles.add(CustomerRole);
        info.setRoles(roles);
        Session session = SecurityUtils.getSubject().getSession();
        session.setAttribute("user", customer);
        LoggingService.add(customer.getId(), CustomerService.ModuleName, "微信登录", customer.getId());
        return new SimpleAuthenticationInfo(new CustomerPrincipal(customer.getTenantId(), customer.getId(), info), token.getPassword(), this.getName());
    }
}
