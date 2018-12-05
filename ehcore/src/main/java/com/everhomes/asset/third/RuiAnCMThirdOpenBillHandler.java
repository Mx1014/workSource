package com.everhomes.asset.third;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.asset.AssetProvider;
import com.everhomes.asset.AssetService;
import com.everhomes.asset.AssetSourceNameCodes;
import com.everhomes.asset.PaymentBillGroup;
import com.everhomes.asset.PaymentBillItems;
import com.everhomes.asset.PaymentBills;
import com.everhomes.asset.bill.AssetBillProvider;
import com.everhomes.customer.EnterpriseCustomer;
import com.everhomes.customer.EnterpriseCustomerProvider;
import com.everhomes.locale.LocaleString;
import com.everhomes.locale.LocaleStringProvider;
import com.everhomes.openapi.Contract;
import com.everhomes.openapi.ContractProvider;
import com.everhomes.rest.asset.AssetPaymentBillDeleteFlag;
import com.everhomes.rest.asset.AssetPaymentBillStatus;
import com.everhomes.rest.asset.AssetSourceType;
import com.everhomes.rest.asset.AssetTargetType;
import com.everhomes.rest.asset.ListBillsCommand;
import com.everhomes.rest.asset.bill.AssetIsCheckProperty;
import com.everhomes.rest.asset.bill.AssetNotifyThirdSign;
import com.everhomes.rest.asset.bill.ListBillsDTO;
import com.everhomes.rest.asset.bill.ListBillsResponse;
import com.everhomes.rest.asset.bill.NotifyThirdSignCommand;
import com.everhomes.rest.common.AssetModuleNotifyConstants;
import com.everhomes.rest.contract.CMBill;
import com.everhomes.rest.contract.CMContractUnit;
import com.everhomes.rest.contract.CMDataObject;
import com.everhomes.rest.contract.CMSyncObject;
import com.everhomes.rest.contract.NamespaceContractType;
import com.everhomes.rest.customer.NamespaceCustomerType;
import com.everhomes.rest.portal.AssetServiceModuleAppDTO;
import com.everhomes.user.UserContext;
import com.everhomes.util.DateHelper;

/**
 * @author created by ycx
 * @date 2018年12月4日 下午2:50:27
 */
@Component(ThirdOpenBillHandler.THIRDOPENBILL_PREFIX + 999929)
public class RuiAnCMThirdOpenBillHandler implements ThirdOpenBillHandler{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RuiAnCMThirdOpenBillHandler.class);

	@Autowired
    private AssetProvider assetProvider;
	
	@Autowired
	private AssetService assetService;
	
	@Autowired
    private AddressProvider addressProvider;

    @Autowired
    private LocaleStringProvider localeStringProvider;
    
    @Autowired
    private ContractProvider contractProvider;
    
    @Autowired
    private AssetBillProvider assetBillProvider;
    
    @Autowired
    private EnterpriseCustomerProvider enterpriseCustomerProvider;
	
	/**
	 * 同步瑞安CM的账单数据到左邻的数据库表中
	 */
	public void syncRuiAnCMBillToZuolin(List<CMSyncObject> cmSyncObjectList, Integer namespaceId, Long contractCategoryId){
		if(cmSyncObjectList != null) {
			for(CMSyncObject cmSyncObject : cmSyncObjectList) {
				List<CMDataObject> data = cmSyncObject.getData();
				if(data != null) {
					for(CMDataObject cmDataObject : data) {
						//CMContractHeader contractHeader = cmDataObject.getContractHeader();
						//1、根据propertyId获取左邻communityId
						Long communityId = null;
//						Community community = addressProvider.findCommunityByThirdPartyId("ruian_cm", contractHeader.getPropertyID());
//						if(community != null) {
//							communityId = community.getId();
//						}
						//2、获取左邻企业ID
						Long targetId = null;
						EnterpriseCustomer customer = enterpriseCustomerProvider.findById(cmDataObject.getCustomerId());
						if(customer != null) {
							targetId = customer.getOrganizationId();
						}
						//3、获取左邻楼栋单元地址ID
						Long addressId = null;
						if(cmDataObject.getContractUnit() != null && cmDataObject.getContractUnit().size() != 0) {
							CMContractUnit cmContractUnit = cmDataObject.getContractUnit().get(0);
							Address address = addressProvider.findApartmentByThirdPartyId("ruian_cm", cmContractUnit.getUnitID());
							if(address != null) {
								addressId = address.getId();
								communityId = address.getCommunityId();
							}
						}

						//获取左邻合同ID、合同编号
						Long contractId = null;
						String contractNum = null;
						String rentalID = "";
						if(cmDataObject.getContractHeader() != null) {
							rentalID = cmDataObject.getContractHeader().getRentalID();//瑞安CM定义的合同ID
							try {
								Long namespaceContractToken = Long.parseLong(rentalID);
								Contract contract = contractProvider.findContractByNamespaceToken(namespaceId, NamespaceContractType.RUIAN_CM.getCode(),
										namespaceContractToken, contractCategoryId);
								if(contract != null) {
									contractId = contract.getId();
									contractNum = contract.getContractNumber();
								}
							}catch (Exception e){
					            LOGGER.error(e.toString());
					        }
						}
						//获取左邻缴费应用categoryId
						Long categoryId = null;
						try {
							List<AssetServiceModuleAppDTO> assetServiceModuleAppDTOs = assetService.listAssetModuleApps(namespaceId);
							if(assetServiceModuleAppDTOs != null && assetServiceModuleAppDTOs.get(0) != null){
								categoryId = assetServiceModuleAppDTOs.get(0).getCategoryId();
							}
						}catch (Exception e){
				            LOGGER.error(e.toString());
				        }
						List<CMBill> cmBills = cmDataObject.getBill();
						for(CMBill cmBill : cmBills) {
							//所有数据以生产方为准
							if(cmBill.getBillType() != null && cmBill.getBillType().equals("服务费账单")) {
								//CM把我方的服务账单又同步回来
								//
								//(2)若用户线下一次性全部支付，财务在CM中直接记录结果，正常同步状态给APP。未同步之前，APP端一直显示为“未支付”。
								//(3) 若用户线下支付部分账单金额，则需要同步剩余应缴金额在APP端显示，账单状态为“未支付”，且剩余金额不支持线上缴纳。
								try{
									PaymentBills zuolinServiceBill = assetProvider.findBillById(Long.parseLong(cmBill.getBillID()));
									if(zuolinServiceBill != null) {
										Long billId = zuolinServiceBill.getId();
										//(1)若用户在APP一次性全部支付，此时在APP端显示的支付状态是“已支付，待确认”。当财务看到了支付结果，在CM中确认收入以后，CM的账单状态变成“已支付”。下一次同步数据时，APP同步显示为 “已确认”。
										if(cmBill.getStatus() != null && cmBill.getStatus().equals("已缴账单")) {
											
										}else {
											
										}
									}else {
										LOGGER.error("The bill is not zuolinServiceBill, billId = {}", cmBill.getBillID());
									}
						        }catch (Exception e){
						            LOGGER.error(e.toString());
						        }
							}else {
								//来源于CM的租金账单场景
								if(cmBill.getStatus().equals("已作废")) {
									PaymentBills existCmBill = assetProvider.getCMBillByThirdBillId(namespaceId, communityId, cmBill.getBillID());
									if(existCmBill != null) {
										//如果账单的唯一标识存在，那么删除该已作废账单
										Long billId = existCmBill.getId();
										assetProvider.deleteBill(billId);
										assetProvider.deleteBillItemByBillId(billId);
									}
								}else {
									BigDecimal amountOwed = BigDecimal.ZERO;//待收(含税 元)
									BigDecimal amountOwedWithoutTax = BigDecimal.ZERO;//待收(不含税 元)
									BigDecimal amountReceivable = BigDecimal.ZERO;//应收含税
									BigDecimal amountReceivableWithoutTax = BigDecimal.ZERO;//应收不含税
									BigDecimal amountReceived = BigDecimal.ZERO;//待收含税
									BigDecimal amountReceivedWithoutTax = BigDecimal.ZERO;//待收不含税
									BigDecimal taxAmount = BigDecimal.ZERO;//税额
									try{
										amountOwed = new BigDecimal(cmBill.getBalanceAmt());
							        }catch (Exception e){
							            LOGGER.error(e.toString());
							        }
									try{
										amountOwedWithoutTax = new BigDecimal(cmBill.getBalanceAmt());
							        }catch (Exception e){
							            LOGGER.error(e.toString());
							        }
									try{
										amountReceivable = new BigDecimal(cmBill.getDocumentAmt());
							        }catch (Exception e){
							            LOGGER.error(e.toString());
							        }
									try{
										amountReceivableWithoutTax = new BigDecimal(cmBill.getChargeAmt());
							        }catch (Exception e){
							            LOGGER.error(e.toString());
							        }
									//已收=账单金额（应收）-账单欠款金额（待收）
									amountReceived = amountReceivable.subtract(amountOwed);
									amountReceivedWithoutTax = amountReceived;
									try{
										taxAmount = new BigDecimal(cmBill.getTaxAmt());
							        }catch (Exception e){
							            LOGGER.error(e.toString());
							        }

									PaymentBills paymentBills = new PaymentBills();
									paymentBills.setNamespaceId(namespaceId);
									paymentBills.setOwnerId(communityId);
									paymentBills.setOwnerType("community");
									paymentBills.setCategoryId(categoryId);
									//通过园区ID获取到对应的默认账单组ID
									PaymentBillGroup group = assetProvider.getBillGroup(namespaceId, communityId, null, null, null, (byte)1);
									paymentBills.setBillGroupId(group.getId());
									paymentBills.setTargetType(AssetTargetType.ORGANIZATION.getCode());//全部默认是企业级别的
									paymentBills.setTargetId(targetId);
									if(cmDataObject.getContractHeader() != null) {
										paymentBills.setTargetName(cmDataObject.getContractHeader().getAccountName());//客户名称
									}
									paymentBills.setContractId(contractId);
									paymentBills.setContractNum(contractNum);
									paymentBills.setDateStrBegin(cmBill.getStartDate());
									paymentBills.setDateStrEnd(cmBill.getEndDate());
									String dateStr = "";
									SimpleDateFormat yyyyMM = new SimpleDateFormat("yyyy-MM");
									try{
							            // 如果传递了计费开始时间
							            if(cmBill.getStartDate() != null){
							            	dateStr = yyyyMM.format(yyyyMM.parse(cmBill.getStartDate()));//账期取的是账单开始时间的yyyy-MM
							            }
							        }catch (Exception e){
							            LOGGER.error(e.toString());
							        }
									paymentBills.setDateStr(dateStr);//账期取的是账单开始时间的yyyy-MM
									if(cmBill.getStatus() != null) {
										if(cmBill.getStatus().equals("已出账单")) {//已出未缴
											paymentBills.setSwitch((byte) 1);
											paymentBills.setStatus(AssetPaymentBillStatus.UNPAID.getCode());
										}else if(cmBill.getStatus().equals("已缴账单")){//已出已缴
											paymentBills.setSwitch((byte) 1);
											paymentBills.setStatus(AssetPaymentBillStatus.PAID.getCode());
										}else {//未出未缴
											paymentBills.setSwitch((byte) 0);
											paymentBills.setStatus(AssetPaymentBillStatus.UNPAID.getCode());
										}
									}else {
										paymentBills.setSwitch((byte) 0);//默认为未出
										paymentBills.setStatus(AssetPaymentBillStatus.UNPAID.getCode());//默认为未缴
									}
									paymentBills.setAmountReceivable(amountReceivable);
									paymentBills.setAmountReceivableWithoutTax(amountReceivableWithoutTax);
									paymentBills.setAmountReceived(amountReceived);
									paymentBills.setAmountReceivedWithoutTax(amountReceivedWithoutTax);
									paymentBills.setAmountOwed(amountOwed);
									paymentBills.setAmountOwedWithoutTax(amountOwedWithoutTax);
									paymentBills.setTaxAmount(taxAmount);
									paymentBills.setAddressId(addressId);
									//物业缴费V6.6（对接统一账单） 账单要增加来源
									paymentBills.setSourceType(AssetModuleNotifyConstants.ASSET_CM_MODULE);
									LocaleString localeString = localeStringProvider.find(AssetSourceNameCodes.SCOPE, AssetSourceNameCodes.ASSET_CM_CREATE_CODE, "zh_CN");
									paymentBills.setSourceName(localeString.getText());
						            //物业缴费V6.0 账单、费项增加是否可以删除、是否可以编辑状态字段
									paymentBills.setCanDelete((byte)0);
									paymentBills.setCanModify((byte)0);
						            //物业缴费V6.0 账单、费项表增加是否删除状态字段
									paymentBills.setDeleteFlag(AssetPaymentBillDeleteFlag.VALID.getCode());
						            //瑞安CM对接 账单、费项表增加是否是只读字段
									paymentBills.setIsReadonly((byte)1);//只读状态：0：非只读；1：只读
									//瑞安CM对接 账单表增加第三方唯一标识字段
									paymentBills.setThirdBillId(cmBill.getBillID());
									paymentBills.setCreatTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

									PaymentBillItems items = new PaymentBillItems();
									items.setNamespaceId(namespaceId);
									items.setOwnerId(communityId);
									items.setOwnerType("community");
									items.setCategoryId(categoryId);
									items.setBillGroupId(group.getId());
									items.setTargetType(AssetTargetType.ORGANIZATION.getCode());//全部默认是企业级别的
									items.setTargetId(targetId);
									if(cmDataObject.getContractHeader() != null) {
										items.setTargetName(cmDataObject.getContractHeader().getAccountName());//客户名称
									}
									items.setContractId(contractId);
									items.setContractNum(contractNum);
									items.setDateStrBegin(cmBill.getStartDate());
									items.setDateStrEnd(cmBill.getEndDate());
									items.setDateStr(dateStr);//账期取的是账单开始时间的yyyy-MM
									items.setChargingItemName(cmBill.getBillItemName());
									items.setAmountReceivable(amountReceivable);
									items.setAmountReceivableWithoutTax(amountReceivableWithoutTax);
									items.setAmountReceived(amountReceived);
									items.setAmountReceivedWithoutTax(amountReceivedWithoutTax);
									items.setAmountOwed(amountOwed);
									items.setAmountOwedWithoutTax(amountOwedWithoutTax);
									items.setTaxAmount(taxAmount);
									items.setAddressId(addressId);
									//物业缴费V6.6（对接统一账单） 账单要增加来源
									items.setSourceType(AssetModuleNotifyConstants.ASSET_CM_MODULE);
									items.setSourceName(localeString.getText());
						            //物业缴费V6.0 账单、费项增加是否可以删除、是否可以编辑状态字段
									items.setCanDelete((byte)0);
									items.setCanModify((byte)0);
						            //物业缴费V6.0 账单、费项表增加是否删除状态字段
									items.setDeleteFlag(AssetPaymentBillDeleteFlag.VALID.getCode());
						            //瑞安CM对接 账单、费项表增加是否是只读字段
									items.setIsReadonly((byte)1);//只读状态：0：非只读；1：只读
									items.setStatus(paymentBills.getStatus());
									items.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

									PaymentBills existCmBill = assetProvider.getCMBillByThirdBillId(namespaceId, communityId, cmBill.getBillID());
									if(existCmBill != null) {
										//如果账单的唯一标识存在，那么是更新
										Long billId = existCmBill.getId();
										paymentBills.setId(billId);
										assetProvider.updateCMBill(paymentBills);
										PaymentBillItems existCmBillItem = assetProvider.getCMBillItemByBillId(billId);
										if(existCmBillItem != null) {
											items.setId(existCmBillItem.getId());
											assetProvider.updateCMBillItem(items);
										}
									}else {
										//如果账单的唯一标识不存在，那么是新增
										Long billId = assetProvider.createCMBill(paymentBills);//创建账单并返回账单ID
										items.setBillId(billId);
										assetProvider.createCMBillItem(items);
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * 物业缴费V7.4(瑞安项目-资产管理对接CM系统)
	 * 根据合同ID删除对应账单
	 * 所以数据以生产方的为准，不用管是否已支付，客户会确保对已支付的进行退款操作
	 */
	public void batchDeleteBillFromContract(Integer namespaceId, List<Long> contractIdList) {
	    assetBillProvider.deleteBillFromContract(namespaceId, contractIdList);
	}
	
	/**
	 * 物业缴费V7.4(瑞安项目-资产管理对接CM系统)
	 * 同步判断规则：
		1）通过第一章节楼宇资产管理数据的映射关系来判断是否为大小办公的场景；
		2）通过账单内是否包含服务费用（资源预定、云打印）；
	 */
	public ListBillsResponse listOpenBills(ListBillsCommand cmd) {
    	LOGGER.info("AssetBillServiceImpl listOpenBills sourceCmd={}", cmd);
    	cmd.setNamespaceId(UserContext.getCurrentNamespaceId());
    	cmd.setOwnerType("community");
    	cmd.setOwnerId(cmd.getCommunityId());
    	//物业缴费V7.4(瑞安项目-资产管理对接CM系统): 只需要传已出账单
    	List<Byte> switchList = new ArrayList<Byte>();
    	switchList.add(new Byte("1"));
    	cmd.setSwitchList(switchList);
    	//通过楼宇资产管理数据的映射关系来判断是否为大小办公的场景
    	cmd.setIsCheckProperty(AssetIsCheckProperty.CHECK.getCode());
    	//物业缴费V7.4(瑞安项目-资产管理对接CM系统) 通过账单内是否包含服务费用（资源预定、云打印）
    	List<String> sourceTypeList = new ArrayList<String>();
    	sourceTypeList.add(AssetSourceType.RENTAL_MODULE);
    	sourceTypeList.add(AssetSourceType.PRINT_MODULE);
    	cmd.setSourceTypeList(sourceTypeList);
    	//物业缴费V7.4(瑞安项目-资产管理对接CM系统) ： 一个特殊error标记给左邻系统，左邻系统以此标记判断该条数据下一次同步不再传输
    	cmd.setThirdSign(AssetNotifyThirdSign.CORRECT.getCode());
    	LOGGER.info("AssetBillServiceImpl listOpenBills convertCmd={}", cmd);
        ListBillsResponse response = new ListBillsResponse();
        Long pageAnchor = cmd.getPageAnchor();
        Integer pageSize = cmd.getPageSize();
        //卸货完毕
        if (pageAnchor == null || pageAnchor < 1l) {
            pageAnchor = 0l;
        }
        if(pageSize == null){
            pageSize = 20;
        }
        //每页大小(最大值为50)，每次请求获取的数据条数
        if(pageSize > 50) {
        	pageSize = 50;
        }
        Integer pageOffSet = pageAnchor.intValue();
        List<ListBillsDTO> list = assetProvider.listBills(cmd.getNamespaceId(), pageOffSet, pageSize, cmd);
        if(list.size() <= pageSize){
            response.setNextPageAnchor(null);
        }else {
            response.setNextPageAnchor(pageAnchor + pageSize.longValue());
            list.remove(list.size() - 1);
        }
        for(ListBillsDTO dto : list) {
        	setAccountIdByOrgId(dto);//根据企业Id获取CM的accountid
        	setBillInvalidParamNull(dto);//屏蔽账单无效参数
        }
        //每次同步都要打印下billId，方便对接过程中出现问题好进行定位
        printBillId(list);
        response.setListBillsDTOS(list);
        return response;
	}
	
	/**
	 * 每次同步都要打印下billId，方便对接过程中出现问题好进行定位
	 */
	public void printBillId(List<ListBillsDTO> list) {
		StringBuilder billIdStringBuilder = new StringBuilder();
        billIdStringBuilder.append("(");
        for(ListBillsDTO dto : list) {
        	billIdStringBuilder.append(dto.getBillId());
        	billIdStringBuilder.append(", ");
        }
        //去掉最后一个逗号
        if(billIdStringBuilder.length() >= 2) {
        	billIdStringBuilder = billIdStringBuilder.deleteCharAt(billIdStringBuilder.length() - 2);
        }
        billIdStringBuilder.append(")");
        LOGGER.info("AssetBillServiceImpl listOpenBills billIds={}", billIdStringBuilder);
	}
	
	/**
	 * 屏蔽账单无效参数
	 */
	private void setBillInvalidParamNull(ListBillsDTO dto) {
		dto.setDateStr(null);
		dto.setTargetId(null);
		dto.setBillGroupName(null);
		dto.setNoticeTimes(null);
		dto.setOwnerId(null);
		dto.setOwnerType(null);
		dto.setSourceId(null);
		dto.setSourceType(null);
		dto.setCanDelete(null);
		dto.setCanModify(null);
		dto.setIsReadOnly(null);
		dto.setNoticeTelList(null);
		dto.setConsumeUserId(null);
	}
	
	/**
	 * 根据企业Id获取CM的accountid
	 */
	private void setAccountIdByOrgId(ListBillsDTO dto) {
		try {
			EnterpriseCustomer enterpriseCustomer = enterpriseCustomerProvider.findByOrganizationIdAndType(
					Long.parseLong(dto.getTargetId()), NamespaceCustomerType.CM.getCode());
			if(enterpriseCustomer != null) {
				dto.setAccountId(enterpriseCustomer.getNamespaceCustomerToken());
			}
		} catch (Exception e) {
			
		}
	}
	
	/**
     * 物业缴费V7.4(瑞安项目-资产管理对接CM系统) ： 一个特殊error标记给左邻系统，左邻系统以此标记判断该条数据下一次同步不再传输
    */
    public void notifyThirdSign(NotifyThirdSignCommand cmd)  {
    	assetBillProvider.notifyThirdSign(cmd.getBillIdList());
    }

}
