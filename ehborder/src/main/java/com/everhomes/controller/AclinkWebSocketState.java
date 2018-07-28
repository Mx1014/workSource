// @formatter:off
package com.everhomes.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.socket.WebSocketSession;

import com.everhomes.rest.aclink.AclinkWebSocketMessage;
import com.everhomes.rest.aclink.DataUtil;
import com.everhomes.util.StringHelper;

import org.apache.commons.codec.binary.Base64;

public class AclinkWebSocketState {
    private Long id;
    private String uuid;
    private String hardwareId;
    
    //Check lastTick for alive
    private long lastTick;
    
    //Check sendingTick for resent
    private long sendingTick;
    private int waitingAck;
    
    private int loopCnt;
    
    private static final long TIMEOUT_TICK = 20*1000;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getHardwareId() {
        return hardwareId;
    }

    public void setHardwareId(String hardwareId) {
        this.hardwareId = hardwareId;
    }

    public void onIncomeMessage(AclinkWebSocketMessage msg, AclinkWebSocketHandler handler) {
        
    }
    
    public void onTick(WebSocketSession session, AclinkWebSocketHandler handler) {
        this.lastTick = System.currentTimeMillis();
        this.loopCnt = 0;
        
        if( (-1 == this.waitingAck) || (this.sendingTick + TIMEOUT_TICK) < this.lastTick ) {
            //already timeout or not waiting, got next message and send it
            this.waitingAck = -1;
            
            AclinkWebSocketMessage cmd = new AclinkWebSocketMessage();
            cmd.setId(this.getId());
            //by liuyilin 20180419 TODO local server syncWebsocketMessages
            if(this.getUuid().length() == 6){
            	cmd.setType(1);
            }
            handler.nextMessage(cmd, session, this);
            this.loopCnt++;
        }
    }
    
    public void onMessage(byte[] buf, WebSocketSession session, AclinkWebSocketHandler handler) {
      byte[] bSeq = Arrays.copyOfRange(buf, 6, 10);
      int seq = DataUtil.byteArrayToInt(bSeq);
        
        if( (this.sendingTick + TIMEOUT_TICK) > System.currentTimeMillis() && this.waitingAck == seq) {
            //Not timeout and It's the sequence
            
            //Not waiting now
            this.waitingAck = -1;
            
            if(this.loopCnt > 10) {
                //wait for next tick
                return;
            }
            
            AclinkWebSocketMessage cmd = new AclinkWebSocketMessage();
            cmd.setId(this.getId());
            cmd.setSeq(new Long(seq));
            cmd.setType(new Integer(0));
            cmd.setPayload(Base64.encodeBase64String(Arrays.copyOfRange(buf, 10, buf.length)));
            handler.nextMessage(cmd, session, this);
            this.loopCnt++;
        }
    }
    
    public void onRequest(byte[] buf, WebSocketSession session, AclinkWebSocketHandler handler){
    	AclinkWebSocketMessage cmd = new AclinkWebSocketMessage();
        cmd.setId(this.getId());
        cmd.setSeq(new Long(0));
        cmd.setType(new Integer(0));
        cmd.setPayload(Base64.encodeBase64String(buf));
        Map<String, String> params = new HashMap<String, String>();
        StringHelper.toStringMap(null, cmd, params);
        handler.excuteMessage(params, session, this, "/aclink/excuteMessage");
    }
    
    public void onServerMessage(AclinkWebSocketMessage cmd, WebSocketSession session, AclinkWebSocketHandler handler) {
        if(-1 == this.waitingAck) {
            this.sendingTick = System.currentTimeMillis();
            this.waitingAck = cmd.getSeq().intValue();
        }
    }
    
}
