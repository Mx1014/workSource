package com.everhomes.rest.quality;

import com.everhomes.util.StringHelper;

/**
 * Created by Rui.Jia  2018/3/16 09 :27
 */

public class OfflineQualityTaskCommand {

    private Long id;

    private Long operatorId;

    private String operatorType;

    private Long executiveStartTime;

    private Long executiveExpireTime;

    private Long processExpireTime;

    private Byte verificationResult;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorType() {
        return operatorType;
    }

    public void setOperatorType(String operatorType) {
        this.operatorType = operatorType;
    }

    public Long getExecutiveStartTime() {
        return executiveStartTime;
    }

    public void setExecutiveStartTime(Long executiveStartTime) {
        this.executiveStartTime = executiveStartTime;
    }

    public Long getExecutiveExpireTime() {
        return executiveExpireTime;
    }

    public void setExecutiveExpireTime(Long executiveExpireTime) {
        this.executiveExpireTime = executiveExpireTime;
    }

    public Long getProcessExpireTime() {
        return processExpireTime;
    }

    public void setProcessExpireTime(Long processExpireTime) {
        this.processExpireTime = processExpireTime;
    }

    public Byte getVerificationResult() {
        return verificationResult;
    }

    public void setVerificationResult(Byte verificationResult) {
        this.verificationResult = verificationResult;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
