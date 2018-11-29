package com.everhomes.search;

import org.elasticsearch.action.WriteConsistencyLevel;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequest;
import org.elasticsearch.action.admin.cluster.node.info.NodesInfoRequest;
import org.elasticsearch.action.admin.cluster.node.info.NodesInfoResponse;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeRequest;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeResponse;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeResponse.AnalyzeToken;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsRequestBuilder;
import org.elasticsearch.action.admin.indices.optimize.OptimizeRequest;
import org.elasticsearch.action.admin.indices.optimize.OptimizeResponse;
import org.elasticsearch.action.admin.indices.refresh.RefreshRequest;
import org.elasticsearch.action.admin.indices.refresh.RefreshResponse;
import org.elasticsearch.action.count.CountResponse;
import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.everhomes.asset.bill.AssetBillService;
import com.everhomes.rest.asset.AssetPaymentBillStatus;
import com.everhomes.rest.asset.bill.CheckContractIsProduceBillCmd;
import com.everhomes.rest.asset.bill.CheckContractIsProduceBillDTO;
import com.everhomes.rest.asset.bill.ListCheckContractIsProduceBillResponse;
import com.everhomes.rest.contract.ContractOperateStatus;
import com.everhomes.rest.contract.SearchContractCommand;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractElasticSearch {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractElasticSearch.class);
    
    @Value("${elastic.index}")
    String elasticIndex;
    
    @Autowired
    TransportClientFactory clientFactory;
    
    @Autowired
	private AssetBillService assetBillService;
    
    public String getIndexName() {
        return elasticIndex;
    }
    
    public abstract String getIndexType();
    
    protected Client getClient() {
        try {
            return clientFactory.getClient();    
        } catch(Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
        
    }
    
    public String nodeInfo() {
        NodesInfoResponse rsp = getClient().admin().cluster()
                .nodesInfo(new NodesInfoRequest()).actionGet();
        String str = "Cluster:" + rsp.getClusterName() + ". Active nodes:";
        str += rsp.getNodesMap().keySet();
        return str;
    }
    
    public boolean indexExists() {
        return indexExists(getIndexName());
    }

    public boolean indexExists(String indexName) {
        return getClient().admin().indices()
                .exists(new IndicesExistsRequest(indexName)).actionGet()
                .isExists();
    }
    
    public String getMapping() throws IOException {
        return getMapping(getIndexName(), getIndexType());
    }

    public String getMapping(String indexName, String typeName)
            throws IOException {
        GetMappingsRequestBuilder getmapping = getClient().admin().indices()
                .prepareGetMappings();
        MappingMetaData response = getmapping.get().getMappings()
                .get(indexName).get(typeName);
        String mapping = new String(response.source().uncompressed());

        return mapping;
    }

    public void refresh() {
        refresh(getIndexName());
    }

    public void refresh(String... indices) {
        RefreshResponse rsp = getClient().admin().indices()
                .refresh(new RefreshRequest(indices)).actionGet();
    }

    public long countAll() {
        CountResponse response = getClient().prepareCount(getIndexName())
                .setQuery(QueryBuilders.matchAllQuery()).execute().actionGet();
        return response.getCount();
    }

    public void feedDoc(String id, XContentBuilder b) {
        IndexRequestBuilder irb = getClient()
                .prepareIndex(getIndexName(), getIndexType(), id)
                .setConsistencyLevel(WriteConsistencyLevel.DEFAULT)
                .setSource(b).setRefresh(true);//add setRefresh by Rui.Jia
        irb.execute().actionGet();
    }

    public String delete(String indexName, String type, String id) {
        DeleteRequestBuilder deleteRequestBuilder = getClient().prepareDelete(indexName, type, id);
        //add refresh action by rui.jia
        return deleteRequestBuilder.setRefresh(true).execute().actionGet()
                .getId();
    }

    public void deleteById(String id) {
        getClient()
                .prepareDelete(getIndexName(), getIndexType(), id).setRefresh(true).execute()
                .actionGet();
    }

    public void deleteAll() {
        // client.prepareIndex().setOpType(OpType.)
        // there is an index delete operation
        // http://www.elasticsearch.com/docs/elasticsearch/rest_api/admin/indices/delete_index/

        getClient().prepareDeleteByQuery(getIndexName())
                .setQuery(QueryBuilders.matchAllQuery())
                .setTypes(getIndexType())
                .execute().actionGet();
        refresh();
    }

    public OptimizeResponse optimize(int optimizeToSegmentsAfterUpdate) {
        return getClient()
                .admin()
                .indices()
                .optimize(
                        new OptimizeRequest(getIndexName())
                                .maxNumSegments(optimizeToSegmentsAfterUpdate))
                .actionGet();
    }

    void waitForYellow() {
        waitForYellow(getIndexName());
    }

    void waitForYellow(String name) {
        getClient().admin().cluster()
                .health(new ClusterHealthRequest(name).waitForYellowStatus())
                .actionGet();
    }

    void waitForGreen(String name) {
        getClient().admin().cluster()
                .health(new ClusterHealthRequest(name).waitForGreenStatus())
                .actionGet();
    }
    
    /**
     * 分词
     * @param analyzer
     * @param input
     * @return
     */
    public List<String> analyze(String analyzer, String input) {
        AnalyzeResponse response = getClient().admin().indices().analyze(new AnalyzeRequest(getIndexName(), input).analyzer(analyzer)).actionGet();
        
        List<String> tokens = new ArrayList<String>();
        for(AnalyzeToken tok: response.getTokens()) {
            tokens.add(tok.getTerm());
        }
        return tokens;
    }
    
    protected List<Long> getIds(SearchResponse rsp) {
        List<Long> results = new ArrayList<Long>();
        SearchHit[] docs = rsp.getHits().getHits();
        for (SearchHit sd : docs) {
            try {
                results.add(Long.parseLong(sd.getId()));
            }
            catch(Exception ex) {
                LOGGER.info("getTopicIds error " + ex.getMessage());
            }
        }
        
        return results;
    }
    
}
