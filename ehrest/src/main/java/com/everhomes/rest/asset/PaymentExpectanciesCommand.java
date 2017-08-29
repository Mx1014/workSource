//@formatter:off
package com.everhomes.rest.asset;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;


/**
 * Created by Wentian Wang on 2017/8/22.
 */

public class PaymentExpectanciesCommand {
    private Long namesapceId;
    private Long ownerId;
    private String ownerType;
    private Long targetId;
    private String targetType;
    private String targetName;
    @ItemType(FeeRules.class)
    private List<FeeRules> feesRules;
    private String contractNum;
    private String noticeTel;
    private Integer pageOffset;
    private Integer pageSize;

    public Long getNamesapceId() {
        return namesapceId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Integer getPageOffset() {
        return pageOffset;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public void setPageOffset(Integer pageOffset) {
        this.pageOffset = pageOffset;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public void setNamesapceId(Long namesapceId) {
        this.namesapceId = namesapceId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }


    public String getContractNum() {
        return contractNum;
    }

    public void setContractNum(String contractNum) {
        this.contractNum = contractNum;
    }

    public String getNoticeTel() {
        return noticeTel;
    }

    public void setNoticeTel(String noticeTel) {
        this.noticeTel = noticeTel;
    }


    public List<FeeRules> getFeesRules() {
        return feesRules;
    }

    public void setFeesRules(List<FeeRules> feesRules) {
        this.feesRules = feesRules;
    }
}
