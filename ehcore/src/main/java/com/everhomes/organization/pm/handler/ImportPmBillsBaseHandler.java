package com.everhomes.organization.pm.handler;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.db.DbProvider;
import com.everhomes.family.FamilyProvider;
import com.everhomes.organization.pm.CommunityAddressMapping;
import com.everhomes.organization.pm.CommunityPmBill;
import com.everhomes.organization.pm.CommunityPmBillItem;
import com.everhomes.organization.pm.PmBillEntityType;
import com.everhomes.organization.pm.PropertyMgrProvider;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.excel.handler.PropMrgOwnerHandler;

/**
 * ImportPmBillsBaseHandle	:	导入物业账单模板处理器
 * 
 * 使用说明:不同账单模板,新建类继承ImportPmBillsBaseHandle基类,根据需要重写基类方法。
 * <p>参考 : {@link com.everhomes.organization.pm.handler.ImportPmBillsHandler1}</p>
 *
 */	
@Component(ImportPmBillsBaseHandler.IMPORT_PM_BILLS_HANDLER_PREFIX)
public abstract class ImportPmBillsBaseHandler {
	public static final String IMPORT_PM_BILLS_HANDLER_PREFIX = "importPmBillsHandler";
	//实现模板编号
	public static final int HANDLER_1 = 1;
	public static final int HANDLER_2 = 2;

	
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ImportPmBillsBaseHandler.class);
	
	@Autowired
	private PropertyMgrProvider propertyMgrProvider;
	@Autowired 
	private FamilyProvider familyProvider;
	@Autowired
	private DbProvider dbProvider;
	
	/**
	 * 执行导入excel文件
	 */
	@SuppressWarnings("rawtypes")
	public void execute(MultipartFile[] files,long orgId){
		List list = verifyFiles(files);
		List<CommunityPmBill> bills = parse(list);
		createPmBills(bills,orgId);
	}
	
	/**
	 * 校验文件格式内容是否合法。
	 * <p>参考 : {@link com.everhomes.organization.pm.handler.ImportPmBillsHandler1}</p>
	 */
	@SuppressWarnings("rawtypes")
	public List verifyFiles(MultipartFile[] files){
		if(files == null){
			LOGGER.error("Files is null.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Files is null.");
		}
		ArrayList list = new ArrayList();
		try {
			list = PropMrgOwnerHandler.processorExcel(files[0].getInputStream());
		} catch (IOException e) {
			LOGGER.error("Convert file to list failure.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Convert file to list failure.");
		}
		if(list == null || list.isEmpty()){
			LOGGER.error("File is empty");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"File is empty");
		}
		return list;
	}
	
	/**
	 * 将文件内容解析成账单类相应字段的值:List<CommunityPmBill>
	 * <p>参考 : {@link com.everhomes.organization.pm.handler.ImportPmBillsHandler1}</p>
	 * <p>
	 * 说明:
	 * 创建CommunityPmBill对象 list，读取files,只需设置CommunityPmBill中如下字段值：
	 * 
	 * address
	 * startDate
	 * endDate
	 * payDate
	 * description
	 * dueAmount
	 * oweAmount
	 * 	
	 * itemList : 可为null,不为空设置如下字段
	 * itemName
	 * startCount
	 * endCount
	 * useCount
	 * price
	 * totalAmount
	 * description
	 * </p>
	 */
	@SuppressWarnings("rawtypes")
	abstract public List<CommunityPmBill> parse(List list);
	
	/**
	 * 将数据插入数据库
	 */
	public void createPmBills(List<CommunityPmBill> bills,Long orgId){
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		User user  = UserContext.current().getUser();
		Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
		List<CommunityAddressMapping> mappingList = propertyMgrProvider.listOrganizationAddressMappingsByOrgId(orgId);
		if(bills != null && bills.size() > 0){
			this.dbProvider.execute( s -> {
				for (CommunityPmBill bill : bills){
					if(mappingList != null && mappingList.size() > 0)
					{
						CommunityAddressMapping mapping = null;
						for (CommunityAddressMapping m : mappingList){
							if(bill != null && bill.getAddress().equals(m.getOrganizationAddress())){
								mapping = m;
								break;
							}
						}
						if(mapping == null){
							LOGGER.error(bill.getAddress() + " not find in address mapping.");
							throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
									bill.getAddress() + " not find in address mapping.");
						}

						CommunityPmBill existedBill = this.propertyMgrProvider.findFamilyPmBillInStartDateToEndDate(mapping.getAddressId(),bill.getStartDate(),bill.getEndDate());
						if(existedBill != null){
							LOGGER.error("the bill is exist.please don't import repeat data.address="+bill.getAddress()+",startDate=" + format.format(bill.getStartDate())+",endDate="+format.format(bill.getEndDate()));
							throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
									"the bill is exist.please don't import repeat data.address="+bill.getAddress()+",startDate=" + format.format(bill.getStartDate())+",endDate="+format.format(bill.getEndDate()));
						}

						bill.setOrganizationId(orgId);
						bill.setEntityId(mapping.getAddressId());
						bill.setEntityType(PmBillEntityType.ADDRESS.getCode());
						bill.setNotifyCount(0);
						bill.setNotifyTime(null);

						cal.setTimeInMillis(bill.getStartDate().getTime());
						StringBuilder builder = new StringBuilder();
						builder.append(cal.get(Calendar.YEAR) +"-");
						if(cal.get(Calendar.MONTH)<9)
							builder.append("0"+(cal.get(Calendar.MONTH)+1));
						else
							builder.append(cal.get(Calendar.MONTH)+1);
						bill.setDateStr(builder.toString());
						bill.setName(bill.getDateStr() + "月账单");
						bill.setCreatorUid(user.getId());
						bill.setCreateTime(timeStamp);
						//往期欠款处理
						if(bill.getOweAmount() == null){
							CommunityPmBill beforeBill = this.propertyMgrProvider.findFamilyNewestBill(mapping.getAddressId(), orgId);
							if(beforeBill != null){
								//payAmount为负
								BigDecimal payedAmount = this.familyProvider.countFamilyTransactionBillingAmountByBillId(beforeBill.getId());
								BigDecimal oweAmount = beforeBill.getDueAmount().add(beforeBill.getOweAmount()).add(payedAmount);
								bill.setOweAmount(oweAmount);
							}
							else
								bill.setOweAmount(BigDecimal.ZERO);
						}

						propertyMgrProvider.createPropBill(bill);
						List<CommunityPmBillItem> itemList =  bill.getItemList();
						if(itemList != null && itemList.size() > 0){
							for (CommunityPmBillItem communityPmBillItem : itemList) {
								communityPmBillItem.setBillId(bill.getId());
								communityPmBillItem.setCreateTime(timeStamp);
								communityPmBillItem.setCreatorUid(user.getId());
								propertyMgrProvider.createPropBillItem(communityPmBillItem);
							}
						}
					}
				}
				
				return s;
			});

		}
	}

}
