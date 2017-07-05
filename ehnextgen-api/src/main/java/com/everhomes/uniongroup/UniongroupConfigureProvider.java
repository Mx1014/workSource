// @formatter:off
package com.everhomes.uniongroup;

import com.everhomes.server.schema.tables.pojos.EhUniongroupMemberDetails;

import java.util.List;

public interface UniongroupConfigureProvider {

    void createUniongroupConfigures(UniongroupConfigures uniongroupConfigures);

    void updateUniongroupConfigures(UniongroupConfigures uniongroupConfigures);

    UniongroupConfigures findUniongroupConfiguresById(Long id);

    UniongroupConfigures findUniongroupConfiguresByTargetId(Integer namespaceId, Long TargetId);

    List<UniongroupConfigures> listUniongroupConfigures(Integer namespaceId);

    List<UniongroupConfigures> listUniongroupConfiguresByGroupId(Integer namespaceId, Long groupId);

    List<Long> listOrgTargetIdsOfUniongroupConfigures(Integer namespaceId);

    void deleteUniongroupConfigres(UniongroupConfigures uniongroupConfigures);

    void createUniongroupMemberDetail(UniongroupMemberDetail uniongroupMemberDetail);

    public void batchCreateUniongroupMemberDetail(List<EhUniongroupMemberDetails> unionDetailList);

    void updateUniongroupMemberDetail(UniongroupMemberDetail uniongroupMemberDetail);

    UniongroupMemberDetail findUniongroupMemberDetailById(Long id);

    List<UniongroupMemberDetail> listUniongroupMemberDetail(Integer namespaceId, Long groupId, Long ownerId);

    void deleteUniongroupMemberDetailsByDetailIds(Integer namespaceId, List<Long> detailIds);

    List<UniongroupMemberDetail> listUniongroupMemberDetailByGroupType(Integer namespaceId, Long ownerId, Long groupId, String groupType);

    List<UniongroupMemberDetail> listUniongroupMemberDetailsWithCondition(Integer namespaceId, Long groupId, String groupType);

    List<UniongroupMemberDetail> listUniongroupMemberDetail(Long groupId);

    List<Object[]> listUniongroupMemberCount(Integer namespaceId, List<Long> groupIds, Long ownerId);
}