package com.everhomes.rest.socialSecurity;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>detailId: 员工detailId</li>
 * <li>inOutType: 增减类型：0-社保, 1-公积金 参考{@link InOutTimeType}</li>
 * <li>organizationId: 公司id</li>
 * <li>startMonth: 开始时间</li>
 * <li>endMonth: 结束时间</li>
 * </ul>
 */
public class AddSocialSecurityInOutTimeCommand {

    private Long detailId;

    private Byte inOutType;

    private Long organizationId;

    private String startMonth;

    private String endMonth;

    public AddSocialSecurityInOutTimeCommand() {
    }

    public Long getDetailId() {
        return detailId;
    }

    public void setDetailId(Long detailId) {
        this.detailId = detailId;
    }

    public Byte getInOutType() {
        return inOutType;
    }

    public void setInOutType(Byte inOutType) {
        this.inOutType = inOutType;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public String getStartMonth() {
        return startMonth;
    }

    public void setStartMonth(String startMonth) {
        this.startMonth = startMonth;
    }

    public String getEndMonth() {
        return endMonth;
    }

    public void setEndMonth(String endMonth) {
        this.endMonth = endMonth;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
