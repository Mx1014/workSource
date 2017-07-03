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


    private XContentBuilder createDoc(UniongroupMemberDetail uniongroupMemberDetail){
        try{
            XContentBuilder b = XContentFactory.jsonBuilder().startObject();
            b.field("id", uniongroupMemberDetail.getId());
            b.field("namespaceId", uniongroupMemberDetail.getNamespaceId());
            b.field("groupType", uniongroupMemberDetail.getGroupType());
            b.field("groupId", uniongroupMemberDetail.getGroupId());
            b.field("detailId", uniongroupMemberDetail.getDetailId());
            b.field("targetType", uniongroupMemberDetail.getTargetType());
            b.field("targetId", uniongroupMemberDetail.getTargetId());
            b.field("enterpriseId", uniongroupMemberDetail.getEnterpriseId());
            b.field("contact_name", uniongroupMemberDetail.getContactName());
            b.field("contact_token", uniongroupMemberDetail.getContactToken());
            b.field("update_time", uniongroupMemberDetail.getUpdateTime());
            b.field("operator_uid", uniongroupMemberDetail.getOperatorUid());
            String tagStr = group.getTag();
            if((null != tagStr) && (!tagStr.isEmpty())) {
                String[] tags = tagStr.split(",");
                if(tags.length > 0) {
                    b.startArray("tags");
                    for(String tag : tags) {
                        String newTag = tag.trim();
                        b.startObject().field("department_name", uniongroupMemberDetail.get)
                                .field("department_id", newTag)
                                .endObject();
                    }
                    b.endArray();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
