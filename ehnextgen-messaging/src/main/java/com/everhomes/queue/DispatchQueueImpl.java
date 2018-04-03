// @formatter:off
package com.everhomes.queue;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.everhomes.bigcollection.Accessor;
import com.everhomes.bigcollection.BigCollectionProvider;
import com.everhomes.bus.LocalBusOneshotSubscriber;
import com.everhomes.bus.LocalBusOneshotSubscriberBuilder;
import com.everhomes.bus.LocalBusProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.WebRequestSequence;
import com.everhomes.util.RuntimeErrorException;

public class DispatchQueueImpl implements DispatchQueue {
    private static final Logger LOGGER = LoggerFactory.getLogger(DispatchQueueImpl.class);

    private static final int DEFAULT_EXEC_WAIT_MILLS = 10000;
    
    @Autowired
    private BigCollectionProvider bigCollectionProvider;
    
    @Autowired
    private LocalBusProvider localBusProvider;
    
    @Autowired
    private LocalBusOneshotSubscriberBuilder localBusSubscriberBuilder;
    
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    
    private String name;
    private Accessor accessor;
    
    public DispatchQueueImpl() {
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public void execAsync(DispatchableCommand command) {
        accessor.getTemplate().opsForList().rightPush(this.name, new DispatchQueueCommandPdu(command,
                WebRequestSequence.current().getRequestSequence()));
    }

    @Override
    public Object exec(DispatchableCommand command) {
        return exec(command, DEFAULT_EXEC_WAIT_MILLS);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public Object exec(DispatchableCommand command, int timeoutMs) {
        String requestUuid = UUID.randomUUID().toString();
        
        DispatchQueueCommandResponsePdu[] response = new DispatchQueueCommandResponsePdu[1];
        
        localBusSubscriberBuilder.build("global." + requestUuid, new LocalBusOneshotSubscriber() {
            @Override
            public Action onLocalBusMessage(Object sender, String subject,
                    Object args, String path) {
                
                if(args instanceof DispatchQueueCommandResponsePdu) {
                    response[0] = (DispatchQueueCommandResponsePdu)args;
                } else {
                    LOGGER.error("Invalid response object when processing request {}", 
                            WebRequestSequence.current().getRequestSequence());
                    
                    response[0] = new DispatchQueueCommandResponsePdu(RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, 
                            ErrorCodes.ERROR_GENERAL_EXCEPTION, "Invalid response"));
                }
                
                synchronized(response) {
                    response.notifyAll();
                }
                return null;
            }

            @Override
            public void onLocalBusListeningTimeout() {
                LOGGER.error("Could not get response in time when processing request {}", 
                    WebRequestSequence.current().getRequestSequence());
                
                response[0] = new DispatchQueueCommandResponsePdu(RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, 
                    ErrorCodes.ERROR_GENERAL_EXCEPTION, "Internal excecution error"));
                
                synchronized(response) {
                    response.notifyAll();
                }
            }
        }).setTimeout(timeoutMs).create();
        
        accessor.getTemplate().opsForList().rightPush(this.name, new DispatchQueueCommandPdu(requestUuid, command,
                WebRequestSequence.current().getRequestSequence()));
        
        synchronized(response) {
            try {
                response.wait();
            } catch (InterruptedException e) {
            }
        }
        
        if(response[0].getExecutionException() != null)
            throw response[0].getExecutionException();
            
        return response[0].getExecutionResponse();
    }

    @SuppressWarnings("unchecked")
    public void start(String name) {
        this.name = name;
        this.accessor = this.bigCollectionProvider.getListAcccesor(name);
        
        executor.submit(()-> {
            while(true) {
                DispatchQueueCommandPdu commandPdu = (DispatchQueueCommandPdu)accessor.getTemplate().opsForList().leftPop(name, 
                    1000, TimeUnit.MILLISECONDS);
                
                if(commandPdu != null) {
                    LOGGER.trace("processStat request {}", commandPdu.getOriginationSequence());
                    
                    DispatchQueueCommandResponsePdu responsePdu = new DispatchQueueCommandResponsePdu();
                    try {
                        WebRequestSequence.current().setRequenceSequence(commandPdu.getOriginationSequence());
                        Object result = commandPdu.getCommand().call();
                        responsePdu.setExecutionResponse(result);
                    } catch (Exception e) {
                        LOGGER.error("Exception in processing request {}", commandPdu.getOriginationSequence());
                        responsePdu.setExecutionException(RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, 
                                ErrorCodes.ERROR_GENERAL_EXCEPTION, "Internal excecution error"));
                    }
                    
                    if(commandPdu.getRequestUuid() != null) {
                        LOGGER.trace("Post response on bus for request {}", commandPdu.getOriginationSequence());
                        localBusProvider.publish(null, "global." + commandPdu.getRequestUuid(), responsePdu);
                    }
                }
            }
        });
    }
}
