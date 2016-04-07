package com.everhomes.controller;

import java.util.HashMap;
import java.util.Map;
import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import com.everhomes.rest.aclink.ConnectingRestResponse;
import com.everhomes.rest.user.AppIdStatusRestResponse;
import com.everhomes.util.StringHelper;
import com.google.gson.Gson;

public class AclinkHandshakeInterceptor implements HandshakeInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(AclinkHandshakeInterceptor.class);
    
    @Autowired
    private HttpRestCallProvider httpRestCallProvider;
    
    @Autowired
    private AclinkWebSocketHandler aclinkWebSocketHandler;
    
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
            Map<String, Object> attributes) throws Exception {
        URI uri = request.getURI();
        String path = uri.getPath();
        String prefix = "/aclink/";
        if(path.startsWith(prefix)) {
            path = path.substring(prefix.length());
            String[] vs = path.split("/", 2);
            if(vs.length != 2) {
                response.setStatusCode(HttpStatus.BAD_REQUEST);
                return false;
            }
            
            Map<String, String> params = new HashMap<String, String>();
            params.put("uuid", vs[0]);
            params.put("encryptBase64", vs[1]);
            
            ResponseEntity<String> result = httpRestCallProvider.syncRestCall("/aclink/connecting", params);
            if(result.getStatusCode() != HttpStatus.OK) {
                response.setStatusCode(result.getStatusCode());
                return false;
            }
            
            ConnectingRestResponse resp = (ConnectingRestResponse) StringHelper.fromJsonString(result.getBody(), ConnectingRestResponse.class);
            
            if(resp == null || resp.getErrorCode() == null) {
                response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
                return false;   
            }
            
            if(!resp.getErrorCode().equals(200)) {
                response.setStatusCode(HttpStatus.FORBIDDEN);
                return false;
            }
            
            //AclinkWebSocketHandler handler = (AclinkWebSocketHandler)wsHandler;
            aclinkWebSocketHandler.beforeHandshake(vs[0], resp.getResponse());
        }
        
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
            Exception exception) {
    }

}
