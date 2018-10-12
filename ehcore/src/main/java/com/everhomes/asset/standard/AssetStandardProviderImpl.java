package com.everhomes.asset.standard;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

import com.everhomes.asset.AssetErrorCodes;
import com.everhomes.asset.AssetPaymentConstants;
import com.everhomes.asset.PaymentBillGroup;
import com.everhomes.asset.PaymentBillGroupRule;
import com.everhomes.asset.PaymentChargingItemScope;
import com.everhomes.asset.PaymentChargingStandards;
import com.everhomes.asset.PaymentChargingStandardsScopes;
import com.everhomes.asset.PaymentFormula;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.paySDK.pojo.PayUserDTO;
import com.everhomes.rest.asset.AddOrModifyRuleForBillGroupCommand;
import com.everhomes.rest.asset.AssetPaymentBillDeleteFlag;
import com.everhomes.rest.asset.AssetProjectDefaultFlag;
import com.everhomes.rest.asset.CreateBillGroupCommand;
import com.everhomes.rest.asset.DeleteBillGroupCommand;
import com.everhomes.rest.asset.DeleteBillGroupReponse;
import com.everhomes.rest.asset.DeleteChargingItemForBillGroupResponse;
import com.everhomes.rest.asset.IsProjectNavigateDefaultCmd;
import com.everhomes.rest.asset.IsProjectNavigateDefaultResp;
import com.everhomes.rest.asset.ListBillGroupsDTO;
import com.everhomes.rest.asset.ListChargingStandardsCommand;
import com.everhomes.rest.asset.ListChargingStandardsDTO;
import com.everhomes.rest.asset.ModifyBillGroupCommand;
import com.everhomes.rest.contract.ContractStatus;
import com.everhomes.rest.order.PaymentUserStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.EhContractChargingItems;
import com.everhomes.server.schema.tables.EhContracts;
import com.everhomes.server.schema.tables.EhPaymentBillGroups;
import com.everhomes.server.schema.tables.EhPaymentBillGroupsRules;
import com.everhomes.server.schema.tables.EhPaymentBills;
import com.everhomes.server.schema.tables.EhPaymentChargingItemScopes;
import com.everhomes.server.schema.tables.EhPaymentChargingStandards;
import com.everhomes.server.schema.tables.EhPaymentChargingStandardsScopes;
import com.everhomes.server.schema.tables.daos.EhPaymentBillGroupsDao;
import com.everhomes.server.schema.tables.daos.EhPaymentBillGroupsRulesDao;
import com.everhomes.server.schema.tables.records.EhPaymentBillGroupsRecord;
import com.everhomes.user.UserContext;
import com.everhomes.util.DateHelper;
import com.everhomes.util.IntegerUtil;
import com.everhomes.util.RuntimeErrorException;
/**
 * @author created by ycx
 * @date 下午8:42:50
 */
@Component
public class AssetStandardProviderImpl implements AssetStandardProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(AssetStandardProviderImpl.class);

    @Autowired
    private DbProvider dbProvider;
    
    @Autowired
    private SequenceProvider sequenceProvider;
 
    @Autowired
    private com.everhomes.paySDK.api.PayService payServiceV2;
    
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
    
    public List<ListChargingStandardsDTO> listOnlyChargingStandards(ListChargingStandardsCommand cmd) {
        List<ListChargingStandardsDTO> list = new ArrayList<>();
        DSLContext context = getReadOnlyContext();
        EhPaymentChargingStandardsScopes t1 = Tables.EH_PAYMENT_CHARGING_STANDARDS_SCOPES.as("t1");
        EhPaymentChargingStandards t = Tables.EH_PAYMENT_CHARGING_STANDARDS.as("t");
        SelectQuery<Record> query = context.selectQuery();
        query.addSelect(t.BILLING_CYCLE,t.ID,t.NAME,t.FORMULA,t.FORMULA_TYPE,t.SUGGEST_UNIT_PRICE,t.AREA_SIZE_TYPE, t.PRICE_UNIT_TYPE);
        query.addFrom(t,t1);
        query.addConditions(t.CHARGING_ITEMS_ID.eq(cmd.getChargingItemId()));
        query.addConditions(t1.CHARGING_STANDARD_ID.eq(t.ID));
        query.addConditions(t1.OWNER_ID.eq(cmd.getOwnerId()));
        query.addConditions(t1.OWNER_TYPE.eq(cmd.getOwnerType()));
        //add category constraint
        query.addConditions(t1.CATEGORY_ID.eq(cmd.getCategoryId()));
        query.addConditions(t1.NAMESPACE_ID.eq(cmd.getNamespaceId()));
        //标准版支持多管理公司，0L是为了兼容历史数据
        if(cmd.getAllScope()){
            query.addConditions(t1.ORG_ID.eq(cmd.getOrganizationId()).or(t1.ORG_ID.eq(0L)));
        }
        query.addLimit(cmd.getPageAnchor().intValue(),cmd.getPageSize()+1);
        query.fetch().map(r -> {
            ListChargingStandardsDTO dto = new ListChargingStandardsDTO();
            dto.setBillingCycle(r.getValue(t.BILLING_CYCLE));
            dto.setChargingStandardId(r.getValue(t.ID));
            dto.setChargingStandardName(r.getValue(t.NAME));
            dto.setFormula(r.getValue(t.FORMULA));
            dto.setFormulaType(r.getValue(t.FORMULA_TYPE));
            dto.setSuggestUnitPrice(r.getValue(t.SUGGEST_UNIT_PRICE));
            dto.setAreaSizeType(r.getValue(t.AREA_SIZE_TYPE));
            Byte priceUnitType = r.getValue(t.PRICE_UNIT_TYPE);
            if(priceUnitType != null){
                //fixed as using unit price of day
                dto.setUseUnitPrice((byte)1);
            }else{
                dto.setUseUnitPrice((byte)0);
            }
            list.add(dto);
            return null;
        });
        for(int i = 0; i < list.size(); i ++){
            ListChargingStandardsDTO dto = list.get(i);
            Long chargingStandardId = dto.getChargingStandardId();
            //获得标准
            com.everhomes.server.schema.tables.pojos.EhPaymentChargingStandards standard = findChargingStandardById(chargingStandardId);
            Set<String> varIdens = new HashSet<>();
            //获得formula的额外内容
            List<PaymentFormula> formulaCondition = null;
            if(standard.getFormulaType()==3 || standard.getFormulaType() == 4){
                formulaCondition = getFormulas(standard.getId());
                for(int m = 0; m < formulaCondition.size(); m ++){
                    varIdens.add(findVariableByIden(formulaCondition.get(m).getConstraintVariableIdentifer()).getName());
                }
            }
            //获得standard公式
            String formula = null;
            if(standard.getFormulaType()==1 || standard.getFormulaType() == 2){
                formulaCondition = getFormulas(standard.getId());
                if(formulaCondition!=null){
                    if(formulaCondition.size()>1){
                        LOGGER.error("普通公式的标准的id为"+standard.getId()+",对应了"+formulaCondition.size()+"条公式!");
                    }
                    PaymentFormula paymentFormula = formulaCondition.get(0);
                    formula = paymentFormula.getFormulaJson();
                }else{
                    throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_INVALID_PARAMETER,"找不到公式,标准的id为"+standard.getId()+"");
                }
            }
            if(formula!=null){
                char[] formularChars = formula.toCharArray();
                int index = 0;
                int start = 0;
                while(index < formularChars.length){
                    if(formularChars[index]=='+'||formularChars[index]=='-'||formularChars[index]=='*'||formularChars[index]=='/'||index == formularChars.length-1){
                        String var = formula.substring(start,index==formula.length()-1?index+1:index);
                        if(!IntegerUtil.hasDigit(var)){
                            varIdens.add(findVariableByIden(var).getName());
                        }
                        start = index+1;
                    }
                    index++;
                }

            }
            dto.setVariableNames(new ArrayList<>(varIdens));
        }
        return list;
    }
    
    public com.everhomes.server.schema.tables.pojos.EhPaymentChargingStandards findChargingStandardById(Long chargingStandardId) {
        DSLContext context = getReadOnlyContext();
        return context.selectFrom(Tables.EH_PAYMENT_CHARGING_STANDARDS)
                .where(Tables.EH_PAYMENT_CHARGING_STANDARDS.ID.eq(chargingStandardId))
                .fetchOneInto(PaymentChargingStandards.class);
    }
    
    public List<PaymentFormula> getFormulas(Long id) {
        DSLContext context = getReadOnlyContext();
        return context.selectFrom(Tables.EH_PAYMENT_FORMULA)
                .where(Tables.EH_PAYMENT_FORMULA.CHARGING_STANDARD_ID.eq(id))
                .fetchInto(PaymentFormula.class);
    }
    
    private com.everhomes.server.schema.tables.pojos.EhPaymentVariables findVariableByIden(String iden) {
        DSLContext context = getReadOnlyContext();
        return context.selectFrom(Tables.EH_PAYMENT_VARIABLES)
                .where(Tables.EH_PAYMENT_VARIABLES.IDENTIFIER.eq(iden))
                .fetchOneInto(com.everhomes.server.schema.tables.pojos.EhPaymentVariables.class);
    }
    
    public IsProjectNavigateDefaultResp isChargingStandardsForJudgeDefault(IsProjectNavigateDefaultCmd cmd) {
		IsProjectNavigateDefaultResp response = new IsProjectNavigateDefaultResp();
		DSLContext context = getReadOnlyContext();
		EhPaymentChargingItemScopes t = Tables.EH_PAYMENT_CHARGING_ITEM_SCOPES.as("t");
		List<PaymentChargingItemScope> itemScopes = context.selectFrom(t)
                .where(t.OWNER_ID.eq(cmd.getOwnerId()))
                .and(t.OWNER_TYPE.eq(cmd.getOwnerType()))
                .and(t.CATEGORY_ID.eq(cmd.getCategoryId()))
                .and(t.NAMESPACE_ID.eq(cmd.getNamespaceId()))
                .fetchInto(PaymentChargingItemScope.class);
		if(itemScopes != null && itemScopes.size() == 0) {
			response.setDefaultStatus((byte)0);//收费项标准Tab页在还没选费项的前提下，不展示使用默认配置
		}else {
			EhPaymentChargingStandardsScopes t1 = Tables.EH_PAYMENT_CHARGING_STANDARDS_SCOPES.as("t1");
	        List<PaymentChargingStandardsScopes> scopes = context.selectFrom(t1)
	        		.where(t1.NAMESPACE_ID.eq(cmd.getNamespaceId()))
	        		.and(t1.CATEGORY_ID.eq(cmd.getCategoryId()))
	                .fetchInto(PaymentChargingStandardsScopes.class);
	        if(scopes != null && scopes.size() == 0) {//判断是否是初始化的时候，初始化的时候全部里面没配置、项目也没配置，该域空间下没有数据
	        	response.setDefaultStatus((byte)1);//1：代表使用的是默认配置，0：代表有做过个性化的修改
	        }else {//说明该域空间下已经有数据了
	        	scopes = context.selectFrom(t1)
	            		.where(t1.OWNER_ID.eq(cmd.getOwnerId()))
	                    .and(t1.OWNER_TYPE.eq(cmd.getOwnerType()))
	                    .and(t1.NAMESPACE_ID.eq(cmd.getNamespaceId()))
	                    .and(t1.CATEGORY_ID.eq(cmd.getCategoryId()))
	                    .fetchInto(PaymentChargingStandardsScopes.class);
	        	if(scopes.size() > 0 && scopes.get(0).getBrotherStandardId() != null) {
	        		response.setDefaultStatus((byte)1);//1：代表使用的是默认配置，0：代表有做过个性化的修改
	        	}else {
	        		scopes = context.selectFrom(t1)
		            		.where(t1.OWNER_ID.eq(cmd.getNamespaceId().longValue()))//如果默认配置有配置，那么说明具体项目有做删除操作
		                    .and(t1.OWNER_TYPE.eq(cmd.getOwnerType()))
		                    .and(t1.NAMESPACE_ID.eq(cmd.getNamespaceId()))
		                    .and(t1.CATEGORY_ID.eq(cmd.getCategoryId()))
		                    .fetchInto(PaymentChargingStandardsScopes.class);
	        		if(scopes.size() > 0) {
            			response.setDefaultStatus((byte)0);//1：代表使用的是默认配置，0：代表有做过个性化的修改
            		}else {
            			response.setDefaultStatus((byte)1);//1：代表使用的是默认配置，0：代表有做过个性化的修改
            		}
	        	}
	        }
		}
        return response;
	}
    
    
}