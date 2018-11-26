package com.doug.kplate.config;

import com.doug.kplate.common.xss.XssFilter;
import com.doug.kplate.filter.ShiroSessionFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.DispatcherType;
import java.util.HashMap;
import java.util.Map;

/**
 * Filter配置
 *
 */
@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean shiroFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new DelegatingFilterProxy("shiroFilter"));
        //该值缺省为false，表示生命周期由SpringApplicationContext管理，设置为true则表示由ServletContainer管理
        registration.addInitParameter("targetFilterLifecycle", "true");
        registration.setEnabled(true);
        registration.setOrder(Integer.MAX_VALUE - 1);
        registration.addUrlPatterns("/*");
        return registration;
    }

    @Bean
    public FilterRegistrationBean xssFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setDispatcherTypes(DispatcherType.REQUEST);
        registration.setFilter(new XssFilter());
        registration.addUrlPatterns("/*");
        registration.setName("xssFilter");
        registration.setOrder(Integer.MAX_VALUE);
        return registration;
    }

    @Value("${server.servlet.session.timeout}")
    private String serverSessionTimeout;

    @Bean
    public FilterRegistrationBean shiroSessionFilterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new ShiroSessionFilter());
        filterRegistrationBean.setOrder(FilterRegistrationBean.LOWEST_PRECEDENCE);
        filterRegistrationBean.setEnabled(true);
        filterRegistrationBean.addUrlPatterns("/*");
        Map<String, String> initParameters = new HashMap<>();
        initParameters.put("serverSessionTimeout", serverSessionTimeout);
        initParameters.put("excludes", "/favicon.ico,/img/*,/js/*,/css/*");
        filterRegistrationBean.setInitParameters(initParameters);
        return filterRegistrationBean;
    }
}
