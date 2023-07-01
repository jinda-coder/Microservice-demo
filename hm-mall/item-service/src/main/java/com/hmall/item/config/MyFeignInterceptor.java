//package com.hmall.item.config;
//
//import com.hmall.common.BaseContext;
//import feign.RequestInterceptor;
//import feign.RequestTemplate;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//@Slf4j
//@Component
//public class MyFeignInterceptor implements RequestInterceptor {
//    @Override
//    public void apply(RequestTemplate template) {
//
//        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//
//        log.info("===request: {}", template.url());
//        template.header("aaaa", "bbbb");
//        Long currentId = BaseContext.getCurrentId();
//        template.header("authorization",currentId + "");
//        System.out.println("拦截到了请求,用户id为:" + currentId);
//    }
//}