package com.everhomes.rest.archives;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>organizationId: 公司 id(必须得传)</li>
 * <li>contactName: 成员姓名</li>
 * <li>contactEnName: 成员英文名</li>
 * <li>gender: 成员性别</li>
 * <li>regionCode: 区号</li>
 * <li>contactToken: 联系号码</li>
 * <li>contactShortToken: 手机短号</li>
 * <li>workEmail: 工作邮箱</li>
 * <li>departmentIds: 部门 id</li>
 * <li>jobPositionIds: 岗位 id</li>
 * <li>jobLevelIds: 职级 id</li>
 * <li>visibleFlag: 隐私设置: 0-显示, 1-隐藏</li>
 * <li>updateFlag: 编辑标志: 0-否, 1-是</li>
 * <li>updateDetailId: 编辑人员的id</li>
 * <li>namespaceId: 域空间ID</li>
 * <li>account: 账号(唯一标识)</li>
 * <li>operateType: 审核类型</li>
 * </ul>
 */
public class AddArchivesContactCommand {

    private Long organizationId;

    private String contactName;

    private String contactEnName;

    private Byte gender;

    private String regionCode;

    private String contactToken;

    private String contactShortToken;

    private String workEmail;

    private Long detailId;

    /**
	 * add by huangliangming 邮箱认证用户的时候无法靠当前环境获取
	 * 得到namespaceId(获取得0,是不对的),所以要靠传参
	 */
	private Integer namespaceId;
	
    @ItemType(Long.class)
    private List<Long> departmentIds;

    @ItemType(Long.class)
    private List<Long> jobPositionIds;

    @ItemType(Long.class)
    private List<Long> jobLevelIds;

    private Byte visibleFlag;

    private Byte updateFlag;
	
	private Long updateDetailId;

    //  组织架构4.6需要添加唯一标识
    private String account;

    private Byte operateType;

    public Byte getOperateType() {
        return operateType;
    }

    public void setOperateType(Byte operateType) {
        this.operateType = operateType;
    }

    public AddArchivesContactCommand() {
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactEnName() {
        return contactEnName;
    }

    public void setContactEnName(String contactEnName) {
        this.contactEnName = contactEnName;
    }

    
    public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public Byte getGender() {
        return gender;
    }

    public void setGender(Byte gender) {
        this.gender = gender;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getContactToken() {
        return contactToken;
    }

    public void setContactToken(String contactToken) {
        this.contactToken = contactToken;
    }

    public String getContactShortToken() {
        return contactShortToken;
    }

    public void setContactShortToken(String contactShortToken) {
        this.contactShortToken = contactShortToken;
    }

    public String getWorkEmail() {
        return workEmail;
    }

    public void setWorkEmail(String workEmail) {
        this.workEmail = workEmail;
    }

    public List<Long> getDepartmentIds() {
        return departmentIds;
    }

    public void setDepartmentIds(List<Long> departmentIds) {
        this.departmentIds = departmentIds;
    }

    public List<Long> getJobPositionIds() {
        return jobPositionIds;
    }

    public void setJobPositionIds(List<Long> jobPositionIds) {
        this.jobPositionIds = jobPositionIds;
    }

    public List<Long> getJobLevelIds() {
        return jobLevelIds;
    }

    public void setJobLevelIds(List<Long> jobLevelIds) {
        this.jobLevelIds = jobLevelIds;
    }

    public Byte getVisibleFlag() {
        return visibleFlag;
    }

    public void setVisibleFlag(Byte visibleFlag) {
        this.visibleFlag = visibleFlag;
    }

    public Byte getUpdateFlag() {
        return updateFlag;
    }

    public void setUpdateFlag(Byte updateFlag) {
        this.updateFlag = updateFlag;
    }
	
	
    public Long getUpdateDetailId() {
        return updateDetailId;
    }

    public void setUpdateDetailId(Long updateDetailId) {
        this.updateDetailId = updateDetailId;
    }

    public Long getDetailId() {
        return detailId;
    }

    public void setDetailId(Long detailId) {
        this.detailId = detailId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
