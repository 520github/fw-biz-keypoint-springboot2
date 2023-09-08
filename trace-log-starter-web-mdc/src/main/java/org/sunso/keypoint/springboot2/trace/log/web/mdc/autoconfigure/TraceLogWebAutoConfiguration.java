package org.sunso.keypoint.springboot2.trace.log.web.mdc.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.sunso.keypoint.springboot2.trace.log.web.mdc.config.TraceLogConfig;
import org.sunso.keypoint.springboot2.trace.log.web.mdc.filter.WebTraceFilter;

@Slf4j
@Configuration
@EnableConfigurationProperties({TraceLogConfig.class})
public class TraceLogWebAutoConfiguration {

    @Bean
    @ConditionalOnProperty(name = "trace.log.enable", havingValue = "true", matchIfMissing = true)
    public FilterRegistrationBean buildTraceFilter() {
        log.info("---------------------buildTraceFilter--------------------");
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setOrder(Integer.MIN_VALUE + 100);
        filterRegistrationBean.setFilter(new WebTraceFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }

}
