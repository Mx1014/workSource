// @formatter:off
package com.everhomes.queue;

import org.springframework.stereotype.Component;

import com.everhomes.spring.AbstractAutowiringFactoryBean;

@Component
public class DispatchQueueFactoryBean extends AbstractAutowiringFactoryBean<DispatchQueue> {
    public DispatchQueueFactoryBean() {
        this.setSingleton(false);
    }
    
    @Override
    protected DispatchQueue doCreateInstance() {
        DispatchQueueImpl queue = new DispatchQueueImpl();
        return queue;
    }

    @Override
    public Class<?> getObjectType() {
        return DispatchQueue.class;
    }
}
