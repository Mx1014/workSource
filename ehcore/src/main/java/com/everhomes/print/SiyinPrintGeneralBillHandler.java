package com.everhomes.print;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.asset.GeneralBillHandler;
import com.everhomes.rest.asset.AssetSourceType;
import com.everhomes.rest.asset.ListBillDetailResponse;
import com.everhomes.rest.print.PrintOrderStatusType;

//@Component(AssetGeneralBillHandler.ASSET_GENERALBILL_PREFIX + AssetSourceType.PRINT_MODULE.getSourceType())
//public class SiyinPrintGeneralBillHandler implements AssetGeneralBillHandler{
//	private static final Logger LOGGER = LoggerFactory.getLogger(SiyinPrintGeneralBillHandler.class);
//	@Autowired
//	private SiyinPrintOrderProvider siyinPrintOrderProvider;
//	@Autowired
//	SiyinPrintService siyinPrintService;
//
//	@Override
//	public void payNotifyBillSourceModule(ListBillDetailResponse billDetail) {
//		SiyinPrintOrder order = siyinPrintOrderProvider.findSiyinPrintOrderByGeneralBillId(billDetail.getBillId()+"");
//		if (null == order || !(order.getId()+"").equals(billDetail.getThirdBillId())) {
//			LOGGER.error("payNotifyBillSourceModule faild, param:"+billDetail.toString());
//			return;
//		}
//		
//		siyinPrintService.updatePrintOrder(order, null);
//	}

// }

public class SiyinPrintGeneralBillHandler{
	
}
