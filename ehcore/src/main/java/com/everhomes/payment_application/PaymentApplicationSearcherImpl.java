package com.everhomes.payment_application;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.openapi.Contract;
import com.everhomes.openapi.ContractProvider;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.payment_application.PaymentApplicationDTO;
import com.everhomes.rest.payment_application.SearchPaymentApplicationCommand;
import com.everhomes.rest.payment_application.SearchPaymentApplicationResponse;
import com.everhomes.search.AbstractElasticSearch;
import com.everhomes.search.PaymentApplicationSearcher;
import com.everhomes.search.SearchUtils;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.util.StringHelper;
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
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ying.xiong on 2017/12/27.
 */
@Component
public class PaymentApplicationSearcherImpl extends AbstractElasticSearch implements PaymentApplicationSearcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentApplicationSearcherImpl.class);

    @Autowired
    private PaymentApplicationProvider paymentApplicationProvider;

    @Autowired
    private OrganizationProvider organizationProvider;

    @Autowired
    private ContractProvider contractProvider;

    @Autowired
    private ConfigurationProvider configProvider;

    @Override
    public void deleteById(Long id) {
        deleteById(id.toString());
    }

    @Override
    public void bulkUpdate(List<PaymentApplication> applications) {
        BulkRequestBuilder brb = getClient().prepareBulk();
        for (PaymentApplication application : applications) {

            XContentBuilder source = createDoc(application);
            if(null != source) {
                LOGGER.info("application id:" + application.getId());
                brb.add(Requests.indexRequest(getIndexName()).type(getIndexType())
                        .id(application.getId().toString()).source(source));
            }

        }
        if (brb.numberOfActions() > 0) {
            brb.execute().actionGet();
        }
    }

    @Override
    public void feedDoc(PaymentApplication application) {
        XContentBuilder source = createDoc(application);

        feedDoc(application.getId().toString(), source);
    }

    @Override
    public void syncFromDb() {
        int pageSize = 200;
        this.deleteAll();

        CrossShardListingLocator locator = new CrossShardListingLocator();
        for(;;) {
            List<PaymentApplication> applications = paymentApplicationProvider.listPaymentApplications(locator, pageSize);

            if(applications.size() > 0) {
                this.bulkUpdate(applications);
            }

            if(locator.getAnchor() == null) {
                break;
            }
        }

        this.optimize(1);
        this.refresh();

        LOGGER.info("sync for payment applications ok");
    }

    @Override
    public SearchPaymentApplicationResponse query(SearchPaymentApplicationCommand cmd) {

        SearchRequestBuilder builder = getClient().prepareSearch(getIndexName()).setTypes(getIndexType());
        QueryBuilder qb = null;
        if(cmd.getKeyword() == null || cmd.getKeyword().isEmpty()) {
            qb = QueryBuilders.matchAllQuery();
        } else {
            qb = QueryBuilders.multiMatchQuery(cmd.getKeyword())
                    .field("title", 5.0f)
                    .field("contractName", 5.0f)
                    .field("title.pinyin_prefix", 2.0f)
                    .field("contractName.pinyin_prefix", 2.0f)
                    .field("title.pinyin_gram", 1.0f)
                    .field("contractName.pinyin_gram", 1.0f);
            builder.setHighlighterFragmentSize(60);
            builder.setHighlighterNumOfFragments(8);
            builder.addHighlightedField("title").addHighlightedField("contractName");

        }

        FilterBuilder fb = FilterBuilders.termFilter("namespaceId", cmd.getNamespaceId());
        fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("communityId", cmd.getCommunityId()));

        if(cmd.getStatus() != null) {
            fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("status", cmd.getStatus()));
        }

        if(cmd.getApplicationNumber() != null) {
            fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("applicationNumber", cmd.getApplicationNumber()));
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
            LOGGER.debug("query payment application :{}", builder);
        }

        List<Long> ids = getIds(rsp);

        SearchPaymentApplicationResponse response = new SearchPaymentApplicationResponse();
        if(ids.size() > pageSize) {
            response.setNextPageAnchor(anchor + 1);
            ids.remove(ids.size() - 1);
        } else {
            response.setNextPageAnchor(null);
        }

        response.setApplicationDTOs(getDTOs(rsp));

        return response;
    }

    @Override
    public String getIndexType() {
        return SearchUtils.PAYMENT_APPLICATION;
    }

    private XContentBuilder createDoc(PaymentApplication application){
        LOGGER.error("Create payment application " + StringHelper.toJsonString(application));
        try {
            XContentBuilder b = XContentFactory.jsonBuilder().startObject();
            b.field("id", application.getId());
            b.field("namespaceId", application.getNamespaceId());
            b.field("communityId", application.getCommunityId());
            if(application.getTitle() != null) {
                b.field("title", application.getTitle());
            } else {
                b.field("title", "");
            }

            if(application.getApplicationNumber() != null) {
                b.field("applicationNumber", application.getApplicationNumber());
            } else {
                b.field("applicationNumber", "");
            }

            if(application.getPaymentAmount() != null) {
                b.field("paymentAmount", application.getPaymentAmount());
            } else {
                b.field("paymentAmount", "");
            }
            if(application.getCreateTime() != null) {
                b.field("createTime", application.getCreateTime().getTime());
            } else {
                b.field("createTime", "");
            }

            b.field("status", application.getStatus());

            OrganizationMember member = organizationProvider.findOrganizationMemberByUIdAndOrgId(application.getApplicantUid(), application.getApplicantOrgId());
            if(member != null) {
                b.field("applicantName", member.getContactName());
            } else {
                b.field("applicantName", "");
            }

            Contract contract = contractProvider.findContractById(application.getContractId());
            if(contract != null) {
                b.field("contractName", contract.getName());
            } else {
                b.field("contractName", "");
            }


            b.endObject();
            return b;
        } catch (IOException ex) {
            LOGGER.error("Create payment application " + application.getId() + " error");
            return null;
        }
    }

    private List<PaymentApplicationDTO> getDTOs(SearchResponse rsp) {
        List<PaymentApplicationDTO> dtos = new ArrayList<>();
        SearchHit[] docs = rsp.getHits().getHits();
        for (SearchHit sd : docs) {
            try {
                PaymentApplicationDTO dto = new PaymentApplicationDTO();
                dto.setId(Long.parseLong(sd.getId()));
                Map<String, Object> source = sd.getSource();

                dto.setContractName(String.valueOf(source.get("contractName")));
                dto.setTitle(String.valueOf(source.get("title")));
                dto.setApplicationNumber(String.valueOf(source.get("applicationNumber")));
                dto.setApplicantName(String.valueOf(source.get("applicantName")));
                dto.setNamespaceId(SearchUtils.getLongField(source.get("namespaceId")).intValue());
                dto.setStatus(SearchUtils.getLongField(source.get("status")).byteValue());
                Object createTime = source.get("createTime");
                dto.setCreateTime(createTime != null ? new Timestamp(Long.valueOf(createTime.toString())) : null);
                Object paymentAmount = source.get("paymentAmount");
                if (paymentAmount != null) {
                    dto.setPaymentAmount(BigDecimal.valueOf((Double) paymentAmount));
                }


                dtos.add(dto);

            }
            catch(Exception ex) {
                LOGGER.info("getTopicIds error " + ex.getMessage());
            }
        }

        return dtos;
    }
}
