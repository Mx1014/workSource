// @formatter:off
package com.everhomes.uniongroup;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.server.schema.tables.pojos.EhUniongroupMemberDetails;

import java.util.List;

public interface UniongroupConfigureProvider {

    void createUniongroupConfigures(UniongroupConfigures uniongroupConfigures);

    void updateUniongroupConfigures(UniongroupConfigures uniongroupConfigures);

    UniongroupConfigures findUniongroupConfiguresById(Long id);

    UniongroupConfigures findUniongroupConfiguresByCurrentId(Integer namespaceId, Long currentId, String groupType, Integer versionCode, String currentType);

    List<UniongroupConfigures> listUniongroupConfigures(Integer namespaceId);

    List<UniongroupConfigures> listUniongroupConfiguresByGroupId(Integer namespaceId, Long groupId, Integer versionCode);

    List<Long> listOrgCurrentIdsOfUniongroupConfigures(Integer namespaceId, Long enterpriseId, String groupType);

    List<Long> listOrgCurrentIdsOfUniongroupConfigures(Integer namespaceId, Long enterpriseId, String groupType, Integer versionCode);

    List<Long> listOrgCurrentIdsOfUniongroupConfiguresByGroupId(Integer namespaceId, Long enterpriseId, Long groupId);

    List<Long> listDetailCurrentIdsOfUniongroupConfigures(Integer namespaceId, Long enterpriseId, String groupType);

    List<Long> listDetailCurrentIdsOfUniongroupConfigures(Integer namespaceId, Long enterpriseId, String groupType, Integer versionCode);

    void deleteUniongroupConfigres(UniongroupConfigures uniongroupConfigures);

    void deleteUniongroupConfigresByCurrentIds(Integer namespaceId, List<Long> currentIds, String groupType, Integer versionCode);

    void createUniongroupMemberDetail(UniongroupMemberDetail uniongroupMemberDetail);

    void batchCreateUniongroupConfigres(List<com.everhomes.server.schema.tables.pojos.EhUniongroupConfigures> unionConfiguresist);

    void batchCreateUniongroupConfigresToVersion(List<com.everhomes.server.schema.tables.pojos.EhUniongroupConfigures> unionConfiguresist, Integer n2);

    void batchCreateUniongroupMemberDetail(List<EhUniongroupMemberDetails> unionDetailList);

    void batchCreateUniongroupMemberDetailToVersion(List<EhUniongroupMemberDetails> unionDetailList, Integer n2);

    void updateUniongroupMemberDetail(UniongroupMemberDetail uniongroupMemberDetail);

    UniongroupMemberDetail findUniongroupMemberDetailById(Long id);

    List<UniongroupMemberDetail> findUniongroupMemberDetailByDetailIdWithoutGroupType(Integer namespaceId, Long detailId);

    UniongroupMemberDetail findUniongroupMemberDetailByDetailId(Integer namespaceId, Long detailId, String groupType);

    UniongroupMemberDetail findUniongroupMemberDetailByDetailId(Integer namespaceId, Long detailId, String groupType, Integer versionCode);

    public List<UniongroupMemberDetail> listUniongroupMemberDetail(Integer namespaceId, Long groupId, Long ownerId);

    List<UniongroupMemberDetail> listUniongroupMemberDetail(Integer namespaceId, Long groupId, Long ownerId, Integer versionCode);

    //获取当前版本
    List<UniongroupMemberDetail> listUniongroupMemberDetail(Long groupId, Integer versionCode);

    void deleteUniongroupMemberDetailsByDetailIds(List<Long> detailIds, String groupType);

    void deleteUniongroupMemberDetailsByDetailIds(List<Long> detailIds, String groupType, Integer versionCode);

    List<UniongroupMemberDetail> listUniongroupMemberDetailByGroupType(Integer namespaceId, Long ownerId, String groupType);


    public Integer countUnionGroupMemberDetailsByOrgId(Integer namespaceId, Long ownerId);

    List<Object[]> listUniongroupMemberDetailsCount(Integer namespaceId, List<Long> groupIds, Long ownerId);

    List<Object[]> listUniongroupMemberDetailsInfo(Integer namespaceId, Long groupId, Long ownerId);

    void deleteUniongroupConfigresByGroupId(Long groupId, Long organizationId);

    void deleteUniongroupConfigresByGroupId(Long groupId, Long organizationId, Integer versionCode);

    void deleteUniongroupMemberDetailByGroupId(Long groupId, Long organizationId);

    void deleteUniongroupMemberDetailByGroupId(Long groupId, Long organizationId, Integer versionCode);

    void deleteUniongroupMemberDetails(UniongroupMemberDetail uniongroupMemberDetail);

    List<Object[]> listUniongroupMemberGroupIds(Integer namespaceId, Long ownerId);

    List listDetailNotInUniongroup(Integer namespaceId, Long organizationId, String contactName, Integer versionCode, Long departmentId);

    List listDetailNotInUniongroup(Integer namespaceId, Long organizationId, String contactName, Integer versionCode, Long departmentId, Integer pageSize, CrossShardListingLocator pageAnchor);

    Integer countUnionGroupMemberDetailsByGroupId(Integer namespaceId, Long groupId);

    void deleteUniongroupConfigresByCurrentIdAndGroupTypeAndVersion(Long detailId, String groupType, Integer versionCode);

    List<UniongroupMemberDetail> listUniongroupMemberDetailsByUserName(Long ownerId, String userName);


    void updateUniongroupConfiguresVersion(Integer namespaceId, String groupType, Long enterpriseId, Integer v1, Integer v2);

    void updateUniongroupMemberDetailsVersion(Integer namespaceId, String groupType, Long enterpriseId, Integer v1, Integer v2);

    void deleteUniongroupConfigresByEnterpriseIdAndGroupType(Integer namespaceId, String groupType, Long enterpriseId, Integer versionCode);

    void deleteUniongroupMemberDetailsByEnterpriseIdAndGroupType(Integer namespaceId, String groupType, Long enterpriseId, Integer versionCode);

//    void cloneGroupTypeDataToVersion(Integer namespaceId, Long enterpriseId, String groupType, Integer n1, Integer n2);

    List<com.everhomes.server.schema.tables.pojos.EhUniongroupConfigures> listUniongroupConfigures(Integer namespaceId, String groupType, Long enterpriseId, Long groupId, Integer versionCode);

    List<EhUniongroupMemberDetails> listUniongroupMemberDetail(Integer namespaceId, String groupType, Long enterpriseId, Long groupId, Integer versionCode);

}