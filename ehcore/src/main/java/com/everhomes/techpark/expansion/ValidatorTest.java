package com.everhomes.techpark.expansion;

import org.aopalliance.aop.Advice;
import org.springframework.aop.Advisor;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

/**
 * Created by Administrator on 2017/8/1.
 */
@Configuration
public class ValidatorTest {


    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }
//    @Bean
//    public LocalValidatorFactoryBean getLocalValidatorFactoryBean() {
//        return new LocalValidatorFactoryBean();
//    }

//    @Bean
//    public Pointcut getPointcut() {
////        return new NameMatchMethodPointcut().addMethodName("test111");
////        return new AnnotationMatchingPointcut(NotBlank.class);
//        return AnnotationMatchingPointcut.forMethodAnnotation(NotBlank.class);
//    }

//    @Bean
//    public Advice getAdvice() {
//        return new TestMethodInterceptor();
//    }
//    @Bean
//    public ProxyFactoryBean getProxyFactoryBean() {
//
//        ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
//        proxyFactoryBean.setTargetName("controllerTest");
//        proxyFactoryBean.setInterceptorNames(new String[]{"getAdvisor"});
//
//        return proxyFactoryBean;
//    }
//    @Bean
//    public Advisor getAdvisor() {
//        return new DefaultPointcutAdvisor(getPointcut(), getAdvice());
//    }

}
