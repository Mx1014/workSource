package com.everhomes.rest.flow;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>applyUserId: 申请人id</li>
 *     <li>applyUserName: 申请人名称</li>
 *     <li>flowMainId: flow main id</li>
 *     <li>ownerType: ownerType</li>
 *     <li>ownerId: ownerId</li>
 *     <li>referType: referType</li>
 *     <li>referId: referId</li>
 *     <li>moduleType: moduleType</li>
 *     <li>processUserId: 处理人id</li>
 *     <li>processUserName: processUserName</li>
 *     <li>namespaceId: namespaceId</li>
 *     <li>content: 渲染内容</li>
 *     <li>status: 状态{@link com.everhomes.rest.flow.FlowCaseStatus}</li>
 *     <li>flowVersion: flowVersion</li>
 *     <li>lastStepTime: lastStepTime</li>
 *     <li>createTime: createTime</li>
 *     <li>moduleId: 模块id</li>
 *     <li>moduleName: 模块名称</li>
 *     <li>currentNodeId: 当前节点id</li>
 *     <li>moduleLink: moduleLink</li>
 *     <li>evaluateScore: 评价分值</li>
 *     <li>allowApplierUpdate: 是否准许申请者修改实体详情</li>
 *     <li>customObject: 自定义对象</li>
 *     <li>currNodeParams: 自定义参数</li>
 *     <li>stepCount: stepCount</li>
 *     <li>rejectCount: rejectCount</li>
 *     <li>title: title</li>
 *     <li>organizationId: 公司id</li>
 *     <li>currentNodeFormOriginId: 当前节点的表单id</li>
 *     <li>currentNodeFormVersion: 当前节点的表单version</li>
 *     <li>entities: 实体的详细信息 {@link com.everhomes.rest.flow.FlowCaseEntity}</li>
 *     <li>lanes: 泳道列表 {@link com.everhomes.rest.flow.FlowLaneLogDTO}</li>
 *     <li>buttons: 按钮详情 {@link com.everhomes.rest.flow.FlowButtonDTO}</li>
 *     <li>moreButtons: moreButtons {@link com.everhomes.rest.flow.FlowButtonDTO}</li>
 * </ul>
 */
public class FlowCaseDetailDTOV2 {

    private Long id;
    private Long applyUserId;
    private String applyUserName;
    private String contactAvatar;
    private Long flowMainId;
    private String ownerType;
    private Long ownerId;
    private String referType;
    private Long referId;
    private String moduleType;
    private Long processUserId;
    private String processUserName;
    private Integer namespaceId;
    private String content;
    private Byte status;
    private Integer flowVersion;
    private Timestamp lastStepTime;
    private Timestamp createTime;
    private Long moduleId;
    private String moduleName;
    private Long currentNodeId;
    private String moduleLink;
    private Integer evaluateScore;
    private Byte allowApplierUpdate;
    private String customObject;
    private String currNodeParams;
    private Long stepCount;
    private Long rejectCount;
    private String title;
    private Long organizationId;
    private Long communityId;

    private Long currentNodeFormOriginId;
    private Long currentNodeFormVersion;


    @ItemType(FlowCaseEntity.class)
    private List<FlowCaseEntity> entities = new ArrayList<>();

    @ItemType(FlowLaneLogDTO.class)
    private List<FlowLaneLogDTO> lanes = new ArrayList<>();

    @ItemType(FlowButtonDTO.class)
    private List<FlowButtonDTO> buttons = new ArrayList<>();
    @ItemType(FlowButtonDTO.class)
    private List<FlowButtonDTO> moreButtons = new ArrayList<>();

    public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getApplyUserId() {
        return applyUserId;
    }

    public void setApplyUserId(Long applyUserId) {
        this.applyUserId = applyUserId;
    }

    public String getApplyUserName() {
        return applyUserName;
    }

    public void setApplyUserName(String applyUserName) {
        this.applyUserName = applyUserName;
    }

    public String getContactAvatar() {
        return contactAvatar;
    }

    public void setContactAvatar(String contactAvatar) {
        this.contactAvatar = contactAvatar;
    }

    public Long getFlowMainId() {
        return flowMainId;
    }

    public void setFlowMainId(Long flowMainId) {
        this.flowMainId = flowMainId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
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

    public String getModuleType() {
        return moduleType;
    }

    public void setModuleType(String moduleType) {
        this.moduleType = moduleType;
    }

    public Long getProcessUserId() {
        return processUserId;
    }

    public void setProcessUserId(Long processUserId) {
        this.processUserId = processUserId;
    }

    public String getProcessUserName() {
        return processUserName;
    }

    public void setProcessUserName(String processUserName) {
        this.processUserName = processUserName;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Integer getFlowVersion() {
        return flowVersion;
    }

    public void setFlowVersion(Integer flowVersion) {
        this.flowVersion = flowVersion;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public Long getCurrentNodeId() {
        return currentNodeId;
    }

    public void setCurrentNodeId(Long currentNodeId) {
        this.currentNodeId = currentNodeId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getModuleLink() {
        return moduleLink;
    }

    public void setModuleLink(String moduleLink) {
        this.moduleLink = moduleLink;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Integer getEvaluateScore() {
        return evaluateScore;
    }

    public void setEvaluateScore(Integer evaluateScore) {
        this.evaluateScore = evaluateScore;
    }

    public Timestamp getLastStepTime() {
        return lastStepTime;
    }

    public void setLastStepTime(Timestamp lastStepTime) {
        this.lastStepTime = lastStepTime;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public List<FlowButtonDTO> getButtons() {
        return buttons;
    }

    public void setButtons(List<FlowButtonDTO> buttons) {
        this.buttons = buttons;
    }

    public List<FlowCaseEntity> getEntities() {
        return entities;
    }

    public void setEntities(List<FlowCaseEntity> entities) {
        this.entities = entities;
    }

	public List<FlowLaneLogDTO> getLanes() {
        return lanes;
    }

    public void setLanes(List<FlowLaneLogDTO> lanes) {
        this.lanes = lanes;
    }

    public Byte getAllowApplierUpdate() {
        return allowApplierUpdate;
    }

    public void setAllowApplierUpdate(Byte allowApplierUpdate) {
        this.allowApplierUpdate = allowApplierUpdate;
    }

    public String getCustomObject() {
        return customObject;
    }

    public void setCustomObject(String customObject) {
        this.customObject = customObject;
    }

    public String getCurrNodeParams() {
        return currNodeParams;
    }

    public void setCurrNodeParams(String currNodeParams) {
        this.currNodeParams = currNodeParams;
    }

    public Long getStepCount() {
        return stepCount;
    }

    public void setStepCount(Long stepCount) {
        this.stepCount = stepCount;
    }

    public Long getRejectCount() {
        return rejectCount;
    }

    public void setRejectCount(Long rejectCount) {
        this.rejectCount = rejectCount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<FlowButtonDTO> getMoreButtons() {
        return moreButtons;
    }

    public void setMoreButtons(List<FlowButtonDTO> moreButtons) {
        this.moreButtons = moreButtons;
    }

    public Long getCurrentNodeFormOriginId() {
        return currentNodeFormOriginId;
    }

    public void setCurrentNodeFormOriginId(Long currentNodeFormOriginId) {
        this.currentNodeFormOriginId = currentNodeFormOriginId;
    }

    public Long getCurrentNodeFormVersion() {
        return currentNodeFormVersion;
    }

    public void setCurrentNodeFormVersion(Long currentNodeFormVersion) {
        this.currentNodeFormVersion = currentNodeFormVersion;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
