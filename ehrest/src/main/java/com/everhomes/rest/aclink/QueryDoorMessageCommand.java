package com.everhomes.rest.aclink;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul> 查询转发的消息
 * <li>doorId: 门禁ID</li>
 * <li>urgent: 是否紧急消息， 1表示紧急</li>
 * <li>inputs: 之前的返回消息 {@link com.everhomes.rest.aclink.DoorMessageResp}</li>
 * </ul>
 * @author janson
 *
 */
public class QueryDoorMessageCommand {
    @NotNull
    String hardwareId;
    
    Byte urgent;
    
    @ItemType(DoorMessage.class)
    List<DoorMessage> inputs;


    public String getHardwareId() {
        return hardwareId;
    }

    public void setHardwareId(String hardwareId) {
        this.hardwareId = hardwareId;
    }

    public List<DoorMessage> getInputs() {
        return inputs;
    }

    public void setInputs(List<DoorMessage> inputs) {
        this.inputs = inputs;
    }

    public Byte getUrgent() {
        return urgent;
    }

    public void setUrgent(Byte urgent) {
        this.urgent = urgent;
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
    
}
