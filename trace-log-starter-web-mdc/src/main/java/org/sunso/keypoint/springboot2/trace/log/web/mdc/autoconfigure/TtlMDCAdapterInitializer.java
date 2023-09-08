package org.sunso.keypoint.springboot2.trace.log.web.mdc.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.TtlMDCAdapter;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

/**
 *
 * 初始化TtlMDCAdapter实例，并替换MDC中的adapter对象
 *
 */
@Slf4j
public class TtlMDCAdapterInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        log.info("TtlMDCAdapterInitializer init..................");
        //加载TtlMDCAdapter实例
        TtlMDCAdapter.getInstance();
    }
}