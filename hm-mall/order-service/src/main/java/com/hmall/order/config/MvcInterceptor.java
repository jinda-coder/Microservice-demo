package com.hmall.order.config;

import com.hmall.common.BaseContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Configuration
@Slf4j
public class MvcInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authorization = request.getHeader("authorization");
        log.info("获取到的请求头为:{}",authorization);
       if (StringUtils.isEmpty(authorization)){
           response.setStatus(403);
       }
        BaseContext.setCurrentId(Long.parseLong(authorization));
        return true;
    }
}
