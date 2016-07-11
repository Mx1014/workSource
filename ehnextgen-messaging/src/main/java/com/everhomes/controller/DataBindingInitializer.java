package com.everhomes.controller;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

@Component
public class DataBindingInitializer implements WebBindingInitializer {

    @Autowired
    private List<HandlerAdapter> adapaters;

    @PostConstruct
    public void setup() {
        adapaters.forEach(handler -> {
            if (handler instanceof RequestMappingHandlerAdapter) {
                ((RequestMappingHandlerAdapter) handler).setWebBindingInitializer(this);
            }
        });

    }

    @Override
    public void initBinder(WebDataBinder binder, WebRequest request) {
        binder.setAutoGrowCollectionLimit(Integer.MAX_VALUE);
    }

}
