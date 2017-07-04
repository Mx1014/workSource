package com.everhomes.uniongroup;

import com.everhomes.rest.organization.SearchOrganizationCommand;
import com.everhomes.rest.search.GroupQueryResult;
import com.everhomes.search.AbstractElasticSearch;
import com.everhomes.search.UniongroupSearcher;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/3.
 */
public class UniongroupSearcherImpl extends AbstractElasticSearch implements UniongroupSearcher {
    @Override
    public void deleteById(Long id) {

    }

    @Override
    public void bulkUpdate(List<UniongroupMemberDetail> uniongroupMemberDetails) {

    }

    @Override
    public void feedDoc(UniongroupMemberDetail uniongroupMemberDetail) {
        XContentBuilder source = createDoc(uniongroupMemberDetail);
        feedDoc(uniongroupMemberDetail.getId().toString(), source);
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


    private XContentBuilder createDoc(UniongroupMemberDetail uniongroupMemberDetail) {
        try {
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
            Map<Long, String> department = uniongroupMemberDetail.getDepartment();
            if (department.size() > 0) {
                for (Long i : department.keySet()) {
                    b.startObject().field("department_id", i)
                            .field("department_name", department.get(i))
                            .endObject();
                }
                b.endArray();
            }
            Map<Long, String> job_level = uniongroupMemberDetail.getJob_position();
            if (department.size() > 0) {
                for (Long i : department.keySet()) {
                    b.startObject().field("job_position_id", i)
                            .field("job_position_name", department.get(i))
                            .endObject();
                }
                b.endArray();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
