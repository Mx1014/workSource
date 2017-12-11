package com.everhomes.point;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>points: 有符号的数</li>
 *     <li>log: log</li>
 *     <li>action: 消息</li>
 * </ul>
 */
public class PointEventProcessResult {

    private Long points;
    private PointLog log;
    private PointAction action;

    public PointEventProcessResult() {
    }

    public PointEventProcessResult(Long points, PointLog log) {
        this.points = points;
        this.log = log;
    }

    public Long getPoints() {
        return points;
    }

    public void setPoints(Long points) {
        this.points = points;
    }

    public PointLog getLog() {
        return log;
    }

    public void setLog(PointLog log) {
        this.log = log;
    }

    public PointAction getAction() {
        return action;
    }

    public void setAction(PointAction action) {
        this.action = action;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
