package com.everhomes.quality;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.rest.quality.ListSampleQualityInspectionResponse;
import com.everhomes.rest.quality.SearchSampleQualityInspectionCommand;
import com.everhomes.search.AbstractElasticSearch;
import com.everhomes.search.QualityInspectionSampleSearcher;
import com.everhomes.search.SearchUtils;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;

/**
 * Created by ying.xiong on 2017/6/9.
 */
public class QualityInspectionSampleSearcherImpl extends AbstractElasticSearch implements QualityInspectionSampleSearcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(QualityInspectionSampleSearcherImpl.class);

    @Autowired
    private QualityProvider qualityProvider;

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
        return null;
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
