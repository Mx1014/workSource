package com.everhomes.rest.uniongroup;

import com.everhomes.util.StringHelper;

import java.sql.Timestamp;

/**
 * <ul>
 * <li>id: 组关系项id</li>
 * <li>namespaceId: 域空间id</li>
 * <li>groupType: 组类型</li>
 * <li>groupId: 组id</li>
 * <li>detailId: 人员档案id</li>
 * <li>targetType: 用户类型</li>
 * <li>targetId: 用户id</li>
 * <li>organizationId: 公司id</li>
 * <li>contactName: 联系名称</li>
 * <li>contactToken: 联系电话</li>
 * <li>updateTime: 更新时间</li>
 * <li>operatorUid: 操作人员id</li>
 * <li>employeeNo: 员工编号</li>
 * <li>department: 员工部门</li>
 * <li>jobposition: 员工职位</li>
 * </ul>
 */
public class UniongroupMemberDetailsDTO {
    private Long id;
    private Integer namespaceId;
    private String groupType;
    private Long groupId;
    private Long detailId;
    private String targetType;
    private Long targetId;
    private Long organizationId;
    private String contactName;
    private String contactToken;
    private Timestamp updateTime;
    private Long operatorUid;
    private String employeeNo;

    /**
     * add
     **/
    private String department;
    private String jobposition;

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

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getDetailId() {
        return detailId;
    }

    public void setDetailId(Long detailId) {
        this.detailId = detailId;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
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

    public String getContactToken() {
        return contactToken;
    }

    public void setContactToken(String contactToken) {
        this.contactToken = contactToken;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public Long getOperatorUid() {
        return operatorUid;
    }

    public void setOperatorUid(Long operatorUid) {
        this.operatorUid = operatorUid;
    }

    public String getEmployeeNo() {
        return employeeNo;
    }

    public void setEmployeeNo(String employeeNo) {
        this.employeeNo = employeeNo;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getJobposition() {
        return jobposition;
    }

    public void setJobposition(String jobposition) {
        this.jobposition = jobposition;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
