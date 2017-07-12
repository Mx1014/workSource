package com.everhomes.rest.equipment;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <ul>
 *  <li>taskIds: 任务id列表</li>
 *  <li>reviewResult: 审阅结果 1-合格 2-不合格</li>
 *  <li>ownerId: 设备所属的主体id</li>
 *  <li>ownerType: 设备所属的主体，参考{@link com.everhomes.rest.quality.OwnerType}</li>
 *  <li>endTime: 维修截止时间</li>
 *  <li>operatorType: 维修执行人类型</li>
 *  <li>operatorId: 维修执行人id</li>
 * </ul>
 * Created by ying.xiong on 2017/4/17.
 */
public class ReviewEquipmentTasksCommand {
    @NotNull
    @ItemType(Long.class)
    private List<Long> taskIds;
    @NotNull
    private Byte reviewResult;
    @NotNull
    private Long ownerId;

    @NotNull
    private String ownerType;

    private Long endTime;

    private String operatorType;

    private Long operatorId;

    public List<Long> getTaskIds() {
        return taskIds;
    }

    public void setTaskIds(List<Long> taskIds) {
        this.taskIds = taskIds;
    }

    public Byte getReviewResult() {
        return reviewResult;
    }

    public void setReviewResult(Byte reviewResult) {
        this.reviewResult = reviewResult;
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

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public String getOperatorType() {
        return operatorType;
    }

    public void setOperatorType(String operatorType) {
        this.operatorType = operatorType;
    }

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
