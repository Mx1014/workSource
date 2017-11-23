package com.everhomes.quality;

import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.quality.*;
import com.everhomes.search.AbstractElasticSearch;
import com.everhomes.search.QualityTaskSearcher;
import com.everhomes.search.SearchUtils;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.mysql.jdbc.StringUtils;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ying.xiong on 2017/6/8.
 */
@Component
public class QualityTaskSearcherImpl extends AbstractElasticSearch implements QualityTaskSearcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(QualityTaskSearcherImpl.class);

    @Autowired
    private QualityProvider qualityProvider;

    @Autowired
    private CommunityProvider communityProvider;

    @Autowired
    private OrganizationProvider organizationProvider;

    @Autowired
    private ConfigurationProvider configProvider;

    @Override
    public void deleteById(Long id) {
        deleteById(id.toString());
    }

    @Override
    public void bulkUpdate(List<QualityInspectionTasks> tasks) {
        BulkRequestBuilder brb = getClient().prepareBulk();
        for (QualityInspectionTasks task : tasks) {

            XContentBuilder source = createDoc(task);
            if(null != source) {
                LOGGER.info("quality task id:" + task.getId());
                brb.add(Requests.indexRequest(getIndexName()).type(getIndexType())
                        .id(task.getId().toString()).source(source));
            }

        }
        if (brb.numberOfActions() > 0) {
            brb.execute().actionGet();
        }
    }

    @Override
    public void feedDoc(QualityInspectionTasks task) {

        XContentBuilder source = createDoc(task);

        feedDoc(task.getId().toString(), source);
    }

    @Override
    public void syncFromDb() {
        int pageSize = 200;
        this.deleteAll();

        CrossShardListingLocator locator = new CrossShardListingLocator();
        for(;;) {
            List<QualityInspectionTasks> tasks = qualityProvider.listQualityInspectionTasks(locator, pageSize);

            if(tasks.size() > 0) {
                this.bulkUpdate(tasks);
            }

            if(locator.getAnchor() == null) {
                break;
            }
        }

        this.optimize(1);
        this.refresh();

        LOGGER.info("sync for quality tasks ok");
    }

    @Override
    public List<Long> query(SearchQualityTasksCommand cmd) {
        SearchRequestBuilder builder = getClient().prepareSearch(getIndexName()).setTypes(getIndexType());
        QueryBuilder qb = null;
        if(cmd.getTargetName() == null || cmd.getTargetName().isEmpty()) {
            qb = QueryBuilders.matchAllQuery();
        } else {
            qb = QueryBuilders.multiMatchQuery(cmd.getTargetName())
                    .field("targetName", 1.2f);

            builder.setHighlighterFragmentSize(60);
            builder.setHighlighterNumOfFragments(8);
            builder.addHighlightedField("targetName");
        }

        FilterBuilder fb = null;
        FilterBuilder nfb = FilterBuilders.termFilter("status", QualityInspectionTaskStatus.NONE.getCode());
        fb = FilterBuilders.notFilter(nfb);
        fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("namespaceId", UserContext.getCurrentNamespaceId()));
        fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("ownerId", cmd.getOwnerId()));
        fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("ownerType",cmd.getOwnerType()));
        if(cmd.getTargetId() != null)
            fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("targetId", cmd.getTargetId()));

        if(!StringUtils.isNullOrEmpty(cmd.getTargetType()))
            fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("targetType", cmd.getTargetType()));

        if(cmd.getExecuteStatus() != null)
            fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("status", cmd.getExecuteStatus()));

        if(!StringUtils.isNullOrEmpty(cmd.getExecutorName())) {
            fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("executorName", cmd.getExecutorName()));
        }

        if(cmd.getManualFlag() != null) {
            fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("manualFlag", cmd.getManualFlag()));
        }

        if(cmd.getReviewResult() != null) {
            fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("reviewResult", cmd.getReviewResult()));
        }

        if(cmd.getSampleId() != null) {
            fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("sampleId", cmd.getSampleId()));
        }

        if(cmd.getStartDate() != null) {
            RangeFilterBuilder rf = new RangeFilterBuilder("startDate");
            rf.gt(cmd.getStartDate());
            fb = FilterBuilders.andFilter(fb, rf);
        }

        if(cmd.getEndDate() != null) {
            RangeFilterBuilder rf = new RangeFilterBuilder("endDate");
            rf.lt(cmd.getEndDate());
            fb = FilterBuilders.andFilter(fb, rf);
        }
        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        Long anchor = 0l;
        if(cmd.getPageAnchor() != null) {
            anchor = cmd.getPageAnchor();
        }

        qb = QueryBuilders.filteredQuery(qb, fb);
        builder.setSearchType(SearchType.QUERY_THEN_FETCH);
        builder.setFrom(anchor.intValue() * pageSize).setSize(pageSize + 1);
        builder.setQuery(qb);

        SearchResponse rsp = builder.execute().actionGet();
        if(LOGGER.isDebugEnabled()) {
            LOGGER.info("quality task searcher query builder ："+builder);
            LOGGER.info("quality task searcher query rsp ："+rsp);
        }
        List<Long> ids = getIds(rsp);
//        ListQualityInspectionTasksResponse response = new ListQualityInspectionTasksResponse();
//
//        if(ids.size() > pageSize) {
//            response.setNextPageAnchor(anchor + 1);
//            ids.remove(ids.size() - 1);
//        }
//
//        List<QualityInspectionTaskDTO> dtos = new ArrayList<QualityInspectionTaskDTO>();
//        for(Long id : ids) {
//            QualityInspectionTasks task = qualityProvider.findVerificationTaskById(id);
//            if(task != null) {
//                QualityInspectionTaskDTO dto = ConvertHelper.convert(task, QualityInspectionTaskDTO.class);
//                Community community = communityProvider.findCommunityById(dto.getTargetId());
//                if(community != null)
//                    dto.setTargetName(community.getName());
//
//                List<OrganizationMember> executors = organizationProvider.listOrganizationMembersByUId(task.getExecutorId());
//                if(executors != null && executors.size() > 0) {
//                    dto.setExecutorName(executors.get(0).getContactName());
//                }
//
//                QualityInspectionSpecifications category = qualityProvider.findSpecificationById(dto.getCategoryId(), dto.getOwnerType(), dto.getOwnerId());
//                if(category != null) {
//                    dto.setCategoryName(getSpecificationNamePath(category.getPath(), category.getOwnerType(), category.getOwnerId()));
//                }
//                dtos.add(dto);
//            }
//        }
//        LOGGER.info("query quality task: {}", dtos);
//        response.setTasks(dtos);
        return ids;
    }

    private String getSpecificationNamePath(String path, String ownerType, Long ownerId) {
        path = path.substring(1,path.length());
        String[] pathIds = path.split("/");
        StringBuilder sb = new StringBuilder();
        for(String pathId : pathIds) {
            Long specificationId = Long.valueOf(pathId);
            QualityInspectionSpecifications specification = qualityProvider.getSpecificationById(specificationId);
            sb.append("/" + specification.getName());
        }

        String namePath = sb.toString();
        return namePath;
    }

    @Override
    public String getIndexType() {
        return SearchUtils.QUALITY_TASK;
    }

    private XContentBuilder createDoc(QualityInspectionTasks task){
        try {
            XContentBuilder b = XContentFactory.jsonBuilder().startObject();
            b.field("namespaceId", task.getNamespaceId());
            b.field("parentId", task.getParentId());
            b.field("ownerId", task.getOwnerId());
            b.field("ownerType", task.getOwnerType());
            b.field("targetId", task.getTargetId());
            b.field("manualFlag", task.getManualFlag());
            b.field("startDate", task.getExecutiveStartTime());
            b.field("endDate", task.getExecutiveExpireTime());
            b.field("sampleId", task.getParentId());
            b.field("reviewResult", task.getReviewResult());
            Community community = communityProvider.findCommunityById(task.getTargetId());
            if(community != null) {
                b.field("targetName", community.getName());
            } else {
                b.field("targetName", "");
            }

            b.field("status", task.getStatus());
            b.field("executorId", task.getExecutorId());
            b.field("operatorId", task.getOperatorId());
            List<OrganizationMember> executors = organizationProvider.listOrganizationMembersByUId(task.getExecutorId());
            if(executors != null && executors.size() > 0) {
                b.field("executorName", executors.get(0).getContactName());
            } else {
                b.field("executorName", "");
            }

            List<OrganizationMember> operators = organizationProvider.listOrganizationMembersByUId(task.getOperatorId());
            if(operators != null && operators.size() > 0) {
                b.field("operatorName", operators.get(0).getContactName());
            } else {
                b.field("operatorName", "");
            }

            b.endObject();
            return b;
        } catch (IOException ex) {
            LOGGER.error("Create quality task " + task.getId() + " error");
            return null;
        }
    }
}
