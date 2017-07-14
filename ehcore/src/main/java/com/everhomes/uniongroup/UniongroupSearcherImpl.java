package com.everhomes.uniongroup;

import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationMemberDetails;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.organization.OrganizationGroupType;
import com.everhomes.rest.uniongroup.SearchUniongroupDetailCommand;
import com.everhomes.rest.uniongroup.UniongroupType;
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
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2017/7/3.
 */
@Service
public class UniongroupSearcherImpl extends AbstractElasticSearch implements UniongroupSearcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(UniongroupSearcherImpl.class);

    @Autowired
    private OrganizationProvider organizationProvider;

    @Autowired
    private UniongroupConfigureProvider uniongroupConfigureProvider;

    @Override
    public void deleteById(Long id) {
        deleteById(id.toString());
    }

    @Override
    public void bulkUpdate(List<UniongroupMemberDetail> uniongroupMemberDetails) {
        BulkRequestBuilder brb = getClient().prepareBulk();
        for (UniongroupMemberDetail detail : uniongroupMemberDetails) {
            XContentBuilder source = createDoc(detail);
            if (null != source) {
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
    public void syncUniongroupDetailsIndexs() {
        this.deleteAll();
        List<Organization> orgs = this.organizationProvider.listHeadEnterprises();
        for (Organization org : orgs) {
            this.syncUniongroupDetailsAtOrg(org, UniongroupType.SALARYGROUP.getCode());
        }
//        this.optimize(1);
        this.refresh();
    }

    @Override
    public void syncUniongroupDetailsAtOrg(Organization org, String groupType) {
        List<UniongroupMemberDetail> details = this.uniongroupConfigureProvider.listUniongroupMemberDetailByGroupType(org.getNamespaceId(), org.getId(), null, UniongroupType.fromCode(groupType).getCode());
        if (details != null && details.size() > 0) {
            //查询部门和岗位和工号
            for (UniongroupMemberDetail detail : details) {
                OrganizationMemberDetails member_detail = this.organizationProvider.findOrganizationMemberDetailsByDetailId(detail.getDetailId());
                if(member_detail != null)
                    detail.setEmployeeNo(member_detail.getEmployeeNo());
                Map depart_map = this.organizationProvider.listOrganizationsOfDetail(org.getNamespaceId(), detail.getDetailId(), OrganizationGroupType.DEPARTMENT.getCode());
                if (depart_map != null)
                    detail.setDepartment(depart_map);
                Map jobp_map = this.organizationProvider.listOrganizationsOfDetail(org.getNamespaceId(), detail.getDetailId(), OrganizationGroupType.JOB_POSITION.getCode());
                if (jobp_map != null) {
                    detail.setJobPosition(jobp_map);
                }
            }
            this.bulkUpdate(details);
            LOGGER.info("uniongroupDetails process count: " + details.size());
        }
    }


    @Override
    public List query(SearchUniongroupDetailCommand cmd) {
        SearchRequestBuilder builder = getClient().prepareSearch(getIndexName()).setTypes(getIndexType());
        BoolQueryBuilder bqb = new BoolQueryBuilder();

        bqb.must(QueryBuilders.termQuery("namespaceId", cmd.getNamespaceId()));
        bqb = bqb.must(QueryBuilders.termQuery("enterpriseId", cmd.getEnterpriseId()));

        if (cmd.getDepartmentId() != null && cmd.getDepartmentId() != 0L) {
            bqb = bqb.must(QueryBuilders.termQuery("department.department_id", cmd.getDepartmentId()));
        }
        if (cmd.getGroupId() != null && cmd.getGroupId() != 0L) {
            bqb = bqb.must(QueryBuilders.termQuery("groupId", cmd.getGroupId()));
        }
        if (cmd.getKeyword() != null && !cmd.getKeyword().isEmpty()){
            bqb = bqb.must(QueryBuilders.matchQuery("contactName", cmd.getKeyword()));
//            bqb = bqb.should(QueryBuilders.matchQuery("employeeNo", cmd.getKeyword()));
        }

        builder.setFrom(cmd.getPageAnchor().intValue() * cmd.getPageSize()).setSize(cmd.getPageSize() + 1);
        builder.setQuery(bqb);
        SearchResponse rsp = builder.execute().actionGet();

        List<UniongroupMemberDetail> list = new ArrayList<>();
        SearchHit[] docs = rsp.getHits().getHits();
        for (SearchHit sd : docs) {
            Map<String, Object> m = sd.getSource();
            UniongroupMemberDetail detail = new UniongroupMemberDetail();
            detail.setId(Long.valueOf(m.get("id").toString()));
            detail.setNamespaceId(Integer.valueOf(m.get("namespaceId").toString()));
            detail.setGroupType(m.get("groupType").toString());
            detail.setGroupId(Long.valueOf(m.get("groupId").toString()));
            detail.setDetailId(Long.valueOf(m.get("detailId").toString()));
            detail.setTargetType(m.get("targetType").toString());
            detail.setTargetId(Long.valueOf(m.get("targetId").toString()));
            detail.setEnterpriseId(Long.valueOf(m.get("enterpriseId").toString()));
            detail.setContactName(m.get("contactName").toString());
            detail.setContactToken(m.get("contactToken").toString());
            if (m.get("department") != null){
                List<Map> department = (List<Map>) m.get("department");
                Map<Long,String> departmentMap = new HashMap<>();
                if(department.size() > 0){
                    department.forEach(r ->{
                        departmentMap.put(Long.valueOf(r.get("department_id").toString()), r.get("department_name").toString());
                    });
                }
                detail.setDepartment(departmentMap);
            }
            if (m.get("job_position") != null){
                List<Map> jobPosition = (List<Map>) m.get("job_position");
                Map<Long,String> jobPositionMap = new HashMap<>();
                if(jobPosition.size() > 0){
                    jobPosition.forEach(r ->{
                        jobPositionMap.put(Long.valueOf(r.get("job_position_id").toString()), r.get("job_position_name").toString());
                    });
                }
                detail.setDepartment(jobPositionMap);
            }
            SimpleDateFormat simpleDateFormat  = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            TimeZone utcZone = TimeZone.getTimeZone("UTC");
            if (!StringUtils.isEmpty(m.get("employeeNo")))
                detail.setEmployeeNo(m.get("employeeNo").toString());
            simpleDateFormat.setTimeZone(utcZone);
            try {
                Date myDate = simpleDateFormat.parse(m.get("updateTime").toString());
                SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                detail.setUpdateTime(Timestamp.valueOf(sdf.format(myDate)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            detail.setOperatorUid(Long.valueOf(m.get("operatorUid").toString()));
            list.add(detail);
        }

        if (list.size() > cmd.getPageSize()) {
            list.remove(list.size() - 1);
            cmd.setPageAnchor(list.get(list.size() - 1).getId());
        }
        return list;
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
            b.field("contactName", uniongroupMemberDetail.getContactName());
            b.field("contactToken", uniongroupMemberDetail.getContactToken());
            b.field("updateTime", uniongroupMemberDetail.getUpdateTime());
            b.field("operatorUid", uniongroupMemberDetail.getOperatorUid());
            b.field("employeeNo", uniongroupMemberDetail.getEmployeeNo());
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
            Map<Long, String> job_position = uniongroupMemberDetail.getJobPosition();
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
