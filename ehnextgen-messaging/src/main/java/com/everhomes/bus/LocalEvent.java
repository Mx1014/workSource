package com.everhomes.bus;

import com.everhomes.rest.approval.TrueOrFalseFlag;
import com.everhomes.util.StringHelper;

import java.util.Map;

/**
 * <ul>
 *     <li>syncFlag: 同步还是异步, 同步表示事件处理成功才会返回，异步表示事件接收成功就返回 {@link com.everhomes.rest.approval.TrueOrFalseFlag}</li>
 *     <li>eventName: 事件名称 {@link com.everhomes.bus.SystemEvent}</li>
 *     <li>entityType: 实体类型 {@link com.everhomes.entity.EntityType}</li>
 *     <li>entityId: 实体id</li>
 *     <li>createTime: 创建时间戳</li>
 *     <li>context: 用户上下文 {@link com.everhomes.bus.LocalEventContext}</li>
 *     <li>params: 每个事件具体的参数</li>
 * </ul>
 */
public class LocalEvent {

    private Byte syncFlag;
    private String eventName;
    private String entityType;
    private Long entityId;
    private Long createTime;
    private LocalEventContext context;
    private Map<String, Object> params;

    public LocalEvent() {
        syncFlag = TrueOrFalseFlag.FALSE.getCode();
    }

    public LocalEvent(String eventName, String entityType, Long entityId) {
        this();
        this.eventName = eventName;
        this.entityType = entityType;
        this.entityId = entityId;
    }

    public LocalEvent(Byte syncFlag, String eventName, String entityType, Long entityId, Long createTime, LocalEventContext context, Map<String, Object> params) {
        this(eventName, entityType, entityId);
        this.syncFlag = syncFlag;
        this.createTime = createTime;
        this.context = context;
        this.params = params;
    }

    public Byte getSyncFlag() {
        return syncFlag;
    }

    public void setSyncFlag(Byte syncFlag) {
        this.syncFlag = syncFlag;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public LocalEventContext getContext() {
        return context;
    }

    public void setContext(LocalEventContext context) {
        this.context = context;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
