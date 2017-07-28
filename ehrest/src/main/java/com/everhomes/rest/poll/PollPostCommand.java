// @formatter:off
package com.everhomes.rest.poll;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>namespaceId: 命名空间</li>
 *     <li>startTime: 开始时间，时间格式为:YYYY-MM-DD hh:mm:ss</li>
 *     <li>stopTime: stopTime</li>
 *     <li>multiChoiceFlag: 多选标记</li>
 *     <li>anonymousFlag: 匿名标记,1 为匿名,0为公开</li>
 *     <li>id: id</li>
 *     <li>tag: tag</li>
 *     <li>itemList: itemList {@link com.everhomes.rest.poll.PollItemDTO}</li>
 * </ul>
 */
public class PollPostCommand {
    private Integer namespaceId;
    private String startTime;
    private String stopTime;
    private Integer multiChoiceFlag;
    private Integer anonymousFlag;
    private transient Long id;
    private String tag;

    @ItemType(PollItemDTO.class)
    private List<PollItemDTO> itemList;

    public PollPostCommand() {
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStopTime() {
        return stopTime;
    }

    public void setStopTime(String stopTime) {
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

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
