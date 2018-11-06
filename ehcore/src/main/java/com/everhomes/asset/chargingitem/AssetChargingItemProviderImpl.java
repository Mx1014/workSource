package com.everhomes.asset.chargingitem;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectConditionStep;
import org.jooq.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import com.everhomes.asset.AssetProvider;
import com.everhomes.asset.PaymentChargingItem;
import com.everhomes.asset.PaymentChargingItemScope;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.portal.PortalService;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.asset.AssetPaymentBillDeleteFlag;
import com.everhomes.rest.asset.AssetProjectDefaultFlag;
import com.everhomes.rest.asset.ChargingVariable;
import com.everhomes.rest.asset.ChargingVariables;
import com.everhomes.rest.asset.ConfigChargingItems;
import com.everhomes.rest.asset.ConfigChargingItemsCommand;
import com.everhomes.rest.asset.CreateChargingItemCommand;
import com.everhomes.rest.asset.IsContractChangeFlagDTO;
import com.everhomes.rest.asset.IsProjectNavigateDefaultCmd;
import com.everhomes.rest.asset.IsProjectNavigateDefaultResp;
import com.everhomes.rest.asset.ListChargingItemsDTO;
import com.everhomes.rest.common.ServiceModuleConstants;
import com.everhomes.rest.portal.ListServiceModuleAppsCommand;
import com.everhomes.rest.portal.ListServiceModuleAppsResponse;
import com.everhomes.rest.portal.ServiceModuleAppDTO;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.EhContractChargingItems;
import com.everhomes.server.schema.tables.EhContracts;
import com.everhomes.server.schema.tables.EhPaymentBillItems;
import com.everhomes.server.schema.tables.EhPaymentBills;
import com.everhomes.server.schema.tables.EhPaymentChargingItemScopes;
import com.everhomes.server.schema.tables.EhPaymentChargingItems;
import com.everhomes.server.schema.tables.daos.EhPaymentChargingItemScopesDao;
import com.everhomes.server.schema.tables.daos.EhPaymentChargingItemsDao;
import com.everhomes.server.schema.tables.records.EhPaymentChargingItemScopesRecord;
import com.everhomes.user.UserContext;
import com.everhomes.util.DateHelper;
import com.everhomes.util.ExecutorUtil;
import com.everhomes.util.StringHelper;
/**
 * @author created by ycx
 * @date 下午8:42:50
 */
@Component
public class AssetChargingItemProviderImpl implements AssetChargingItemProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(AssetChargingItemProviderImpl.class);

    @Autowired
    private DbProvider dbProvider;
    
    @Autowired
    private SequenceProvider sequenceProvider;
    
    @Autowired
    private PortalService portalService;
    
    @Autowired
    private AssetProvider assetProvider;
    
    private DSLContext getReadOnlyContext(){
        return this.dbProvider.getDslContext(AccessSpec.readOnly());
    }
    
    private DSLContext getReadWriteContext(){
        return this.dbProvider.getDslContext(AccessSpec.readWrite());
    }
    
    @SuppressWarnings("rawtypes")
	private Long getNextSequence(Class clz){
        return this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(clz));
    }
    
    public void configChargingItems(ConfigChargingItemsCommand cmd, byte de_coupling, Boolean allScope) {
    	configChargingItemForOneCommunity(cmd.getChargingItemConfigs(), cmd.getOwnerId(), cmd.getOwnerType(), cmd.getNamespaceId(), de_coupling, cmd.getCategoryId(), 
    			allScope, cmd.getOrganizationId());
    }

    private void configChargingItemForOneCommunity(List<ConfigChargingItems> configChargingItems, Long communityId, String ownerType, Integer namespaceId, 
    		Byte decouplingFlag, Long categoryId, Boolean allScope, Long organizationId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhPaymentChargingItemScopes t = Tables.EH_PAYMENT_CHARGING_ITEM_SCOPES.as("t");
        EhPaymentChargingItemScopesDao dao = new EhPaymentChargingItemScopesDao(context.configuration());
        List<com.everhomes.server.schema.tables.pojos.EhPaymentChargingItemScopes> list = new ArrayList<>();
        if(configChargingItems == null){
        	//标准版增加的allScope参数，true：默认/全部，false：具体项目
        	if(allScope) {
        		context.delete(t)
	                .where(t.OWNER_TYPE.eq(ownerType))
	                .and(t.OWNER_ID.eq(communityId))
	                .and(t.CATEGORY_ID.eq(categoryId))
	                .and(t.NAMESPACE_ID.eq(namespaceId))
	                .and(t.ORG_ID.eq(organizationId))//标准版新增的管理公司ID
	                .execute();
        	}else {
        		context.delete(t)
	                .where(t.OWNER_TYPE.eq(ownerType))
	                .and(t.OWNER_ID.eq(communityId))
	                .and(t.CATEGORY_ID.eq(categoryId))
	                .and(t.NAMESPACE_ID.eq(namespaceId))
	                .execute();
        	}
            return;
        }
        for(int i = 0; i < configChargingItems.size(); i ++) {
            ConfigChargingItems vo = configChargingItems.get(i);
            PaymentChargingItemScope scope = new PaymentChargingItemScope();
            scope.setChargingItemId(vo.getChargingItemId());
            long nextSequence = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(com.everhomes.server.schema.tables.pojos.EhPaymentChargingItemScopes.class));
            scope.setId(nextSequence);
            scope.setNamespaceId(namespaceId);
            scope.setOwnerId(communityId);
            scope.setOwnerType(ownerType);
            scope.setCategoryId(categoryId);
            scope.setProjectLevelName(vo.getProjectChargingItemName());
            scope.setDecouplingFlag(decouplingFlag);
            scope.setTaxRate(vo.getTaxRate());//增加税率
            if(allScope) {
            	scope.setOrgId(organizationId);//标准版新增的管理公司ID
            }
            list.add(scope);
        }
        this.dbProvider.execute((TransactionStatus status) -> {
        	//标准版增加的allScope参数，true：默认/全部，false：具体项目
        	if(allScope) {
        		context.delete(t)
	                .where(t.OWNER_TYPE.eq(ownerType))
	                .and(t.OWNER_ID.eq(communityId))
	                .and(t.NAMESPACE_ID.eq(namespaceId))
	                .and(t.CATEGORY_ID.eq(categoryId))
	                .and(t.ORG_ID.eq(organizationId))//标准版新增的管理公司ID
	                .execute();
        	}else {
        		context.delete(t)
	                .where(t.OWNER_TYPE.eq(ownerType))
	                .and(t.OWNER_ID.eq(communityId))
	                .and(t.NAMESPACE_ID.eq(namespaceId))
	                .and(t.CATEGORY_ID.eq(categoryId))
	                .execute();
        	}
            if(list.size()>0){
                dao.insert(list);
            }
            return null;
        });

        //判断一下是否该物业缴费模块是否配置了走合同变更的开关
        if(isContractChangeFlag(namespaceId, ServiceModuleConstants.ASSET_MODULE, categoryId)) {
        	DbProvider dbProvider = this.dbProvider;
        	ExecutorUtil.submit(new Runnable() {
                @Override
                public void run() {
                    try{
                    	//若开关关闭，则税率修改后，系统自动按照修改后的税率更新未出账单及未出账单对应的合同费用清单
                    	Set<Long> billIds = new HashSet<>();
                    	EhPaymentBills bill = Tables.EH_PAYMENT_BILLS.as("bill");
                    	//获取所有未出账单的id、以及合同那边的费用清单列表
                    	getReadOnlyContext().select(bill.ID)
            	            .from(bill)
            	            .where(bill.NAMESPACE_ID.eq(namespaceId))
            	            .and(bill.CATEGORY_ID.eq(categoryId))
            	            .and(bill.OWNER_ID.eq(communityId))
            	            .and(bill.SWITCH.eq((byte) 0).or(bill.SWITCH.eq((byte) 3))) //未出账单 or 合同那边的草稿合同费用清单
            	            .and(bill.DELETE_FLAG.eq(AssetPaymentBillDeleteFlag.VALID.getCode()))//物业缴费V6.0 账单、费项表增加是否删除状态字段
            	            .fetch()
            	            .forEach(r -> {
            	            	billIds.add(r.getValue(bill.ID));
                        });
                    	//根据设置的税率重新计算费项
                    	EhPaymentBillItems billItems = Tables.EH_PAYMENT_BILL_ITEMS.as("billItems");
                        getReadOnlyContext().select(billItems.ID,billItems.BILL_ID,billItems.AMOUNT_RECEIVABLE,t.TAX_RATE)
                                .from(billItems)
                                .leftOuterJoin(t)
                                .on(billItems.CHARGING_ITEMS_ID.eq(t.CHARGING_ITEM_ID))
                                .where(billItems.NAMESPACE_ID.eq(namespaceId))
                                .and(billItems.CATEGORY_ID.eq(categoryId))
                                .and(billItems.OWNER_ID.eq(communityId))
                                .and(t.NAMESPACE_ID.eq(namespaceId))
                                .and(t.CATEGORY_ID.eq(categoryId))
                                .and(t.OWNER_ID.eq(communityId))
                                .and(billItems.BILL_ID.in(billIds))
                                .and(billItems.DELETE_FLAG.eq(AssetPaymentBillDeleteFlag.VALID.getCode()))//物业缴费V6.0 账单、费项表增加是否删除状态字段
                                .fetch()
                                .forEach(r -> {
                                	//不含税金额=含税金额/（1+税率）    不含税金额=1000/（1+10%）=909.09
                                	final BigDecimal[] amountReceivable = {new BigDecimal("0")};
                    		        final BigDecimal[] amountReceivableWithoutTax = {new BigDecimal("0")};
                    		        final BigDecimal[] amountReceived = {new BigDecimal("0")};
                    		        final BigDecimal[] amountReceivedWithoutTax = {new BigDecimal("0")};
                    		        //final BigDecimal[] amountOwed = {new BigDecimal("0")};
                    		        //final BigDecimal[] amountOwedWithoutTax = {new BigDecimal("0")};
                    		        final BigDecimal[] taxAmount = {new BigDecimal("0")};
                    		        final BigDecimal[] taxRate = {new BigDecimal("0")};
                    		        taxRate[0] = r.getValue(t.TAX_RATE);
                        			if(taxRate[0] != null) {
                        				Long billItemId = r.getValue(billItems.ID);
                        				amountReceivable[0] = r.getValue(billItems.AMOUNT_RECEIVABLE);
                        				BigDecimal taxRateDiv = taxRate[0].divide(new BigDecimal(100));
                            			amountReceivableWithoutTax[0] = amountReceivable[0].divide(BigDecimal.ONE.add(taxRateDiv), 2, BigDecimal.ROUND_HALF_UP);
                            			//税额=含税金额-不含税金额       税额=1000-909.09=90.91
                            			taxAmount[0] = amountReceivable[0].subtract(amountReceivableWithoutTax[0]);
                            			dbProvider.execute((TransactionStatus status) -> {
                            	            context.update(billItems)
                            	            		.set(billItems.AMOUNT_RECEIVABLE, amountReceivable[0])
                            	            		.set(billItems.AMOUNT_RECEIVABLE_WITHOUT_TAX, amountReceivableWithoutTax[0])
                            	            		.set(billItems.TAX_AMOUNT, taxAmount[0])
                            	            		.set(billItems.TAX_RATE, taxRate[0])
                            	            		.set(billItems.AMOUNT_RECEIVED, amountReceived[0])
                            	            		.set(billItems.AMOUNT_RECEIVED_WITHOUT_TAX, amountReceivedWithoutTax[0])
                            	            		.set(billItems.AMOUNT_OWED, amountReceivable[0])
                            	            		.set(billItems.AMOUNT_OWED_WITHOUT_TAX, amountReceivableWithoutTax[0])
                            	                    .where(billItems.NAMESPACE_ID.eq(namespaceId))
                            	                    .and(billItems.OWNER_ID.eq(communityId))
                            	                    .and(billItems.CATEGORY_ID.eq(categoryId))
                            	                    .and(billItems.ID.eq(billItemId))
                            	                    .execute();
                            	            return null;
                            	        });
                        			}
                                });
                    	//重新计算账单
                        for (Long billId : billIds) {
                        	assetProvider.reCalBillById(billId);
                        }
                        //修改税率之后，合同管理要重新修改计价条款中已保存的税率（包括单价（不含税）、单价（含税）、税率等等）
                        //第一步：首先修改“缴费”模块的计价条款变量值 eh_default_charging_items
                        getReadOnlyContext().select(Tables.EH_DEFAULT_CHARGING_ITEMS.ID,Tables.EH_DEFAULT_CHARGING_ITEMS.CHARGING_ITEM_ID,
                        		Tables.EH_DEFAULT_CHARGING_ITEMS.CHARGING_VARIABLES,t.TAX_RATE)
            	            .from(Tables.EH_DEFAULT_CHARGING_ITEMS)
            	            .leftOuterJoin(t)
            	            .on(Tables.EH_DEFAULT_CHARGING_ITEMS.CHARGING_ITEM_ID.eq(t.CHARGING_ITEM_ID))
            	            .where(Tables.EH_DEFAULT_CHARGING_ITEMS.NAMESPACE_ID.eq(namespaceId))
            	            .and(Tables.EH_DEFAULT_CHARGING_ITEMS.COMMUNITY_ID.eq(communityId))
            	            .and(Tables.EH_DEFAULT_CHARGING_ITEMS.STATUS.eq(CommonStatus.ACTIVE.getCode()))
            	            .and(t.NAMESPACE_ID.eq(namespaceId))
            	            .and(t.CATEGORY_ID.eq(categoryId))
            	            .and(t.OWNER_ID.eq(communityId))
            	            .fetch()
            	            .forEach(r -> {
            	            	Long id = r.getValue(Tables.EH_DEFAULT_CHARGING_ITEMS.ID);
            	        		String chargingVariables = r.getValue(Tables.EH_DEFAULT_CHARGING_ITEMS.CHARGING_VARIABLES);
            	        		BigDecimal taxRate = r.getValue(t.TAX_RATE);
            	        		updateChargingVariables(id, chargingVariables, taxRate, "EH_DEFAULT_CHARGING_ITEMS");
            	            });

                        //第二步：修改合同管理要重新修改计价条款中已保存的税率（包括单价（不含税）、单价（含税）、税率等等）eh_contract_charging_items
                        EhContractChargingItems t2 = Tables.EH_CONTRACT_CHARGING_ITEMS.as("t2");
                        EhContracts t3 = Tables.EH_CONTRACTS.as("t3");
                        SelectQuery<Record> query = context.selectQuery();
                        query.addSelect(t2.ID,t2.CHARGING_ITEM_ID,t2.CHARGING_VARIABLES,t.TAX_RATE);
                        query.addFrom(t2);
                        query.addJoin(t3, t3.ID.eq(t2.CONTRACT_ID));
                        query.addJoin(t, t.CHARGING_ITEM_ID.eq(t2.CHARGING_ITEM_ID));
                        query.addConditions(t3.NAMESPACE_ID.eq(namespaceId));
                        query.addConditions(t3.COMMUNITY_ID.eq(communityId));
                        query.addConditions(t.NAMESPACE_ID.eq(namespaceId));
                        query.addConditions(t.CATEGORY_ID.eq(categoryId));
                        query.addConditions(t.OWNER_ID.eq(communityId));
                        query.fetch().forEach(r -> {
                        	Long id = r.getValue(Tables.EH_CONTRACT_CHARGING_ITEMS.ID);
                    		String chargingVariables = r.getValue(Tables.EH_CONTRACT_CHARGING_ITEMS.CHARGING_VARIABLES);
                    		BigDecimal taxRate = r.getValue(t.TAX_RATE);
                    		updateChargingVariables(id, chargingVariables, taxRate, "EH_CONTRACT_CHARGING_ITEMS");
                        });
                    }catch (Exception e){
                        LOGGER.error("executor task error. error: {}", e);
                    }
                }
            });
        }
    }

    public void updateChargingVariables(Long id, String chargingVariables, BigDecimal taxRate,String tableName) {
    	try {
        	if(chargingVariables != null && chargingVariables != "") {
        		if(chargingVariables.contains("\"variableIdentifier\":\"dj\"")) {//单价
        			ChargingVariables chargingVariableList =
	                		(ChargingVariables) StringHelper.fromJsonString(chargingVariables, ChargingVariables.class);
	                if(chargingVariableList != null && chargingVariableList.getChargingVariables() != null) {
	                	BigDecimal dj = BigDecimal.ZERO;//单价
	                	BigDecimal djbhs = BigDecimal.ZERO;//单价不含税
	                	BigDecimal mj = BigDecimal.ZERO;
	                	for(ChargingVariable chargingVariable : chargingVariableList.getChargingVariables()) {
	                		if(chargingVariable.getVariableIdentifier() != null) {
	                			if(chargingVariable.getVariableIdentifier().equals("dj")) {
	                				dj = new BigDecimal(chargingVariable.getVariableValue());
	                			}
	                			if(chargingVariable.getVariableIdentifier().equals("mj")) {
	                				mj = new BigDecimal(chargingVariable.getVariableValue());
	                			}
	                		}
	                	}
	                	BigDecimal taxRateDiv = taxRate.divide(new BigDecimal(100));
	                	djbhs = dj.divide(BigDecimal.ONE.add(taxRateDiv), 2, BigDecimal.ROUND_HALF_UP);//修改税率之后，重新计算不含税
	                	//重新组装Tables.EH_DEFAULT_CHARGING_ITEMS.CHARGING_VARIABLES的值
	                	ChargingVariables newChargingVariableList = new ChargingVariables();
	                	newChargingVariableList.setChargingVariables(new ArrayList<>());
	                	ChargingVariable djChargingVariable = new ChargingVariable();
	                	djChargingVariable.setVariableIdentifier("dj");
	                	djChargingVariable.setVariableName("单价含税(元)");
	                	djChargingVariable.setVariableValue(dj.toString());
	                	newChargingVariableList.getChargingVariables().add(djChargingVariable);
	                	ChargingVariable taxRateChargingVariable = new ChargingVariable();
	                	taxRateChargingVariable.setVariableIdentifier("taxRate");
	                	taxRateChargingVariable.setVariableName("税率(%)");
	                	taxRateChargingVariable.setVariableValue(taxRate.toString());
	                	newChargingVariableList.getChargingVariables().add(taxRateChargingVariable);
	                	ChargingVariable djbhsChargingVariable = new ChargingVariable();
	                	djbhsChargingVariable.setVariableIdentifier("djbhs");
	                	djbhsChargingVariable.setVariableName("单价不含税(元)");
	                	djbhsChargingVariable.setVariableValue(djbhs.toString());
	                	newChargingVariableList.getChargingVariables().add(djbhsChargingVariable);
	                	ChargingVariable mjChargingVariable = new ChargingVariable();
	                	mjChargingVariable.setVariableIdentifier("mj");
	                	mjChargingVariable.setVariableName("面积(㎡)");
	                	mjChargingVariable.setVariableValue(mj.toString());
	                	newChargingVariableList.getChargingVariables().add(mjChargingVariable);
	                	DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
	                	if(tableName.equals("EH_DEFAULT_CHARGING_ITEMS")) {
	                		this.dbProvider.execute((TransactionStatus status) -> {
	            	            context.update(Tables.EH_DEFAULT_CHARGING_ITEMS)
	            	            		.set(Tables.EH_DEFAULT_CHARGING_ITEMS.CHARGING_VARIABLES, newChargingVariableList.toString())
	            	                    .where(Tables.EH_DEFAULT_CHARGING_ITEMS.ID.eq(id))
	            	                    .execute();
	            	            return null;
	            	        });
	                	}else if(tableName.equals("EH_CONTRACT_CHARGING_ITEMS")) {
	                		this.dbProvider.execute((TransactionStatus status) -> {
	            	            context.update(Tables.EH_CONTRACT_CHARGING_ITEMS)
	            	            		.set(Tables.EH_CONTRACT_CHARGING_ITEMS.CHARGING_VARIABLES, newChargingVariableList.toString())
	            	                    .where(Tables.EH_CONTRACT_CHARGING_ITEMS.ID.eq(id))
	            	                    .execute();
	            	            return null;
	            	        });
	                	}
	                }
        		}else if(chargingVariables.contains("\"variableIdentifier\":\"gdje\"")) {//固定金额
        			ChargingVariables chargingVariableList =
	                		(ChargingVariables) StringHelper.fromJsonString(chargingVariables, ChargingVariables.class);
	                if(chargingVariableList != null && chargingVariableList.getChargingVariables() != null) {
	                	BigDecimal gdje = BigDecimal.ZERO;//固定金额(含税)
	                	BigDecimal gdjebhs = BigDecimal.ZERO;//固定金额(不含税)
	                	for(ChargingVariable chargingVariable : chargingVariableList.getChargingVariables()) {
	                		if(chargingVariable.getVariableIdentifier() != null) {
	                			if(chargingVariable.getVariableIdentifier().equals("gdje")) {
	                				gdje = new BigDecimal(chargingVariable.getVariableValue());
	                			}
	                		}
	                	}
	                	BigDecimal taxRateDiv = taxRate.divide(new BigDecimal(100));
	                	gdjebhs = gdje.divide(BigDecimal.ONE.add(taxRateDiv), 2, BigDecimal.ROUND_HALF_UP);//修改税率之后，重新计算不含税
	                	//重新组装Tables.EH_DEFAULT_CHARGING_ITEMS.CHARGING_VARIABLES的值
	                	ChargingVariables newChargingVariableList = new ChargingVariables();
	                	newChargingVariableList.setChargingVariables(new ArrayList<>());
	                	ChargingVariable gdjeChargingVariable = new ChargingVariable();
	                	gdjeChargingVariable.setVariableIdentifier("gdje");
	                	gdjeChargingVariable.setVariableName("固定金额含税(元)");
	                	gdjeChargingVariable.setVariableValue(gdje.toString());
	                	newChargingVariableList.getChargingVariables().add(gdjeChargingVariable);
	                	ChargingVariable taxRateChargingVariable = new ChargingVariable();
	                	taxRateChargingVariable.setVariableIdentifier("taxRate");
	                	taxRateChargingVariable.setVariableName("税率(%)");
	                	taxRateChargingVariable.setVariableValue(taxRate.toString());
	                	newChargingVariableList.getChargingVariables().add(taxRateChargingVariable);
	                	ChargingVariable gdjebhsChargingVariable = new ChargingVariable();
	                	gdjebhsChargingVariable.setVariableIdentifier("gdjebhs");
	                	gdjebhsChargingVariable.setVariableName("固定金额不含税(元)");
	                	gdjebhsChargingVariable.setVariableValue(gdjebhs.toString());
	                	newChargingVariableList.getChargingVariables().add(gdjebhsChargingVariable);

	                	DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
	                	if(tableName.equals("EH_DEFAULT_CHARGING_ITEMS")) {
	                		this.dbProvider.execute((TransactionStatus status) -> {
	            	            context.update(Tables.EH_DEFAULT_CHARGING_ITEMS)
	            	            		.set(Tables.EH_DEFAULT_CHARGING_ITEMS.CHARGING_VARIABLES, newChargingVariableList.toString())
	            	                    .where(Tables.EH_DEFAULT_CHARGING_ITEMS.ID.eq(id))
	            	                    .execute();
	            	            return null;
	            	        });
	                	}else if(tableName.equals("EH_CONTRACT_CHARGING_ITEMS")) {
	                		this.dbProvider.execute((TransactionStatus status) -> {
	            	            context.update(Tables.EH_CONTRACT_CHARGING_ITEMS)
	            	            		.set(Tables.EH_CONTRACT_CHARGING_ITEMS.CHARGING_VARIABLES, newChargingVariableList.toString())
	            	                    .where(Tables.EH_CONTRACT_CHARGING_ITEMS.ID.eq(id))
	            	                    .execute();
	            	            return null;
	            	        });
	                	}
	                }
        		}
        	}
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    
    //判断一下是否该物业缴费模块是否配置了走合同变更的开关
  	public Boolean isContractChangeFlag(Integer namespaceId, Long moduleId, Long categoryId) {
  		ListServiceModuleAppsCommand cmd = new ListServiceModuleAppsCommand();
  		cmd.setNamespaceId(namespaceId);
  		cmd.setModuleId(moduleId);
  		ListServiceModuleAppsResponse response = portalService.listServiceModuleApps(cmd);
  		List<ServiceModuleAppDTO> serviceModuleApps = response.getServiceModuleApps();
  		for(ServiceModuleAppDTO serviceModuleAppDTO : serviceModuleApps) {
  			String instanceConfig = serviceModuleAppDTO.getInstanceConfig();
  			IsContractChangeFlagDTO isContractChangeFlagDTO =
  					(IsContractChangeFlagDTO) StringHelper.fromJsonString(instanceConfig, IsContractChangeFlagDTO.class);
  			Byte contractChangeFlag = 0; //0：代表不走合同变更
  			if(isContractChangeFlagDTO != null && categoryId.equals(isContractChangeFlagDTO.getCategoryId())
  					&& contractChangeFlag.equals(isContractChangeFlagDTO.getContractChangeFlag())) {
  				return true;
  			}
  		}
  		return false;
  	}
    
    public IsProjectNavigateDefaultResp isChargingItemsForJudgeDefault(IsProjectNavigateDefaultCmd cmd) {
		IsProjectNavigateDefaultResp response = new IsProjectNavigateDefaultResp();
		DSLContext context = getReadOnlyContext();
		EhPaymentChargingItemScopes t1 = Tables.EH_PAYMENT_CHARGING_ITEM_SCOPES.as("t1");
		//查询出管理公司下的默认配置
		List<PaymentChargingItemScope> defaultChargingItem = context.selectFrom(t1)
				.where(t1.OWNER_ID.eq(cmd.getNamespaceId().longValue()))
				.and(t1.OWNER_TYPE.eq(cmd.getOwnerType()))
				.and(t1.NAMESPACE_ID.eq(cmd.getNamespaceId()))
				.and(t1.CATEGORY_ID.eq(cmd.getCategoryId()))
				.and(t1.ORG_ID.eq(cmd.getOrganizationId()).or(t1.ORG_ID.eq(0L)))//标准版支持多管理公司，0L是为了兼容历史数据
				.fetchInto(PaymentChargingItemScope.class);
		//查询出该项目下的具体配置
		List<PaymentChargingItemScope> projectChargingItem = context.selectFrom(t1)
				.where(t1.OWNER_ID.eq(cmd.getOwnerId()))
				.and(t1.OWNER_TYPE.eq(cmd.getOwnerType()))
				.and(t1.NAMESPACE_ID.eq(cmd.getNamespaceId()))
				.and(t1.CATEGORY_ID.eq(cmd.getCategoryId()))
				.fetchInto(PaymentChargingItemScope.class);
		if(defaultChargingItem.size() == 0) {
			if(projectChargingItem.size() == 0) {
				//如果默认配置和项目具体配置都为空，那么是初始化状态，使用默认配置
				response.setDefaultStatus(AssetProjectDefaultFlag.DEFAULT.getCode());
			}else {
				//如果默认配置为空，项目具体配置不为空，那么该具体项目做过个性化的修改（新增）
				response.setDefaultStatus(AssetProjectDefaultFlag.PERSONAL.getCode());
			}
		}else {
			if(projectChargingItem.size() == 0) {
				//如果默认配置不为空，项目具体配置为空，那么该具体项目做过个性化的修改（删除）
				response.setDefaultStatus(AssetProjectDefaultFlag.PERSONAL.getCode());
			}else {
				//如果默认配置和项目具体配置都不为空
				Byte deCouplingFlag = 1;
				if(deCouplingFlag.equals(projectChargingItem.get(0).getDecouplingFlag())){
					response.setDefaultStatus(AssetProjectDefaultFlag.PERSONAL.getCode());
				}else {
					List<Long> defaultChargingItemId = new ArrayList<Long>(); 
	        		List<BigDecimal> defaultChargingItemTaxRate = new ArrayList<BigDecimal>(); 
	        		List<String> defaultChargingItemProjectLevelName = new ArrayList<String>(); 
	        		
	        		List<Long> projectChargingItemId = new ArrayList<Long>();
	        		List<BigDecimal> projectChargingItemTaxRate = new ArrayList<BigDecimal>(); 
	        		List<String> projectChargingItemProjectLevelName = new ArrayList<String>(); 
	        		
	        		for(PaymentChargingItemScope paymentChargingItemScope : defaultChargingItem) {
	        			defaultChargingItemId.add(paymentChargingItemScope.getChargingItemId());
	        			defaultChargingItemTaxRate.add(paymentChargingItemScope.getTaxRate());
	        			defaultChargingItemProjectLevelName.add(paymentChargingItemScope.getProjectLevelName());
	        		}
	        		for(PaymentChargingItemScope paymentChargingItemScope : projectChargingItem) {
	        			projectChargingItemId.add(paymentChargingItemScope.getChargingItemId());
	        			projectChargingItemTaxRate.add(paymentChargingItemScope.getTaxRate());
	        			projectChargingItemProjectLevelName.add(paymentChargingItemScope.getProjectLevelName());
	        		}
	        		if(defaultChargingItemId.containsAll(projectChargingItemId) 
	        				&& projectChargingItemId.containsAll(defaultChargingItemId)
	        				&& defaultChargingItemTaxRate.containsAll(projectChargingItemTaxRate) 
	        				&& projectChargingItemTaxRate.containsAll(defaultChargingItemTaxRate)
	        				&& defaultChargingItemProjectLevelName.containsAll(projectChargingItemProjectLevelName) 
	        				&& projectChargingItemProjectLevelName.containsAll(defaultChargingItemProjectLevelName)) {
	        			response.setDefaultStatus(AssetProjectDefaultFlag.DEFAULT.getCode());
	        		}else {
	        			response.setDefaultStatus(AssetProjectDefaultFlag.PERSONAL.getCode());
	        		}
				}
			}
		}
        return response;
    }
    
    public void createChargingItem(CreateChargingItemCommand cmd, List<Long> communityIds) {
    	byte de_coupling = 1;
        if(communityIds!=null && communityIds.size() >1){
        	//1、创建基础费项数据(eh_payment_charging_items)
        	PaymentChargingItem paymentChargingItem = createChargingItemForOneCommunity(cmd);
            for(int i = 0; i < communityIds.size(); i ++){
                Long cid = communityIds.get(i);
                IsProjectNavigateDefaultCmd isProjectNavigateDefaultCmd = new IsProjectNavigateDefaultCmd();
                isProjectNavigateDefaultCmd.setOwnerId(cid);
                isProjectNavigateDefaultCmd.setOwnerType("community");
                isProjectNavigateDefaultCmd.setNamespaceId(cmd.getNamespaceId());
                isProjectNavigateDefaultCmd.setCategoryId(cmd.getCategoryId());
                IsProjectNavigateDefaultResp isProjectNavigateDefaultResp = isChargingItemsForJudgeDefault(isProjectNavigateDefaultCmd);
                if(isProjectNavigateDefaultResp != null && isProjectNavigateDefaultResp.getDefaultStatus().equals((byte)1)) {
                	de_coupling = 0;
                	cmd.setOwnerId(cid);
                	//2、创建收费项配置作用域数据
                	createChargingItemScopesForOneCommunity(cmd, paymentChargingItem, de_coupling);
                }
            }
        }else{
            //只有一个园区,不是list过来的
        	//1、创建基础费项数据(eh_payment_charging_items)
        	PaymentChargingItem paymentChargingItem = createChargingItemForOneCommunity(cmd);
        	//2、创建收费项配置作用域数据
        	createChargingItemScopesForOneCommunity(cmd, paymentChargingItem, de_coupling);
        }
    }

	private PaymentChargingItem createChargingItemForOneCommunity(CreateChargingItemCommand cmd) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        //创建基础费项数据(eh_payment_charging_items)
        EhPaymentChargingItemsDao chargingItemsDao = new EhPaymentChargingItemsDao(context.configuration());
        PaymentChargingItem paymentChargingItem = new PaymentChargingItem();
        long nextSequence = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(com.everhomes.server.schema.tables.pojos.EhPaymentChargingItems.class));
        paymentChargingItem.setId(nextSequence);
        paymentChargingItem.setName(cmd.getChargingItemName());
        paymentChargingItem.setCreatorUid(UserContext.currentUserId());
        paymentChargingItem.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        paymentChargingItem.setNamespaceId(cmd.getNamespaceId());
        paymentChargingItem.setOwnerId(cmd.getOwnerId());
        paymentChargingItem.setOwnerType(cmd.getOwnerType());
        paymentChargingItem.setCategoryId(cmd.getCategoryId());
        chargingItemsDao.insert(paymentChargingItem);
        return paymentChargingItem;
	}

	private void createChargingItemScopesForOneCommunity(CreateChargingItemCommand cmd, PaymentChargingItem paymentChargingItem, Byte decouplingFlag) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        //创建收费项配置作用域数据
        EhPaymentChargingItemScopesDao chargingItemScopesDao = new EhPaymentChargingItemScopesDao(context.configuration());
        PaymentChargingItemScope scope = new PaymentChargingItemScope();
        scope.setChargingItemId(paymentChargingItem.getId());
        long scopesNextSequence = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(com.everhomes.server.schema.tables.pojos.EhPaymentChargingItemScopes.class));
        scope.setId(scopesNextSequence);
        scope.setNamespaceId(cmd.getNamespaceId());
        scope.setOwnerId(cmd.getOwnerId());
        scope.setOwnerType(cmd.getOwnerType());
        scope.setCategoryId(cmd.getCategoryId());
        scope.setProjectLevelName(cmd.getProjectLevelName());
        scope.setDecouplingFlag(decouplingFlag);
        scope.setTaxRate(cmd.getTaxRate());//增加税率
        chargingItemScopesDao.insert(scope);
	}
    
    public List<ListChargingItemsDTO> listChargingItems(String ownerType, Long ownerId, Long categoryId,Long orgId,Boolean allScope) {
        List<ListChargingItemsDTO> list = new ArrayList<>();
        DSLContext context = getReadOnlyContext();
        EhPaymentChargingItems t = Tables.EH_PAYMENT_CHARGING_ITEMS.as("t");
        EhPaymentChargingItemScopes t1 = Tables.EH_PAYMENT_CHARGING_ITEM_SCOPES.as("t1");
        List<PaymentChargingItem> items = context.selectFrom(t)
	    		.where(t.NAMESPACE_ID.isNull().or(t.NAMESPACE_ID.eq(UserContext.getCurrentNamespaceId()))) //物业缴费V6.0 收费项配置可手动新增
	    		.and(t.OWNER_ID.isNull().or(t.OWNER_ID.eq(ownerId)).or(t.OWNER_ID.eq(UserContext.getCurrentNamespaceId().longValue()))) //物业缴费V6.0 收费项配置可手动新增
	            .orderBy(t.CREATE_TIME.desc()) //物业缴费V6.0 收费项配置可手动新增
	    		.fetchInto(PaymentChargingItem.class);

        SelectConditionStep<EhPaymentChargingItemScopesRecord> step = context.selectFrom(t1)
                .where(t1.OWNER_ID.eq(ownerId))
                .and(t1.OWNER_TYPE.eq(ownerType))
                .and(t1.CATEGORY_ID.eq(categoryId));
        //标准版支持多管理公司，0L是为了兼容历史数据
        if (allScope) {
            step.and(t1.ORG_ID.eq(orgId).or(t1.ORG_ID.eq(0L)));
        }
        List<PaymentChargingItemScope> scopes = step.and(t1.NAMESPACE_ID.eq(UserContext.getCurrentNamespaceId()))
                .fetchInto(PaymentChargingItemScope.class);

        Byte isSelected = 0;
        for(int i = 0; i < items.size(); i ++){
            PaymentChargingItem item = items.get(i);
            ListChargingItemsDTO dto = new ListChargingItemsDTO();
            dto.setChargingItemName(item.getName());
            dto.setChargingItemId(item.getId());
            dto.setIsSelected(isSelected);
            for(int j = 0; j < scopes.size(); j ++){
                PaymentChargingItemScope scope = scopes.get(j);
                if(item.getId().equals(scope.getChargingItemId())){
                    isSelected = 1;
                    dto.setProjectChargingItemName(scope.getProjectLevelName());
                    dto.setIsSelected(isSelected);
                    isSelected = 0;
                    dto.setTaxRate(scope.getTaxRate());//增加税率
                }
            }
            if(dto.getTaxRate() == null) {
            	dto.setTaxRate(BigDecimal.ZERO);
            }
            list.add(dto);
        }
        return list;
    }
}