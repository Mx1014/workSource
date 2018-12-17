package com.everhomes.asset.group;

import com.everhomes.asset.AssetErrorCodes;
import com.everhomes.asset.AssetPaymentConstants;
import com.everhomes.asset.PaymentBillGroup;
import com.everhomes.asset.PaymentBillGroupRule;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.paySDK.pojo.PayUserDTO;
import com.everhomes.rest.asset.*;
import com.everhomes.rest.contract.ContractStatus;
import com.everhomes.rest.order.PaymentUserStatus;
import com.everhomes.rest.promotion.merchant.ListPayUsersByMerchantIdsCommand;
import com.everhomes.rest.promotion.merchant.controller.ListPayUsersByMerchantIdsRestResponse;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.*;
import com.everhomes.server.schema.tables.daos.EhPaymentBillGroupsDao;
import com.everhomes.server.schema.tables.daos.EhPaymentBillGroupsRulesDao;
import com.everhomes.server.schema.tables.records.EhPaymentBillGroupsRecord;
import com.everhomes.user.UserContext;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import org.apache.commons.collections.CollectionUtils;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.jooq.UpdateQuery;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * @author created by ycx
 * @date 下午8:42:50
 */
@Component
public class AssetGroupProviderImpl implements AssetGroupProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(AssetGroupProviderImpl.class);

    @Autowired
    private DbProvider dbProvider;
    
    @Autowired
    private SequenceProvider sequenceProvider;
 
//    @Autowired
//    private com.everhomes.paySDK.api.PayService payServiceV2;
    @Autowired
    private com.everhomes.gorder.sdk.order.GeneralOrderService payServiceV2;
    
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

    public List<ListBillGroupsDTO> listBillGroups(Long ownerId, String ownerType, Long categoryId, Long organizationId, Boolean allScope) {
        List<ListBillGroupsDTO> list = new ArrayList<>();
        //List<Long> userIds = new ArrayList<Long>();
        DSLContext context = getReadOnlyContext();
        EhPaymentBillGroups t = Tables.EH_PAYMENT_BILL_GROUPS.as("t");
        SelectQuery<Record> query = context.selectQuery();
        query.addSelect(t.ID,t.NAME,t.DEFAULT_ORDER,t.BALANCE_DATE_TYPE,t.BALANCE_DATE_TYPE,t.BILLS_DAY,
        		t.DUE_DAY,t.DUE_DAY_TYPE,t.BILLS_DAY_TYPE,t.BILLS_DAY_TYPE,t.BIZ_PAYEE_TYPE,t.BIZ_PAYEE_ID,
        		t.BILLING_CYCLE_EXPRESSION, t.BILLS_DAY_EXPRESSION, t.DUE_DAY_EXPRESSION);
        query.addFrom(t);
        query.addConditions(t.OWNER_ID.eq(ownerId));
        query.addConditions(t.OWNER_TYPE.eq(ownerType));
        if(categoryId != null){
            query.addConditions(t.CATEGORY_ID.eq(categoryId));
        }
        //标准版支持多管理公司，0L是为了兼容历史数据
        if(allScope){
            query.addConditions(t.ORG_ID.eq(organizationId).or(t.ORG_ID.eq(0L)));
        }
        query.addOrderBy(t.DEFAULT_ORDER);
        query.fetch().map(r -> {
        	ListBillGroupsDTO dto = new ListBillGroupsDTO();
            dto.setBillGroupId(r.getValue(t.ID));
            dto.setBillGroupName(r.getValue(t.NAME));
            dto.setDefaultOrder(r.getValue(t.DEFAULT_ORDER)); 
            dto.setBillingCycle(r.getValue(t.BALANCE_DATE_TYPE));
            dto.setBillingDay(r.getValue(t.BILLS_DAY));
            dto.setDueDay(r.getValue(t.DUE_DAY));
            dto.setDueDayType(r.getValue(t.DUE_DAY_TYPE));
            dto.setBillDayType(r.getValue(t.BILLS_DAY_TYPE));
            dto.setBizPayeeType(r.getValue(t.BIZ_PAYEE_TYPE));//收款方账户类型
            dto.setBizPayeeId(r.getValue(t.BIZ_PAYEE_ID));//收款方账户id
            dto.setBillingCycleExpression(r.getValue(t.BILLING_CYCLE_EXPRESSION));//添加账单周期表达式
            dto.setBillDayExpression(r.getValue(t.BILLS_DAY_EXPRESSION));//添加出账单日表达式
            dto.setDueDayExpression(r.getValue(t.DUE_DAY_EXPRESSION));//添加最晚还款日表达式
            if(r.getValue(t.BIZ_PAYEE_ID) != null) {
            	//由于收款方账户名称可能存在修改的情况，故重新请求电商
                if(LOGGER.isDebugEnabled()) {
                    LOGGER.debug("listBillGroups(request), cmd={}", r.getValue(t.BIZ_PAYEE_ID));
                }
                //List<PayUserDTO> payUserDTOs = payServiceV2.listPayUsersByIds(userIds);
                ListPayUsersByMerchantIdsCommand cmd = new ListPayUsersByMerchantIdsCommand();
        		cmd.setIds(Arrays.asList(r.getValue(t.BIZ_PAYEE_ID)));
        		ListPayUsersByMerchantIdsRestResponse resp = payServiceV2.listPayUsersByMerchantIds(cmd);
        		if(null == resp || CollectionUtils.isEmpty(resp.getResponse())) {
        			LOGGER.error("resp:"+(null == resp ? null :StringHelper.toJsonString(resp)));
        			//throw RuntimeErrorException.errorWith(PrintErrorCode.SCOPE, PrintErrorCode.ERROR_MERCHANT_ID_NOT_FOUND, "merchant id not found");
        		}else {
        			List<PayUserDTO> payUserDTOs = resp.getResponse();
                    if(LOGGER.isDebugEnabled()) {
                        LOGGER.debug("listBillGroups(response), response={}", payUserDTOs);
                    }
                    if(payUserDTOs != null && payUserDTOs.size() != 0) {
                    	PayUserDTO payUserDTO = payUserDTOs.get(0);
                    	dto.setAccountName(payUserDTO.getRemark());// 用户向支付系统注册帐号时填写的帐号名称
                    	dto.setAccountAliasName(payUserDTO.getUserAliasName());//企业名称（认证企业）
            			// 企业账户：0未审核 1审核通过  ; 个人帐户：0 未绑定手机 1 绑定手机
                        Integer registerStatus = payUserDTO.getRegisterStatus();
                        if(registerStatus != null && registerStatus.intValue() == 1) {
                        	dto.setAccountStatus(PaymentUserStatus.ACTIVE.getCode());
                        } else {
                        	dto.setAccountStatus(PaymentUserStatus.WAITING_FOR_APPROVAL.getCode());
                        }
                    }
        		}
            }
            list.add(dto);
            return null;
        });
        return list;
    }

    public Long createBillGroup(CreateBillGroupCommand cmd, byte deCouplingFlag, Long brotherGroupId, Long nextGroupId, Boolean allScope) {
        DSLContext context = getReadWriteContext();
        EhPaymentBillGroups t = Tables.EH_PAYMENT_BILL_GROUPS.as("t");
        Long nullId = null;
        if(nextGroupId == null) {
        	nextGroupId = getNextSequence(com.everhomes.server.schema.tables.pojos.EhPaymentBillGroups.class);
        }
        if(deCouplingFlag == (byte) 1){
            //添加
            InsertBillGroup(cmd, brotherGroupId, context, t, nextGroupId, allScope);
            //去解耦同伴
            context.update(t)
                    .set(t.BROTHER_GROUP_ID,nullId)
                    .where(t.OWNER_ID.eq(cmd.getOwnerId()))
                    .and(t.OWNER_TYPE.eq(cmd.getOwnerType()))
                    .and(t.CATEGORY_ID.eq(cmd.getCategoryId()))
                    .and(t.NAMESPACE_ID.eq(cmd.getNamespaceId()))
                    .execute();
            return nextGroupId;
        }else if(deCouplingFlag == (byte)0){
        	//添加
            InsertBillGroup(cmd, brotherGroupId, context, t, nextGroupId, allScope);
            return nextGroupId;
        }
        return null;
    }
    
    private void InsertBillGroup(CreateBillGroupCommand cmd, Long brotherGroupId, DSLContext context, EhPaymentBillGroups t, Long nextGroupId, Boolean allScope) {
        com.everhomes.server.schema.tables.pojos.EhPaymentBillGroups group = new PaymentBillGroup();
        group.setId(nextGroupId);
        group.setBalanceDateType(cmd.getBillingCycle());
        group.setBillsDay(cmd.getBillDay());
        group.setBillsDayType(cmd.getBillDayType());
        group.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        group.setCreatorUid(UserContext.currentUserId());
        group.setBrotherGroupId(brotherGroupId);
        Integer nextOrder = context.select(DSL.max(t.DEFAULT_ORDER))
                .from(t)
                .where(t.OWNER_ID.eq(cmd.getOwnerId()))
                .and(t.OWNER_TYPE.eq(cmd.getOwnerType()))
                .and(t.NAMESPACE_ID.eq(cmd.getNamespaceId()))
                .and(t.CATEGORY_ID.eq(cmd.getCategoryId()))
                .fetchOne(0,Integer.class);
        group.setDefaultOrder(nextOrder==null?1:nextOrder+1);
        group.setDueDay(cmd.getDueDay());
        group.setDueDayType(cmd.getDueDayType());
        group.setName(cmd.getBillGroupName());
        group.setNamespaceId(cmd.getNamespaceId());
        group.setOwnerId(cmd.getOwnerId());
        group.setOwnerType(cmd.getOwnerType());
        group.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        group.setBizPayeeId(cmd.getBizPayeeId());//增加收款方id
        group.setBizPayeeType(cmd.getBizPayeeType());//增加收款方类型
        group.setCategoryId(cmd.getCategoryId());
        group.setBillingCycleExpression(cmd.getBillingCycleExpression());
        group.setBillsDayExpression(cmd.getBillDayExpression());
        group.setDueDayExpression(cmd.getDueDayExpression());
        if(allScope) {
        	group.setOrgId(cmd.getOrganizationId());//标准版新增的管理公司ID
        }
        EhPaymentBillGroupsDao dao = new EhPaymentBillGroupsDao(context.configuration());
        dao.insert(group);
    }

    public void modifyBillGroup(ModifyBillGroupCommand cmd, List<Long> allCommunity) {
        DSLContext context = getReadWriteContext();
        EhPaymentBillGroups t = Tables.EH_PAYMENT_BILL_GROUPS.as("t");
        UpdateQuery<EhPaymentBillGroupsRecord> query = context.updateQuery(t);
        query.addValue(t.NAME, cmd.getBillGroupName());
        query.addValue(t.BILLS_DAY, cmd.getBillDay());
        query.addValue(t.BALANCE_DATE_TYPE, cmd.getBillingCycle());
        query.addValue(t.DUE_DAY_TYPE, cmd.getDueDayType());
        if (cmd.getBillingCycleExpression()!=null){
            query.addValue(t.BILLING_CYCLE_EXPRESSION,cmd.getBillingCycleExpression());
        }
        if (cmd.getBillDayExpression()!=null){
            query.addValue(t.BILLS_DAY_EXPRESSION,cmd.getBillDayExpression());
        }
        if (cmd.getDueDayExpression()!=null){
            query.addValue(t.DUE_DAY_EXPRESSION,cmd.getDueDayExpression());
        }
        if (cmd.getDueDay()!=null){
            query.addValue(t.DUE_DAY, cmd.getDueDay());
        }
        if(cmd.getBizPayeeId() != null) {
            query.addValue(t.BIZ_PAYEE_ID, Long.parseLong(cmd.getBizPayeeId()));//更新收款方账户id
        }
        query.addValue(t.BIZ_PAYEE_TYPE, cmd.getBizPayeeType());//更新收款方账户类型
        if(cmd.getBillDayType()!= null){
            query.addValue(t.BILLS_DAY_TYPE, cmd.getBillDayType());
        }

        if(cmd.getAllScope()){
            //更新账单组配置
            query.addConditions(t.ID.eq(cmd.getBillGroupId()).or(t.BROTHER_GROUP_ID.eq(cmd.getBillGroupId())));
            query.addConditions(t.OWNER_ID.in(allCommunity));//兼容标准版引入的转交项目管理权
            query.execute();
            //全部项目修改billGroup的，具体项目与其有关联关系，但是由于标准版引入了转交项目管理权，那么需要解耦已经转交项目管理权的项目
            //根据billGroupId查询出所有关联的项目（包括已经授权出去的项目）
            decouplingHistoryBillGroup(cmd.getNamespaceId(), cmd.getCategoryId(), cmd.getBillGroupId(), allCommunity);
        }else {
            //更新账单组配置
            query.addConditions(t.ID.eq(cmd.getBillGroupId()));
            query.execute();
            //具体项目修改billGroup的，此项目的所有group解耦
            Long nullId = null;
            context.update(t)
                    .set(t.BROTHER_GROUP_ID,nullId)
                    .where(t.OWNER_ID.eq(cmd.getOwnerId()))
                    .and(t.OWNER_TYPE.eq(cmd.getOwnerType()))
                    .and(t.CATEGORY_ID.eq(cmd.getCategoryId()))
                    .and(t.NAMESPACE_ID.eq(cmd.getNamespaceId()))
                    .and(t.OWNER_ID.in(allCommunity))//兼容标准版引入的转交项目管理权
                    .execute();
        }
    }


    public PaymentBillGroup getBillGroupById(Long billGroupId) {
        DSLContext context = getReadOnlyContext();
        return context.selectFrom(Tables.EH_PAYMENT_BILL_GROUPS)
                .where(Tables.EH_PAYMENT_BILL_GROUPS.ID.eq(billGroupId))
                .fetchOneInto(PaymentBillGroup.class);
    }

    public String getbillGroupNameById(Long billGroupId) {
        return dbProvider.getDslContext(AccessSpec.readOnly()).select(Tables.EH_PAYMENT_BILL_GROUPS.NAME)
                .from(Tables.EH_PAYMENT_BILL_GROUPS)
                .where(Tables.EH_PAYMENT_BILL_GROUPS.ID.eq(billGroupId))
                .fetchOne(Tables.EH_PAYMENT_BILL_GROUPS.NAME);
    }
    
    public DeleteBillGroupReponse deleteBillGroupAndRules(DeleteBillGroupCommand cmd, List<Long> allCommunity) {
        DeleteBillGroupReponse response = new DeleteBillGroupReponse();
        DSLContext context = getReadWriteContext();
        //标准版增加的allScope参数，true：默认/全部，false：具体项目
        if(cmd.getAllScope()){
        	//获取当前管理公司管理下的所有与其有继承关系的项目
            List<Long> existProjectGroupIds = context.select(Tables.EH_PAYMENT_BILL_GROUPS.ID)
                    .from(Tables.EH_PAYMENT_BILL_GROUPS)
                    .where(Tables.EH_PAYMENT_BILL_GROUPS.ID.eq(cmd.getBillGroupId())
                    		.or(Tables.EH_PAYMENT_BILL_GROUPS.BROTHER_GROUP_ID.eq(cmd.getBillGroupId())))
                    .and(Tables.EH_PAYMENT_BILL_GROUPS.OWNER_ID.in(allCommunity)) //兼容标准版引入的转交项目管理权
                    .fetch(Tables.EH_PAYMENT_BILL_GROUPS.ID);
            //校验是否可以删除
            for(Long groupId : existProjectGroupIds){
                boolean workFlag = checkBillsByBillGroupId(groupId);
                if(workFlag){
                    response.setFailCause(AssetPaymentConstants.DELTE_GROUP_UNSAFE);
                    return response;
                }
            }
            //删除“获取当前管理公司管理下的所有与其有继承关系的项目"
            for(Long groupId : existProjectGroupIds){
                this.dbProvider.execute((TransactionStatus status) -> {
                    context.delete(Tables.EH_PAYMENT_BILL_GROUPS_RULES)
                            .where(Tables.EH_PAYMENT_BILL_GROUPS_RULES.BILL_GROUP_ID.eq(groupId))
                            .and(Tables.EH_PAYMENT_BILL_GROUPS_RULES.NAMESPACE_ID.eq(cmd.getNamespaceId()))
                            .execute();

                    context.delete(Tables.EH_PAYMENT_BILL_GROUPS)
                            .where(Tables.EH_PAYMENT_BILL_GROUPS.ID.eq(groupId))
                            .and(Tables.EH_PAYMENT_BILL_GROUPS.NAMESPACE_ID.eq(cmd.getNamespaceId()))
                            .execute();
                    return null;
                });
            }
            //全部项目修改billGroup的，具体项目与其有关联关系，但是由于标准版引入了转交项目管理权，那么需要解耦已经转交项目管理权的项目
            //根据billGroupId查询出所有关联的项目（包括已经授权出去的项目）
            decouplingHistoryBillGroup(cmd.getNamespaceId(), cmd.getCategoryId(), cmd.getBillGroupId(), allCommunity);
        }else{
            boolean workFlag = checkBillsByBillGroupId(cmd.getBillGroupId());
            if(workFlag) {
                response.setFailCause(AssetPaymentConstants.DELTE_GROUP_UNSAFE);
                return response;
            }
            //删除会导致其他同伴解耦，解决issue-32616:删除从全部继承过来的账单组，具体显示还是显示了“注：该项目使用默认配置”文案
            Long nullId = null;
            this.dbProvider.execute((TransactionStatus status) -> {
                context.delete(Tables.EH_PAYMENT_BILL_GROUPS_RULES)
                        .where(Tables.EH_PAYMENT_BILL_GROUPS_RULES.BILL_GROUP_ID.eq(cmd.getBillGroupId()))
                        .execute();
                context.delete(Tables.EH_PAYMENT_BILL_GROUPS)
                        .where(Tables.EH_PAYMENT_BILL_GROUPS.ID.eq(cmd.getBillGroupId()))
                        .execute();
                //解耦
                context.update(Tables.EH_PAYMENT_BILL_GROUPS)
                        .set(Tables.EH_PAYMENT_BILL_GROUPS.BROTHER_GROUP_ID,nullId)
                        .where(Tables.EH_PAYMENT_BILL_GROUPS.OWNER_ID.eq(cmd.getOwnerId()))
                        .and(Tables.EH_PAYMENT_BILL_GROUPS.OWNER_TYPE.eq(cmd.getOwnerType()))
                        .and(Tables.EH_PAYMENT_BILL_GROUPS.CATEGORY_ID.eq(cmd.getCategoryId()))
                        .and(Tables.EH_PAYMENT_BILL_GROUPS.NAMESPACE_ID.eq(cmd.getNamespaceId()))
                        .execute();
                return null;
            });
        }
        return response;
    }
    
    /**
     * 标准版：为了兼容标准版引入的转交项目管理权
     * 全部项目修改billGroup的，具体项目与其有关联关系，但是由于标准版引入了转交项目管理权，那么需要解耦已经转交项目管理权的项目
     */
    public void decouplingHistoryBillGroup(Integer namespaceId, Long categoryId, Long billGroupId, List<Long> allCommunity) {
    	DSLContext context = getReadWriteContext();
    	EhPaymentBillGroups t = Tables.EH_PAYMENT_BILL_GROUPS.as("t");
    	List<Long> decouplingOwnerId = context.select(t.OWNER_ID)
        		.from(t)
                .where(t.NAMESPACE_ID.eq(namespaceId))
                .and(t.CATEGORY_ID.eq(categoryId))
                .and(t.BROTHER_GROUP_ID.eq(billGroupId))
                .and(t.OWNER_ID.notIn(allCommunity))//为了兼容标准版引入的转交项目管理权
                .fetch(t.OWNER_ID);
        if(decouplingOwnerId != null && decouplingOwnerId.size() != 0) {
        	Long nullId = null;
            context.update(t)
                    .set(t.BROTHER_GROUP_ID,nullId)
                    .where(t.OWNER_ID.in(decouplingOwnerId))
                    .execute();
        }
    }
    
    public boolean checkBillsByBillGroupId(Long billGroupId) {
        DSLContext context = getReadOnlyContext();
        List<PaymentBillGroupRule> rules = context.selectFrom(Tables.EH_PAYMENT_BILL_GROUPS_RULES)
                .where(Tables.EH_PAYMENT_BILL_GROUPS_RULES.BILL_GROUP_ID.eq(billGroupId))
                .fetchInto(PaymentBillGroupRule.class);
        for(int i = 0; i < rules.size(); i ++) {
            boolean inWorkGroupRule = isInWorkGroupRule(rules.get(i));
            if(inWorkGroupRule){
                return true;
            }
        }
        return  false;
    }
    
    public boolean isInWorkGroupRule(com.everhomes.server.schema.tables.pojos.EhPaymentBillGroupsRules rule) {
        // todo change the way to examine bill group validation, if not bill and not valid contract, work flag should be false
        boolean workFlag = true;
        DSLContext context = getReadOnlyContext();
        //issue-35885 【物业缴费6.3】存在一个待发起合同，关联“账单组A”，删除后，修改合同修改计价条款页面，添加免租计划，显示数字
        //只要关联了合同（包括草稿合同）就不能删除账单组，也不能修改其中的费项。
        //看是否关联了合同
        EhContractChargingItems t = Tables.EH_CONTRACT_CHARGING_ITEMS.as("t");
        EhContracts t2 = Tables.EH_CONTRACTS.as("t2");
        List<Long> fetch1 = context.select(t.CONTRACT_ID)
              .from(t,t2)
              .where(t.NAMESPACE_ID.eq(rule.getNamespaceId()))
              .and(t.BILL_GROUP_ID.eq(rule.getBillGroupId()))
              .and(t.CHARGING_ITEM_ID.eq(rule.getChargingItemId()))
              //issue-36219 修复“【物业缴费6.3】当把账单组关联的合同全部删除掉时，账单组还是不能删除”，如果合同处于已删除状态，合同展示列表不会展示，此时账单组应该可以删除
              .and(t.CONTRACT_ID.eq(t2.ID))
              .and(t2.STATUS.notEqual(ContractStatus.INACTIVE.getCode()))
              .fetch(t.CONTRACT_ID);
        if(fetch1.size()>0){
        	return true;
        }else {
        	workFlag = false;
        }
        //先看是否产生了账单
        EhPaymentBills bills = Tables.EH_PAYMENT_BILLS.as("bills");
        List<Long> fetch = context.select(bills.ID)
                .from(bills)
                .where(bills.BILL_GROUP_ID.eq(rule.getBillGroupId()))
                .and(bills.SWITCH.notEqual((byte)3))
                //todo 限定条件应该排除已经缴纳的账单，但已经缴纳的账单的需要增加一个账单组历史名称,且兼容历史数据，即数据迁移
                .and(bills.DELETE_FLAG.eq(AssetPaymentBillDeleteFlag.VALID.getCode()))//物业缴费V6.0 账单、费项表增加是否删除状态字段
                .fetch(bills.ID);
        if(fetch.size()>0){
            workFlag = true;
        }else{
            workFlag = false;
        }
        return workFlag;
    }
    
    public Long addOrModifyRuleForBillGroup(AddOrModifyRuleForBillGroupCommand cmd,Long brotherRuleId,byte deCouplingFlag) {
        EhPaymentBillGroupsRules t = Tables.EH_PAYMENT_BILL_GROUPS_RULES.as("t");
        EhPaymentBillGroups group = Tables.EH_PAYMENT_BILL_GROUPS.as("group");
        DSLContext readOnlyContext = getReadOnlyContext();
        DSLContext writeContext = getReadWriteContext();
        EhPaymentBillGroupsRulesDao dao = new EhPaymentBillGroupsRulesDao(writeContext.configuration());
        //先获得cateogryId
        Long categoryId = readOnlyContext.select(group.CATEGORY_ID).from(group).where(group.ID.eq(cmd.getBillGroupId()))
                .fetchOne(group.CATEGORY_ID);
        if(deCouplingFlag == (byte) 0){
            //耦合中
            return insertOrUpdateBillGroupRule(cmd, brotherRuleId, t, readOnlyContext, dao,deCouplingFlag, categoryId);
        }else if(deCouplingFlag == (byte) 1){
            //解耦合
            insertOrUpdateBillGroupRule(cmd, brotherRuleId, t, readOnlyContext, dao,deCouplingFlag, categoryId);
            Long nullId = null;
            writeContext.update(t)
                    .set(t.BROTHER_RULE_ID,nullId)
                    .where(t.OWNERID.eq(cmd.getOwnerId()))
                    .and(t.OWNERTYPE.eq(cmd.getOwnerType()))
                    .execute();
            writeContext.update(group)
                    .set(group.BROTHER_GROUP_ID,nullId)
                    .where(group.OWNER_ID.eq(cmd.getOwnerId()))
                    .and(group.OWNER_TYPE.eq(cmd.getOwnerType()))
                    .execute();
        }
        return null;
    }
    
    private Long insertOrUpdateBillGroupRule(AddOrModifyRuleForBillGroupCommand cmd, Long brotherRuleId, EhPaymentBillGroupsRules t, DSLContext readOnlyContext, EhPaymentBillGroupsRulesDao dao,byte deCouplingFlag, Long categoryId) {
        Long ruleId = cmd.getBillGroupRuleId();
        com.everhomes.server.schema.tables.pojos.EhPaymentBillGroupsRules rule = new PaymentBillGroupRule();
        if(ruleId == null){
        	List<Long> chargingItemIdList = readOnlyContext.select(t.CHARGING_ITEM_ID).from(t)
                    .where(t.OWNERID.eq(cmd.getOwnerId()))
                    .and(t.OWNERTYPE.eq(cmd.getOwnerType()))
                    .and(t.BILL_GROUP_ID.eq(cmd.getBillGroupId()))
                    .fetch(t.CHARGING_ITEM_ID);
        	if(chargingItemIdList.contains(cmd.getChargingItemId())){
	            throw RuntimeErrorException.errorWith(AssetErrorCodes.SCOPE,AssetErrorCodes.GROUP_UNIQUE_BILL_ITEM_CHECK,"a bill item can only exists in one bill group for a specific community");
	        }

            //新增 一条billGroupRule
            long nextRuleId = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(com.everhomes.server.schema.tables.pojos.EhPaymentBillGroupsRules.class));
            ruleId = nextRuleId;
            Long billGroupId = cmd.getBillGroupId();
            //拿到正确的billGroupId
            if(deCouplingFlag == 0 && cmd.getOwnerId().intValue() != cmd.getNamespaceId().intValue()){
                List<Long> groupIds = readOnlyContext.select(Tables.EH_PAYMENT_BILL_GROUPS.ID)
                        .from(Tables.EH_PAYMENT_BILL_GROUPS)
                        .where(Tables.EH_PAYMENT_BILL_GROUPS.BROTHER_GROUP_ID.eq(cmd.getBillGroupId()))
                        .and(Tables.EH_PAYMENT_BILL_GROUPS.OWNER_TYPE.eq(cmd.getOwnerType()))
                        .and(Tables.EH_PAYMENT_BILL_GROUPS.OWNER_ID.eq(cmd.getOwnerId()))
                        .fetch(Tables.EH_PAYMENT_BILL_GROUPS.ID);
                if(groupIds.size()<0 || groupIds.size() > 1){
                    LOGGER.error("add group rule for coupled communities, "+groupIds.size()+" brother groups are found, not one which leads to a violation to the law here");
                    return null;
                }
                billGroupId = groupIds.get(0);
            }
            rule.setId(nextRuleId);
            rule.setBillGroupId(billGroupId);
            rule.setChargingItemId(cmd.getChargingItemId());
            rule.setChargingItemName(cmd.getGroupChargingItemName());
            rule.setChargingStandardsId(cmd.getChargingStandardId());
            rule.setNamespaceId(cmd.getNamespaceId());
            rule.setOwnerid(cmd.getOwnerId());
            rule.setOwnertype(cmd.getOwnerType());
            rule.setBillItemMonthOffset(cmd.getBillItemMonthOffset());
            rule.setBillItemDayOffset(cmd.getBillItemDayOffset());
            rule.setBrotherRuleId(brotherRuleId);
            dao.insert(rule);
        }else{
            //拿到正确的rule
            if(cmd.getOwnerId().longValue() == cmd.getNamespaceId().longValue() || deCouplingFlag == 1){
                rule = readOnlyContext.selectFrom(t)
                        .where(t.ID.eq(ruleId))
                        .fetchOneInto(PaymentBillGroupRule.class);
            }else{
                rule = readOnlyContext.selectFrom(t)
                        .where(t.OWNERID.eq(cmd.getOwnerId()))
                        .and(t.OWNERTYPE.eq(cmd.getOwnerType()))
                        .and(t.BROTHER_RULE_ID.eq(ruleId))
                        .fetchOneInto(PaymentBillGroupRule.class);
            }
            boolean workFlag = isInWorkGroupRule(rule);
            if(workFlag){
                throw RuntimeErrorException.errorWith(AssetErrorCodes.SCOPE,AssetErrorCodes.CHANGE_SAFE_CHECK,"object is on work, modify or delete is not allowed.");
            }
            //如果没有关联则不修改
            rule.setChargingItemId(cmd.getChargingItemId());
            rule.setBillItemMonthOffset(cmd.getBillItemMonthOffset());
            rule.setBillItemDayOffset(cmd.getBillItemDayOffset());
            rule.setBrotherRuleId(brotherRuleId);
            dao.update(rule);
        }
        return ruleId;
    }
    
    @SuppressWarnings("unused")
    public DeleteChargingItemForBillGroupResponse deleteBillGroupRuleById(Long billGroupRuleId,byte deCouplingFlag) {
        DeleteChargingItemForBillGroupResponse response = new DeleteChargingItemForBillGroupResponse();
        DSLContext context = getReadWriteContext();
        if(deCouplingFlag == 0){
            List<com.everhomes.server.schema.tables.pojos.EhPaymentBillGroupsRules> rules = context.selectFrom(Tables.EH_PAYMENT_BILL_GROUPS_RULES)
                    .where(Tables.EH_PAYMENT_BILL_GROUPS_RULES.ID.eq(billGroupRuleId))
                    .or(Tables.EH_PAYMENT_BILL_GROUPS_RULES.BROTHER_RULE_ID.eq(billGroupRuleId))
                    .fetchInto(com.everhomes.server.schema.tables.pojos.EhPaymentBillGroupsRules.class);
            rules:for(int i = 0; i < rules.size(); i ++){
                boolean workFlag = isInWorkGroupRule(rules.get(i));
                if(workFlag){
                	response.setFailCause(AssetPaymentConstants.DELETE_GROUP_RULE_UNSAFE);
                    return response;
                }
            }
            rules:for(int i = 0; i < rules.size(); i ++){
                context.delete(Tables.EH_PAYMENT_BILL_GROUPS_RULES)
                        .where(Tables.EH_PAYMENT_BILL_GROUPS_RULES.ID.eq(rules.get(i).getId()))
                        .execute();
            }
        }else{
            com.everhomes.server.schema.tables.pojos.EhPaymentBillGroupsRules rule = findBillGroupRuleById(billGroupRuleId);
            boolean workFlag = isInWorkGroupRule(rule);
            if(workFlag){
                response.setFailCause(AssetPaymentConstants.DELETE_GROUP_RULE_UNSAFE);
                return response;
            }
            context.delete(Tables.EH_PAYMENT_BILL_GROUPS_RULES)
                    .where(Tables.EH_PAYMENT_BILL_GROUPS_RULES.ID.eq(billGroupRuleId))
                    .execute();
        }
        return response;
    }

    public com.everhomes.server.schema.tables.pojos.EhPaymentBillGroupsRules findBillGroupRuleById(Long billGroupRuleId) {
        DSLContext context = getReadOnlyContext();
        return context.selectFrom(Tables.EH_PAYMENT_BILL_GROUPS_RULES)
                .where(Tables.EH_PAYMENT_BILL_GROUPS_RULES.ID.eq(billGroupRuleId))
                .fetchOneInto(PaymentBillGroupRule.class);
    }
    
    public IsProjectNavigateDefaultResp isBillGroupsForJudgeDefault(IsProjectNavigateDefaultCmd cmd) {
		IsProjectNavigateDefaultResp response = new IsProjectNavigateDefaultResp();
		DSLContext context = getReadOnlyContext();
        EhPaymentBillGroups t1 = Tables.EH_PAYMENT_BILL_GROUPS.as("t1");
        //查询出管理公司下的默认配置
        List<PaymentBillGroup> defaultBillGroup = context.selectFrom(t1)
        		.where(t1.OWNER_ID.eq(cmd.getNamespaceId().longValue()))
                .and(t1.OWNER_TYPE.eq(cmd.getOwnerType()))
                .and(t1.NAMESPACE_ID.eq(cmd.getNamespaceId()))
                .and(t1.CATEGORY_ID.eq(cmd.getCategoryId()))
                .and(t1.ORG_ID.eq(cmd.getOrganizationId()).or(t1.ORG_ID.eq(0L)))//标准版支持多管理公司，0L是为了兼容历史数据
                .fetchInto(PaymentBillGroup.class);
        //查询出该项目下的具体配置
        List<PaymentBillGroup> projectBillGroup = context.selectFrom(t1)
        		.where(t1.OWNER_ID.eq(cmd.getOwnerId()))
                .and(t1.OWNER_TYPE.eq(cmd.getOwnerType()))
                .and(t1.NAMESPACE_ID.eq(cmd.getNamespaceId()))
                .and(t1.CATEGORY_ID.eq(cmd.getCategoryId()))
                .fetchInto(PaymentBillGroup.class);
        if(defaultBillGroup.size() == 0) {
        	if(projectBillGroup.size() == 0) {
        		//如果默认配置和项目具体配置都为空，那么是初始化状态，使用默认配置
        		response.setDefaultStatus(AssetProjectDefaultFlag.DEFAULT.getCode());
        	}else {
        		//如果默认配置为空，项目具体配置不为空，那么该具体项目做过个性化的修改（新增）
        		response.setDefaultStatus(AssetProjectDefaultFlag.PERSONAL.getCode());
        	}
        }else {
        	if(projectBillGroup.size() == 0) {
        		//如果默认配置不为空，项目具体配置为空，那么该具体项目做过个性化的修改（删除）
        		response.setDefaultStatus(AssetProjectDefaultFlag.PERSONAL.getCode());
        	}else {
        		//如果默认配置和项目具体配置都不为空
        		List<Long> defaultBillGroupId = new ArrayList<Long>(); 
        		List<Long> projectBrotherGroupId = new ArrayList<Long>();
        		for(PaymentBillGroup paymentBillGroup : defaultBillGroup) {
        			defaultBillGroupId.add(paymentBillGroup.getId());
        		}
        		for(PaymentBillGroup paymentBillGroup : projectBillGroup) {
        			projectBrotherGroupId.add(paymentBillGroup.getBrotherGroupId());
        		}
        		if(defaultBillGroupId.containsAll(projectBrotherGroupId) && projectBrotherGroupId.containsAll(defaultBillGroupId)) {
        			response.setDefaultStatus(AssetProjectDefaultFlag.DEFAULT.getCode());
        		}else {
        			response.setDefaultStatus(AssetProjectDefaultFlag.PERSONAL.getCode());
        		}
        	}
        }
        return response;
	}

}