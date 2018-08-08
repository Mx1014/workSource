package com.everhomes.controller;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

@Component
public class DataBindingInitializer implements WebBindingInitializer, ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private List<HandlerAdapter> adapaters;

    // 升级平台包到1.0.1，把@PostConstruct换成ApplicationListener，
    // 因为PostConstruct存在着平台PlatformContext.getComponent()会有空指针问题 by lqs 20180516
    //@PostConstruct
    public void setup() {
        adapaters.forEach(handler -> {
            if (handler instanceof RequestMappingHandlerAdapter) {
                ((RequestMappingHandlerAdapter) handler).setWebBindingInitializer(this);
            }
        });

    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(event.getApplicationContext().getParent() == null) {
            setup();
        }
    }
    
    @Override
    public void initBinder(WebDataBinder binder, WebRequest request) {
        binder.setAutoGrowCollectionLimit(Integer.MAX_VALUE);
    }

}
