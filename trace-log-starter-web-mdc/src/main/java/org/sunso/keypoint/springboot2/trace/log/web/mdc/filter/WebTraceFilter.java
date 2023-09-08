package org.sunso.keypoint.springboot2.trace.log.web.mdc.filter;

import cn.hutool.core.util.StrUtil;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;
import org.sunso.keypoint.springboot2.trace.log.common.utils.MDCTraceUtils;

import java.io.IOException;

@Slf4j
public class WebTraceFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String traceId = request.getHeader(MDCTraceUtils.TRACE_ID_HEADER);
            log.info("WebTraceFilter traceId[{}]", traceId);
            if (StrUtil.isEmpty(traceId)) {
                MDCTraceUtils.addTrace();
            } else {
                MDCTraceUtils.putTrace(traceId);
            }
            filterChain.doFilter(request, response);
        } finally {
            MDCTraceUtils.removeTrace();
        }
    }
}
