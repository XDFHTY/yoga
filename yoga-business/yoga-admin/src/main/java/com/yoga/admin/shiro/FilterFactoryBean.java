package com.yoga.admin.shiro;

import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.FilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class FilterFactoryBean extends ShiroFilterFactoryBean {
    private static Logger logger = LoggerFactory.getLogger(FilterFactoryBean.class);

    @Autowired
    private OperatorShiroFilter filter;

    @Override
    protected AbstractShiroFilter createInstance() throws Exception {
        FilterChainManager manager = createFilterChainManager();
        PathMatchingFilterChainResolver chainResolver = new PathMatchingFilterChainResolver();
        chainResolver.setFilterChainManager(manager);
        filter.setFilterChainResolver(chainResolver);
        return filter;
    }
}
