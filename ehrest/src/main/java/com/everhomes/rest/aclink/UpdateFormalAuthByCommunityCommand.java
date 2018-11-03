// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

/**
 * <ul> 
 * <li>communityId: 园区id (公共门禁所在园区)</li>
 * <li>status: 状态 0失效 1有效 {@link com.everhomes.rest.aclink.DoorAuthStatus}}</li>
 * <li>targetId: 用户/企业id </li>
 * <li>targetType: 用户/企业 类型  0用户 1企业{@link com.everhomes.rest.aclink.DoorLicenseType}</li>
 * <li>operateOrgId: 管理公司id</li>
 * </ul>
 *
 */
public class UpdateFormalAuthByCommunityCommand {
	private Long communityId;
	private Byte status;
	private Long targetId;
	private Byte targetType;
	private Long operateOrgId;
	
	public Long getCommunityId() {
		return communityId;
	}
	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}
	public Byte getStatus() {
		return status;
	}
	public void setStatus(Byte status) {
		this.status = status;
	}
	public Long getTargetId() {
		return targetId;
	}
	public void setTargetId(Long targetId) {
		this.targetId = targetId;
	}
	public Byte getTargetType() {
		return targetType;
	}
	public void setTargetType(Byte targetType) {
		this.targetType = targetType;
	}
	public Long getOperateOrgId() {
		return operateOrgId;
	}
	public void setOperateOrgId(Long operateOrgId) {
		this.operateOrgId = operateOrgId;
	}
	
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	
}
