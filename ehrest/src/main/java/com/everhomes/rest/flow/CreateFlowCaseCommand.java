// @formatter:off
package com.everhomes.rest.flow;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>flowCaseId: 预先申请的flowCaseId, 可以使用flowService#getNextFlowCaseId申请，推荐设置这个值</li>
 *     <li>flowMainId: enabled flow main id</li>
 *     <li>flowVersion: flow 版本</li>
 *     <li>applyUserId: 申请人id</li>
 *     <li>referId: 业务对象id</li>
 *     <li>referType: 业务对象类型, 各业务唯一, 推荐使用{@link com.everhomes.rest.common.EntityType}</li>
 *     <li>projectId: 小区id</li>
 *     <li>projectType: 小区类型 {@link com.everhomes.rest.common.EntityType#COMMUNITY}</li>
 *     <li>projectIdA: 拓展项目类型 id, 如子项目id, 非必填</li>
 *     <li>projectTypeA: 拓展项目类型 type, 如子项目类型, 非必填</li>
 *     <li>content: 列表展示内容</li>
 *     <li>title: 标题</li>
 *     <li>currentOrganizationId: 用户当前场景下的公司id,推荐传这个值，要不然就无法设置工作流的某些功能</li>
 *     <li>serviceType: 业务类型, 在App上搜索的时候会用到的</li>
 *     <li>routeUri: 路由, 如果在任务列表需要自定义跳转的话,设置此路由,如果不需要就不用设置</li>
 *     <li>originAppId: 应用 id, 必传字段</li>
 *     <li>additionalFieldDTO: 额外字段,更加特殊的情况需要用到这个,一般不需要 {@link com.everhomes.rest.flow.FlowCaseAdditionalFieldDTO}</li>
 * </ul>
 */
public class CreateFlowCaseCommand {

    private Long flowCaseId;
    private Long flowMainId;
    private Integer flowVersion;
    private Long applyUserId;
    private Long referId;
    private String referType;
    private Long projectId;
    private String projectType;
    private Long projectIdA;
    private String projectTypeA;
    private String content;
    private String title;
    @NotNull
    private Long currentOrganizationId;
    private String serviceType;
    private Long applierOrganizationId;

    private String routeUri;
    private Long subFlowParentId; // 内部使用, 创建子流程的时候用到

    private Long originAppId;

    @ItemType(FlowCaseAdditionalFieldDTO.class)
    private FlowCaseAdditionalFieldDTO additionalFieldDTO;

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

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public FlowCaseAdditionalFieldDTO getAdditionalFieldDTO() {
        return additionalFieldDTO;
    }

    public void setAdditionalFieldDTO(FlowCaseAdditionalFieldDTO additionalFieldDTO) {
        this.additionalFieldDTO = additionalFieldDTO;
    }

    public Long getProjectIdA() {
        return projectIdA;
    }

    public void setProjectIdA(Long projectIdA) {
        this.projectIdA = projectIdA;
    }

    public String getProjectTypeA() {
        return projectTypeA;
    }

    public void setProjectTypeA(String projectTypeA) {
        this.projectTypeA = projectTypeA;
    }

    public String getRouteUri() {
        return routeUri;
    }

    public void setRouteUri(String routeUri) {
        this.routeUri = routeUri;
    }

    public Long getFlowCaseId() {
        return flowCaseId;
    }

    public void setFlowCaseId(Long flowCaseId) {
        this.flowCaseId = flowCaseId;
    }

    public Long getApplierOrganizationId() {
		return applierOrganizationId;
	}

	public void setApplierOrganizationId(Long applierOrganizationId) {
		this.applierOrganizationId = applierOrganizationId;
	}

    public Long getSubFlowParentId() {
        return subFlowParentId;
    }

    public void setSubFlowParentId(Long subFlowParentId) {
        this.subFlowParentId = subFlowParentId;
    }

    public Long getOriginAppId() {
        return originAppId;
    }

    public void setOriginAppId(Long originAppId) {
        this.originAppId = originAppId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
