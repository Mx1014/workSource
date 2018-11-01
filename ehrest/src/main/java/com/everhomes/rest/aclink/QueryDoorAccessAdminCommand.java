// @formatter:off
package com.everhomes.rest.aclink;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerId: 属于的上级ID</li>
 * <li>ownerType: 属于上级的类型</li>
 * <li>groupId: 分组信息</li>
 * <li>search: 搜索字符串</li>
 * <li>displayName:名称</li>
 * <li>name:标识</li>
 * <li>hardwareId:mac地址</li>
 * <li>linkStatus: 连接状态{@link com.everhomes.rest.aclink.DoorAccessLinkStatus}</li>
 * <li>doorType: 门禁类型</li>
 * <li>serverId: 关联服务器id</li>
 * <li>pageAnchor: 锚点</li>
 * <li>pageSize: 每一页数量</li>
 * </ul>
 * @author janson
 *
 */
public class QueryDoorAccessAdminCommand {
    @NotNull
    private Long ownerId;
    
    @NotNull
    private Byte ownerType;
    
    private Long groupId;
    
    private String search;
    private Byte linkStatus;
    private Byte doorType;
    private Long serverId;
    private Long pageAnchor;
    
    private Integer pageSize;
    //门禁3.0需要区分以下三种搜索条件,原参数search不用,暂时保留,by liuyilin 20180827
    private String name;
    private String displayName;
    private String hardwareId;
    
    public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getHardwareId() {
		return hardwareId;
	}
	public void setHardwareId(String hardwareId) {
		this.hardwareId = hardwareId;
	}
	public Long getOwnerId() {
        return ownerId;
    }
    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }
    public Byte getOwnerType() {
        return ownerType;
    }
    public void setOwnerType(Byte ownerType) {
        this.ownerType = ownerType;
    }
    public String getSearch() {
        return search;
    }
    public void setSearch(String search) {
        this.search = search;
    }
    public Byte getLinkStatus() {
        return linkStatus;
    }
    public void setLinkStatus(Byte linkStatus) {
        this.linkStatus = linkStatus;
    }
    public Byte getDoorType() {
        return doorType;
    }
    public void setDoorType(Byte doorType) {
        this.doorType = doorType;
    }
    
    public Long getServerId() {
		return serverId;
	}
	public void setServerId(Long serverId) {
		this.serverId = serverId;
	}
	public Long getPageAnchor() {
        return pageAnchor;
    }
    public void setPageAnchor(Long pageAnchor) {
        this.pageAnchor = pageAnchor;
    }
    public Integer getPageSize() {
		return pageSize == null ? 0 : pageSize;
    }
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
    
    public Long getGroupId() {
        return groupId;
    }
    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
    
}
