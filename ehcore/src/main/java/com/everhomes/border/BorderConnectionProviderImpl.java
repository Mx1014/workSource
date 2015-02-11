// @formatter:off
package com.everhomes.border;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.util.RuntimeErrorException;

/**
 * WebSocket based Core-Border connection management
 * 
 * @author Kelven Yang
 *
 */
@Component
public class BorderConnectionProviderImpl implements BorderConnectionProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(BorderConnectionProviderImpl.class);
    
    @Autowired
    private BorderConnectionFactoryBean connectionFactory;
    
    private Map<Integer, BorderConnection> borderConnections = new HashMap<Integer, BorderConnection>();
    
    public BorderConnection getBorderConnection(int borderId) {
        BorderConnection connection = null;
        synchronized(this) {
            connection = borderConnections.get(borderId);
            
            if(connection == null) {
                try {
                    connection = connectionFactory.getObject();
                } catch (Exception e) {
                    LOGGER.error("Unexpected exception", e);
                    throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, "Unexpected exception");
                }
                
                borderConnections.put(borderId, connection);
            }
        }

        try {
            connection.connect(borderId);
        } catch(Exception e) {
            LOGGER.error("Unexpected exception", e);
            
            // swallow the exception, rely on auto-reconnect or cleanup routine to manage it
        }
        
        return connection;
    }
}
