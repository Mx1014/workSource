package com.everhomes.rest.quality;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>id: 主键id</li>
 *  <li>sampleId: 检查id</li>
 *  <li>organizationId: 机构id</li>
 *  <li>positionId: 通用岗位id</li>
 *  <li>organizationName: 机构名</li>
 * </ul>
 * Created by ying.xiong on 2017/6/9.
 */
public class SampleGroupDTO {

    private Long id;

    private Long organizationId;

    private String organizationName;

    private Long sampleId;

    private Long positionId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public Long getPositionId() {
        return positionId;
    }

    public void setPositionId(Long positionId) {
        this.positionId = positionId;
    }

    public Long getSampleId() {
        return sampleId;
    }

    public void setSampleId(Long sampleId) {
        this.sampleId = sampleId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
