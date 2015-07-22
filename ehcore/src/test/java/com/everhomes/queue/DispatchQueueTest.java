package com.everhomes.queue;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.everhomes.junit.CoreServerTestCase;

public class DispatchQueueTest extends CoreServerTestCase {

    @Autowired
    private DispatchQueueProvider queueProvider;
    
    private DispatchQueue queue;
    
    @Before
    public void setup() {
        this.queue = this.queueProvider.getQueue("test");
    }
    
    @Test
    public void queuedCommandTest() {
        String result = (String)queue.exec(()-> {
            return "Hello, world";
        });
        
        System.out.println(result);
    }
}
