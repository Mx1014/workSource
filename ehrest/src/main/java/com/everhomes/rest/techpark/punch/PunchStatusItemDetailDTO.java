package com.everhomes.rest.techpark.punch;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>punchDate: 打卡日期</li>
 * <li>description: 申请时间等描述信息</li>
 * </ul>
 */
public class PunchStatusItemDetailDTO {
    private Long punchDate;
    private String description;

    public Long getPunchDate() {
        return punchDate;
    }

    public void setPunchDate(Long punchDate) {
        this.punchDate = punchDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
