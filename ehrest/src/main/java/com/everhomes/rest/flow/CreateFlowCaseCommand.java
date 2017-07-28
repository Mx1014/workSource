// @formatter:off
package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>flowMainId: enabled flow id</li>
 *     <li>flowVersion: flow 版本</li>
 *     <li>applyUserId: 申请人id</li>
 *     <li>referId: 业务对象id</li>
 *     <li>referType: 业务对象类型, 各业务唯一, 推荐使用{@link com.everhomes.rest.common.EntityType}</li>
 *     <li>projectId: 小区id</li>
 *     <li>projectType: 小区类型 {@link com.everhomes.rest.common.EntityType#COMMUNITY}</li>
 *     <li>content: 列表展示内容</li>
 *     <li>title: 标题</li>
 *     <li>currentOrganizationId: 用户当前场景下的公司id</li>
 * </ul>
 */
public class CreateFlowCaseCommand {

    private Long flowMainId;
    private Integer flowVersion;
    private Long applyUserId;
    private Long referId;
    private String referType;
    private Long projectId;
    private String projectType;
    private String content;
    private String title;
    @NotNull
    private Long currentOrganizationId;

    public Long getFlowMainId() {
        return flowMainId;
    }

    public void setFlowMainId(Long flowMainId) {
        this.flowMainId = flowMainId;
    }

    public Long getCurrentOrganizationId() {
        return currentOrganizationId;
    }

    public void setCurrentOrganizationId(Long currentOrganizationId) {
        this.currentOrganizationId = currentOrganizationId;
    }

    public Integer getFlowVersion() {
        return flowVersion;
    }

    public void setFlowVersion(Integer flowVersion) {
        this.flowVersion = flowVersion;
    }

    public Long getApplyUserId() {
        return applyUserId;
    }

    public void setApplyUserId(Long applyUserId) {
        this.applyUserId = applyUserId;
    }

    public Long getReferId() {
        return referId;
    }

    public void setReferId(Long referId) {
        this.referId = referId;
    }

    public String getReferType() {
        return referType;
    }

    public void setReferType(String referType) {
        this.referType = referType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
