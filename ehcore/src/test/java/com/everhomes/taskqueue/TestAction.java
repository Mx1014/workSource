package com.everhomes.taskqueue;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TestAction implements Runnable {
    
    private static final Logger log = LoggerFactory.getLogger(TestAction.class);

    private Integer i;
    private Double d;
    private Boolean b;
    private String s;
    private List<Object> l;

    public TestAction(final Integer i, final Double d, final Boolean b, final String s, final List<Object> l) {
        this.i = i;
        this.d = d;
        this.b = b;
        this.s = s;
        this.l = l;
    }

    public void run() {
        log.info("TestAction.run() {} {} {} {} {}", new Object[] { this.i, this.d, this.b, this.s, this.l });
        try {
            Thread.sleep(100);
        } catch (Exception e) {
        }
    }

    public void setSomeVariable(int myVar) {
        this.i = new Integer(myVar);
    }
}
