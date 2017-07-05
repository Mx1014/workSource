package com.everhomes.uniongroup;

import com.everhomes.rest.organization.SearchOrganizationCommand;
import com.everhomes.rest.search.GroupQueryResult;
import com.everhomes.rest.uniongroup.SearchUniongroupDetailCommand;
import com.everhomes.search.AbstractElasticSearch;
import com.everhomes.search.SearchUtils;
import com.everhomes.search.UniongroupSearcher;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/3.
 */
@Service
public class UniongroupSearcherImpl extends AbstractElasticSearch implements UniongroupSearcher {
    @Override
    public void deleteById(Long id) {

    }

    @Override
    public void bulkUpdate(List<UniongroupMemberDetail> uniongroupMemberDetails) {
        BulkRequestBuilder brb = getClient().prepareBulk();
        for (UniongroupMemberDetail detail : uniongroupMemberDetails) {
            XContentBuilder source = createDoc(detail);
            if(null != source) {
                brb.add(Requests.indexRequest(getIndexName()).type(getIndexType()).id(detail.getId().toString()).source(source));
            }
        }
        if (brb.numberOfActions() > 0) {
            brb.execute().actionGet();
        }
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
    public List query(SearchUniongroupDetailCommand cmd) {
        SearchRequestBuilder builder = getClient().prepareSearch(getIndexName()).setTypes(getIndexType());
        BoolQueryBuilder bqb = new BoolQueryBuilder();

        bqb.must(QueryBuilders.termQuery("namespaceId",cmd.getNamespaceId()));
        bqb.must(QueryBuilders.termQuery("enterpriseId",cmd.getEnterpriseId()));

        if(cmd.getDepartmentId() != null && cmd.getDepartmentId() != 0L){
            bqb = bqb.must(QueryBuilders.termQuery("department.department_id",cmd.getDepartmentId()));
        }
        if(cmd.getGroupId() != null && cmd.getGroupId() != 0L){
            bqb = bqb.must(QueryBuilders.termQuery("groupId",cmd.getGroupId()));
        }
        builder.setQuery(bqb);
        SearchResponse rsp = builder.execute().actionGet();
        rsp.getHits();
        return null;
    }

    @Override
    public String getIndexType() {
        return SearchUtils.UNIONGROUP_DETAILS;
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
            if (department != null && department.size() > 0) {
                b.startArray("department");
                for (Long i : department.keySet()) {
                    b.startObject().field("department_id", i)
                            .field("department_name", department.get(i))
                            .endObject();
                }
                b.endArray();
            }
            Map<Long, String> job_position = uniongroupMemberDetail.getJob_position();
            if (job_position != null && job_position.size() > 0) {
                b.startArray("job_position");
                for (Long i : job_position.keySet()) {
                    b.startObject().field("job_position_id", i)
                            .field("job_position_name", job_position.get(i))
                            .endObject();
                }
                b.endArray();
            }
            b.endObject();
            return b;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
