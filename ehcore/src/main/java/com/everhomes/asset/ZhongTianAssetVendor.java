//@formatter:off
package com.everhomes.asset;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.everhomes.rest.asset.bill.ListBillsDTO;
import com.everhomes.rest.asset.bill.ListOpenBillsCommand;


/**
 * @author created by ycx
 * @date 下午7:18:10
 */
@Component(AssetVendorHandler.ASSET_VENDOR_PREFIX+"ZHONGTIAN")
public class ZhongTianAssetVendor extends AssetVendorHandler{

    private static final Logger LOGGER = LoggerFactory.getLogger(ZhongTianAssetVendor.class);
    
    public List<ListBillsDTO> listOpenBills(ListOpenBillsCommand cmd) {
    	
    	
    	return null;
    }
	
    
}
