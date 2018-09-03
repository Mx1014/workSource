package com.everhomes.quality;

import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.equipment.Status;
import com.everhomes.rest.quality.CountSampleTaskScoresCommand;
import com.everhomes.rest.quality.CountSampleTaskScoresResponse;
import com.everhomes.rest.quality.ListSampleQualityInspectionResponse;
import com.everhomes.rest.quality.SampleQualityInspectionDTO;
import com.everhomes.rest.quality.SampleTaskScoreDTO;
import com.everhomes.rest.quality.SearchSampleQualityInspectionCommand;
import com.everhomes.search.AbstractElasticSearch;
import com.everhomes.search.QualityInspectionSampleSearcher;
import com.everhomes.search.SearchUtils;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.User;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeFilterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ying.xiong on 2017/6/9.
 */
@Component
public class QualityInspectionSampleSearcherImpl extends AbstractElasticSearch implements QualityInspectionSampleSearcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(QualityInspectionSampleSearcherImpl.class);

    @Autowired
    private QualityProvider qualityProvider;

    @Autowired
    private ConfigurationProvider configProvider;

    @Autowired
    private OrganizationProvider organizationProvider;

    @Autowired
    private CommunityProvider communityProvider;

    @Autowired
    private UserProvider userProvider;

    @Override
    public String getIndexType() {
        return SearchUtils.QUALITY_SAMPLE;
    }

    @Override
    public void bulkUpdate(List<QualityInspectionSamples> samples) {
        BulkRequestBuilder brb = getClient().prepareBulk();
        for (QualityInspectionSamples sample : samples) {

            XContentBuilder source = createDoc(sample);
            if(null != source) {
                LOGGER.info("quality sample id:" + sample.getId());
                brb.add(Requests.indexRequest(getIndexName()).type(getIndexType())
                        .id(sample.getId().toString()).source(source));
            }

        }
        if (brb.numberOfActions() > 0) {
            brb.execute().actionGet();
        }
    }

    @Override
    public void deleteById(Long id) {
        deleteById(id.toString());
    }

    @Override
    public void feedDoc(QualityInspectionSamples sample) {
        XContentBuilder source = createDoc(sample);

        feedDoc(sample.getId().toString(), source);
    }

    @Override
    public ListSampleQualityInspectionResponse query(SearchSampleQualityInspectionCommand cmd) {
        LOGGER.debug("SearchSampleQualityInspectionCommand time :{}"+System.currentTimeMillis());
        SearchResponse rsp = search(cmd);
        List<Long> ids = getIds(rsp);

        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        Long anchor = 0L;
        if(cmd.getPageAnchor() != null) {
            anchor = cmd.getPageAnchor();
        }

        ListSampleQualityInspectionResponse response = new ListSampleQualityInspectionResponse();
        if(ids.size() > pageSize) {
            response.setNextPageAnchor(anchor + 1);
            ids.remove(ids.size() - 1);
        }

        List<SampleQualityInspectionDTO> dtos = new ArrayList<>();
        for(Long id : ids) {
            QualityInspectionSamples sample = qualityProvider.findQualityInspectionSample(id, cmd.getOwnerType(), cmd.getOwnerId());
            SampleQualityInspectionDTO dto = ConvertHelper.convert(sample, SampleQualityInspectionDTO.class);
            List<OrganizationMember> creator = organizationProvider.listOrganizationMembersByUId(sample.getCreatorUid());
            if (creator != null && creator.size() > 0) {
                String userName = creator.get(0).getContactName();
                if (userName != null && !userName.equals("")) {
                    dto.setCreatorName(userName);
                }
            } else {
                User user = userProvider.findUserById(sample.getCreatorUid());
                if (user != null) {
                    dto.setCreatorName(user.getNickName());
                }
            }

            dtos.add(dto);
        }
        response.setSampleQualityInspectionDTOList(dtos);

        return response;
    }

    @Override
    public CountSampleTaskScoresResponse queryCount(CountSampleTaskScoresCommand cmd) {
        SearchSampleQualityInspectionCommand command = ConvertHelper.convert(cmd, SearchSampleQualityInspectionCommand.class);
        SearchResponse rsp = search(command);
        List<Long> ids = getIds(rsp);

        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        Long anchor = 0l;
        if(cmd.getPageAnchor() != null) {
            anchor = cmd.getPageAnchor();
        }

        CountSampleTaskScoresResponse response = new CountSampleTaskScoresResponse();

        if(ids.size() > pageSize) {
            response.setNextPageAnchor(anchor + 1);
            ids.remove(ids.size() - 1);
        }

        List<SampleTaskScoreDTO> dtos = new ArrayList<>();
        //可能是这里导致Alpha环境空指针
        if (ids.size() > 0) {
            for (Long id : ids) {
                QualityInspectionSamples sample = qualityProvider.findQualityInspectionSample(id, cmd.getOwnerType(), cmd.getOwnerId());
                if (sample != null) {
                    SampleTaskScoreDTO dto = ConvertHelper.convert(sample, SampleTaskScoreDTO.class);
                    List<OrganizationMember> members = organizationProvider.listOrganizationMembersByUId(dto.getCreatorUid());
                    if (members != null && members.size() > 0) {
                        dto.setCreatorName(members.get(0).getContactName());
                    }
                    dtos.add(dto);
                }

            }
        }
        response.setSampleTasks(dtos);
        return response;
    }

    private SearchResponse search(SearchSampleQualityInspectionCommand cmd) {
        SearchRequestBuilder builder = getClient().prepareSearch(getIndexName()).setTypes(getIndexType());
        QueryBuilder qb = null;
        if(cmd.getName() == null || cmd.getName().isEmpty()) {
            qb = QueryBuilders.matchAllQuery();
        } else {
            qb = QueryBuilders.multiMatchQuery(cmd.getName())
                    .field("name", 5.0f)
                    .field("name.pinyin_prefix", 2.0f)
                    .field("name.pinyin_gram", 1.0f);
            builder.setHighlighterFragmentSize(60);
            builder.setHighlighterNumOfFragments(8);
            builder.addHighlightedField("name");

        }

        FilterBuilder fb = FilterBuilders.termFilter("ownerId", cmd.getOwnerId());
        fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("namespaceId", cmd.getNamespaceId()));
//        fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("ownerType", OwnerType.fromCode(cmd.getOwnerType()).getCode()));
        fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("status", Status.ACTIVE.getCode()));

        if(cmd.getStartTime() != null) {
            RangeFilterBuilder rf = new RangeFilterBuilder("startTime");
            rf.gt(cmd.getStartTime());
            fb = FilterBuilders.andFilter(fb, rf);
        }

        if(cmd.getEndTime() != null) {
            RangeFilterBuilder rf = new RangeFilterBuilder("endTime");
            rf.lt(cmd.getEndTime());
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
            LOGGER.info("quality sample task searcher query builder ："+builder);
            LOGGER.info("quality sample task searcher query rsp ："+rsp);
        }

        return rsp;
    }


    @Override
    public void syncFromDb() {
        int pageSize = 200;
        this.deleteAll();

        CrossShardListingLocator locator = new CrossShardListingLocator();
        for(;;) {
            List<QualityInspectionSamples> samples = qualityProvider.listQualityInspectionSamples(locator, pageSize);

            if(samples.size() > 0) {
                this.bulkUpdate(samples);
            }

            if(locator.getAnchor() == null) {
                break;
            }
        }

        this.optimize(1);
        this.refresh();

        LOGGER.info("sync for quality sample ok");
    }

    private XContentBuilder createDoc(QualityInspectionSamples sample){
        try {
            XContentBuilder b = XContentFactory.jsonBuilder().startObject();
            b.field("namespaceId", sample.getNamespaceId());
            b.field("ownerId", sample.getOwnerId());
            b.field("ownerType", sample.getOwnerType());
            b.field("name", sample.getName());
            b.field("startTime", sample.getStartTime());
            b.field("endTime", sample.getEndTime());
            b.field("status", sample.getStatus());


            b.endObject();
            return b;
        } catch (IOException ex) {
            LOGGER.error("Create quality sample " + sample.getId() + " error");
            return null;
        }
    }
}
