package com.everhomes.asset.standard;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.elasticsearch.common.cli.CliToolConfig.Cmd;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.jooq.UpdateQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import com.everhomes.asset.AssetErrorCodes;
import com.everhomes.asset.AssetPaymentConstants;
import com.everhomes.asset.AssetProvider;
import com.everhomes.asset.PaymentChargingItemScope;
import com.everhomes.asset.PaymentChargingStandards;
import com.everhomes.asset.PaymentChargingStandardsScopes;
import com.everhomes.asset.PaymentFormula;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.asset.AssetProjectDefaultFlag;
import com.everhomes.rest.asset.DeleteChargingStandardCommand;
import com.everhomes.rest.asset.IsProjectNavigateDefaultCmd;
import com.everhomes.rest.asset.IsProjectNavigateDefaultResp;
import com.everhomes.rest.asset.ListChargingStandardsCommand;
import com.everhomes.rest.asset.ListChargingStandardsDTO;
import com.everhomes.rest.asset.ModifyChargingStandardCommand;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.EhPaymentBillGroups;
import com.everhomes.server.schema.tables.EhPaymentChargingItemScopes;
import com.everhomes.server.schema.tables.EhPaymentChargingStandards;
import com.everhomes.server.schema.tables.EhPaymentChargingStandardsScopes;
import com.everhomes.server.schema.tables.records.EhPaymentChargingStandardsRecord;
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
    
    @Autowired
    private AssetProvider assetProvider;
    
    private static EhPaymentChargingStandards standard = Tables.EH_PAYMENT_CHARGING_STANDARDS.as("standard");
    
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
			//收费项标准Tab页在还没选费项的前提下，不展示使用默认配置
			response.setDefaultStatus(AssetProjectDefaultFlag.PERSONAL.getCode());
		}else {
			EhPaymentChargingStandardsScopes t1 = Tables.EH_PAYMENT_CHARGING_STANDARDS_SCOPES.as("t1");
			//查询出管理公司下的默认配置
	        List<PaymentChargingStandardsScopes> defaultStandard = context.selectFrom(t1)
	        		.where(t1.OWNER_ID.eq(cmd.getNamespaceId().longValue()))
	                .and(t1.OWNER_TYPE.eq(cmd.getOwnerType()))
	                .and(t1.NAMESPACE_ID.eq(cmd.getNamespaceId()))
	                .and(t1.CATEGORY_ID.eq(cmd.getCategoryId()))
	                .and(t1.ORG_ID.eq(cmd.getOrganizationId()).or(t1.ORG_ID.eq(0L)))//标准版支持多管理公司，0L是为了兼容历史数据
	                .fetchInto(PaymentChargingStandardsScopes.class);
	        //查询出该项目下的具体配置
	        List<PaymentChargingStandardsScopes> projectStandard = context.selectFrom(t1)
	        		.where(t1.OWNER_ID.eq(cmd.getOwnerId()))
	                .and(t1.OWNER_TYPE.eq(cmd.getOwnerType()))
	                .and(t1.NAMESPACE_ID.eq(cmd.getNamespaceId()))
	                .and(t1.CATEGORY_ID.eq(cmd.getCategoryId()))
	                .fetchInto(PaymentChargingStandardsScopes.class);
	        if(defaultStandard.size() == 0) {
	        	if(projectStandard.size() == 0) {
	        		//如果默认配置和项目具体配置都为空，那么是初始化状态，使用默认配置
	        		response.setDefaultStatus(AssetProjectDefaultFlag.DEFAULT.getCode());
	        	}else {
	        		//如果默认配置为空，项目具体配置不为空，那么该具体项目做过个性化的修改（新增）
	        		response.setDefaultStatus(AssetProjectDefaultFlag.PERSONAL.getCode());
	        	}
	        }else {
	        	if(projectStandard.size() == 0) {
	        		//如果默认配置不为空，项目具体配置为空，那么该具体项目做过个性化的修改（删除）
	        		response.setDefaultStatus(AssetProjectDefaultFlag.PERSONAL.getCode());
	        	}else {
	        		//如果默认配置和项目具体配置都不为空
	        		List<Long> defaultStandardId = new ArrayList<Long>(); 
	        		List<Long> projectBrotherStandardId = new ArrayList<Long>();
	        		for(PaymentChargingStandardsScopes standard : defaultStandard) {
	        			defaultStandardId.add(standard.getChargingStandardId());
	        		}
	        		for(PaymentChargingStandardsScopes standard : projectStandard) {
	        			projectBrotherStandardId.add(standard.getBrotherStandardId());
	        		}
	        		if(defaultStandardId.containsAll(projectBrotherStandardId) && projectBrotherStandardId.containsAll(defaultStandardId)) {
	        			response.setDefaultStatus(AssetProjectDefaultFlag.DEFAULT.getCode());
	        		}else {
	        			response.setDefaultStatus(AssetProjectDefaultFlag.PERSONAL.getCode());
	        		}
	        	}
	        }
		}
        return response;
	}
    
    public void modifyChargingStandard(ModifyChargingStandardCommand cmd, List<Long> allCommunity) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
        EhPaymentChargingStandards t = Tables.EH_PAYMENT_CHARGING_STANDARDS.as("t");
        EhPaymentChargingStandardsScopes standardScope = Tables.EH_PAYMENT_CHARGING_STANDARDS_SCOPES.as("standardScope");
        if(cmd.getAllScope()){
        	//bro和本人是 chargingStandardId的进行修改
            List<Long> fetch = context.select(standardScope.CHARGING_STANDARD_ID)
                    .from(standardScope)
                    .where(standardScope.BROTHER_STANDARD_ID.eq(cmd.getChargingStandardId())
                    		.or(standardScope.CHARGING_STANDARD_ID.eq(cmd.getChargingStandardId())))//修复issue-29576 收费项计算规则-标准名称不能修改
                    .and(standardScope.NAMESPACE_ID.eq(cmd.getNamespaceId()))
                    .and(standardScope.CATEGORY_ID.eq(cmd.getCategoryId()))
                    .and(standardScope.OWNER_ID.in(allCommunity))//兼容标准版引入的转交项目管理权
                    .fetch(standardScope.CHARGING_STANDARD_ID);
            UpdateQuery<EhPaymentChargingStandardsRecord> query = context.updateQuery(t);
            query.addValue(t.NAME, cmd.getChargingStandardName());
            if(cmd.getUseUnitPrice() != null && cmd.getUseUnitPrice().byteValue() == 1){
                query.addValue(t.PRICE_UNIT_TYPE, (byte)1);
            }
            query.addValue(t.INSTRUCTION, cmd.getInstruction());
            query.addConditions(t.ID.in(fetch));
            query.execute();
            //全部项目修改standard的，具体项目与其有关联关系，但是由于标准版引入了转交项目管理权，那么需要解耦已经转交项目管理权的项目
            //根据standard查询出所有关联的项目（包括已经授权出去的项目）
            decouplingHistoryStandard(cmd.getNamespaceId(), cmd.getCategoryId(), cmd.getChargingStandardId(), allCommunity);
        }else{
        	//去解耦
            Long nullId = null;
            context.update(t)
                    .set(t.NAME,cmd.getChargingStandardName())
                    .set(t.INSTRUCTION,cmd.getInstruction())
                    .where(t.ID.eq(cmd.getChargingStandardId()))
                    .execute();
            context.update(standardScope)
                    .set(standardScope.BROTHER_STANDARD_ID,nullId)
                    .where(standardScope.OWNER_TYPE.eq(cmd.getOwnerType()))
                    .and(standardScope.OWNER_ID.eq(cmd.getOwnerId()))
                    .and(standardScope.NAMESPACE_ID.eq(cmd.getNamespaceId()))
                    .and(standardScope.CATEGORY_ID.eq(cmd.getCategoryId()))
                    .and(standardScope.OWNER_ID.in(allCommunity))//兼容标准版引入的转交项目管理权
                    .execute();
        }
    }
    
    @Override
    public void deleteChargingStandard(DeleteChargingStandardCommand cmd, List<Long> allCommunity) {
        DSLContext context = getReadWriteContext();
        EhPaymentChargingStandardsScopes standardScope = Tables.EH_PAYMENT_CHARGING_STANDARDS_SCOPES.as("standardScope");
        com.everhomes.server.schema.tables.EhPaymentFormula formula = Tables.EH_PAYMENT_FORMULA.as("formula");
        Long nullId = null;
        //标准版增加的allScope参数，true：默认/全部，false：具体项目
        if(cmd.getAllScope()){
        	//获取当前管理公司管理下的所有与其有继承关系的项目
        	List<Long> existProjectStandardIds = context.select(standardScope.CHARGING_STANDARD_ID)
                  .from(standardScope)
                  .where(standardScope.BROTHER_STANDARD_ID.eq(cmd.getChargingStandardId()))
                  .and(standardScope.OWNER_ID.in(allCommunity))//兼容标准版引入的转交项目管理权
                  .fetch(standardScope.CHARGING_STANDARD_ID);
        	existProjectStandardIds.add(cmd.getChargingStandardId());
            //删除“获取当前管理公司管理下的所有与其有继承关系的项目"
            for(Long chargingStandardId : existProjectStandardIds){
                this.dbProvider.execute((TransactionStatus status) -> {
                	context.delete(standard)
		                  .where(standard.ID.eq(chargingStandardId))
		                  .execute();
		          context.delete(standardScope)
		                  .where(standardScope.CHARGING_STANDARD_ID.in(existProjectStandardIds))
		                  .and(standardScope.NAMESPACE_ID.eq(cmd.getNamespaceId()))
		                  .execute();
		          context.delete(formula)
		                  .where(formula.CHARGING_STANDARD_ID.in(existProjectStandardIds))
		                  .execute();
                    return null;
                });
            }
            //全部项目修改standard的，具体项目与其有关联关系，但是由于标准版引入了转交项目管理权，那么需要解耦已经转交项目管理权的项目
            //根据standard查询出所有关联的项目（包括已经授权出去的项目）
            decouplingHistoryStandard(cmd.getNamespaceId(), cmd.getCategoryId(), cmd.getChargingStandardId(), allCommunity);
        }else{
        	//去解耦
            context.delete(standard)
                    .where(standard.ID.eq(cmd.getChargingStandardId()))
                    .execute();
            //issue-34458 在具体项目新增一条标准（自定义的标准），“注：该项目使用默认配置”文案不消失，刷新也不消失
            //只要做了删除动作，那么该项目下的配置全部解耦
            context.update(standardScope)
                    .set(standardScope.BROTHER_STANDARD_ID,nullId)
                    .where(standardScope.OWNER_ID.eq(cmd.getOwnerId()))
                    .and(standardScope.OWNER_TYPE.eq(cmd.getOwnerType()))
                    .and(standardScope.CATEGORY_ID.eq(cmd.getCategoryId()))
                    .and(standardScope.NAMESPACE_ID.eq(cmd.getNamespaceId()))
                    .execute();
            context.delete(standardScope)
                    .where(standardScope.CHARGING_STANDARD_ID.eq(cmd.getChargingStandardId()))
                    .and(standardScope.CATEGORY_ID.eq(cmd.getCategoryId()))
                    .and(standardScope.NAMESPACE_ID.eq(cmd.getNamespaceId()))
                    .execute();
            context.delete(formula)
                    .where(formula.CHARGING_STANDARD_ID.eq(cmd.getChargingStandardId()))
                    .execute();
        }
    }
    
    /**
     * 标准版：为了兼容标准版引入的转交项目管理权
     * 全部项目修改standard的，具体项目与其有关联关系，但是由于标准版引入了转交项目管理权，那么需要解耦已经转交项目管理权的项目
     */
    public void decouplingHistoryStandard(Integer namespaceId, Long categoryId, Long chargingStandardId, List<Long> allCommunity) {
    	DSLContext context = getReadWriteContext();
    	EhPaymentChargingStandardsScopes t = Tables.EH_PAYMENT_CHARGING_STANDARDS_SCOPES.as("t");
    	List<Long> decouplingOwnerId = context.select(t.OWNER_ID)
        		.from(t)
                .where(t.NAMESPACE_ID.eq(namespaceId))
                .and(t.CATEGORY_ID.eq(categoryId))
                .and(t.BROTHER_STANDARD_ID.eq(chargingStandardId))
                .and(t.OWNER_ID.notIn(allCommunity))//为了兼容标准版引入的转交项目管理权
                .fetch(t.OWNER_ID);
        if(decouplingOwnerId != null && decouplingOwnerId.size() != 0) {
        	Long nullId = null;
            context.update(t)
                    .set(t.BROTHER_STANDARD_ID,nullId)
                    .where(t.OWNER_ID.in(decouplingOwnerId))
                    .execute();
        }
    }
    
    
}