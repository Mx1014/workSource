
package com.everhomes.asset.app;

import java.util.List;

import com.everhomes.rest.asset.BillIdCommand;
import com.everhomes.rest.asset.ClientIdentityCommand;
import com.everhomes.rest.asset.FunctionDisableListCommand;
import com.everhomes.rest.asset.FunctionDisableListDto;
import com.everhomes.rest.asset.ListAllBillsForClientCommand;
import com.everhomes.rest.asset.ListAllBillsForClientDTO;
import com.everhomes.rest.asset.ListBillDetailOnDateChangeCommand;
import com.everhomes.rest.asset.ShowBillDetailForClientResponse;
import com.everhomes.rest.asset.ShowBillForClientDTO;
import com.everhomes.rest.asset.ShowBillForClientV2Command;
import com.everhomes.rest.asset.ShowBillForClientV2DTO;

/**
 * @author created by ycx
 * @date 下午7:56:36
 */
public interface AssetAppService {
	
	ShowBillForClientDTO showBillForClient(ClientIdentityCommand cmd);
	
	FunctionDisableListDto functionDisableList(FunctionDisableListCommand cmd);
	
	List<ShowBillForClientV2DTO> showBillForClientV2(ShowBillForClientV2Command cmd);
	
	List<ListAllBillsForClientDTO> listAllBillsForClient(ListAllBillsForClientCommand cmd);
	
	ShowBillDetailForClientResponse getBillDetailForClient(BillIdCommand cmd);
	
	ShowBillDetailForClientResponse listBillDetailOnDateChange(ListBillDetailOnDateChangeCommand cmd);
	
}
