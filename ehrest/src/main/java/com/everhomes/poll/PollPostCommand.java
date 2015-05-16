// @formatter:off
package com.everhomes.poll;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class PollPostCommand{
    private Long startTime;
    private Long stopTime;
    private Integer multiChoiceFlag;
    private Integer anonymousFlag;
    
    @ItemType(PollItemDTO.class)
    private List<PollItemDTO> itemList;
    
    public PollPostCommand() {
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getStopTime() {
        return stopTime;
    }

    public void setStopTime(Long stopTime) {
        this.stopTime = stopTime;
    }

    public Integer getMultiChoiceFlag() {
        return multiChoiceFlag;
    }

    public void setMultiChoiceFlag(Integer multiChoiceFlag) {
        this.multiChoiceFlag = multiChoiceFlag;
    }

    public Integer getAnonymousFlag() {
        return anonymousFlag;
    }

    public void setAnonymousFlag(Integer anonymousFlag) {
        this.anonymousFlag = anonymousFlag;
    }
    
    public List<PollItemDTO> getItemList() {
        return itemList;
    }

    public void setItemList(List<PollItemDTO> itemList) {
        this.itemList = itemList;
    }

    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
