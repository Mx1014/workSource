package com.everhomes.asset.chargingitem;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.SelectConditionStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.asset.PaymentChargingItem;
import com.everhomes.asset.PaymentChargingItemScope;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.asset.AssetProjectDefaultFlag;
import com.everhomes.rest.asset.CreateChargingItemCommand;
import com.everhomes.rest.asset.IsProjectNavigateDefaultCmd;
import com.everhomes.rest.asset.IsProjectNavigateDefaultResp;
import com.everhomes.rest.asset.ListChargingItemsDTO;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.EhPaymentChargingItemScopes;
import com.everhomes.server.schema.tables.EhPaymentChargingItems;
import com.everhomes.server.schema.tables.daos.EhPaymentChargingItemScopesDao;
import com.everhomes.server.schema.tables.daos.EhPaymentChargingItemsDao;
import com.everhomes.server.schema.tables.records.EhPaymentChargingItemScopesRecord;
import com.everhomes.user.UserContext;
import com.everhomes.util.DateHelper;
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
        		List<Long> defaultChargingItemId = new ArrayList<Long>(); 
        		List<Long> projectChargingItemId = new ArrayList<Long>();
        		for(PaymentChargingItemScope paymentChargingItemScope : defaultChargingItem) {
        			defaultChargingItemId.add(paymentChargingItemScope.getChargingItemId());
        		}
        		for(PaymentChargingItemScope paymentChargingItemScope : projectChargingItem) {
        			projectChargingItemId.add(paymentChargingItemScope.getChargingItemId());
        		}
        		if(defaultChargingItemId.containsAll(projectChargingItemId) && projectChargingItemId.containsAll(defaultChargingItemId)) {
        			response.setDefaultStatus(AssetProjectDefaultFlag.DEFAULT.getCode());
        		}else {
        			response.setDefaultStatus(AssetProjectDefaultFlag.PERSONAL.getCode());
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