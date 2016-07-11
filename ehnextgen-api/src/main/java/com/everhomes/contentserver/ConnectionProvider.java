package com.everhomes.contentserver;

import java.io.IOException;
import java.util.List;

import org.springframework.web.socket.WebSocketSession;

import com.everhomes.rest.rpc.PduFrame;

/**
 * content server channel to communicate via core to content server
 * 
 * @author elians
 *
 */
public interface ConnectionProvider {
    /**
     * say hi to server
     * 
     * @param host internal host
     * @param port port
     * @throws Exception
     */
    void connect(String host, int port) throws Exception;

    /**
     * 
     * @param name namespace
     * @param payLoad content
     * @param host host
     * @param port port
     * @param callback callback handler
     * @throws Exception
     */
    void sendMessage(String name, Object payLoad, String host, int port) throws Exception;

    /**
     * 
     * @param session current session
     * @param name namespace
     * @param payLoad content
     * @throws IOException
     */
    void sendMessage(WebSocketSession session, String name, Object payLoad) throws IOException;

    /**
     * add config item to all proxies
     * 
     * @param frame config frame
     */
    void configServers(PduFrame frame);

    /**
     * remove current session
     * 
     * @param host
     * @param port
     */
    void removeSession(String host, int port);

    /**
     * disconnect
     */
    void cleanAll();

    ContentServer chooseContentServer(List<ContentServer> servers, Long uid);
}
