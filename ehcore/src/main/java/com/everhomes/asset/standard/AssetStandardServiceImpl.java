
package com.everhomes.asset.standard;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jooq.tools.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import com.everhomes.asset.AssetErrorCodes;
import com.everhomes.asset.AssetProvider;
import com.everhomes.asset.AssetService;
import com.everhomes.asset.PaymentBillCertificate;
import com.everhomes.asset.PaymentChargingStandardScope;
import com.everhomes.asset.PaymentChargingStandards;
import com.everhomes.asset.PaymentFormula;
import com.everhomes.db.DbProvider;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.asset.AssetProjectDefaultFlag;
import com.everhomes.rest.asset.CreateChargingStandardCommand;
import com.everhomes.rest.asset.CreateFormulaCommand;
import com.everhomes.rest.asset.DeleteChargingStandardCommand;
import com.everhomes.rest.asset.DeleteChargingStandardDTO;
import com.everhomes.rest.asset.GetChargingStandardCommand;
import com.everhomes.rest.asset.GetChargingStandardDTO;
import com.everhomes.rest.asset.IsProjectNavigateDefaultCmd;
import com.everhomes.rest.asset.IsProjectNavigateDefaultResp;
import com.everhomes.rest.asset.ListBillGroupsDTO;
import com.everhomes.rest.asset.ListChargingStandardsCommand;
import com.everhomes.rest.asset.ListChargingStandardsDTO;
import com.everhomes.rest.asset.ListChargingStandardsResponse;
import com.everhomes.rest.asset.ModifyChargingStandardCommand;
import com.everhomes.rest.asset.VariableConstraints;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhPaymentChargingStandards;
import com.everhomes.server.schema.tables.pojos.EhPaymentChargingStandardsScopes;
import com.everhomes.server.schema.tables.pojos.EhPaymentFormula;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;

/**
 * @author created by ycx
 * @date 下午8:16:22
 */
@Component
public class AssetStandardServiceImpl implements AssetStandardService {
	
	@Autowired
	private AssetService assetService;
	
	@Autowired
    private AssetProvider assetProvider;
	
	@Autowired 
	private AssetStandardProvider assetStandardProvider;
	
	@Autowired
	private SequenceProvider sequenceProvider;

	@Autowired
	protected LocaleStringService localeStringService;
	
	@Autowired
	private DbProvider dbProvider;
	
    public ListChargingStandardsResponse listOnlyChargingStandards(ListChargingStandardsCommand cmd) {
        ListChargingStandardsResponse response = new ListChargingStandardsResponse();
        if(cmd.getPageSize() ==null){
            cmd.setPageSize(20);
        }
        if(cmd.getPageAnchor() == null){
            cmd.setPageAnchor(0l);
        }
        //标准版增加的allScope参数，true：默认/全部，false：具体项目
        if(cmd.getOwnerId() == null || cmd.getOwnerId() == -1){
            cmd.setOwnerId(cmd.getNamespaceId().longValue());
            cmd.setAllScope(true);
        }else {
        	cmd.setAllScope(false);
        }
        // set category default is 0 representing the old data
        if(cmd.getCategoryId() == null){
            cmd.setCategoryId(0l);
        }
        List<ListChargingStandardsDTO> list = assetStandardProvider.listOnlyChargingStandards(cmd);
        if(list.size() > cmd.getPageSize()){
            response.setNextPageAnchor(cmd.getPageAnchor()+cmd.getPageSize().longValue());
            list.remove(list.size()-1);
        }else{
            response.setNextPageAnchor(null);
        }
        response.setList(list);
        return response;
    }
    
    public void createChargingStandard(CreateChargingStandardCommand cmd) {
         // set category default is 0 representing the old data
        if(cmd.getCategoryId() == null){
            cmd.setCategoryId(0l);
        }
        if(cmd.getOwnerId() == null || cmd.getOwnerId() == -1){
            Long brotherStandardId = null;
            brotherStandardId = getBrotherStandardId();
            //1、先同步下去
            List<Long> allCommunityIds = assetService.getAllCommunity(cmd.getNamespaceId(),cmd.getOrganizationId(),cmd.getAppId(),false);
            for(int i = 0; i < allCommunityIds.size(); i ++){
                Long communityId = allCommunityIds.get(i);
                cmd.setOwnerId(communityId);
                //全部配置要同步到具体项目的时候，首先要判断一下该项目是否解耦了，如果解耦了，则不需要
                IsProjectNavigateDefaultCmd isProjectNavigateDefaultCmd = new IsProjectNavigateDefaultCmd();
                isProjectNavigateDefaultCmd.setOwnerId(communityId);
                isProjectNavigateDefaultCmd.setOwnerType("community");
                isProjectNavigateDefaultCmd.setNamespaceId(cmd.getNamespaceId());
                isProjectNavigateDefaultCmd.setCategoryId(cmd.getCategoryId());
                isProjectNavigateDefaultCmd.setOrganizationId(cmd.getOrganizationId());//标准版新增的管理公司ID
                IsProjectNavigateDefaultResp isProjectNavigateDefaultResp = assetStandardProvider.isChargingStandardsForJudgeDefault(isProjectNavigateDefaultCmd);
                if(isProjectNavigateDefaultResp != null && isProjectNavigateDefaultResp.getDefaultStatus().equals(AssetProjectDefaultFlag.DEFAULT.getCode())) {
                	Boolean allScope = false;
                	InsertChargingStandards(cmd, brotherStandardId, null, allScope);
                }
            }
            //2、再创建全部配置
            Boolean allScope = true;
            cmd.setOwnerId(cmd.getNamespaceId().longValue());
            InsertChargingStandards(cmd, null, brotherStandardId, allScope);
            //全部项目修改standard的，具体项目与其有关联关系，但是由于标准版引入了转交项目管理权，那么需要解耦已经转交项目管理权的项目
            //根据standard查询出所有关联的项目（包括已经授权出去的项目）
            ListChargingStandardsCommand listChargingStandardsCommand = ConvertHelper.convert(cmd, ListChargingStandardsCommand.class);
            listChargingStandardsCommand.setChargingItemId(cmd.getChargingItemId());
            listChargingStandardsCommand.setPageSize(999999999);
            ListChargingStandardsResponse response = listOnlyChargingStandards(listChargingStandardsCommand);
            List<ListChargingStandardsDTO> listChargingStandardsDTOs = response.getList();
            for(ListChargingStandardsDTO dto : listChargingStandardsDTOs) {
            	Long chargingStandardId = dto.getChargingStandardId();
            	assetStandardProvider.decouplingHistoryStandard(cmd.getNamespaceId(), cmd.getCategoryId(), chargingStandardId, allCommunityIds);
            }
        }else{
        	Boolean allScope = false;
            InsertChargingStandards(cmd, null, null, allScope);
            assetProvider.deCoupledForChargingItem(cmd.getOwnerId(),cmd.getOwnerType(), cmd.getCategoryId());
        }
    }
	
    private Long getBrotherStandardId() {
    	long nextStandardId = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(Tables.EH_PAYMENT_CHARGING_STANDARDS.getClass()));
    	return nextStandardId;
    }

    private Long InsertChargingStandards(CreateChargingStandardCommand cmd, Long brotherStandardId, Long nextStandardId, Boolean allScope) {
        if(cmd.getFormulaType() == 1 || cmd.getFormulaType() == 2){
            String formula_no_quote = cmd.getFormula();
            formula_no_quote = formula_no_quote.replace("[[","");
            formula_no_quote = formula_no_quote.replace("]]","");
            cmd.setFormula(formula_no_quote);
        }
        EhPaymentChargingStandards c = new PaymentChargingStandards();
        EhPaymentChargingStandardsScopes s = new PaymentChargingStandardScope();
        // create a chargingstandard
        c.setBillingCycle(cmd.getBillingCycle());
        c.setChargingItemsId(cmd.getChargingItemId());
        c.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        c.setCreatorUid(0l);
        c.setFormula(cmd.getFormula());
        c.setFormulaJson(cmd.getFormulaJson());
        c.setFormulaType(cmd.getFormulaType());
        if(nextStandardId == null) {
        	nextStandardId = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(Tables.EH_PAYMENT_CHARGING_STANDARDS.getClass()));
        }
        c.setId(nextStandardId);
        c.setName(cmd.getChargingStandardName());
        c.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        c.setInstruction(cmd.getInstruction());
        c.setSuggestUnitPrice(cmd.getSuggestUnitPrice());
        c.setAreaSizeType(cmd.getAreaSizeType());
        if(cmd.getUseUnitPrice() != null && cmd.getUseUnitPrice().byteValue() == (byte)1){
            c.setPriceUnitType(cmd.getUseUnitPrice());
        }

        // create formula that fits the standard
        CreateFormulaCommand cmd1 = ConvertHelper.convert(cmd,CreateFormulaCommand.class);
        cmd1.setChargingStandardId(nextStandardId);
        List<EhPaymentFormula> f = createFormula(cmd1);

        // create a scope corresponding to the chargingstandard just created
        s.setChargingStandardId(nextStandardId);
        s.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        s.setCreatorUid(0l);
        s.setId(this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(Tables.EH_PAYMENT_CHARGING_STANDARDS_SCOPES.getClass())));
        s.setOwnerType(cmd.getOwnerType());
        s.setOwnerId(cmd.getOwnerId());
        // add categoryId constraint
        s.setCategoryId(cmd.getCategoryId());

        s.setNamespaceId(cmd.getNamespaceId());
        s.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        s.setBrotherStandardId(brotherStandardId);
        if(allScope) {
        	s.setOrgId(cmd.getOrganizationId());//标准版新增的管理公司ID
        }
        this.dbProvider.execute((TransactionStatus status) ->{
            assetProvider.createChargingStandard(c,s,f);
            return null;
        });
        return nextStandardId;
    }
    
    /**
     * 解析公式，传来的公式可以是identifer构成的，也可以是汉字构成的，先按照后者解析
     * 返回值
     *  1. formula公式名称，将解析符号去掉即可
     *  普通公式时：
     *      2. formulaJson公式解析式，去掉解析符号将汉字替换为对应的标识
     *      3. formulaType，将传来的返回
     *  阶梯公式时：
     *      2. 需要自己组装公式，从公式表中得到条件与公式并存储
     *      3.
     *  梯度公式时：
     *      2. 需要自己组装公式，从公式表中得到条件与公式并存储
     *      3. 返回id
     */
    @Override
    public List<EhPaymentFormula> createFormula(CreateFormulaCommand cmd) {
        List<EhPaymentFormula> list = new ArrayList<>();
        Byte formulaType = cmd.getFormulaType();
        if (formulaType == 1 ) {
            EhPaymentFormula paymentFormula = new PaymentFormula();
            long nextPaymentFormulaId = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(Tables.EH_PAYMENT_FORMULA.getClass()));
            paymentFormula.setId(nextPaymentFormulaId);
            paymentFormula.setChargingStandardId(cmd.getChargingStandardId());
            paymentFormula.setCreatorUid(0l);
            paymentFormula.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            paymentFormula.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            paymentFormula.setName(cmd.getFormula());
            paymentFormula.setFormulaType(formulaType);
            paymentFormula.setFormula(cmd.getFormula());
            paymentFormula.setFormulaJson("gdje");
            list.add(paymentFormula);
        } else if (formulaType == 2) {
            //普通公式时
            String str = cmd.getFormula();
            List<String> formulaAndJson = setFormula(str);

            EhPaymentFormula paymentFormula = new PaymentFormula();
            long nextPaymentFormulaId = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(Tables.EH_PAYMENT_FORMULA.getClass()));
            paymentFormula.setId(nextPaymentFormulaId);
            paymentFormula.setChargingStandardId(cmd.getChargingStandardId());
            paymentFormula.setCreatorUid(0l);
            paymentFormula.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            paymentFormula.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            paymentFormula.setName(formulaAndJson.get(0));
            paymentFormula.setFormulaType(formulaType);
            paymentFormula.setFormula(formulaAndJson.get(0));
            paymentFormula.setFormulaJson(formulaAndJson.get(1));
            list.add(paymentFormula);
        } else if (formulaType == 3 || formulaType == 4) {
            //存储条件
            List<VariableConstraints> envelop = cmd.getStepValuePairs();
            StringBuilder name = new StringBuilder();
            for (int i = 0; i < envelop.size(); i++) {
                VariableConstraints variableConstraints = envelop.get(i);
                EhPaymentFormula paymentFormula = new PaymentFormula();

                String eachFormula = variableConstraints.getFormula();
                List<String> formularAndJson = setFormula(eachFormula);

                paymentFormula.setFormulaType(cmd.getFormulaType());
                paymentFormula.setFormula(formularAndJson.get(0));
                paymentFormula.setFormulaJson(formularAndJson.get(1));
                long nextPaymentFormulaId = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(Tables.EH_PAYMENT_FORMULA.getClass()));
                paymentFormula.setId(nextPaymentFormulaId);
                paymentFormula.setChargingStandardId(cmd.getChargingStandardId());
                //获得一个区间5个约束条件
                paymentFormula.setConstraintVariableIdentifer(variableConstraints.getVariableIdentifier());
                paymentFormula.setStartConstraint(variableConstraints.getStartConstraint());
                if(variableConstraints.getStartNum()!=null){
                    paymentFormula.setStartNum(new BigDecimal(variableConstraints.getStartNum()));
                }
                paymentFormula.setEndConstraint(variableConstraints.getEndConstraint());
                if(variableConstraints.getEndNum()!=null){
                    paymentFormula.setEndNum(new BigDecimal(variableConstraints.getEndNum()));
                }


                paymentFormula.setCreatorUid(0l);
                paymentFormula.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                paymentFormula.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                list.add(paymentFormula);
                name.append(formularAndJson.get(0) + "+");
            }
            if(name.lastIndexOf("+")==name.length()-1) name.deleteCharAt(name.length()-1);
            for(int i = 0 ; i < list.size(); i ++){
                list.get(i).setName(name.toString());
            }
        }
        return list;
    }
    
    private List<String> setFormula( String str) {
        List<String> formulaAndJson = new ArrayList<>();
        String formula = str;
        formula = formula.replace("[[","");
        formula = formula.replace("]]","");

        formulaAndJson.add(formula);
        Set<String> replaces = new HashSet<>();
        String formulaJson = formula;
        char[] formularChars = formulaJson.toCharArray();
        int index = 0;
        int start = 0;
        while(index < formularChars.length){
            if(formularChars[index]=='+'||formularChars[index]=='-'||formularChars[index]=='*'||formularChars[index]=='/'||index == formularChars.length-1){
                replaces.add(formulaJson.substring(start,index==formulaJson.length()-1?index+1:index));
                start = index+1;
            }
            index++;
        }
        Iterator<String> iterator = replaces.iterator();
        while(iterator.hasNext()){
            String targetStr = iterator.next();
            String substitute = assetProvider.getVariableIdenfitierByName(targetStr);
            if(!StringUtils.isBlank(substitute)){
                formulaJson = formulaJson.replace(targetStr,substitute);
            }
        }

        formulaAndJson.add(formulaJson);
        return formulaAndJson;
    }
    
    public void modifyChargingStandard(ModifyChargingStandardCommand cmd) {
        assetService.checkNullProhibit("chargingStandardId",cmd.getChargingStandardId());
        assetService.checkNullProhibit("new chargingStandardName",cmd.getChargingStandardName());
        
        List<Long> allCommunity = assetService.getAllCommunity(cmd.getNamespaceId(), cmd.getOrganizationId(), cmd.getAppId(), true);
        // 由于chargingStandard要变化，所以对于全部的情况直接修改，不需要coupling；
        // 对于单个园区要求修改，则 删除原来的scope和，拿到原来的standard，删除原来的standard，修改后新建一个standard和同一个scope
    	//标准版增加的allScope参数，true：默认/全部，false：具体项目
        if(cmd.getOwnerId() == null || cmd.getOwnerId() == -1){
            //全部的情况,修改billGroup的以及bro为billGroup的
            cmd.setAllScope(true);
            assetStandardProvider.modifyChargingStandard(cmd, allCommunity);
        }else {
        	//具体项目修改billGroup的，此园区的所有group解耦
        	cmd.setAllScope(false);
        	assetStandardProvider.modifyChargingStandard(cmd, allCommunity);
        }
    }
    
    /**
     * 删除一个收费标准，删除之前，查询其是否已经被引用
     * 1. 是否已经被处于有效期的合同引用
     */
    public DeleteChargingStandardDTO deleteChargingStandard(DeleteChargingStandardCommand cmd) {
    	//issue-27671 【合同管理】删除收费项“租金”的标准后，进入修改合同条款信息页面，进入“修改”页面，标准显示了“数字”
        //只要关联了合同（包括草稿合同）就不能删除标准
    	boolean workFlag = assetProvider.isInWorkChargingStandard(cmd.getNamespaceId(), cmd.getChargingStandardId());
    	if(workFlag){
    		throw RuntimeErrorException.errorWith(AssetErrorCodes.SCOPE,AssetErrorCodes.STANDARD_RELEATE_CONTRACT_CHECK,"if a standard relates contracts,it cannot be deleled!");
        }
    	List<Long> allCommunity = assetService.getAllCommunity(cmd.getNamespaceId(), cmd.getOrganizationId(), cmd.getAppId(), true);
    	if(cmd.getOwnerId() == null || cmd.getOwnerId() ==-1) {
    		// 全部：在工作的收费标准(所属的item在账单组中存在视为工作中)，一定是没有bro的，所以直接删除，id和bro id的即可
        	cmd.setAllScope(true);
        	assetStandardProvider.deleteChargingStandard(cmd, allCommunity);
        }else {
        	cmd.setAllScope(false);
        	assetStandardProvider.deleteChargingStandard(cmd, allCommunity);
        }
        DeleteChargingStandardDTO dto = new DeleteChargingStandardDTO();
        return dto;
    }
    
    public GetChargingStandardDTO getChargingStandardDetail(GetChargingStandardCommand cmd) {
        return assetProvider.getChargingStandardDetail(cmd);
    }

	
}