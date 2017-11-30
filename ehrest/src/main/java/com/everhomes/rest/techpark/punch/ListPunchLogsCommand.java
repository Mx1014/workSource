package com.everhomes.rest.techpark.punch;

/**
 * <ul>
 * <li>enterpriseId：long 企业Id</li>
 * <li>queryTime：long 查询时间戳-查询当下的状态不用传这个参数</li>
 * <li>userId：long 查询某人的打卡记录,不传就是查自己的</li>
 * </ul>
 */
public class ListPunchLogsCommand {
    private Long queryTime;
    private Long userId;
    private Long enterpriseId;

    public Long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public Long getQueryTime() {
        return queryTime;
    }

    public void setQueryTime(Long queryTime) {
        this.queryTime = queryTime;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
