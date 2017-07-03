package com.everhomes.uniongroup;

import com.everhomes.address.Address;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationAddress;
import com.everhomes.rest.organization.SearchOrganizationCommand;
import com.everhomes.rest.search.GroupQueryResult;
import com.everhomes.search.AbstractElasticSearch;
import com.everhomes.search.UniongroupSearcher;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/3.
 */
public class UniongroupSearcherImpl extends AbstractElasticSearch implements UniongroupSearcher{
    @Override
    public void deleteById(Long id) {

    }

    @Override
    public void bulkUpdate(List<Organization> organizations) {

    }

    @Override
    public void feedDoc(Organization organization) {

    }

    @Override
    public void syncFromDb() {

    }

    @Override
    public GroupQueryResult query(SearchOrganizationCommand cmd) {
        return null;
    }

    @Override
    public String getIndexType() {
        return null;
    }


    private XContentBuilder createDoc(Organization organization){
        return null;
    }
}
