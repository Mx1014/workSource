package com.everhomes.rest.quality;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>ownerId: 例行检查所属owner id</li>
 *     <li>ownerType: 例行检查所属owner类型例如PM</li>
 *     <li>name: 检查名称</li>
 *     <li>startTime: 开始时间</li>
 *     <li>endTime: 结束时间</li>
 *     <li>creatorName: 创建人姓名</li>
 *     <li>sampleCommunities: 关联小区列表 参考{@link com.everhomes.rest.quality.SampleCommunity}</li>
 *     <li>executeGroupAndPositionList:执行组部门岗位id列表 参考{@link com.everhomes.rest.quality.ExecuteGroupAndPosition}</li>
 * </ul>
 * Created by ying.xiong on 2017/6/1.
 */
public class SampleQualityInspectionDTO {
    private Long id;
    @NotNull
    private Long ownerId;

    @NotNull
    private String ownerType;

    private String name;

    private Timestamp startTime;

    private Timestamp endTime;

    private String creatorName;

    @ItemType(Long.class)
    private List<SampleCommunity> sampleCommunities;

    @ItemType(SampleGroupDTO.class)
    private List<SampleGroupDTO> sampleGroupDTOs;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public List<SampleCommunity> getSampleCommunities() {
        return sampleCommunities;
    }

    public void setSampleCommunities(List<SampleCommunity> sampleCommunities) {
        this.sampleCommunities = sampleCommunities;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public List<SampleGroupDTO> getSampleGroupDTOs() {
        return sampleGroupDTOs;
    }

    public void setSampleGroupDTOs(List<SampleGroupDTO> sampleGroupDTOs) {
        this.sampleGroupDTOs = sampleGroupDTOs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Timestamp getStartTime() {
        return startTime;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
