package com.everhomes.rest.point;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>systemId: 配置id</li>
 *     <li>startTime: startTime</li>
 *     <li>endTime: endTime</li>
 *     <li>phone: phone</li>
 *     <li>userId: 用户id, 不传就是不区分用户</li>
 *     <li>arithmeticType: 计算类型</li>
 *     <li>pageAnchor: 锚点</li>
 *     <li>pageSize: pageSize</li>
 * </ul>
 */
public class ListPointLogsCommand {

    @NotNull
    private Long systemId;

    private Long startTime;
    private Long endTime;

    private String phone;

    private Long userId;
    private Byte arithmeticType;
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

    public Byte getArithmeticType() {
        return arithmeticType;
    }

    public void setArithmeticType(Byte arithmeticType) {
        this.arithmeticType = arithmeticType;
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
