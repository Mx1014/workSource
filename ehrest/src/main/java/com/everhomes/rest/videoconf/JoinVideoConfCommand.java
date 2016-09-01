package com.everhomes.rest.videoconf;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>confId: 会议id或主持人手机号</li>
 *  <li>namespaceId: 命名空间</li>
 *  <li>enterpriseId: 企业id</li>
 *
 */
public class JoinVideoConfCommand {
	@NotNull
	private String confId;
	@NotNull
	private Integer namespaceId;
	
	private Long enterpriseId;

	public String getConfId() {
		return confId;
	}

	public void setConfId(String confId) {
		this.confId = confId;
	}
	
	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public Long getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
