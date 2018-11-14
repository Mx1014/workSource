// @formatter:off
package com.everhomes.rest.community.admin;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.acl.admin.RoleDTO;
import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>organizationId: 组织id</li>
 *     <li>organizationName: 组织名</li>
 *     <li>targetType: 成员是否注册 参考{@link com.everhomes.rest.organization.pm.PmMemberTargetType}</li>
 *     <li>targetId: 注册用户对应的userId，未注册填0</li>
 *     <li>memberGroup: 组织角色类型 参考{@link com.everhomes.rest.organization.pm.PmMemberGroup}</li>
 *     <li>contactName: 成员名称</li>
 *     <li>contactType: 成员类型：{@link com.everhomes.use.IdentifierType}</li>
 *     <li>contactToken: 成员标识</li>
 *     <li>contactDescription: 描述/部门</li>
 *     <li>status: 状态</li>
 *     <li>initial: 首字母</li>
 *     <li>fullPinyin: fullPinyin</li>
 *     <li>fullInitial: fullInitial</li>
 *     <li>roles: 角色列表 {@link com.everhomes.rest.acl.admin.RoleDTO}</li>
 *     <li>groupId: groupId</li>
 *     <li>groupName: groupName</li>
 *     <li>nickName: nickName</li>
 *     <li>avatar: avatar</li>
 *     <li>employeeNo: 工号</li>
 *     <li>gender: gender</li>
 *     <li>createTime: 创建时间</li>
 *     <li>creatorUid: 创建人</li>
 *     <li>approveTime: 操作(同意、拒绝)时间</li>
 *     <li>operatorName: 操作人名字</li>
 *     <li>operatorPhone: 操作人电话</li>
 *     <li>operateType: 认证方式,请参考{@link com.everhomes.rest.community.admin.OperateType}</li>
 *     <li>authFlag: 是否有审核权限, 请参考{@link com.everhomes.rest.common.TrueOrFalseFlag}</li>
 * </ul>
 */
public class ComOrganizationMemberDTO {
    @NotNull
    private Long id;
    @NotNull
    private Long organizationId;
    private String organizationName;
    private String targetType;
    @NotNull
    private Long targetId;

    private String memberGroup;
    private String contactName;
    private Byte contactType;
    private String contactToken;
    private String contactDescription;
    private Byte status;
    private String initial;
    private String fullPinyin;
    private String fullInitial;

    @ItemType(RoleDTO.class)
    private List<RoleDTO> roles;

    private Long groupId;
    private String groupName;

    private String nickName;
    private String avatar;

    private Long employeeNo;
    private Byte gender;

    private Timestamp createTime;
    private Long creatorUid;
    private Long approveTime;
    private String operatorName;
    private String operatorPhone;
    private Byte operateType;
    private Byte authFlag;

    public Byte getAuthFlag() {
        return authFlag;
    }

    public void setAuthFlag(Byte authFlag) {
        this.authFlag = authFlag;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getTargetType() {
        return targetType;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public Long getApproveTime() {
        return approveTime;
    }

    public void setApproveTime(Long approveTime) {
        this.approveTime = approveTime;
    }

    public String getOperatorPhone() {
        return operatorPhone;
    }

    public void setOperatorPhone(String operatorPhone) {
        this.operatorPhone = operatorPhone;
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

    public String getMemberGroup() {
        return memberGroup;
    }

    public void setMemberGroup(String memberGroup) {
        this.memberGroup = memberGroup;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public Byte getContactType() {
        return contactType;
    }

    public void setContactType(Byte contactType) {
        this.contactType = contactType;
    }

    public String getContactToken() {
        return contactToken;
    }

    public void setContactToken(String contactToken) {
        this.contactToken = contactToken;
    }

    public String getContactDescription() {
        return contactDescription;
    }

    public void setContactDescription(String contactDescription) {
        this.contactDescription = contactDescription;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }


    public List<RoleDTO> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleDTO> roles) {
        this.roles = roles;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Long getCreatorUid() {
        return creatorUid;
    }

    public void setCreatorUid(Long creatorUid) {
        this.creatorUid = creatorUid;
    }

    public Long getEmployeeNo() {
        return employeeNo;
    }

    public void setEmployeeNo(Long employeeNo) {
        this.employeeNo = employeeNo;
    }

    public Byte getGender() {
        return gender;
    }

    public void setGender(Byte gender) {
        this.gender = gender;
    }

    public String getInitial() {
        return initial;
    }

    public void setInitial(String initial) {
        this.initial = initial;
    }

    public String getFullPinyin() {
        return fullPinyin;
    }

    public void setFullPinyin(String fullPinyin) {
        this.fullPinyin = fullPinyin;
    }

    public String getFullInitial() {
        return fullInitial;
    }

    public void setFullInitial(String fullInitial) {
        this.fullInitial = fullInitial;
    }

    public Byte getOperateType() {
        return operateType;
    }

    public void setOperateType(Byte operateType) {
        this.operateType = operateType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
