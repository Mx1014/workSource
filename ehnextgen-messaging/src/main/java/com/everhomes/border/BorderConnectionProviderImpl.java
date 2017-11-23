// @formatter:off
package com.everhomes.border;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.everhomes.coordinator.CoordinationProvider;

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
    
    @Autowired
    private BorderProvider borderProvider;
    
    @Autowired
    private CoordinationProvider coordinator;
    
    private Map<Integer, BorderConnection> borderConnections = new HashMap<Integer, BorderConnection>();
    
    public BorderConnection getBorderConnection(int borderId) {
        BorderConnection connection = null;
        synchronized(this) {
            connection = borderConnections.get(borderId);
            
            if(connection == null) {
                try {
                    connection = connectionFactory.getObject();
                } catch (Exception e) {
                    LOGGER.error("Failed to connect to border {} due to exception {}", borderId, e.toString());
                    connection.onConnectionFailed();
                }
                
                borderConnections.put(borderId, connection);
            }
        }

        try {
            connection.connect(borderId);
        } catch(Exception e) {
            LOGGER.error("Failed to connect to border server {}: " + e.toString(), borderId);
            
            // swallow the exception, rely on auto-reconnect or cleanup routine to manage it
            connection.onConnectionFailed();
        }
        
        return connection;
    }
    
    @Override
    public void checkBorderConnections() {
        List<Border> allBorders = borderProvider.listAllBorders();
        
        for(Border border : allBorders) {
            this.getBorderConnection(border.getId());
        }
    }
    
    public boolean broadcastToAllBorders(long requestId, Object obj) {
//        Map<Integer, BorderConnection> allBorders = new HashMap<Integer, BorderConnection>();
//        synchronized(this) {
//            borderConnections.forEach((i, conn) -> {
//                allBorders.put(i, conn);
//            });         
//        }
        
        List<Border> allBorders = borderProvider.listAllBorders();
        boolean ok = true;
        
        // 日志过多，先注释掉 by lqs 20171102
        // LOGGER.info("broacastToAll, len: " + allBorders.size());
        
        //outside synchronized      
        for(Border border : allBorders) {
            BorderConnection conn = this.getBorderConnection(border.getId());
            if(conn != null) {
                try {
                    conn.sendMessage(requestId, obj);
                    // 日志过多，先注释掉 by lqs 20171102
                    // LOGGER.info("sended to border, i= " + border.getId());
                } catch (IOException e) {
                    LOGGER.error("BroadcastToAllBorders got unexpected exception", e);
                    ok = false;
                }    
            }
        }
        
        return ok;
    }
    
    @Scheduled(fixedDelay=5000)
    private void scanBorders() {
        this.coordinator.getNamedLock("border.scan").tryEnter(()-> {
            checkBorderConnections();
        });
    }
}
