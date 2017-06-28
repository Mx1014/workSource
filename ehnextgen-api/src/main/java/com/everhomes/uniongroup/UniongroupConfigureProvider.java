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

    void deleteUniongroupConfigres(UniongroupConfigures uniongroupConfigure);

    void createUniongroupMemberDetail(UniongroupMemberDetail uniongroupMemberDetail);

    public void batchCreateUniongroupMemberDetail(List<EhUniongroupMemberDetails> unionDetailList);

    void updateUniongroupMemberDetail(UniongroupMemberDetail uniongroupMemberDetail);

    UniongroupMemberDetail findUniongroupMemberDetailById(Long id);

    List<UniongroupMemberDetail> listUniongroupMemberDetail();
}