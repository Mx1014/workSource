package com.everhomes.rest.rentalv2;

/**
 * <ul>
 * <li>resourceName：资源名称</li>
 * <li>usingDetail: 预订时间</li>
 * <li>userName: 预订方</li>
 * <li>startTime: 订单开始时间</li>
 * <li>userName: 订单结束时间</li>
 * </ul>
 */
public class UsingInfoDTO {
    private String resourceName;
    private String usingDetail;
    private String userName;
    private Long startTime;
    private Long endTime;
    private Byte rentalType;

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getUsingDetail() {
        return usingDetail;
    }

    public void setUsingDetail(String usingDetail) {
        this.usingDetail = usingDetail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

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

    public Byte getRentalType() {
        return rentalType;
    }

    public void setRentalType(Byte rentalType) {
        this.rentalType = rentalType;
    }
}
