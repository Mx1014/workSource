package com.everhomes.rest.point;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>startTime: startTime</li>
 *     <li>endTime: endTime</li>
 *     <li>phone: phone</li>
 *     <li>userId: 用户id, 不传就不区分用户</li>
 *     <li>systemId: systemId</li>
 *     <li>operateType: 操作类型 {@link com.everhomes.rest.point.PointArithmeticType}</li>
 *     <li>pageAnchor: 锚点</li>
 *     <li>pageSize: pageSize</li>
 * </ul>
 */
public class ExportPointLogsCommand {

    private Long startTime;
    private Long endTime;

    private String phone;

    private Long userId;
    private Long systemId;
    private Byte operateType;
    private Long pageAnchor;
    private Integer pageSize;

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getSystemId() {
        return systemId;
    }

    public void setSystemId(Long systemId) {
        this.systemId = systemId;
    }

    public Byte getOperateType() {
        return operateType;
    }

    public void setOperateType(Byte operateType) {
        this.operateType = operateType;
    }

    public Long getPageAnchor() {
        return pageAnchor;
    }

    public void setPageAnchor(Long pageAnchor) {
        this.pageAnchor = pageAnchor;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
