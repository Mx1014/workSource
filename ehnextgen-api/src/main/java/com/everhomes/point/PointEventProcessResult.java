package com.everhomes.point;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>points: 有符号的数</li>
 *     <li>log: log</li>
 * </ul>
 */
public class PointEventProcessResult {

    private Long points;
    private PointLog log;

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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
