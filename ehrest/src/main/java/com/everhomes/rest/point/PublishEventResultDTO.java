package com.everhomes.rest.point;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>operateType: 操作类型{@link com.everhomes.rest.point.PointArithmeticType}</li>
 *     <li>points: 积分数量</li>
 *     <li>eventName: 事件名称</li>
 * </ul>
 */
public class PublishEventResultDTO {

    private Byte operateType;
    private Long points;
    private String eventName;

    public Byte getOperateType() {
        return operateType;
    }

    public void setOperateType(Byte operateType) {
        this.operateType = operateType;
    }

    public Long getPoints() {
        return points;
    }

    public void setPoints(Long points) {
        this.points = points;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
