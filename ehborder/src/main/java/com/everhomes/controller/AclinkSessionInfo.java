package com.everhomes.controller;

import org.springframework.web.socket.WebSocketSession;

import com.everhomes.rest.aclink.DoorAccessDTO;

public class AclinkSessionInfo {
    private WebSocketSession session;
    private DoorAccessDTO dto;
    
    public WebSocketSession getSession() {
        return session;
    }
    public void setSession(WebSocketSession session) {
        this.session = session;
    }
    public DoorAccessDTO getDto() {
        return dto;
    }
    public void setDto(DoorAccessDTO dto) {
        this.dto = dto;
    }
    
    
}
