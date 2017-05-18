package com.everhomes.warehouse;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.rest.warehouse.SearchWarehouseMaterialsCommand;
import com.everhomes.rest.warehouse.SearchWarehouseMaterialsResponse;
import com.everhomes.search.AbstractElasticSearch;
import com.everhomes.search.SearchUtils;
import com.everhomes.search.WarehouseRequestMaterialSearcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by ying.xiong on 2017/5/18.
 */
@Component
public class WarehouseRequestMaterialSearcherImpl extends AbstractElasticSearch implements WarehouseRequestMaterialSearcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(WarehouseSearcherImpl.class);

    @Autowired
    private WarehouseProvider warehouseProvider;

    @Autowired
    private ConfigurationProvider configProvider;

    @Override
    public String getIndexType() {
        return SearchUtils.WAREHOUSE_REQUEST_MATERIAL;
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public void bulkUpdate(List<WarehouseRequestMaterials> materials) {

    }

    @Override
    public void feedDoc(WarehouseRequestMaterials material) {

    }

    @Override
    public void syncFromDb() {

    }

    @Override
    public SearchWarehouseMaterialsResponse query(SearchWarehouseMaterialsCommand cmd) {
        return null;
    }
}
