// @formatter:off
package com.everhomes.uniongroup;

import com.everhomes.server.schema.tables.pojos.EhUniongroupMemberDetails;

import java.util.List;

public interface UniongroupConfigureProvider {

    void createUniongroupConfigures(UniongroupConfigures uniongroupConfigures);

    void updateUniongroupConfigures(UniongroupConfigures uniongroupConfigures);

    UniongroupConfigures findUniongroupConfiguresById(Long id);

    UniongroupConfigures findUniongroupConfiguresByCurrentId(Integer namespaceId, Long currentId, String groupType);

    UniongroupConfigures findUniongroupConfiguresByCurrentId(Integer namespaceId, Long currentId, String groupType, Integer versionCode);

    List<UniongroupConfigures> listUniongroupConfigures(Integer namespaceId);

    List<UniongroupConfigures> listUniongroupConfiguresByGroupId(Integer namespaceId, Long groupId);

    List<Long> listOrgCurrentIdsOfUniongroupConfigures(Integer namespaceId, Long enterpriseId, String groupType);

    List<Long> listOrgCurrentIdsOfUniongroupConfigures(Integer namespaceId, Long enterpriseId, String groupType, Integer versionCode);

    List<Long> listOrgCurrentIdsOfUniongroupConfiguresByGroupId(Integer namespaceId, Long enterpriseId, Long groupId);

    List<Long> listDetailCurrentIdsOfUniongroupConfigures(Integer namespaceId, Long enterpriseId, String groupType);

    List<Long> listDetailCurrentIdsOfUniongroupConfigures(Integer namespaceId, Long enterpriseId, String groupType, Integer versionCode);

    void deleteUniongroupConfigres(UniongroupConfigures uniongroupConfigures);

    void deleteUniongroupConfigresByOrgIds(Integer namespaceId, List<Long> orgIds);


    void createUniongroupMemberDetail(UniongroupMemberDetail uniongroupMemberDetail);

    void batchCreateUniongroupMemberDetail(List<EhUniongroupMemberDetails> unionDetailList);

    void updateUniongroupMemberDetail(UniongroupMemberDetail uniongroupMemberDetail);

    UniongroupMemberDetail findUniongroupMemberDetailById(Long id);

    List<UniongroupMemberDetail> findUniongroupMemberDetailByDetailIdWithoutGroupType(Integer namespaceId, Long detailId);

    UniongroupMemberDetail findUniongroupMemberDetailByDetailId(Integer namespaceId, Long detailId, String groupType);

    UniongroupMemberDetail findUniongroupMemberDetailByDetailId(Integer namespaceId, Long detailId, String groupType, Integer versionCode);

    List<UniongroupMemberDetail> listUniongroupMemberDetail(Integer namespaceId, Long groupId, Long ownerId);

    void deleteUniongroupMemberDetailsByDetailIds(List<Long> detailIds, String groupType);

    void deleteUniongroupMemberDetailsByDetailIds(List<Long> detailIds, String groupType, Integer versionCode);

    List<UniongroupMemberDetail> listUniongroupMemberDetailByGroupType(Integer namespaceId, Long ownerId, Long groupId, String groupType);

    List<UniongroupMemberDetail> listUniongroupMemberDetail(Long groupId);

    public Integer countUnionGroupMemberDetailsByOrgId(Integer namespaceId, Long ownerId);

    List<Object[]> listUniongroupMemberDetailsCount(Integer namespaceId, List<Long> groupIds, Long ownerId);

    List<Object[]> listUniongroupMemberDetailsInfo(Integer namespaceId, Long groupId, Long ownerId);

    void deleteUniongroupConfigresByGroupId(Long groupId, Long organizationId);

    void deleteUniongroupConfigresByGroupId(Long groupId, Long organizationId, Integer versionCode);

    void deleteUniongroupMemberDetailByGroupId(Long groupId, Long organizationId);

    void deleteUniongroupMemberDetailByGroupId(Long groupId, Long organizationId, Integer versionCode);

    void deleteUniongroupMemberDetails(UniongroupMemberDetail uniongroupMemberDetail);

    List<Object[]> listUniongroupMemberGroupIds(Integer namespaceId, Long ownerId);

    List listDetailNotInUniongroup(Integer namespaceId, Long organizationId);

	Integer countUnionGroupMemberDetailsByGroupId(Integer namespaceId, Long groupId);

    public void deleteUniongroupConfigresByCurrentIdAndGroupTypeAndVersion(Long detailId, String groupType, Integer versionCode);
}