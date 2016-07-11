// @formatter:off
package com.everhomes.queue;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.bigcollection.BigCollectionProvider;

@Component
public class DispatchQueueProviderImpl implements DispatchQueueProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(DispatchQueueProviderImpl.class);

    @Autowired
    private BigCollectionProvider bigCollectionProvider;
    
    @Autowired 
    private DispatchQueueFactoryBean queueFactory;
    
    private Map<String, DispatchQueue> dispatchQueues = new HashMap<>();
        
    @Override
    public DispatchQueue getQueue(String name) {
        DispatchQueue queue = null;
        synchronized(dispatchQueues) {
            queue = dispatchQueues.get(name);
            if(queue == null) {
                try {
                    queue = queueFactory.getObject();
                } catch (Exception e) {
                    LOGGER.error("Unexepected exception", e);
                    assert(false);
                }
                
                dispatchQueues.put(name, queue);
                ((DispatchQueueImpl)queue).start(name);
            }
        }
        return queue;
    }
}
