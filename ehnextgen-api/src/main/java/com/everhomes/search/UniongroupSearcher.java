package com.everhomes.search;

import com.everhomes.organization.Organization;
import com.everhomes.rest.uniongroup.SearchUniongroupDetailCommand;
import com.everhomes.uniongroup.UniongroupMemberDetail;

import java.util.List;

/**
 * Created by lei.lv on 2017/7/3.
 */
public interface UniongroupSearcher {
    void deleteById(Long id);

    void bulkUpdate(List<UniongroupMemberDetail> uniongroupMemberDetails);

    void feedDoc(UniongroupMemberDetail uniongroupMemberDetail);

    void syncUniongroupDetailsIndexs();

    void syncUniongroupDetailsAtOrg(Organization org);

    List query(SearchUniongroupDetailCommand cmd);
}
