package com.everhomes.user;

import org.springframework.context.ApplicationEvent;

/**
 * Created by xq.tian on 2017/7/13.
 */
public class BorderRegisterEvent extends ApplicationEvent {

    /**
     * Create a new ApplicationEvent.
     * @param source the component that published the event (never {@code null})
     */
    public BorderRegisterEvent(UserLogin source) {
        super(source);
    }
}
