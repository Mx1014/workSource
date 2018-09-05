package com.everhomes.rest.customer;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>namespaceId: 域空间id</li>
 *     <li>communityId: 项目id</li>
 *     <li>allSyncFlag: 是否全量同步：0-增量同步，1-全量同步</li>
 *     <li>contractApplicationScene: 合同应用场景contractApplicationScene，用于多入口</li>
 *     <li>categoryId: 合同类型多入口</li>
 * </ul>
 * Created by ying.xiong on 2017/8/30.
 */
public class SyncCustomersCommand {
	@NotNull
	private Integer namespaceId;
	@NotNull
    private Long communityId;
	@NotNull
    private Long orgId;

    private Byte allSyncFlag;
    
    private Byte contractApplicationScene;
    @NotNull
    private Long categoryId;

    public Byte getContractApplicationScene() {
		return contractApplicationScene;
	}

	public void setContractApplicationScene(Byte contractApplicationScene) {
		this.contractApplicationScene = contractApplicationScene;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Byte getAllSyncFlag() {
        return allSyncFlag;
    }

    public void setAllSyncFlag(Byte allSyncFlag) {
        this.allSyncFlag = allSyncFlag;
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
