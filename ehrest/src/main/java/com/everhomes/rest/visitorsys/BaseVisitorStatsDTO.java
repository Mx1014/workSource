// @formatter:off
package com.everhomes.rest.visitorsys;

import com.everhomes.util.StringHelper;

/**
  *<ul>
  *<li>timePoint : (必填)时间点，如1点，周日，11.11</li>
  *<li>timePointCount : (必填)时间点对应统计的数量</li>
  *</ul>
  */
public class BaseVisitorStatsDTO {
    private String timePoint;
    private Integer timePointCount;

    public String getTimePoint() {
        return timePoint;
    }

    public void setTimePoint(String timePoint) {
        this.timePoint = timePoint;
    }

    public Integer getTimePointCount() {
        return timePointCount;
    }

    public void setTimePointCount(Integer timePointCount) {
        this.timePointCount = timePointCount;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
