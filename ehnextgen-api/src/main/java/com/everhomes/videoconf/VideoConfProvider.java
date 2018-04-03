package com.everhomes.videoconf;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.rest.videoconf.CountAccountOrdersAndMonths;
import com.everhomes.rest.videoconf.InvoiceDTO;
import com.everhomes.rest.videoconf.OrderBriefDTO;

public interface VideoConfProvider {

	void createConfAccountCategories(ConfAccountCategories rule);
	void updateConfAccountCategories(ConfAccountCategories rule);
	ConfAccountCategories findAccountCategoriesById(Long id);
	List<Long> findAccountCategoriesByConfType(Byte confType);
	
	void createWarningContacts(WarningContacts contactor);
	void updateWarningContacts(WarningContacts contactor);
	void deleteWarningContacts(long id);
	
	void createSourceVideoConfAccount(ConfSourceAccounts account);
	void deleteSourceVideoConfAccount(long id);
	void extendedSourceAccountPeriod(ConfSourceAccounts account);
	ConfSourceAccounts findSourceAccountById(long id);
	List<ConfSourceAccounts> listSourceAccount(String sourceAccount, List<Long> accountCategory, int pageOffset,int pageSize);
	
	List<ConfAccountCategories> listConfAccountCategories(Byte confType, Byte isOnline, int pageOffset,int pageSize);
	List<WarningContacts> listWarningContacts(int pageOffset,int pageSize);
	
	void createInvoice(ConfInvoices invoice);
	void updateInvoice(ConfInvoices invoice);
	InvoiceDTO getInvoiceByOrderId(Long orderId);
	
	
	List<ConfEnterprises> listEnterpriseWithVideoConfAccount(Integer namespaceId, Byte status, CrossShardListingLocator locator, Integer pageSize);
//	List<Long> ListVideoconfEnterprise(Long communityId);
//	
//	void createVideoconfEnterprise(ConfEnterprises enterprise);
	void updateVideoconfEnterprise(ConfEnterprises enterprise);
//	
	ConfEnterprises findByEnterpriseId(Long enterpriseId);
	ConfEnterprises findConfEnterpriseById(Long id);
	void createConfEnterprises(ConfEnterprises enterprise);
	void updateConfEnterprises(ConfEnterprises enterprise);
//	
	ConfAccounts findAccountByAssignedSourceId(Long assignedSourceId);
	ConfAccounts findVideoconfAccountById(Long id);
	void createConfAccounts(ConfAccounts account);
	void updateConfAccounts(ConfAccounts account);
//
//	void createEnterpriseVideoconfAccount(ConfAccountHistories account);
//	void updateEnterpriseVideoconfAccount(ConfAccountHistories account);
//	ConfAccountHistories findEnterpriseAccountByAccountId(Long accountId);
	ConfAccounts findAccountByUserId(Long userId);
	List<ConfAccounts> findAccountsByUserId(Long userId);
	ConfAccounts findAccountByUserIdAndEnterpriseId(Long userId, Long enterpriseId);
	List<Long> findUsersByEnterpriseId(Long enterpriseId);
//	
	void createConfOrderAccountMap(ConfOrderAccountMap map);
	void updateConfOrderAccountMap(ConfOrderAccountMap map); 
	List<ConfOrderAccountMap> findOrderAccountByAccountId(Long accountId);
//	
//	Long countVideoconfAccountByConfType(Byte confType);
//	
//	Long countValidAccountByConfType(Byte confType);
	
	Long newAccountsByConfType(Long values, Timestamp startTime, Timestamp endTime);
	
	List<ConfOrders> findOrdersByEnterpriseId(Long enterpriseId, CrossShardListingLocator locator, Integer pageSize);
	
	void createConfConferences(ConfConferences conf);
	void updateConfConferences(ConfConferences conf);
	ConfConferences findConfConferencesById(Long id);
	ConfConferences findConfConferencesByConfId(Long confId);
	
	void createReserveVideoConf(ConfReservations reservation);
	void updateReserveVideoConf(ConfReservations reservation);
	ConfReservations findReservationConfById(long id);
	List<ConfReservations> findReservationConfByAccountId(Long accountId, CrossShardListingLocator locator, Integer pageSize);
	
	ConfSourceAccounts findSpareAccount(List<Long> accountCategory);
	
	void createConfAccountHistories(ConfAccountHistories history);
	
	Set<Long> listOrdersWithUnassignAccount(Long enterpriseId);
	
	int countOrderAccounts(Long orderId, Byte assignFlag);
	List<Long> listUnassignAccountIds(Long orderId);
	
	List<ConfAccounts> listConfAccountsByEnterpriseId(Long enterpriseId, Byte status, CrossShardListingLocator locator, Integer pageSize);
	
	List<OrderBriefDTO> findOrdersByAccountId(Long accountId, CrossShardListingLocator locator, Integer pageSize);
	
	ConfOrders findOredrById(Long id);
	void updateConfOrders(ConfOrders order);
	void createConfOrders(ConfOrders order);
	CountAccountOrdersAndMonths  countAccountOrderInfo(Long accountId);
	
	int countConfByAccount(Long accountId);
	int countOrdersByAccountId(Long accountId);
	
	int countActiveAccounts(List<Long> accountCategoryIds);
	int countOccupiedAccounts(List<Long> accountCategoryIds);
	int countActiveSourceAccounts(List<Long> accountCategoryIds);
	
	void updateInvaildAccount();
	void updateEnterpriseAccounts();
	int countAccountsByEnterprise(Long enterpriseId, Byte accountType);
	int countEnterpriseAccounts(Long enterpriseId);
	boolean allTrialEnterpriseAccounts(Long enterpriseId);
	List<ConfAccounts> listOccupiedConfAccounts(Timestamp assignedTime);
	Long listConfTimeByAccount(Long accountId);
	List<ConfConferences> listConfbyAccount(Long accountId, CrossShardListingLocator locator, Integer pageSize);  
	List<ConfOrders> findConfOrdersByCategoriesAndDate(List<Long> categories, Calendar calendar);
	List<Long> findAccountCategoriesByNotInConfType(Byte confType);
	List<ConfOrderAccountMap> findOrderAccountByOrderId(Long orderId,
			CrossShardListingLocator locator, Integer pageSize, Byte assigedFlag);
	ConfAccounts findAccountByUserIdAndEnterpriseIdAndStatus(Long userId, Long enterpriseId,
			Byte status);
	
}
