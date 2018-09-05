package com.everhomes.rest.customer;

import java.sql.Timestamp;

/**
 * <ul>
 *     <li>startTime: 同步开始时间</li>
 *     <li>endTime: 同步结束时间</li>
 *     <li>manualFlag: 是否手动 0：自动 1：手动</li>
 *     <li>rateOfProgress: 同步进度</li>
 *     <li>status: 同步状态 参考{@link com.everhomes.rest.customer.SyncDataTaskStatus}</li>
 *     <li>result: 同步结果</li>
 * </ul>
 * Created by ying.xiong on 2018/1/15.
 */
public class SyncDataResult {
    private Timestamp startTime;

    private Timestamp endTime;

    private Double rateOfProgress;

    private Byte manualFlag;

    private Byte status;

    private String result;

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public Byte getManualFlag() {
        return manualFlag;
    }

    public void setManualFlag(Byte manualFlag) {
        this.manualFlag = manualFlag;
    }

    public Double getRateOfProgress() {
        return rateOfProgress;
    }

    public void setRateOfProgress(Double rateOfProgress) {
        this.rateOfProgress = rateOfProgress;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }
}
