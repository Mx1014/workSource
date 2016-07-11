package com.everhomes.organization.pmsy;

import java.sql.Timestamp;
import java.util.List;

public interface PmsyProvider {
	List<PmsyPayer> listPmPayers(Long id,Integer namespaceId);
	
	PmsyPayer findPmPayersById(Long id);
	
	void createPmPayer(PmsyPayer pmsyPayer);
	
	void updatePmPayer(PmsyPayer pmsyPayer);
	
	void updatePmsyCommunity(PmsyCommunity pmsyCommunity);
	
	void createPmsyCommunity(PmsyCommunity pmsyCommunity);
	
	PmsyCommunity findPmsyCommunityById(Long communityId);
	
	void createPmsyOrderItem(PmsyOrderItem pmsyOrderItem);
	
	void createPmsyOrderItem(List<PmsyOrderItem> list);
	
	void createPmsyOrder(PmsyOrder pmsyOrder);
	
	List<PmsyOrder> searchBillingOrders(Integer pageSize ,Long communityId,Long pageAnchor,Timestamp startDate,Timestamp endDate,String userName,String userContact);
	
	PmsyOrder findPmsyOrderById(Long id);
	
	void updatePmsyOrder(PmsyOrder pmsyOrder);
	
	List<PmsyOrderItem> ListPmsyOrderItem(Long orderId);
	
	void updatePmsyOrderItem(PmsyOrderItem pmsyOrderItem);
	
	PmsyCommunity findPmsyCommunityByToken(String communityToken);
	
	PmsyPayer findPmPayersByNameAndContact(String userName, String userContact);
	
	List<PmsyOrderItem> ListBillOrderItems(Long...orderIds);
}
