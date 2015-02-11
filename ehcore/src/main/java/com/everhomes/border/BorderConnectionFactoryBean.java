// @formatter:off
package com.everhomes.border;

import org.springframework.stereotype.Component;

import com.everhomes.spring.AbstractAutowiringFactoryBean;

/**
 * Factory bean to build and wire BorderConnection instances
 * 
 * @author Kelven Yang
 *
 */
@Component
public class BorderConnectionFactoryBean extends AbstractAutowiringFactoryBean<BorderConnection> {

    public BorderConnectionFactoryBean() {
        this.setSingleton(false);
    }
    
    @Override
    protected BorderConnection doCreateInstance() {
        return new BorderConnection();
    }

    @Override
    public Class<?> getObjectType() {
        return BorderConnection.class;
    }
}
