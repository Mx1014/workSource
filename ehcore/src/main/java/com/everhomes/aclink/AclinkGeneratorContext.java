package com.everhomes.aclink;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.everhomes.rest.aclink.DoorMessage;

public class AclinkGeneratorContext {
    Map<Byte, List<DoorCommand>> orderMessages;
    List<DoorMessage> genMessages;
    
    int validSize;
    
    public AclinkGeneratorContext() {
        orderMessages = new HashMap<Byte, List<DoorCommand>>();
        genMessages = new ArrayList<DoorMessage>();
    }
    
    private int sizeOf(Byte cmdId) {
        List<DoorCommand> ds = getMessages(cmdId);
        if (ds == null) {
            return 0;
        }
        return ds.size();
    }
    
    private Byte orderId(Byte cmdId) {
        AclinkCommandType id = AclinkCommandType.fromCode(cmdId);
        if(id == AclinkCommandType.RM_UNDO_LIST) {
            return Byte.valueOf(AclinkCommandType.ADD_UNDO_LIST.getCode());
        }
        
        return cmdId;
    }
    
    private boolean checkValid(DoorCommand cmd) {
        AclinkCommandType t = AclinkCommandType.fromCode(cmd.getCmdId());
        if (t == null) {
            return false;
        }
        
        switch(t) {
        case ADD_UNDO_LIST:
            return true;
        case RM_UNDO_LIST:
            return true;
        default:
            if (sizeOf(cmd.getCmdId()) == 0) {
                return true;
            } else {
                return false;
            }
        }
    }
    
    public boolean putMessage(DoorCommand cmd) {
        if(!checkValid(cmd)) {
            return false;
        }
        
        List<DoorCommand> cmdList = orderMessages.get(orderId(cmd.getCmdId()));
        if(cmdList == null) {
            cmdList = new ArrayList<DoorCommand>();
            orderMessages.put(cmd.getCmdId(), cmdList);
        }
        cmdList.add(cmd);
        validSize++;
        
        //added
        return true;
    }
    
    private List<DoorCommand> getMessages(Byte cmdId) {       
        if(orderId(cmdId) != cmdId) {
            return new ArrayList<DoorCommand>();
        }
        List<DoorCommand> cmds = orderMessages.get(orderId(cmdId));
        //Never return null;
        if(null == cmds) {
            return new ArrayList<DoorCommand>();
        }
        
        return cmds;
    }
    
    public List<DoorCommand> getOrderMessages() {
        List<DoorCommand> cmds = new ArrayList<DoorCommand>();
        
        for(AclinkCommandType t : AclinkCommandType.accessOrder()) {
            cmds.addAll(getMessages(t.getCode()));
        }
        
        return cmds;
    }
    
    public List<DoorMessage> getDoorMessages() {
        return genMessages;
    }
    
    public int size() {
        return validSize;
    }
}
