package com.ddgs.interceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author: codeyung  E-mail:yjc199308@gmail.com
 * @date: 2017/11/23.下午1:44
 */
@Configuration
public class MyWebAppConfiguer extends WebMvcConfigurerAdapter {

    /**
     * 不@Bean service 为 null
     * 拦截器加载的时间点在springcontext之前，所以在拦截器中注入自然为null
     *
     * @return
     */
    @Bean
    public HandlerInterceptor getLoginInterceptor() {
        return new OAuth2Interceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        HandlerInterceptor interceptor = this.getLoginInterceptor();
        registry.addInterceptor(interceptor).excludePathPatterns("/oauth2/**");
        super.addInterceptors(registry);
    }
}
