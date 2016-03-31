package com.everhomes.aclink;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum AclinkCommandType {
    CMD_ACTIVE((byte)0x1), INIT_SERVER_KEY((byte)0x2), CMD_SET_SERVER_KEY((byte)0x3), CMD_UPDATE_DEVNAME((byte)0x4),
    CMD_UPDATE_TIME((byte)0x5), ADD_UNDO_LIST((byte)0x6), NEW_USER_CONN((byte)0x7), OPEN_DOOR((byte)0x8), ACCESS_LOG((byte)0x9),
    REMOTE_OPEN((byte)0xA), WIFI_MGMR_SSID((byte)0xB), WIFI_CURR_SSID((byte)0xC), RM_UNDO_LIST((byte)0xD);
    
    private byte code;
    
    static List<AclinkCommandType> slist = new ArrayList<AclinkCommandType>();
    
    private AclinkCommandType(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    private static void init() {
        if(slist.size() == 0) {
            synchronized(slist) {
                for(int i = 0; i <= RM_UNDO_LIST.getCode(); i++) {
                    slist.add(null);
                }
                
                for(AclinkCommandType t : AclinkCommandType.values()) {
                    slist.set(t.getCode(), t);
                }
            }
        }
    }
    
    public static AclinkCommandType fromCode(int index) {
        init();
        if(index > slist.size()) {
            return null;
        }
        
        return slist.get(index);
    }
    
    public static List<AclinkCommandType> accessOrder() {
        return Arrays.asList(CMD_ACTIVE, INIT_SERVER_KEY, CMD_UPDATE_TIME, CMD_SET_SERVER_KEY, CMD_UPDATE_DEVNAME, RM_UNDO_LIST, ADD_UNDO_LIST, ACCESS_LOG);
    }
}
