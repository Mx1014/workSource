package com.everhomes.rest.aclink;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>doorId: 门禁 ID</li>
 * <li>outputs: 返回门禁消息列表</li>
 * </ul>
 * @author janson
 *
 */
public class QueryDoorMessageResponse {
    @NotNull
    Long doorId;
    
    @ItemType(DoorMessage.class)
    List<DoorMessage> outputs;

    public Long getDoorId() {
        return doorId;
    }

    public void setDoorId(Long doorId) {
        this.doorId = doorId;
    }

    public List<DoorMessage> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<DoorMessage> outputs) {
        this.outputs = outputs;
    }
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
