package com.everhomes.rest.techpark.expansion;

import com.everhomes.util.StringHelper;

/**
 * @author sw on 2017/10/19.
 * <li>currentPMId: 当前管理公司ID</li>
 * <li>currentProjectId: 当前选中项目Id，如果是全部则不传</li>
 * <li>appId: 应用id</li>
*/
public class SetLeasePromotionConfigCommand {
    private Integer namespaceId;
    private Byte buildingIntroduceFlag;

    private Long categoryId;
	private Long currentPMId;
	private Long currentProjectId;
	private Long appId;

	public Long getCurrentPMId() {
		return currentPMId;
	}

	public void setCurrentPMId(Long currentPMId) {
		this.currentPMId = currentPMId;
	}

	public Long getCurrentProjectId() {
		return currentProjectId;
	}

	public void setCurrentProjectId(Long currentProjectId) {
		this.currentProjectId = currentProjectId;
	}
	
	public Long getAppId() {
		return appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Byte getBuildingIntroduceFlag() {
        return buildingIntroduceFlag;
    }

    public void setBuildingIntroduceFlag(Byte buildingIntroduceFlag) {
        this.buildingIntroduceFlag = buildingIntroduceFlag;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
