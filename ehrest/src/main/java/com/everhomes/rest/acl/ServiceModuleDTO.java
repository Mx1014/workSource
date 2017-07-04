package com.everhomes.rest.acl;

import com.everhomes.discover.ItemType;

import java.io.Serializable;
import java.util.List;

/**
 * <p>业务模块</p>
 * <ul>
 * <li>id: 模块id</li>
 * <li>name: 模块名称</li>
 * <li>vType: 值类型 0业务模块，1权限</li>
 * <li>parentId: 父级id </li>
 * <li>path: 层次关系</li>
 * <li>level: 级别</li>
 * <li>serviceModules: 子业务模块</li>
 * <li>description: 描述</li>
 * <li>updateTime: 更新时间</li>
 * <li>createTime: 创建时间</li>
 * <li>operatorUName: 操作人</li>
 * <li>instanceConfig: 参数，比如第三方链接值就是{"url":"http......."}</li>
 * </ul>
 */
public class ServiceModuleDTO implements Serializable {
	private Long id;

	private String name;

	private Byte vType;

	private Long parentId;

	private String path;

	private Integer level;

	@ItemType(ServiceModuleDTO.class)
	private List<ServiceModuleDTO> serviceModules;

	private Byte type;

	private String description;

	private Long updateTime;

	private Long createTime;

	private Long operatorUid;

	private String operatorUName;

	private String instanceConfig;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Byte getvType() {
		return vType;
	}

	public void setvType(Byte vType) {
		this.vType = vType;
	}

	public List<ServiceModuleDTO> getServiceModules() {
		return serviceModules;
	}

	public void setServiceModules(List<ServiceModuleDTO> serviceModules) {
		this.serviceModules = serviceModules;
	}

	public Byte getType() {
		return type;
	}

	public void setType(Byte type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Long getOperatorUid() {
		return operatorUid;
	}

	public void setOperatorUid(Long operatorUid) {
		this.operatorUid = operatorUid;
	}

	public String getOperatorUName() {
		return operatorUName;
	}

	public void setOperatorUName(String operatorUName) {
		this.operatorUName = operatorUName;
	}

	public String getInstanceConfig() {
		return instanceConfig;
	}

	public void setInstanceConfig(String instanceConfig) {
		this.instanceConfig = instanceConfig;
	}
}