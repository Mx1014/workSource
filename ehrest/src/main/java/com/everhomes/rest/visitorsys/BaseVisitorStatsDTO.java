// @formatter:off
package com.everhomes.rest.visitorsys;

import com.everhomes.util.StringHelper;

/**
  *<ul>
  *<li>timePoint : (必填)时间点，如0,1,2,3,...23点，1(周日)，2(周一)，3(周二)....，7(周六)，2018-05-16</li>
  *<li>timePointCount : (必填)时间点对应统计的数量</li>
  *</ul>
  */
public class BaseVisitorStatsDTO {
    private String timePoint;
    private Long timePointCount;

    public String getTimePoint() {
        return timePoint;
    }

    public void setTimePoint(String timePoint) {
        this.timePoint = timePoint;
    }

    public Long getTimePointCount() {
        return timePointCount;
    }

    public void setTimePointCount(Long timePointCount) {
        this.timePointCount = timePointCount;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
