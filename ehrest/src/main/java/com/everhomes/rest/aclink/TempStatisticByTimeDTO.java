package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;
/**
 * <ul>
 *     <li>createTime: 开门日期</li>
 *     <li>number: 对应临时授权次数</li>
 * </ul>
 */
public class TempStatisticByTimeDTO {
    private Long logTime;

    private String createTime;

    private Long number;

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public Long getLogTime() {
        return logTime;
    }

    public void setLogTime(Long logTime) {
        this.logTime = logTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
