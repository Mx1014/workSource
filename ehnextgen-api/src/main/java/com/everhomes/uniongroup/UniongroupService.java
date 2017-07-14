// @formatter:off
package com.everhomes.uniongroup;

import com.everhomes.organization.OrganizationMember;
import com.everhomes.rest.uniongroup.*;

import java.util.List;

public interface UniongroupService {

    /**
     * 保存一次组配置
     **/
    public void saveUniongroupConfigures(SaveUniongroupConfiguresCommand cmd);

    /**
     * 根据组Id获取配置项记录
     **/
    public List getConfiguresListByGroupId(GetUniongroupConfiguresCommand cmd);

    /**
     * 根据组Id获取组内人员记录
     **/
    public List listUniongroupMemberDetailsByGroupId(ListUniongroupMemberDetailsCommand cmd);

    List<UniongroupMemberDetailsDTO> listUniongroupMemberDetailsByGroupId(Long groupId);

    /**
     * 删除一条配置记录
     **/
    public void deleteUniongroupConfigures(UniongroupConfigures uniongroupConfigure);

    /**
     * 根据条件查询记录
     **/
    public ListUniongroupMemberDetailResponse listUniongroupMemberDetailsWithCondition(ListUniongroupMemberDetailsWithConditionCommand cmd);

    public Integer countUnionGroupMemberDetailsByOrgId(Integer namespaceId, Long ownerId);
    /**
     * 根据薪酬组id获取相关人数
     */
    public List<Object[]> listUniongroupMemberCount(Integer namespaceId, List<Long> groupIds, Long ownerId);

    public List<Object[]> listUniongroupMemberDetailsInfo(Integer namespaceId, Long salaryGroupIds, Long ownerId);

    void deleteUniongroupConfigresByGroupId(Long groupId, Long organizationId);

    void deleteUniongroupMemberDetailByGroupId(Long groupId, Long organizationId);

    List<Object[]> listUniongroupMemberGroupIds(Integer namespaceId, Long ownerId);
    /**
     * 新增或修改人员重新分配薪酬组
     **/
    void reallocatedUnion(Long enterpriseId, List<Long> departmentIds, OrganizationMember organizationMember);

    /**离职时薪酬组相关的改动**/
    void syncUniongroupAfterLeaveTheJob(Long detailId);
}