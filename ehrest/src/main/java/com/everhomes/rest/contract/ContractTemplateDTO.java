package com.everhomes.rest.contract;

import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: id</li>
 * <li>namespaceId: 域空间id</li>
 * <li>categoryId: 合同类型多入口</li>
 * <li>ownerId: 所有者id</li>
 * <li>ownerType: 所有者类型，通用配置为空</li>
 * <li>name: 模板名称</li>
 * <li>contracttemplateType: 0 收款合同模板 1付款合同模板</li>
 * <li>status: 0: inactive, 1: confirming, 2: active</li>
 * <li>contentType: gogs:存储gogs的commitId txt:存储文本内容</li>
 * <li>contents: 模板内容</li>
 * <li>parentId: 复制于哪个合同模板</li>
 * <li>version: 版本记录</li>
 * <li>creatorUid: 创建者uid</li>
 * <li>createTime: 创建时间</li>
 * <li>createDate: 创建时间界面显示</li>
 * <li>deleteFlag: 控制按钮是否可以删除  0表示可以删除, 1表示关联合同不能删除(已引用),2表示园区下不能删除通用模板</li>
 * <li>initParams: 合同模板初始化参数（计价条款、关联资产等的数目），前端会解析这个json</li>
 * </ul>
 * Created by jm.ding on 2018/6/27.
 */
public class ContractTemplateDTO {
	private Long id;
	private Integer namespaceId;
	private Long categoryId;
	private Long ownerId;
	private String ownerType;
	private String name;
	private Byte status;
	private String contentType;
	private String contents;
	private String lastCommit;
	private Long parentId;
	private Integer version;
	private Timestamp createTime;
	private Long creatorUid;
	private Byte contracttemplateType;
	private String creatorName;
	private String templateOwner;
	private String createDate;
	private Byte deleteFlag;
	private String initParams;
	
	public String getInitParams() {
		return initParams;
	}
	public void setInitParams(String initParams) {
		this.initParams = initParams;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public String getLastCommit() {
		return lastCommit;
	}
	public void setLastCommit(String lastCommit) {
		this.lastCommit = lastCommit;
	}
	public Byte getDeleteFlag() {
		return deleteFlag;
	}
	public void setDeleteFlag(Byte deleteFlag) {
		this.deleteFlag = deleteFlag;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getTemplateOwner() {
		return templateOwner;
	}
	public void setTemplateOwner(String templateOwner) {
		this.templateOwner = templateOwner;
	}
	public String getCreatorName() {
		return creatorName;
	}
	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getNamespaceId() {
		return namespaceId;
	}
	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}
	public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	public Long getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}
	public String getOwnerType() {
		return ownerType;
	}
	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Byte getContracttemplateType() {
		return contracttemplateType;
	}
	public void setContracttemplateType(Byte contracttemplateType) {
		this.contracttemplateType = contracttemplateType;
	}
	public Byte getStatus() {
		return status;
	}
	public void setStatus(Byte status) {
		this.status = status;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public Long getCreatorUid() {
		return creatorUid;
	}
	public void setCreatorUid(Long creatorUid) {
		this.creatorUid = creatorUid;
	}
	
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
	public String gogsPath() {
		return this.getName() + ".txt";
    }

}
