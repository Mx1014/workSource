package com.everhomes.rest.general_approval;

import com.everhomes.util.StringHelper;
import java.util.List;
import java.sql.Timestamp;

import com.everhomes.discover.ItemType;

public class GeneralFormDTO {
    private Timestamp     updateTime;
    private String     ownerType;
    private String     moduleType;
    private Long     id;
    private String     templateText;
    private Integer     namespaceId;
    private String     stringTag5;
    private String     stringTag4;
    private String     stringTag1;
    private String     stringTag3;
    private String     stringTag2;
    private String     formName;
    private Byte     status;
    private String     templateType;
    private Long     formOriginId;
    private Long     formVersion;
    private Timestamp     createTime;
    private Long     moduleId;
    private Long     organizationId;
    private Long     integralTag1;
    private Long     integralTag2;
    private Long     integralTag3;
    private Long     integralTag4;
    private Long     integralTag5;
    private Long     ownerId;

    public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public String getModuleType() {
		return moduleType;
	}

	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTemplateText() {
		return templateText;
	}

	public void setTemplateText(String templateText) {
		this.templateText = templateText;
	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public String getStringTag5() {
		return stringTag5;
	}

	public void setStringTag5(String stringTag5) {
		this.stringTag5 = stringTag5;
	}

	public String getStringTag4() {
		return stringTag4;
	}

	public void setStringTag4(String stringTag4) {
		this.stringTag4 = stringTag4;
	}

	public String getStringTag1() {
		return stringTag1;
	}

	public void setStringTag1(String stringTag1) {
		this.stringTag1 = stringTag1;
	}

	public String getStringTag3() {
		return stringTag3;
	}

	public void setStringTag3(String stringTag3) {
		this.stringTag3 = stringTag3;
	}

	public String getStringTag2() {
		return stringTag2;
	}

	public void setStringTag2(String stringTag2) {
		this.stringTag2 = stringTag2;
	}

	public String getFormName() {
		return formName;
	}

	public void setFormName(String formName) {
		this.formName = formName;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public String getTemplateType() {
		return templateType;
	}

	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}

	public Long getFormOriginId() {
		return formOriginId;
	}

	public void setFormOriginId(Long formOriginId) {
		this.formOriginId = formOriginId;
	}

	public Long getFormVersion() {
		return formVersion;
	}

	public void setFormVersion(Long formVersion) {
		this.formVersion = formVersion;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Long getModuleId() {
		return moduleId;
	}

	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public Long getIntegralTag1() {
		return integralTag1;
	}

	public void setIntegralTag1(Long integralTag1) {
		this.integralTag1 = integralTag1;
	}

	public Long getIntegralTag2() {
		return integralTag2;
	}

	public void setIntegralTag2(Long integralTag2) {
		this.integralTag2 = integralTag2;
	}

	public Long getIntegralTag3() {
		return integralTag3;
	}

	public void setIntegralTag3(Long integralTag3) {
		this.integralTag3 = integralTag3;
	}

	public Long getIntegralTag4() {
		return integralTag4;
	}

	public void setIntegralTag4(Long integralTag4) {
		this.integralTag4 = integralTag4;
	}

	public Long getIntegralTag5() {
		return integralTag5;
	}

	public void setIntegralTag5(Long integralTag5) {
		this.integralTag5 = integralTag5;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}

