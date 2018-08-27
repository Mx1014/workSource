package com.everhomes.asset;



import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.rest.asset.ListBillsCommand;
import com.everhomes.rest.asset.ListBillsDTO;
import com.everhomes.rest.asset.ListBillsResponse;
import com.everhomes.rest.contract.CMBill;
import com.everhomes.rest.contract.CMDataObject;
import com.everhomes.rest.contract.CMSyncObject;
import com.everhomes.util.RuntimeErrorException;

/**
 * @author created by ycx
 * @date 上午11:43:56
 */
@Component(AssetVendorHandler.ASSET_VENDOR_PREFIX + "RUIANCM")
public class RuiAnCMAssetVendorHandler extends AssetVendorHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(RuiAnCMAssetVendorHandler.class);

    @Autowired
    private AssetProvider assetProvider;
    
    /**
	 * 同步瑞安CM的账单数据到左邻的数据库表中
	 */
	public void syncRuiAnCMBillToZuolin(CMSyncObject cmSyncObject, Integer namespaceId){
		List<CMDataObject> data = cmSyncObject.getData();
		if(data != null) {
			for(CMDataObject cmDataObject : data) {
				List<CMBill> cmBills = cmDataObject.getBill();
				for(CMBill cmBill : cmBills) {
					PaymentBills paymentBills = new PaymentBills();
					paymentBills.setNamespaceId(namespaceId);
					paymentBills.setOwnerId(240111044332063578L);//TODO 后面鹏宇会一起传过来
					paymentBills.setBillGroupId(4L);//TODO 后面看是否给一个默认的账单组
					if(cmDataObject.getContractHeader() != null) {
						paymentBills.setTargetName(cmDataObject.getContractHeader().getAccountName());//客户名称
					}
					paymentBills.setContractId(cmBill.getRentalID() != null ? Long.parseLong(cmBill.getRentalID()) : null);
					paymentBills.setDateStrBegin(cmBill.getStartDate());
					paymentBills.setDateStrEnd(cmBill.getEndDate());
					if(cmBill.getStatus() != null && cmBill.getStatus().equals("已出账单")) {
						paymentBills.setSwitch((byte) 1);
					}else {
						paymentBills.setSwitch((byte) 01);
					}
					paymentBills.setAmountOwed(new BigDecimal(cmBill.getBalanceAmt()));
					//paymentBills.setAmountOwedWithoutTax(new BigDecimal(cmBill.getDocumentAmt()));
					paymentBills.setAmountReceivable(new BigDecimal(cmBill.getDocumentAmt()));
					paymentBills.setAmountReceived(new BigDecimal(cmBill.getDocumentAmt()));
					
					Long billId = assetProvider.createCMBill(paymentBills);//创建账单并返回账单ID
					
					PaymentBillItems items = new PaymentBillItems();
					items.setBillId(billId);
					items.setNamespaceId(namespaceId);
					items.setOwnerId(240111044332063578L);//TODO 后面鹏宇会一起传过来
					items.setBillGroupId(4L);//TODO 后面看是否给一个默认的账单组
					if(cmDataObject.getContractHeader() != null) {
						items.setTargetName(cmDataObject.getContractHeader().getAccountName());//客户名称
					}
					items.setChargingItemName(cmBill.getBillItemName());
					
					assetProvider.createCMBillItem(items);
					
				}
			}
		}
	}
	
	public List<ListBillsDTO> listBills(Integer currentNamespaceId,ListBillsResponse response, ListBillsCommand cmd) {
        //修改传递参数为一个对象，卸货
        Long pageAnchor = cmd.getPageAnchor();
        Integer pageSize = cmd.getPageSize();
        //卸货完毕
        if (pageAnchor == null || pageAnchor < 1l) {
            pageAnchor = 0l;
        }
        if(pageSize == null){
            pageSize = 20;
        }
        Integer pageOffSet = pageAnchor.intValue();
        List<ListBillsDTO> list = assetProvider.listBills(currentNamespaceId,pageOffSet,pageSize, cmd);
        if(list.size() <= pageSize){
            response.setNextPageAnchor(null);
        }else {
            response.setNextPageAnchor(pageAnchor+pageSize.longValue());
            list.remove(list.size()-1);
        }
        return list;
    }
    
}