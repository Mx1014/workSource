package com.everhomes.rest.varField;

/**
 * <ul>
 *     <li>communityId: 项目id</li>
 *     <li>fieldId: 字段在系统中的id</li>
 *     <li>beginDate: 开始时间</li>
 *     <li>endDate: 结束时间</li>
 * </ul>
 * Created by ying.xiong on 2017/8/1.
 */
public class ListFieldStatisticsCommand {
    private Long communityId;

    private Long fieldId;

    private Long beginDate;

    private Long endDate;

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Long getFieldId() {
        return fieldId;
    }

    public void setFieldId(Long fieldId) {
        this.fieldId = fieldId;
    }

    public Long getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Long beginDate) {
        this.beginDate = beginDate;
    }

    public Long getEndDate() {
        return endDate;
    }

    public void setEndDate(Long endDate) {
        this.endDate = endDate;
    }
}
