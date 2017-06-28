// @formatter:off
package com.everhomes.uniongroup;

import com.everhomes.server.schema.tables.pojos.EhUniongroupMemberDetails;

import java.util.List;

public interface UniongroupConfigureProvider {

    void createUniongroupConfigures(UniongroupConfigures uniongroupConfigures);

    void updateUniongroupConfigures(UniongroupConfigures uniongroupConfigures);

    UniongroupConfigures findUniongroupConfiguresById(Long id);

    List<UniongroupConfigures> listUniongroupConfigures();

    List<UniongroupConfigures> listUniongroupConfiguresByGroupId(Long groupId);

    void deleteUniongroupConfigres(UniongroupConfigures uniongroupConfigure);

    void createUniongroupMemberDetail(UniongroupMemberDetail uniongroupMemberDetail);

    public void batchCreateUniongroupMemberDetail(List<EhUniongroupMemberDetails> unionDetailList);

    void updateUniongroupMemberDetail(UniongroupMemberDetail uniongroupMemberDetail);

    UniongroupMemberDetail findUniongroupMemberDetailById(Long id);

    List<UniongroupMemberDetail> listUniongroupMemberDetail();
}