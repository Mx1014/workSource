package com.everhomes.aclink;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul> 查询转发的消息
 * <li>doorId: 门禁ID</li>
 * <li>urgent: 是否紧急消息， 1表示紧急</li>
 * <li>inputs: 之前的返回消息 {@link com.everhomes.aclink.DoorMessageResp}</li>
 * </ul>
 * @author janson
 *
 */
public class QueryDoorMessageCommand {
    @NotNull
    String hardwareId;
    
    Byte urgent;
    
    @ItemType(DoorMessageResp.class)
    List<DoorMessageResp> inputs;


    public String getHardwareId() {
        return hardwareId;
    }

    public void setHardwareId(String hardwareId) {
        this.hardwareId = hardwareId;
    }

    public List<DoorMessageResp> getInputs() {
        return inputs;
    }

    public void setInputs(List<DoorMessageResp> inputs) {
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
