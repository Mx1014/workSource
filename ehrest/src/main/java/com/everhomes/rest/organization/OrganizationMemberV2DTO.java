package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;
import java.sql.Date;


/**
 * <ul>
 * <li>detailId: 关联detail表，唯一的员工序列号</li>
 * <li>organizationId: 小区id</li>
 * <li>targetType：成员是否注册 参考{@link com.everhomes.rest.organization.pm.PmMemberTargetType}</li>
 * <li>targetId：注册用户对应的userId，未注册填0</li>
 * <li>memberGroup：组织角色类型 参考{@link com.everhomes.rest.organization.pm.PmMemberGroup}</li>
 * <li>contactName：成员名称</li>
 * <li>contactType：成员类型：{@link com.everhomes.use.IdentifierType}</li>
 * <li>contactToken：成员标识</li>
 * <li>contactDescription：描述</li>
 * <li>status：状态</li>
 * <li>roles：角色列表</li>
 * <li>departments：部门列表</li>
 * <li>groups：群组列表</li>
 * <li>employeeNo：工号</li>
 * <li>initial：首字母</li>
 * <li>proccesingTaskCount：执行任务数量</li>
 * <li>executiveFlag：是否高管 1-是 0-否</li>
 * <li>position：职位</li>
 * <li>idNumber：身份证号码</li>
 * <li>namespaceId: 域空间</li>
 * <li>endTime: 合同到期时间</li>
 * <li>employeeStatus：员工状态, 0: 试用 1: 在职 2: 离职 参考{@link com.everhomes.rest.organization.EmployeeStatus}</li>
 * <li>employmentTime：转正时间</li>
 * <li>profileIntegrity: 档案完整度,0-100%</li>
 * <li>checkInTime: 入职日期</li>
 * <li>visibleFlag: 成员隐藏性, 0: 显示 1: 隐藏 参考{@link com.everhomes.rest.organization.VisibleFlag}</li>
 * </ul>
 */
public class OrganizationMemberV2DTO extends OrganizationMemberDTO {

    private Integer namespaceId;

    private Date endTime;

    public OrganizationMemberV2DTO() {
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
