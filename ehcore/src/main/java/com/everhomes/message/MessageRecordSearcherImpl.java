package com.everhomes.message;

import com.everhomes.namespace.Namespace;
import com.everhomes.namespace.NamespaceProvider;
import com.everhomes.rest.message.MessageRecordDto;
import com.everhomes.rest.message.MessageRecordSenderTag;
import com.everhomes.rest.messaging.SearchMessageRecordCommand;
import com.everhomes.search.AbstractElasticSearch;
import com.everhomes.search.MessageRecordSearcher;
import com.everhomes.search.SearchUtils;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;
import org.elasticsearch.search.aggregations.metrics.tophits.InternalTopHits;
import org.elasticsearch.search.aggregations.metrics.tophits.TopHitsBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by lei.lv on 2018/1/8
 * 为消息日志提供搜索引擎支持
 */
@Service
public class MessageRecordSearcherImpl extends AbstractElasticSearch implements MessageRecordSearcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageRecordSearcherImpl.class);

    @Autowired
    private MessageProvider messageProvider;

    @Autowired
    private NamespaceProvider namespaceProvider;

    @Override
    public void deleteById(Long id) {
        deleteById(id.toString());
    }

    @Override
    public void bulkUpdate(List<MessageRecord> messageRecords) {
        BulkRequestBuilder brb = getClient().prepareBulk();

        for (MessageRecord record : messageRecords) {
            XContentBuilder source = createDoc(record);
            if (null != source) {
                brb.add(Requests.indexRequest(getIndexName()).type(getIndexType()).id(record.getId().toString()).source(source));
            }
        }
        if (brb.numberOfActions() > 0) {
            brb.execute().actionGet();
        }
    }

    @Override
    public void feedDoc(MessageRecord messageRecord) {
        XContentBuilder source = createDoc(messageRecord);
        feedDoc(messageRecord.getId().toString(), source);
    }

    @Override
    public void syncMessageRecordsByNamespace(Integer namespaceId) {
        List<MessageRecord> records = this.messageProvider.listMessageRecords(namespaceId);
        if (records != null && records.size() > 0) {
            this.bulkUpdate(records);
            LOGGER.info("syncMessageRecordsByNamespace processStat count: " + records.size());
        }
    }

    @Override
    public void syncMessageRecords() {
        List<MessageRecord> records = this.messageProvider.listMessageRecords();
        if (records != null && records.size() > 0) {
            this.bulkUpdate(records);
            LOGGER.info("syncMessageRecordsByNamespace processStat count: " + records.size());
        }
    }

    @Override
    public void syncMessageRecordIndexs() {
        this.deleteAll();
//        List<Namespace> namespaces = this.namespaceProvider.listNamespaces();
//        namespaces.forEach(r -> {
//            this.syncMessageRecordsByNamespace(r.getId());
//        });

        syncMessageRecords();

        this.refresh();
    }

    @Override
    public List queryMessage(SearchMessageRecordCommand cmd) {
        SearchRequestBuilder builder = getClient().prepareSearch(getIndexName()).setTypes(getIndexType());
        BoolQueryBuilder bqb = new BoolQueryBuilder();
        if (cmd.getIndexId() != null)
            bqb = bqb.must(QueryBuilders.matchQuery("indexId", cmd.getIndexId()));
        if (cmd.getKeywords() != null)
            bqb = bqb.must(QueryBuilders.matchQuery("body", cmd.getKeywords()));
        if (cmd.getNamespaceId() != null )
            bqb = bqb.must(QueryBuilders.termQuery("namespaceId", cmd.getNamespaceId()));
        if (StringUtils.isNotEmpty(cmd.getBodyType()))
            bqb = bqb.must(QueryBuilders.termQuery("bodyType", cmd.getBodyType()));
        if (cmd.getSenderUid() != null)
            bqb = bqb.must(QueryBuilders.termQuery("senderUid", cmd.getSenderUid()));
        if (StringUtils.isNotEmpty(cmd.getDstChannelToken()))
            bqb = bqb.must(QueryBuilders.termQuery("dstChannelToken", cmd.getDstChannelToken()));
        if (StringUtils.isNotEmpty(cmd.getSenderTag()))
            bqb = bqb.must(QueryBuilders.termQuery("senderTag", cmd.getSenderTag()));
        if (cmd.getStartTime() != null && cmd.getEndTime() != null)
            bqb = bqb.must(QueryBuilders.rangeQuery("createTime").to(new Date(cmd.getEndTime())).from(new Date(cmd.getStartTime())));
        if (cmd.getIsGroupBy() == 1) {
            TermsBuilder indexAgg = AggregationBuilders.terms("indexAgg").field("indexId");
            TopHitsBuilder infoAgg = AggregationBuilders.topHits("infoAgg").setFetchSource(new String[]{"senderUid","bodyType","body"}, null).setSize(1);
            builder.addAggregation(indexAgg.subAggregation(infoAgg));
        }

        if(cmd.getPageAnchor() == null){
            cmd.setPageAnchor(1000000L);
        }

        SortBuilder sortBuilder = SortBuilders.fieldSort("id").order(SortOrder.DESC);
        builder.addSort(sortBuilder);

        builder.setFrom(cmd.getPageAnchor().intValue() * cmd.getPageSize()).setSize(cmd.getPageSize() + 1).setSize(cmd.getPageSize()+1);
        builder.setQuery(bqb);
        SearchResponse rsp = builder.execute().actionGet();

        List<MessageRecordDto> list = new ArrayList<>();
        Aggregations aggregations = rsp.getAggregations();
        if(aggregations != null){
            Map<String, Aggregation> sendAggMap = aggregations.asMap();
            LongTerms indexAggTerms = (LongTerms) sendAggMap.get("indexAgg");
            Iterator<Terms.Bucket> it = indexAggTerms.getBuckets().iterator();
            while(it.hasNext()){
                MessageRecordDto record = new MessageRecordDto();
                Terms.Bucket indexAggBucket = it.next();
                record.setIndexId(Long.valueOf(indexAggBucket.getKey()));
                record.setNum(indexAggBucket.getDocCount());
                InternalTopHits infoAgg = (InternalTopHits) indexAggBucket.getAggregations().asMap().get("infoAgg");
                SearchHits hits = infoAgg.getHits();
                if(hits.getHits() != null && hits.getHits().length > 0){
                    SearchHit hit = hits.getHits()[0];
                    record.setSenderUid(hit.getSource().get("senderUid") != null ? Long.valueOf(hit.getSource().get("senderUid").toString()) : null);
                    record.setBodyType(hit.getSource().get("bodyType") != null ? hit.getSource().get("bodyType").toString(): null);
                    record.setBody(hit.getSource().get("body") != null ? hit.getSource().get("body").toString(): null);
                }
                list.add(record);
            }
        }

        if (list.size() > cmd.getPageSize()) {
            list.remove(list.size() - 1);
            cmd.setPageAnchor(list.get(list.size() - 1).getId());
        }else{
            cmd.setPageAnchor(null);
        }

        return list;
    }

    @Override
    public List queryMessageByIndex(SearchMessageRecordCommand cmd) {
        SearchRequestBuilder builder = getClient().prepareSearch(getIndexName()).setTypes(getIndexType());
        BoolQueryBuilder bqb = new BoolQueryBuilder();
        if (StringUtils.isNotEmpty(cmd.getDstChannelToken()))
            bqb = bqb.must(QueryBuilders.termQuery("dstChannelToken", cmd.getDstChannelToken()));
        if (null != cmd.getIndexId())
            bqb = bqb.must(QueryBuilders.termQuery("indexId", cmd.getIndexId()));
        if (cmd.getStartTime() != null && cmd.getEndTime() != null)
            bqb = bqb.must(QueryBuilders.rangeQuery("createTime").to(new Date(cmd.getEndTime())).from(new Date(cmd.getStartTime())));
        if (cmd.getType() != null){
            List<String> tagMatchs = new ArrayList<>();

            switch (cmd.getType()){
                case 0:
                    break;
                case 1:
                    tagMatchs.add(MessageRecordSenderTag.FORWARD_EVENT.getCode());
                    tagMatchs.add(MessageRecordSenderTag.APPIDSTATUS.getCode());
                    tagMatchs.add(MessageRecordSenderTag.REGISTER_LOGIN.getCode());
                    tagMatchs.add(MessageRecordSenderTag.FETCH_PASTTORECENT_MESSAGES.getCode());
                    tagMatchs.add(MessageRecordSenderTag.ROUTE_STORE_MESSAGE.getCode());
                    tagMatchs.add(MessageRecordSenderTag.ROUTE_REALTIME_MESSAGE.getCode());
                    tagMatchs.add(MessageRecordSenderTag.ROUTE_MESSAGE.getCode());
                    bqb = bqb.must(QueryBuilders.termsQuery("senderTag", tagMatchs));
                    break;
                case 2:
                    tagMatchs.add(MessageRecordSenderTag.NOTIFY_EVENT.getCode());
                    tagMatchs.add(MessageRecordSenderTag.NOTIFY_REQUEST.getCode());
                    tagMatchs.add(MessageRecordSenderTag.FETCH_NOTIFY_MESSAGES.getCode());
                    bqb = bqb.must(QueryBuilders.termsQuery("senderTag", tagMatchs));
                    break;
            }
        }

        if(cmd.getPageAnchor() == null){
            cmd.setPageAnchor(0L);
        }

        SortBuilder sortBuilder = SortBuilders.fieldSort("id").order(SortOrder.DESC);
        builder.addSort(sortBuilder);

        builder.setFrom(cmd.getPageAnchor().intValue() * cmd.getPageSize()).setSize(cmd.getPageSize() + 1).setSize(cmd.getPageSize()+1);

        builder.setQuery(bqb);
        SearchResponse rsp = builder.execute().actionGet();


        List<MessageRecordDto> list = new ArrayList<>();
        SearchHit[] docs = rsp.getHits().getHits();
        for (SearchHit sd : docs) {
            Map<String, Object> m = sd.getSource();
            MessageRecordDto record = new MessageRecordDto();
            record.setId(Long.valueOf(m.get("id").toString()));
            if(m.get("namespaceId") != null)
                record.setNamespaceId(Integer.valueOf(m.get("namespaceId").toString()));
            if(m.get("dstChannelToken") != null)
                record.setDstChannelToken(m.get("dstChannelToken").toString());
            if(m.get("dstChannelType") != null)
                record.setDstChannelType(m.get("dstChannelType").toString());
            if(m.get("status") != null)
                record.setStatus(m.get("status").toString());
            if(m.get("appId") != null)
                record.setAppId(Long.valueOf(m.get("appId").toString()));
            if(m.get("messageSeq") != null)
                record.setMessageSeq(Long.valueOf(m.get("messageSeq").toString()));
            if(m.get("senderUid") != null)
                record.setSenderUid(Long.valueOf(m.get("senderUid").toString()));
            if(m.get("senderTag") != null)
                record.setSenderTag(m.get("senderTag").toString());
            if(m.get("channelsInfo") != null)
                record.setChannelsInfo(m.get("channelsInfo").toString());
            if(m.get("bodyType") != null)
                record.setBodyType(m.get("bodyType").toString());
            if(m.get("body") != null)
                record.setBody(m.get("body").toString());
            if(m.get("deliveryOption") != null)
                record.setDeliveryOption(Integer.valueOf(m.get("deliveryOption").toString()));

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            TimeZone utcZone = TimeZone.getTimeZone("UTC");
            simpleDateFormat.setTimeZone(utcZone);
            Date myDate = null;
            try {
                myDate = simpleDateFormat.parse(m.get("createTime").toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            record.setCreateTime(Timestamp.valueOf(sdf.format(myDate)));

            if(m.get("deviceId") != null)
                record.setDeviceId(m.get("deviceId").toString());
            if(m.get("indexId") != null)
                record.setIndexId(Long.valueOf(m.get("indexId").toString()));

            list.add(record);
        }

        if (list.size() > cmd.getPageSize()) {
            list.remove(list.size() - 1);
            cmd.setPageAnchor(list.get(list.size() - 1).getId());
        }else{
            cmd.setPageAnchor(null);
        }

        return list;
    }

    @Override
    public String getIndexType() {
        return SearchUtils.MESSAGE_RECORD;
    }

    private XContentBuilder createDoc(MessageRecord messageRecord) {
        try {
            XContentBuilder b = XContentFactory.jsonBuilder().startObject();
            b.field("id", messageRecord.getId());
            b.field("namespaceId", messageRecord.getNamespaceId());
            b.field("dstChannelToken", messageRecord.getDstChannelToken());
            b.field("dstChannelType", messageRecord.getDstChannelType());
            b.field("status", messageRecord.getStatus());
            b.field("appId", messageRecord.getAppId());
            b.field("messageSeq", messageRecord.getMessageSeq());
            b.field("senderUid", messageRecord.getSenderUid());
            b.field("senderTag", messageRecord.getSenderTag());
            b.field("channelsInfo", messageRecord.getChannelsInfo());
            b.field("bodyType", messageRecord.getBodyType());
            b.field("body", messageRecord.getBody());
            b.field("deliveryOption", messageRecord.getDeliveryOption());
            b.field("createTime", messageRecord.getCreateTime());
            b.field("deviceId", messageRecord.getDeviceId());
            b.field("indexId", messageRecord.getIndexId());
            b.endObject();
            return b;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
