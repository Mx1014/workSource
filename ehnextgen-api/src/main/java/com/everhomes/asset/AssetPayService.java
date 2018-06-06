package com.everhomes.asset;

import java.util.List;

import com.everhomes.rest.order.ListBizPayeeAccountDTO;
import com.everhomes.rest.order.PreOrderCommand;
import com.everhomes.rest.order.PreOrderDTO;

public interface AssetPayService {
	
	List<ListBizPayeeAccountDTO> listBizPayeeAccounts(Long orgnizationId, String... tags);
	
    public PreOrderDTO createPreOrder(PreOrderCommand cmd);

}
