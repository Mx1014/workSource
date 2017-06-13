package com.everhomes.videoconf;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;















import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.jooq.tools.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;















import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.namespace.Namespace;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.techpark.onlinePay.PayStatus;
import com.everhomes.rest.videoconf.ConfAccountStatus;
import com.everhomes.rest.videoconf.CountAccountOrdersAndMonths;
import com.everhomes.rest.videoconf.InvoiceDTO;
import com.everhomes.rest.videoconf.OrderBriefDTO;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhConfAccountCategoriesDao;
import com.everhomes.server.schema.tables.daos.EhConfAccountHistoriesDao;
import com.everhomes.server.schema.tables.daos.EhConfAccountsDao;
import com.everhomes.server.schema.tables.daos.EhConfConferencesDao;
import com.everhomes.server.schema.tables.daos.EhConfEnterprisesDao;
import com.everhomes.server.schema.tables.daos.EhConfInvoicesDao;
import com.everhomes.server.schema.tables.daos.EhConfOrderAccountMapDao;
import com.everhomes.server.schema.tables.daos.EhConfOrdersDao;
import com.everhomes.server.schema.tables.daos.EhConfReservationsDao;
import com.everhomes.server.schema.tables.daos.EhConfSourceAccountsDao;
import com.everhomes.server.schema.tables.daos.EhWarningContactsDao;
import com.everhomes.server.schema.tables.pojos.EhCommunities;
import com.everhomes.server.schema.tables.pojos.EhConfAccountCategories;
import com.everhomes.server.schema.tables.pojos.EhConfAccountHistories;
import com.everhomes.server.schema.tables.pojos.EhConfAccounts;
import com.everhomes.server.schema.tables.pojos.EhConfConferences;
import com.everhomes.server.schema.tables.pojos.EhConfEnterprises;
import com.everhomes.server.schema.tables.pojos.EhConfInvoices;
import com.everhomes.server.schema.tables.pojos.EhConfOrderAccountMap;
import com.everhomes.server.schema.tables.pojos.EhConfOrders;
import com.everhomes.server.schema.tables.pojos.EhConfReservations;
import com.everhomes.server.schema.tables.pojos.EhConfSourceAccounts;
import com.everhomes.server.schema.tables.pojos.EhWarningContacts;
import com.everhomes.server.schema.tables.records.EhConfAccountCategoriesRecord;
import com.everhomes.server.schema.tables.records.EhConfAccountsRecord;
import com.everhomes.server.schema.tables.records.EhConfConferencesRecord;
import com.everhomes.server.schema.tables.records.EhConfEnterprisesRecord;
import com.everhomes.server.schema.tables.records.EhConfOrderAccountMapRecord;
import com.everhomes.server.schema.tables.records.EhConfOrdersRecord;
import com.everhomes.server.schema.tables.records.EhConfReservationsRecord;
import com.everhomes.sharding.ShardIterator;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.IterationMapReduceCallback.AfterAction;

@Component
public class VideoConfProviderImpl implements VideoConfProvider {
	private static final Logger LOGGER = LoggerFactory.getLogger(VideoConfProviderImpl.class);

	@Autowired
    private DbProvider dbProvider;
    
    @Autowired
    private SequenceProvider sequenceProvider;
    
	@Override
	public void createConfAccountCategories(ConfAccountCategories rule) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());

        long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhConfAccountCategories.class));
        rule.setId(id);

        EhConfAccountCategoriesDao dao = new EhConfAccountCategoriesDao(context.configuration());
        dao.insert(rule);
	}

	@Override
	public void updateConfAccountCategories(ConfAccountCategories rule) {

		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        
		EhConfAccountCategoriesDao dao = new EhConfAccountCategoriesDao(context.configuration());
        dao.update(rule);
	}

	@Override
	public List<ConfAccountCategories> listConfAccountCategories(Byte confType, Byte isOnline, int pageOffset,int pageSize) {
		
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhConfAccountCategories.class));
		List<ConfAccountCategories> rules = new ArrayList<ConfAccountCategories>();
		SelectQuery<EhConfAccountCategoriesRecord> query = context.selectQuery(Tables.EH_CONF_ACCOUNT_CATEGORIES);

		if(confType != null)
			query.addConditions(Tables.EH_CONF_ACCOUNT_CATEGORIES.CONF_TYPE.eq(confType));
		
		if(isOnline != null)
			query.addConditions(Tables.EH_CONF_ACCOUNT_CATEGORIES.DISPLAY_FLAG.eq(isOnline));
		
		query.addLimit(pageOffset, pageSize);

		query.fetch().map((r) ->{

			ConfAccountCategories rule = new ConfAccountCategories();
			rule.setId(r.getValue(Tables.EH_CONF_ACCOUNT_CATEGORIES.ID));
			rule.setMultipleAccountThreshold(r.getValue(Tables.EH_CONF_ACCOUNT_CATEGORIES.MULTIPLE_ACCOUNT_THRESHOLD));
			rule.setConfType(r.getValue(Tables.EH_CONF_ACCOUNT_CATEGORIES.CONF_TYPE));
			rule.setMinPeriod(r.getValue(Tables.EH_CONF_ACCOUNT_CATEGORIES.MIN_PERIOD));
			rule.setMultipleAccountPrice(r.getValue(Tables.EH_CONF_ACCOUNT_CATEGORIES.MULTIPLE_ACCOUNT_PRICE));
			rule.setSingleAccountPrice(r.getValue(Tables.EH_CONF_ACCOUNT_CATEGORIES.SINGLE_ACCOUNT_PRICE));
			
			rules.add(rule);
			return null;
        });
		
		return rules;
	}

	@Override
	public void createWarningContacts(WarningContacts contactor) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());

        long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhWarningContacts.class));
        contactor.setId(id);

        EhWarningContactsDao dao = new EhWarningContactsDao(context.configuration());
        dao.insert(contactor);
		
	}

	@Override
	public void updateWarningContacts(WarningContacts contactor) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        
		EhWarningContactsDao dao = new EhWarningContactsDao(context.configuration());
        dao.update(contactor);
		
	}

	@Override
	public void deleteWarningContacts(long id) {
		 DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhWarningContacts.class));
		 EhWarningContactsDao dao = new EhWarningContactsDao(context.configuration());
		 dao.deleteById(id);		
	}

	@Override
	public List<WarningContacts> listWarningContacts(int pageOffset,
			int pageSize) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhWarningContacts.class));
		List<WarningContacts> contacts = new ArrayList<WarningContacts>();
		
		context.select().from(Tables.EH_WARNING_CONTACTS).limit(pageSize).offset(pageOffset).fetch().map(r ->{

			WarningContacts contact = new WarningContacts();
			contact.setId(r.getValue(Tables.EH_WARNING_CONTACTS.ID));
			contact.setContactor(r.getValue(Tables.EH_WARNING_CONTACTS.CONTACTOR));
			contact.setMobile(r.getValue(Tables.EH_WARNING_CONTACTS.MOBILE));
			contact.setEmail(r.getValue(Tables.EH_WARNING_CONTACTS.EMAIL));
			
			contacts.add(contact);
			return null;
        });
		
		return contacts;
	}

	@Override
	public void createSourceVideoConfAccount(ConfSourceAccounts account) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());

        long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhConfSourceAccounts.class));
        account.setId(id);
        
        if(account.getExpiredDate().before(new Timestamp(DateHelper.currentGMTTime().getTime()))) {
        	account.setStatus((byte) 0);
        }
        else {
        	account.setStatus((byte) 1);
        }

        EhConfSourceAccountsDao dao = new EhConfSourceAccountsDao(context.configuration());
        dao.insert(account);
		
	}

	@Override
	public void extendedSourceAccountPeriod(ConfSourceAccounts account) {
		
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        
		if(account.getExpiredDate().before(new Timestamp(DateHelper.currentGMTTime().getTime()))) {
			account.setStatus((byte) 0);
        }
        else {
        	account.setStatus((byte) 1);
        }
		
		EhConfSourceAccountsDao dao = new EhConfSourceAccountsDao(context.configuration());
		dao.update(account);
	}

	@Override
	public List<ConfSourceAccounts> listSourceAccount(String sourceAccount, List<Long> accountCategory,
			int pageOffset, int pageSize) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhConfSourceAccounts.class));
		Condition cond = Tables.EH_CONF_SOURCE_ACCOUNTS.ID.ge(1L);
		if(!StringUtils.isBlank(sourceAccount)){
			cond = cond.and(Tables.EH_CONF_SOURCE_ACCOUNTS.ACCOUNT_NAME.eq(sourceAccount));
		}
		
		if(accountCategory != null) {
			cond = cond.and(Tables.EH_CONF_SOURCE_ACCOUNTS.ACCOUNT_CATEGORY_ID.in(accountCategory));
		}
		List<ConfSourceAccounts> accounts = new ArrayList<ConfSourceAccounts>();
		
		context.select().from(Tables.EH_CONF_SOURCE_ACCOUNTS).where(cond).limit(pageSize).offset(pageOffset).fetch().map(r ->{
			ConfSourceAccounts account = new ConfSourceAccounts();
			account.setId(r.getValue(Tables.EH_CONF_SOURCE_ACCOUNTS.ID));
			account.setAccountCategoryId(r.getValue(Tables.EH_CONF_SOURCE_ACCOUNTS.ACCOUNT_CATEGORY_ID));
			account.setPassword(r.getValue(Tables.EH_CONF_SOURCE_ACCOUNTS.PASSWORD));
			account.setAccountName(r.getValue(Tables.EH_CONF_SOURCE_ACCOUNTS.ACCOUNT_NAME));
			account.setStatus(r.getValue(Tables.EH_CONF_SOURCE_ACCOUNTS.STATUS));
			account.setExpiredDate(r.getValue(Tables.EH_CONF_SOURCE_ACCOUNTS.EXPIRED_DATE));
			
			accounts.add(account);
			return null;
        });
		
		return accounts;
	}

	@Override
	public ConfSourceAccounts findSourceAccountById(long id) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhConfSourceAccounts.class, id));
		EhConfSourceAccountsDao dao = new EhConfSourceAccountsDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), ConfSourceAccounts.class);
	}

	@Override
	public void createInvoice(ConfInvoices invoice) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());

        long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhConfInvoices.class));
        invoice.setId(id);

        EhConfInvoicesDao dao = new EhConfInvoicesDao(context.configuration());
        dao.insert(invoice);		
	}

	@Override
	public List<ConfEnterprises> listEnterpriseWithVideoConfAccount(Integer namespaceId, Byte status, CrossShardListingLocator locator, Integer pageSize) {

		List<ConfEnterprises> enterprises=new ArrayList<ConfEnterprises>();
		if (locator.getShardIterator() == null) {
            AccessSpec accessSpec = AccessSpec.readOnlyWith(EhConfEnterprises.class);
            ShardIterator shardIterator = new ShardIterator(accessSpec);
            locator.setShardIterator(shardIterator);
        }
        this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (context, obj) -> {
            SelectQuery<EhConfEnterprisesRecord> query = context.selectQuery(Tables.EH_CONF_ENTERPRISES);
            
            if(locator.getAnchor() != null)
            	query.addConditions(Tables.EH_CONF_ENTERPRISES.ID.gt(locator.getAnchor()));
            
            if(namespaceId != null)
            	query.addConditions(Tables.EH_CONF_ENTERPRISES.NAMESPACE_ID.eq(namespaceId));
            
            //status: 状态 0-formally use 1-on trial 2-overdue
            if(status != null) {
            	if(status == 0)
            		query.addConditions(Tables.EH_CONF_ENTERPRISES.ACTIVE_ACCOUNT_AMOUNT.gt(0));
            	if(status == 1) {
            		query.addConditions(Tables.EH_CONF_ENTERPRISES.ACTIVE_ACCOUNT_AMOUNT.eq(0));
            		query.addConditions(Tables.EH_CONF_ENTERPRISES.TRIAL_ACCOUNT_AMOUNT.gt(0));
            	}
            	if(status == 2) {
            		query.addConditions(Tables.EH_CONF_ENTERPRISES.ACTIVE_ACCOUNT_AMOUNT.eq(0));
            		query.addConditions(Tables.EH_CONF_ENTERPRISES.TRIAL_ACCOUNT_AMOUNT.eq(0));
            	}
            		
            }
            
            query.addLimit(pageSize - enterprises.size());
            
            query.fetch().map((r) -> {
            	
            	enterprises.add(ConvertHelper.convert(r, ConfEnterprises.class));
                return null;
            });

            if (enterprises.size() >= pageSize) {
                locator.setAnchor(enterprises.get(enterprises.size() - 1).getId());
                return AfterAction.done;
            } else {
                locator.setAnchor(null);
            }
            return AfterAction.next;
        });

		return enterprises;
	}

//	@Override
//	public void createVideoconfEnterprise(ConfEnterprises enterprise) {
//		 DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhCommunities.class, enterprise.getCommunityId()));
//
//		long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhVideoconfEnterprise.class));
//        enterprise.setId(id);
//        enterprise.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
//        enterprise.setBehaviorTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
//        enterprise.setCreatorUid(UserContext.current().getUser().getId());
//        enterprise.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
//        enterprise.setStatus((byte) 1);
//
//        EhVideoconfEnterpriseDao dao = new EhVideoconfEnterpriseDao(context.configuration());
//        dao.insert(enterprise);
//		
//	}

	@Override
	public void updateVideoconfEnterprise(ConfEnterprises enterprise) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhConfEnterprises.class));
		enterprise.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		EhConfEnterprisesDao dao = new EhConfEnterprisesDao(context.configuration());
        dao.update(enterprise);
		
	}

	@Override
	public ConfEnterprises findByEnterpriseId(Long enterpriseId) {
		
		final ConfEnterprises[] result = new ConfEnterprises[1];
		dbProvider.mapReduce(AccessSpec.readOnlyWith(EhConfEnterprises.class), result, 
				(DSLContext context, Object reducingContext) -> {
					List<ConfEnterprises> list = context.select().from(Tables.EH_CONF_ENTERPRISES)
							.where(Tables.EH_CONF_ENTERPRISES.ENTERPRISE_ID.eq(enterpriseId))
							.fetch().map((r) -> {
								return ConvertHelper.convert(r, ConfEnterprises.class);
							});

					if(list != null && !list.isEmpty()){
						result[0] = list.get(0);
						return false;
					}

					return true;
				});

		return result[0];
	}

//	@Override
//	public List<Long> ListVideoconfEnterprise(Long communityId) {
//		final List<Long> enterpriseIds = new ArrayList<Long>();
//        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhCommunities.class, communityId));
// 
//        SelectQuery<EhVideoconfEnterpriseRecord> query = context.selectQuery(Tables.EH_VIDEOCONF_ENTERPRISE);
//       
//        query.addConditions(Tables.EH_VIDEOCONF_ENTERPRISE.COMMUNITY_ID.eq(communityId));
//        query.fetch().map((r) -> {
//        	enterpriseIds.add(r.getEnterpriseId());
//             return null;
//        });
//        
//       
//        return enterpriseIds;
//	}

	@Override
	public InvoiceDTO getInvoiceByOrderId(Long orderId) {
		
		final InvoiceDTO[] result = new InvoiceDTO[1];
		dbProvider.mapReduce(AccessSpec.readOnlyWith(EhConfInvoices.class), result, 
				(DSLContext context, Object reducingContext) -> {
					List<ConfInvoices> list = context.select().from(Tables.EH_CONF_INVOICES)
							.where(Tables.EH_CONF_INVOICES.ORDER_ID.eq(orderId))
							.fetch().map((r) -> {
								return ConvertHelper.convert(r, ConfInvoices.class);
							});

					if(list != null && !list.isEmpty()){
						ConfInvoices invoice = list.get(0);
						result[0] = ConvertHelper.convert(invoice, InvoiceDTO.class);
						return false;
					}

					return true;
				});

		return result[0];
	}

	@Override
	public ConfAccounts findVideoconfAccountById(Long id) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhConfAccounts.class, id));
		EhConfAccountsDao dao = new EhConfAccountsDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), ConfAccounts.class);
	}

	@Override
	public void updateConfAccounts(ConfAccounts account) {

		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        
		EhConfAccountsDao dao = new EhConfAccountsDao(context.configuration());
        dao.update(account);
	}

//	@Override
//	public void updateEnterpriseVideoconfAccount(
//			ConfAccountHistories account) {
//		
//		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
//        
//		EhEnterpriseVideoconfAccountDao dao = new EhEnterpriseVideoconfAccountDao(context.configuration());
//        dao.update(account);
//	}
//
//	@Override
//	public void createEnterpriseVideoconfAccount(ConfAccountHistories account) {
//		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
//
//        long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhEnterpriseVideoconfAccount.class));
//        account.setId(id);
//
//        EhEnterpriseVideoconfAccountDao dao = new EhEnterpriseVideoconfAccountDao(context.configuration());
//        dao.insert(account);
//	}

	@Override
	public List<ConfOrderAccountMap> findOrderAccountByOrderId(Long orderId,
			CrossShardListingLocator locator, Integer pageSize,Byte assigedFlag) {
		int namespaceId = (UserContext.current().getUser().getNamespaceId() == null) ? Namespace.DEFAULT_NAMESPACE : UserContext.current().getUser().getNamespaceId();
		List<ConfOrderAccountMap> accounts=new ArrayList<ConfOrderAccountMap>();
		if (locator.getShardIterator() == null) {
            AccessSpec accessSpec = AccessSpec.readOnlyWith(EhConfOrderAccountMap.class);
            ShardIterator shardIterator = new ShardIterator(accessSpec);
            locator.setShardIterator(shardIterator);
        }
        this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (context, obj) -> {
            SelectQuery<EhConfOrderAccountMapRecord> query = context.selectQuery(Tables.EH_CONF_ORDER_ACCOUNT_MAP);
            query.addConditions(Tables.EH_CONF_ORDER_ACCOUNT_MAP.CONF_ACCOUNT_NAMESPACE_ID.eq(namespaceId));
            
            if(locator.getAnchor() != null)
            	query.addConditions(Tables.EH_CONF_ORDER_ACCOUNT_MAP.ID.gt(locator.getAnchor()));
            
            if(orderId != null)
            	query.addConditions(Tables.EH_CONF_ORDER_ACCOUNT_MAP.ORDER_ID.eq(orderId));

            if(assigedFlag != null)
            	query.addConditions(Tables.EH_CONF_ORDER_ACCOUNT_MAP.ASSIGED_FLAG.eq(assigedFlag));
            query.addLimit(pageSize - accounts.size());
            
            query.fetch().map((r) -> {
            	
            	accounts.add(ConvertHelper.convert(r, ConfOrderAccountMap.class));
                return null;
            });

            if (accounts.size() >= pageSize) {
                locator.setAnchor(accounts.get(accounts.size() - 1).getId());
                return AfterAction.done;
            }
            return AfterAction.next;
        });

        if (accounts.size() > 0) {
            locator.setAnchor(accounts.get(accounts.size() - 1).getId());
        }

        return accounts;
	}

//	@Override
//	public ConfAccountHistories findEnterpriseAccountByAccountId(
//			Long accountId) {
//		final ConfAccountHistories[] result = new ConfAccountHistories[1];
//		dbProvider.mapReduce(AccessSpec.readOnlyWith(EhEnterpriseVideoconfAccount.class), result, 
//				(DSLContext context, Object reducingContext) -> {
//					List<ConfAccountHistories> list = context.select().from(Tables.EH_ENTERPRISE_VIDEOCONF_ACCOUNT)
//							.where(Tables.EH_ENTERPRISE_VIDEOCONF_ACCOUNT.ACCOUNT_ID.eq(accountId))
//							.fetch().map((r) -> {
//								return ConvertHelper.convert(r, ConfAccountHistories.class);
//							});
//
//					if(list != null && !list.isEmpty()){
//						result[0] = list.get(0);
//						return false;
//					}
//
//					return true;
//				});
//
//		return result[0];
//	}
//
	@Override
	public List<ConfAccounts> findAccountsByUserId(Long userId) {
//		final ConfAccounts[] result = new ConfAccounts[1];
		List<ConfAccounts> list = new ArrayList<ConfAccounts>();
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhConfAccounts.class));
		 
        SelectQuery<EhConfAccountsRecord> query = context.selectQuery(Tables.EH_CONF_ACCOUNTS);
        
        query.addConditions(Tables.EH_CONF_ACCOUNTS.OWNER_ID.eq(userId));
        query.addConditions(Tables.EH_CONF_ACCOUNTS.STATUS.ne((byte) 0));
        query.fetch().map((r) -> {
        	list.add(ConvertHelper.convert(r, ConfAccounts.class));
             return null;
        });
//		dbProvider.mapReduce(AccessSpec.readOnlyWith(EhConfAccounts.class), result, 
//				(DSLContext context, Object reducingContext) -> {
//					List<ConfAccounts> list = context.select().from(Tables.EH_CONF_ACCOUNTS)
//							.where(Tables.EH_CONF_ACCOUNTS.OWNER_ID.eq(userId))
//							.and(Tables.EH_CONF_ACCOUNTS.STATUS.ne((byte) 0))
//							.fetch().map((r) -> {
//								return ConvertHelper.convert(r, ConfAccounts.class);
//							});

//					if(list != null && !list.isEmpty()){
//						result[0] = list.get(0);
//						return false;
//					}

//					return true;
//				});

		return list;
	}
	
	@Override
	public ConfAccounts findAccountByUserId(Long userId) {
		final ConfAccounts[] result = new ConfAccounts[1];
		dbProvider.mapReduce(AccessSpec.readOnlyWith(EhConfAccounts.class), result, 
				(DSLContext context, Object reducingContext) -> {
					List<ConfAccounts> list = context.select().from(Tables.EH_CONF_ACCOUNTS)
							.where(Tables.EH_CONF_ACCOUNTS.OWNER_ID.eq(userId))
//							.and(Tables.EH_CONF_ACCOUNTS.STATUS.ne((byte) 0))
							.and(Tables.EH_CONF_ACCOUNTS.DELETE_UID.eq(0L))
							.orderBy(Tables.EH_CONF_ACCOUNTS.STATUS.desc())
							.fetch().map((r) -> {
								return ConvertHelper.convert(r, ConfAccounts.class);
							});

					if(list != null && !list.isEmpty()){
						result[0] = list.get(0);
						return false;
					}

					return true;
				});

		return result[0];
	}

	@Override
	public List<Long> findUsersByEnterpriseId(
			Long enterpriseId) {

		final List<Long> userIds = new ArrayList<Long>();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhConfAccounts.class));
 
        SelectQuery<EhConfAccountsRecord> query = context.selectQuery(Tables.EH_CONF_ACCOUNTS);
       
        if(enterpriseId != null)
        	query.addConditions(Tables.EH_CONF_ACCOUNTS.ENTERPRISE_ID.eq(enterpriseId));
        query.addConditions(Tables.EH_CONF_ACCOUNTS.STATUS.ne((byte) 0));
        query.fetch().map((r) -> {
        	userIds.add(r.getOwnerId());
             return null;
        });
        
       
        return userIds;
	}

//	@Override
//	public Long countVideoconfAccountByConfType(Byte confType) {
//		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhVideoconfAccount.class));
//		Long count = context.selectCount().from(Tables.EH_VIDEOCONF_ACCOUNT)
//                .where(Tables.EH_VIDEOCONF_ACCOUNT.CONF_TPYE.eq(confType))
//                .fetchOne(0, Long.class);
//		return count;
//	}
//
//	@Override
//	public Long countValidAccountByConfType(Byte confType) {
//		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhVideoconfAccount.class));
//		Long count = context.selectCount().from(Tables.EH_VIDEOCONF_ACCOUNT)
//                .where(Tables.EH_VIDEOCONF_ACCOUNT.CONF_TPYE.eq(confType))
//                .and(Tables.EH_VIDEOCONF_ACCOUNT.STATUS.ne((byte) 0))
//                .fetchOne(0, Long.class);
//		return count;
//	}

	@Override
	public Long newAccountsByConfType(Long values, Timestamp startTime,
			Timestamp endTime) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhConfAccounts.class));
		Long count = context.selectCount().from(Tables.EH_CONF_ACCOUNTS)
                .where(Tables.EH_CONF_ACCOUNTS.ACCOUNT_CATEGORY_ID.in(values))
                .and(Tables.EH_CONF_ACCOUNTS.CREATE_TIME.between(startTime, endTime))
                .fetchOne(0, Long.class);
		return count;
	}

	@Override
	public List<ConfOrders> findOrdersByEnterpriseId(Long enterpriseId,
			CrossShardListingLocator locator, Integer pageSize) {
		List<ConfOrders> orders = new ArrayList<ConfOrders>();
		
		if (locator.getShardIterator() == null) {
            AccessSpec accessSpec = AccessSpec.readOnlyWith(EhConfOrders.class);
            ShardIterator shardIterator = new ShardIterator(accessSpec);
            locator.setShardIterator(shardIterator);
        }
        this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (context, obj) -> {
            SelectQuery<EhConfOrdersRecord> query = context.selectQuery(Tables.EH_CONF_ORDERS);
            
            if (locator.getAnchor() == null){
            	locator.setAnchor(0L);
            	query.addConditions(Tables.EH_CONF_ORDERS.ID.gt(locator.getAnchor()));
            }
            if(locator.getAnchor() != 0L){
            	query.addConditions(Tables.EH_CONF_ORDERS.ID.lt(locator.getAnchor()));
            }
            
            if(enterpriseId != null)
            	query.addConditions(Tables.EH_CONF_ORDERS.OWNER_ID.eq(enterpriseId));
           
            query.addOrderBy(Tables.EH_CONF_ORDERS.CREATE_TIME.desc());
            query.addLimit(pageSize - orders.size());
            
            query.fetch().map((r) -> {
            	
            	orders.add(ConvertHelper.convert(r, ConfOrders.class));
                return null;
            });

            if (orders.size() >= pageSize) {
                locator.setAnchor(orders.get(orders.size() - 1).getId());
                return AfterAction.done;
            } else {
                locator.setAnchor(null);
            }
            return AfterAction.next;
        });

        return orders;
	}

	@Override
	public void updateConfConferences(ConfConferences conf) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        
		EhConfConferencesDao dao = new EhConfConferencesDao(context.configuration());
        dao.update(conf);
	}

	@Override
	public ConfSourceAccounts findSpareAccount(List<Long> accountCategory) {
		final ConfSourceAccounts[] result = new ConfSourceAccounts[1];
		Set<Long> sourceAccountId = findAssignedSourceAccount();
		dbProvider.mapReduce(AccessSpec.readOnlyWith(EhConfSourceAccounts.class), result, 
				(DSLContext context, Object reducingContext) -> {
					List<ConfSourceAccounts> list = context.select().from(Tables.EH_CONF_SOURCE_ACCOUNTS)
							.where(Tables.EH_CONF_SOURCE_ACCOUNTS.ACCOUNT_CATEGORY_ID.in(accountCategory))
							.and(Tables.EH_CONF_SOURCE_ACCOUNTS.ID.notIn(sourceAccountId))
							.and(Tables.EH_CONF_SOURCE_ACCOUNTS.STATUS.eq((byte) 1))
							.and(Tables.EH_CONF_SOURCE_ACCOUNTS.EXPIRED_DATE.ge(new Timestamp(System.currentTimeMillis())))
							.fetch().map((r) -> {
								return ConvertHelper.convert(r, ConfSourceAccounts.class);
							});

					if(list != null && !list.isEmpty()){
						result[0] = list.get(0);
						return false;
					}

					return true;
				});

		return result[0];
	}
	
	private Set<Long> findAssignedSourceAccount() {
		Set<Long> sourceAccountId = new HashSet<Long>();
		sourceAccountId.add(0L);
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhConfAccounts.class));
		SelectQuery<EhConfAccountsRecord> query = context.selectQuery(Tables.EH_CONF_ACCOUNTS);
		query.addConditions(Tables.EH_CONF_ACCOUNTS.ASSIGNED_SOURCE_ID.ne(0L));
        query.fetch().map((r) -> {
        	sourceAccountId.add(r.getAssignedSourceId());
            return null;
        });
        
        return sourceAccountId;
		
	}

	@Override
	public List<Long> findAccountCategoriesByConfType(Byte confType) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhConfAccountCategories.class));
		List<Long> accountCategories = new ArrayList<Long>();
		
		SelectQuery<EhConfAccountCategoriesRecord> query = context.selectQuery(Tables.EH_CONF_ACCOUNT_CATEGORIES);
		
		if(confType != null)
			query.addConditions(Tables.EH_CONF_ACCOUNT_CATEGORIES.CONF_TYPE.eq(confType));
		
		query.fetch().map((r) ->{
			accountCategories.add(r.getValue(Tables.EH_CONF_ACCOUNT_CATEGORIES.ID));
			return null;
        });
		
		return accountCategories;
	}

	@Override
	public ConfAccountCategories findAccountCategoriesById(Long id) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhConfAccountCategories.class, id));
		EhConfAccountCategoriesDao dao = new EhConfAccountCategoriesDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), ConfAccountCategories.class);
	}

	@Override
	public void createConfConferences(ConfConferences conf) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());

        long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhConfConferences.class));
        conf.setId(id);

        EhConfConferencesDao dao = new EhConfConferencesDao(context.configuration());
        dao.insert(conf);
		
	}

	@Override
	public void createReserveVideoConf(ConfReservations reservation) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());

        long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhConfReservations.class));
        reservation.setId(id);

        EhConfReservationsDao dao = new EhConfReservationsDao(context.configuration());
        dao.insert(reservation);
	}

	@Override
	public void updateReserveVideoConf(ConfReservations reservation) {

		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		
		reservation.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		EhConfReservationsDao dao = new EhConfReservationsDao(context.configuration());
        dao.update(reservation);
	}

	@Override
	public ConfReservations findReservationConfById(long id) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhConfReservations.class, id));
		EhConfReservationsDao dao = new EhConfReservationsDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), ConfReservations.class);
	}

	@Override
	public List<ConfReservations> findReservationConfByAccountId(Long accountId, CrossShardListingLocator locator, Integer pageSize) {

		int namespaceId = (UserContext.current().getUser().getNamespaceId() == null) ? Namespace.DEFAULT_NAMESPACE : UserContext.current().getUser().getNamespaceId();
		List<ConfReservations> reservations = new ArrayList<ConfReservations>();
		
		if (locator.getShardIterator() == null) {
            AccessSpec accessSpec = AccessSpec.readOnlyWith(EhConfReservations.class);
            ShardIterator shardIterator = new ShardIterator(accessSpec);
            locator.setShardIterator(shardIterator);
        }
        this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (context, obj) -> {
            SelectQuery<EhConfReservationsRecord> query = context.selectQuery(Tables.EH_CONF_RESERVATIONS);
            query.addConditions(Tables.EH_CONF_RESERVATIONS.STATUS.eq((byte) 1));
            query.addConditions(Tables.EH_CONF_RESERVATIONS.NAMESPACE_ID.eq(namespaceId));
            query.addConditions(Tables.EH_CONF_RESERVATIONS.START_TIME.gt(new Timestamp(DateHelper.currentGMTTime().getTime())));
            if(locator.getAnchor() != null)
            	query.addConditions(Tables.EH_CONF_RESERVATIONS.START_TIME.gt(new Timestamp(locator.getAnchor())));
            
            if(accountId != null)
            	query.addConditions(Tables.EH_CONF_RESERVATIONS.CONF_ACCOUNT_ID.eq(accountId));
           
            query.addOrderBy(Tables.EH_CONF_RESERVATIONS.START_TIME.asc());
            query.addLimit(pageSize - reservations.size());
            
            query.fetch().map((r) -> {
            	
            	reservations.add(ConvertHelper.convert(r, ConfReservations.class));
                return null;
            });

            if (reservations.size() >= pageSize) {
            	locator.setAnchor(reservations.get(reservations.size() - 1).getCreateTime().getTime());
 //               locator.setAnchor(reservations.get(reservations.size() - 1).getId());
                return AfterAction.done;
            }
            return AfterAction.next;
        });

        return reservations;
	}

	@Override
	public ConfAccounts findAccountByAssignedSourceId(Long assignedSourceId) {
		
		final ConfAccounts[] result = new ConfAccounts[1];
		dbProvider.mapReduce(AccessSpec.readOnlyWith(EhConfAccounts.class), result, 
				(DSLContext context, Object reducingContext) -> {
					List<ConfAccounts> list = context.select().from(Tables.EH_CONF_ACCOUNTS)
							.where(Tables.EH_CONF_ACCOUNTS.ASSIGNED_SOURCE_ID.eq(assignedSourceId))
							.fetch().map((r) -> {
								return ConvertHelper.convert(r, ConfAccounts.class);
							});

					if(list != null && !list.isEmpty()){
						result[0] = list.get(0);
						return false;
					}

					return true;
				});

		return result[0];
	}

	@Override
	public ConfConferences findConfConferencesById(Long id) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhConfConferences.class, id));
		EhConfConferencesDao dao = new EhConfConferencesDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), ConfConferences.class);
	}

	@Override
	public ConfConferences findConfConferencesByConfId(Long confId) {

		final ConfConferences[] result = new ConfConferences[1];
		dbProvider.mapReduce(AccessSpec.readOnlyWith(EhConfConferences.class), result, 
				(DSLContext context, Object reducingContext) -> {
					List<ConfConferences> list = context.select().from(Tables.EH_CONF_CONFERENCES)
							.where(Tables.EH_CONF_CONFERENCES.MEETING_NO.eq(confId))
							.fetch().map((r) -> {
								return ConvertHelper.convert(r, ConfConferences.class);
							});

					if(list != null && !list.isEmpty()){
						result[0] = list.get(0);
						return false;
					}

					return true;
				});

		return result[0];
	}

	@Override
	public void createConfAccountHistories(ConfAccountHistories history) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());

        long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhConfAccountHistories.class));
        history.setId(id);

        EhConfAccountHistoriesDao dao = new EhConfAccountHistoriesDao(context.configuration());
        dao.insert(history);
	}

	@Override
	public Set<Long> listOrdersWithUnassignAccount(Long enterpriseId) {

		final Set<Long> orders = new HashSet<Long>();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhConfOrderAccountMap.class));
 
        SelectQuery<EhConfOrderAccountMapRecord> query = context.selectQuery(Tables.EH_CONF_ORDER_ACCOUNT_MAP);
       
        query.addConditions(Tables.EH_CONF_ORDER_ACCOUNT_MAP.ENTERPRISE_ID.eq(enterpriseId));
        query.addConditions(Tables.EH_CONF_ORDER_ACCOUNT_MAP.ASSIGED_FLAG.eq((byte)0));
        query.fetch().map((r) -> {
        	orders.add(r.getOrderId());
             return null;
        });
        
       
        return orders;
	}

	@Override
	public int countOrderAccounts(Long orderId, Byte assignFlag) {
		final Integer[] count = new Integer[1];
		if(assignFlag == null) {
			this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhCommunities.class), null, 
                (DSLContext context, Object reducingContext)-> {
                    count[0] = context.selectCount().from(Tables.EH_CONF_ORDER_ACCOUNT_MAP)
                            .where(Tables.EH_CONF_ORDER_ACCOUNT_MAP.ORDER_ID.eq(orderId))
                    .fetchOneInto(Integer.class);
                    return true;
                });
		}
		else {
			this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhCommunities.class), null, 
	                (DSLContext context, Object reducingContext)-> {
	                    count[0] = context.selectCount().from(Tables.EH_CONF_ORDER_ACCOUNT_MAP)
	                            .where(Tables.EH_CONF_ORDER_ACCOUNT_MAP.ORDER_ID.eq(orderId))
	                            .and(Tables.EH_CONF_ORDER_ACCOUNT_MAP.ASSIGED_FLAG.eq(assignFlag))
	                    .fetchOneInto(Integer.class);
	                    return true;
	                });
		}
		
		return count[0];
	}

	@Override
	public List<Long> listUnassignAccountIds(Long orderId) {
		List<Long> accountIds = new ArrayList<Long>();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhConfOrderAccountMap.class));
 
        SelectQuery<EhConfOrderAccountMapRecord> query = context.selectQuery(Tables.EH_CONF_ORDER_ACCOUNT_MAP);
       
        query.addConditions(Tables.EH_CONF_ORDER_ACCOUNT_MAP.ORDER_ID.eq(orderId));
        query.addConditions(Tables.EH_CONF_ORDER_ACCOUNT_MAP.ASSIGED_FLAG.eq((byte)0));
        query.fetch().map((r) -> {
        	accountIds.add(r.getConfAccountId());
             return null;
        });
       
        return accountIds;
	}

	@Override
	public List<ConfAccounts> listConfAccountsByEnterpriseId(Long enterpriseId, Byte status,
			CrossShardListingLocator locator, Integer pageSize) {
		List<ConfAccounts> accounts = new ArrayList<ConfAccounts>();
		
		if (locator.getShardIterator() == null) {
            AccessSpec accessSpec = AccessSpec.readOnlyWith(EhConfReservations.class);
            ShardIterator shardIterator = new ShardIterator(accessSpec);
            locator.setShardIterator(shardIterator);
        }
        this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (context, obj) -> {
            SelectQuery<EhConfAccountsRecord> query = context.selectQuery(Tables.EH_CONF_ACCOUNTS);
 //           query.addConditions(Tables.EH_CONF_ACCOUNTS.OWNER_ID.ne(0L));
            if(locator.getAnchor() != null)
            	query.addConditions(Tables.EH_CONF_ACCOUNTS.ID.gt(locator.getAnchor()));
            
            if(enterpriseId != null)
            	query.addConditions(Tables.EH_CONF_ACCOUNTS.ENTERPRISE_ID.eq(enterpriseId));
            
            if(status != null)
            	query.addConditions(Tables.EH_CONF_ACCOUNTS.STATUS.eq(status));
            
            query.addConditions(Tables.EH_CONF_ACCOUNTS.DELETE_UID.eq(0L));
           
            query.addLimit(pageSize - accounts.size());
            
            query.fetch().map((r) -> {
            	
            	accounts.add(ConvertHelper.convert(r, ConfAccounts.class));
                return null;
            });

            if (accounts.size() >= pageSize) {
                locator.setAnchor(accounts.get(accounts.size() - 1).getId());
                return AfterAction.done;
            } else {
                locator.setAnchor(null);
            }
            return AfterAction.next;
        });

        return accounts;
	}

	@Override
	public List<OrderBriefDTO> findOrdersByAccountId(Long accountId,
			CrossShardListingLocator locator, Integer pageSize) {
		List<OrderBriefDTO> orders = new ArrayList<OrderBriefDTO>();
		
		if (locator.getShardIterator() == null) {
            AccessSpec accessSpec = AccessSpec.readOnlyWith(EhConfOrderAccountMap.class);
            ShardIterator shardIterator = new ShardIterator(accessSpec);
            locator.setShardIterator(shardIterator);
        }
        this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (context, obj) -> {
            SelectQuery<EhConfOrderAccountMapRecord> query = context.selectQuery(Tables.EH_CONF_ORDER_ACCOUNT_MAP);
            query.addConditions(Tables.EH_CONF_ORDER_ACCOUNT_MAP.CONF_ACCOUNT_ID.ne(accountId));
            if(locator.getAnchor() != null)
            	query.addConditions(Tables.EH_CONF_ORDER_ACCOUNT_MAP.ID.gt(locator.getAnchor()));
            
            query.addLimit(pageSize - orders.size());
            
            query.fetch().map((r) -> {
            	/////////////
            	ConfOrders order = findOredrById(r.getOrderId());
            	if(order != null) {
	            	OrderBriefDTO dto = new OrderBriefDTO();
	            	dto.setId(order.getId());
	            	dto.setCreateTime(order.getCreateTime());
	            	dto.setPeriod(order.getPeriod());
	            	ConfAccountCategories categority = findAccountCategoriesById(order.getAccountCategoryId());
	            	if(categority != null)
	            		dto.setConfType(categority.getConfType());
	            	
	            	orders.add(dto);
            	}
                return null;
            });

            if (orders.size() >= pageSize) {
                locator.setAnchor(orders.get(orders.size() - 1).getId());
                return AfterAction.done;
            }
            return AfterAction.next;
        });
       
        return orders;
	}

	@Override
	public ConfOrders findOredrById(Long id) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhConfOrders.class, id));
		EhConfOrdersDao dao = new EhConfOrdersDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), ConfOrders.class);
	}

	@Override
	public CountAccountOrdersAndMonths countAccountOrderInfo(Long accountId) {
		CountAccountOrdersAndMonths counts = new CountAccountOrdersAndMonths();
		final Integer[] count = new Integer[1];
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhConfOrderAccountMap.class), null, 
                (DSLContext context, Object reducingContext)-> {
                    count[0] = context.selectCount().from(Tables.EH_CONF_ORDER_ACCOUNT_MAP)
                            .where(Tables.EH_CONF_ORDER_ACCOUNT_MAP.CONF_ACCOUNT_ID.equal(accountId))
                            .fetchOneInto(Integer.class);
                    return true;
                });
        
        counts.setOrders(count[0]);
        
        final BigDecimal[] months = new BigDecimal[1];
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhConfOrders.class), null, 
                (DSLContext context, Object reducingContext)-> {
                	months[0] = context.select(Tables.EH_CONF_ORDERS.PERIOD.sum()).from(Tables.EH_CONF_ORDERS)
                            .leftOuterJoin(Tables.EH_CONF_ORDER_ACCOUNT_MAP)
                            .on(Tables.EH_CONF_ORDERS.ID.eq(Tables.EH_CONF_ORDER_ACCOUNT_MAP.ORDER_ID))
                            .where(Tables.EH_CONF_ORDER_ACCOUNT_MAP.CONF_ACCOUNT_ID.equal(accountId))
                                    .and(Tables.EH_CONF_ORDERS.STATUS.equal(PayStatus.PAID.getCode()))
                    .fetchOneInto(BigDecimal.class);
                    return true;
                });
        counts.setMonths(months[0]);
        return counts;
	}

	@Override
	public int countConfByAccount(Long accountId) {
//		int namespaceId = (UserContext.current().getUser().getNamespaceId() == null) ? Namespace.DEFAULT_NAMESPACE : UserContext.current().getUser().getNamespaceId();
		final Integer[] count = new Integer[1];
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhConfConferences.class), null, 
                (DSLContext context, Object reducingContext)-> {
                    count[0] = context.selectCount().from(Tables.EH_CONF_CONFERENCES)
                            .where(Tables.EH_CONF_CONFERENCES.CONF_ACCOUNT_ID.equal(accountId))
//                            .and(Tables.EH_CONF_CONFERENCES.NAMESPACE_ID.eq(namespaceId))
                            .fetchOneInto(Integer.class);
                    return true;
                });
        
        return count[0];
	}

	@Override
	public void updateConfOrders(ConfOrders order) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        
		EhConfOrdersDao dao = new EhConfOrdersDao(context.configuration());
        dao.update(order);
	}

	@Override
	public void createConfOrders(ConfOrders order) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());

        long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhConfOrders.class));
        order.setId(id);
        order.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        order.setCreatorUid(UserContext.current().getUser().getId());
        EhConfOrdersDao dao = new EhConfOrdersDao(context.configuration());
        dao.insert(order);
	}

	@Override
	public void createConfEnterprises(ConfEnterprises enterprise) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());

        long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhConfEnterprises.class));
        enterprise.setId(id);
        enterprise.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        enterprise.setUpdateTime(enterprise.getCreateTime());
        enterprise.setCreatorUid(UserContext.current().getUser().getId());
        EhConfEnterprisesDao dao = new EhConfEnterprisesDao(context.configuration());
        dao.insert(enterprise);
		
	}

	@Override
	public void updateConfEnterprises(ConfEnterprises enterprise) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		
		enterprise.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		EhConfEnterprisesDao dao = new EhConfEnterprisesDao(context.configuration());
        dao.update(enterprise);
	}

	@Override
	public void createConfAccounts(ConfAccounts account) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());

        long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhConfAccounts.class));
        account.setId(id);
        account.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        account.setUpdateTime(account.getCreateTime());
        account.setCreatorUid(UserContext.current().getUser().getId());
        EhConfAccountsDao dao = new EhConfAccountsDao(context.configuration());
        dao.insert(account);
		
	}

	@Override
	public void createConfOrderAccountMap(ConfOrderAccountMap map) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());

        long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhConfOrderAccountMap.class));
        map.setId(id);

        EhConfOrderAccountMapDao dao = new EhConfOrderAccountMapDao(context.configuration());
        dao.insert(map);
		
	}

	@Override
	public int countOrdersByAccountId(Long accountId) {
		final Integer[] count = new Integer[1];
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhConfOrderAccountMap.class), null, 
                (DSLContext context, Object reducingContext)-> {
                    count[0] = context.selectCount().from(Tables.EH_CONF_ORDER_ACCOUNT_MAP)
                            .where(Tables.EH_CONF_ORDER_ACCOUNT_MAP.CONF_ACCOUNT_ID.equal(accountId))
                            .fetchOneInto(Integer.class);
                    return true;
                });
        
        return count[0];
	}

	@Override
	public int countActiveAccounts(List<Long> accountCategoryIds) {
		final Integer[] count = new Integer[1];
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhConfAccounts.class), null, 
                (DSLContext context, Object reducingContext)-> {
                    count[0] = context.selectCount().from(Tables.EH_CONF_ACCOUNTS)
                            .where(Tables.EH_CONF_ACCOUNTS.ACCOUNT_CATEGORY_ID.in(accountCategoryIds))
                            .and(Tables.EH_CONF_ACCOUNTS.STATUS.ne((byte) 0))
                            .and(Tables.EH_CONF_ACCOUNTS.EXPIRED_DATE.ge(new Timestamp(DateHelper.currentGMTTime().getTime())))
                            .and(Tables.EH_CONF_ACCOUNTS.DELETE_UID.eq(0L))
                            .fetchOneInto(Integer.class);
                    return true;
                });
        
        return count[0];
	}

	@Override
	public int countOccupiedAccounts(List<Long> accountCategoryIds) {
		final Integer[] count = new Integer[1];
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhConfAccounts.class), null, 
                (DSLContext context, Object reducingContext)-> {
                    count[0] = context.selectCount().from(Tables.EH_CONF_ACCOUNTS)
                            .where(Tables.EH_CONF_ACCOUNTS.ACCOUNT_CATEGORY_ID.in(accountCategoryIds))
                            .and(Tables.EH_CONF_ACCOUNTS.ASSIGNED_FLAG.eq((byte) 1))
                            .and(Tables.EH_CONF_ACCOUNTS.STATUS.ne((byte) 0))
                            .and(Tables.EH_CONF_ACCOUNTS.EXPIRED_DATE.ge(new Timestamp(DateHelper.currentGMTTime().getTime())))
                            .and(Tables.EH_CONF_ACCOUNTS.DELETE_UID.eq(0L))
                            .fetchOneInto(Integer.class);
                    return true;
                });
        
        return count[0];
	}

	@Override
	public int countActiveSourceAccounts(List<Long> accountCategoryIds) {
		final Integer[] count = new Integer[1];
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhConfSourceAccounts.class), null, 
                (DSLContext context, Object reducingContext)-> {
                    count[0] = context.selectCount().from(Tables.EH_CONF_SOURCE_ACCOUNTS)
                    		.where(Tables.EH_CONF_SOURCE_ACCOUNTS.ACCOUNT_CATEGORY_ID.in(accountCategoryIds))
                            .and(Tables.EH_CONF_SOURCE_ACCOUNTS.STATUS.ne((byte) 0))
                            .and(Tables.EH_CONF_SOURCE_ACCOUNTS.EXPIRED_DATE.ge(new Timestamp(DateHelper.currentGMTTime().getTime())))
                            .fetchOneInto(Integer.class);
                    return true;
                });
        
        return count[0];
	}

	@Override
	public void updateConfOrderAccountMap(ConfOrderAccountMap map) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        
		EhConfOrderAccountMapDao dao = new EhConfOrderAccountMapDao(context.configuration());
        dao.update(map);
		
	}

	@Override
	public List<ConfOrderAccountMap> findOrderAccountByAccountId(Long accountId) {
		List<ConfOrderAccountMap> maps = new ArrayList<ConfOrderAccountMap>();
		
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhConfAccountCategories.class));
        SelectQuery<EhConfOrderAccountMapRecord> query = context.selectQuery(Tables.EH_CONF_ORDER_ACCOUNT_MAP);
        
        
        if(accountId != null)
        	query.addConditions(Tables.EH_CONF_ORDER_ACCOUNT_MAP.CONF_ACCOUNT_ID.eq(accountId));
        
        query.addConditions(Tables.EH_CONF_ORDER_ACCOUNT_MAP.ASSIGED_FLAG.eq((byte)0));
        
        query.fetch().map((r) -> {
        	
        	maps.add(ConvertHelper.convert(r, ConfOrderAccountMap.class));
            return null;
        });

        

        return maps;
	}

	@Override
	public ConfEnterprises findConfEnterpriseById(Long id) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhConfEnterprises.class, id));
		EhConfEnterprisesDao dao = new EhConfEnterprisesDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), ConfEnterprises.class);
	}

	@Override
	public void updateInvaildAccount() {
		Timestamp current = new Timestamp(System.currentTimeMillis());

		CrossShardListingLocator locator = new CrossShardListingLocator();
		if (locator.getShardIterator() == null) {
            AccessSpec accessSpec = AccessSpec.readWriteWith(EhConfAccounts.class);
            ShardIterator shardIterator = new ShardIterator(accessSpec);
            locator.setShardIterator(shardIterator);
        }
		this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (DSLContext context, Object reducingContext) -> {
			SelectQuery<EhConfAccountsRecord> query = context.selectQuery(Tables.EH_CONF_ACCOUNTS);
			if (locator.getAnchor() != null && locator.getAnchor() != 0)
				query.addConditions(Tables.EH_CONF_ACCOUNTS.ID.gt(locator.getAnchor()));
			
			query.addConditions(Tables.EH_CONF_ACCOUNTS.EXPIRED_DATE.lt(current));
			query.addOrderBy(Tables.EH_CONF_ACCOUNTS.ID.asc());
			
			query.fetch().map((r) -> {
				r.setStatus((byte) 0);;
				EhConfAccounts account = ConvertHelper.convert(r, EhConfAccounts.class);
				EhConfAccountsDao dao = new EhConfAccountsDao(context.configuration());
		        dao.update(account);
		        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhConfAccounts.class, account.getId());
				return null;
			});
			return AfterAction.next;
		});
	}

	@Override
	public void updateEnterpriseAccounts() {
		CrossShardListingLocator locator = new CrossShardListingLocator();
		if (locator.getShardIterator() == null) {
            AccessSpec accessSpec = AccessSpec.readWriteWith(EhConfEnterprises.class);
            ShardIterator shardIterator = new ShardIterator(accessSpec);
            locator.setShardIterator(shardIterator);
        }
		this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (DSLContext context, Object reducingContext) -> {
			SelectQuery<EhConfEnterprisesRecord> query = context.selectQuery(Tables.EH_CONF_ENTERPRISES);
			if (locator.getAnchor() != null && locator.getAnchor() != 0)
				query.addConditions(Tables.EH_CONF_ENTERPRISES.ID.gt(locator.getAnchor()));
			
			query.addConditions(Tables.EH_CONF_ENTERPRISES.DELETER_UID.eq(0L));
			query.addOrderBy(Tables.EH_CONF_ENTERPRISES.ID.asc());
			
			query.fetch().map((r) -> {
				int activeCount = countAccountsByEnterprise(r.getEnterpriseId(), null);
				int trialCount = countAccountsByEnterprise(r.getEnterpriseId(), (byte) 1);

				r.setActiveAccountAmount(activeCount);
				r.setTrialAccountAmount(trialCount);
				EhConfEnterprises enterprise = ConvertHelper.convert(r, EhConfEnterprises.class);
				EhConfEnterprisesDao dao = new EhConfEnterprisesDao(context.configuration());
		        dao.update(enterprise);
		        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhConfEnterprises.class, enterprise.getId());
				return null;
			});
			return AfterAction.next;
		});
		
	}
	
	@Override
	public int countAccountsByEnterprise(Long enterpriseId, Byte accountType) {
	    
	    final Integer[] count = new Integer[1];
        
        if(accountType == null) {
        	this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhConfAccounts.class), null, 
                    (DSLContext context, Object reducingContext)-> {
                        count[0] = context.selectCount().from(Tables.EH_CONF_ACCOUNTS)
                        		.where(Tables.EH_CONF_ACCOUNTS.ENTERPRISE_ID.eq(enterpriseId))
                                .and(Tables.EH_CONF_ACCOUNTS.STATUS.ne((byte) 0))
                                .and(Tables.EH_CONF_ACCOUNTS.DELETE_UID.eq(0L))
                                .fetchOneInto(Integer.class);
                        return true;
                    });
		}
		else {
			this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhConfAccounts.class), null, 
	                (DSLContext context, Object reducingContext)-> {
	                    count[0] = context.selectCount().from(Tables.EH_CONF_ACCOUNTS)
	                    		.where(Tables.EH_CONF_ACCOUNTS.ENTERPRISE_ID.eq(enterpriseId))
	                            .and(Tables.EH_CONF_ACCOUNTS.ACCOUNT_TYPE.eq(accountType))
	                            .and(Tables.EH_CONF_ACCOUNTS.STATUS.ne((byte) 0))
	                            .and(Tables.EH_CONF_ACCOUNTS.DELETE_UID.eq(0L))
	                            .fetchOneInto(Integer.class);
	                    return true;
	                });
		}
        
        return count[0];
	}

	@Override
	public void deleteSourceVideoConfAccount(long id) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhConfSourceAccounts.class));
		EhConfSourceAccountsDao dao = new EhConfSourceAccountsDao(context.configuration());
		dao.deleteById(id);
	}

	@Override
	public void updateInvoice(ConfInvoices invoice) {

		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        
		EhConfInvoicesDao dao = new EhConfInvoicesDao(context.configuration());
        dao.update(invoice);
	}

	@Override
	public int countEnterpriseAccounts(Long enterpriseId) {

		final Integer[] count = new Integer[1];
		this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhConfAccounts.class), null, 
                (DSLContext context, Object reducingContext)-> {
                    count[0] = context.selectCount().from(Tables.EH_CONF_ACCOUNTS)
                    		.where(Tables.EH_CONF_ACCOUNTS.ENTERPRISE_ID.eq(enterpriseId))
                            .and(Tables.EH_CONF_ACCOUNTS.DELETE_UID.eq(0L))
                            .fetchOneInto(Integer.class);
                    return true;
                });
		return count[0];
	}

	@Override
	public List<ConfAccounts> listOccupiedConfAccounts(Timestamp assignedTime) {
		List<ConfAccounts> accounts = new ArrayList<ConfAccounts>();
		CrossShardListingLocator locator = new CrossShardListingLocator();
		if (locator.getShardIterator() == null) {
            AccessSpec accessSpec = AccessSpec.readWriteWith(EhConfAccounts.class);
            ShardIterator shardIterator = new ShardIterator(accessSpec);
            locator.setShardIterator(shardIterator);
        }
		this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (DSLContext context, Object reducingContext) -> {
            SelectQuery<EhConfAccountsRecord> query = context.selectQuery(Tables.EH_CONF_ACCOUNTS);
            query.addConditions(Tables.EH_CONF_ACCOUNTS.ASSIGNED_FLAG.eq((byte) 1));
            query.addConditions(Tables.EH_CONF_ACCOUNTS.ASSIGNED_TIME.le(assignedTime));
            
            query.fetch().map((r) -> {
            	
            	accounts.add(ConvertHelper.convert(r, ConfAccounts.class));
                return null;
            });

            return AfterAction.next;
        });

        return accounts;
	}

	@Override
	public Long listConfTimeByAccount(Long accountId) {
		Long count = 0L;
		List<Integer> realDurations = new ArrayList<Integer>();
		CrossShardListingLocator locator=new CrossShardListingLocator();
		if (locator.getShardIterator() == null) {
            AccessSpec accessSpec = AccessSpec.readWriteWith(EhConfConferences.class);
            ShardIterator shardIterator = new ShardIterator(accessSpec);
            locator.setShardIterator(shardIterator);
        }
        this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (context, obj) -> {
            SelectQuery<EhConfConferencesRecord> query = context.selectQuery(Tables.EH_CONF_CONFERENCES);
            query.addConditions(Tables.EH_CONF_CONFERENCES.CONF_ACCOUNT_ID.eq(accountId));
            
            if(LOGGER.isDebugEnabled()) {
                LOGGER.debug("Query EquipmentInspectionTasksLogs by count, sql=" + query.getSQL());
                LOGGER.debug("Query EquipmentInspectionTasksLogs by count, bindValues=" + query.getBindValues());
            }
            
            query.fetch().map((r) -> {
            	
            	realDurations.add(r.getRealDuration());
                return null;
            });

            return AfterAction.next;
        });

        if(realDurations != null && realDurations.size() > 0) {
        	for(Integer realDuration : realDurations) {
        		count += realDuration;
        	}
        }
        
        return count;
	}

	@Override
	public List<ConfConferences> listConfbyAccount(Long accountId, CrossShardListingLocator locator, Integer pageSize) {

		List<ConfConferences> conferences = new ArrayList<ConfConferences>();
		
		if (locator.getShardIterator() == null) {
            AccessSpec accessSpec = AccessSpec.readOnlyWith(EhConfConferences.class);
            ShardIterator shardIterator = new ShardIterator(accessSpec);
            locator.setShardIterator(shardIterator);
        }
        this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (context, obj) -> {
            SelectQuery<EhConfConferencesRecord> query = context.selectQuery(Tables.EH_CONF_CONFERENCES);
            if(locator.getAnchor() != null)
            	query.addConditions(Tables.EH_CONF_CONFERENCES.ID.lt(locator.getAnchor()));
            
            query.addConditions(Tables.EH_CONF_CONFERENCES.CONF_ACCOUNT_ID.eq(accountId));
           
            query.addOrderBy(Tables.EH_CONF_CONFERENCES.ID.desc());
            query.addLimit(pageSize - conferences.size());
            
            query.fetch().map((r) -> {
            	
            	conferences.add(ConvertHelper.convert(r, ConfConferences.class));
                return null;
            });

            return AfterAction.next;
        });

        return conferences;
	}

	@Override
	public boolean allTrialEnterpriseAccounts(Long enterpriseId) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		
		boolean trial = true;
		
		List<ConfAccounts> accounts = new ArrayList<ConfAccounts>();
		List<Long> categories = findAccountCategoriesByConfType((byte) 4);
		
		SelectQuery<EhConfAccountsRecord> query = context.selectQuery(Tables.EH_CONF_ACCOUNTS);
            
        if(enterpriseId != null)
        	query.addConditions(Tables.EH_CONF_ACCOUNTS.ENTERPRISE_ID.eq(enterpriseId));
        
        
        query.addConditions(Tables.EH_CONF_ACCOUNTS.ACCOUNT_CATEGORY_ID.notIn(categories));
        query.addConditions(Tables.EH_CONF_ACCOUNTS.DELETE_UID.eq(0L));
       
        
        query.fetch().map((r) -> {
        	accounts.add(ConvertHelper.convert(r, ConfAccounts.class));
            return null;
        });
        
        if(accounts != null && accounts.size() > 0) {
        	trial = false;
        }
		
		return trial;
	}

	@Override
	public ConfAccounts findAccountByUserIdAndEnterpriseId(Long userId,
			Long enterpriseId) {
		List<ConfAccounts> accounts = new ArrayList<ConfAccounts>();
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		SelectQuery<EhConfAccountsRecord> query = context.selectQuery(Tables.EH_CONF_ACCOUNTS);
		query.addConditions(Tables.EH_CONF_ACCOUNTS.ENTERPRISE_ID.eq(enterpriseId));
		query.addConditions(Tables.EH_CONF_ACCOUNTS.OWNER_ID.eq(userId));
		query.addConditions(Tables.EH_CONF_ACCOUNTS.DELETE_UID.eq(0L));
		
		query.addOrderBy(Tables.EH_CONF_ACCOUNTS.STATUS.desc());
		
		if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("Query findAccountByUserIdAndEnterpriseId, sql=" + query.getSQL());
            LOGGER.debug("Query findAccountByUserIdAndEnterpriseId, bindValues=" + query.getBindValues());
        }
		
		
		query.fetch().map((r) -> {
			accounts.add(ConvertHelper.convert(r, ConfAccounts.class));
            return null;
		});

		if(accounts != null && accounts.size() > 0) {
			return accounts.get(0);
		}

		return null;
	}

	@Override
	public ConfAccounts findAccountByUserIdAndEnterpriseIdAndStatus(Long userId,
			Long enterpriseId,Byte status) {
		List<ConfAccounts> accounts = new ArrayList<ConfAccounts>();
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		SelectQuery<EhConfAccountsRecord> query = context.selectQuery(Tables.EH_CONF_ACCOUNTS);
		query.addConditions(Tables.EH_CONF_ACCOUNTS.ENTERPRISE_ID.eq(enterpriseId));
		query.addConditions(Tables.EH_CONF_ACCOUNTS.OWNER_ID.eq(userId));
		query.addConditions(Tables.EH_CONF_ACCOUNTS.DELETE_UID.eq(0L));
		query.addConditions(Tables.EH_CONF_ACCOUNTS.STATUS.eq(status));
		 
		
		if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("Query findAccountByUserIdAndEnterpriseId, sql=" + query.getSQL());
            LOGGER.debug("Query findAccountByUserIdAndEnterpriseId, bindValues=" + query.getBindValues());
        }
		
		
		query.fetch().map((r) -> {
			accounts.add(ConvertHelper.convert(r, ConfAccounts.class));
            return null;
		});

		if(accounts != null && accounts.size() > 0) {
			return accounts.get(0);
		}

		return null;
	}
	@Override
	public List<Long> findAccountCategoriesByNotInConfType(Byte confType) {

		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhConfAccountCategories.class));
		List<Long> accountCategories = new ArrayList<Long>();
		
		SelectQuery<EhConfAccountCategoriesRecord> query = context.selectQuery(Tables.EH_CONF_ACCOUNT_CATEGORIES);
		
		if(confType != null)
			query.addConditions(Tables.EH_CONF_ACCOUNT_CATEGORIES.CONF_TYPE.ne(confType));
		
		query.fetch().map((r) ->{
			accountCategories.add(r.getValue(Tables.EH_CONF_ACCOUNT_CATEGORIES.ID));
			return null;
        });
		
		return accountCategories;
	}

	@Override
	public List<ConfOrders> findConfOrdersByCategoriesAndDate(List<Long> categories,
			Calendar calendar1) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(calendar1.getTime());
		List<ConfOrders> results = new ArrayList<ConfOrders>();
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		SelectQuery<EhConfOrdersRecord> query = context.selectQuery(Tables.EH_CONF_ORDERS);
		query.addConditions(Tables.EH_CONF_ORDERS.ACCOUNT_CATEGORY_ID.in(categories));
		query.addConditions(Tables.EH_CONF_ORDERS.EXPIRED_DATE.greaterOrEqual(new Timestamp(calendar.getTimeInMillis())));
		calendar.add(Calendar.DAY_OF_MONTH, 1); 
		query.addConditions(Tables.EH_CONF_ORDERS.EXPIRED_DATE.lt(new Timestamp(calendar.getTimeInMillis()))); 
		
		if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("Query findAccountByUserIdAndEnterpriseId, sql=" + query.getSQL());
            LOGGER.debug("Query findAccountByUserIdAndEnterpriseId, bindValues=" + query.getBindValues());
        }
		
		
		query.fetch().map((r) -> {
			results.add(ConvertHelper.convert(r, ConfOrders.class));
            return null;
		});
 

		return results;
	}
	
}
