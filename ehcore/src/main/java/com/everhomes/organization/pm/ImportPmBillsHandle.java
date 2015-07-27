package com.everhomes.organization.pm;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.everhomes.db.DbProvider;
import com.everhomes.family.FamilyProvider;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;

public class ImportPmBillsHandle {
	private ImportPmBillsBaseParser parser;
	
	private PropertyMgrProvider propertyMgrProvider;
	private DbProvider dbProvider;
	private FamilyProvider familyProvider;
	private Long orgId;
	
	public ImportPmBillsHandle(ImportPmBillsBaseParser parser, 
			DbProvider dbProvider, PropertyMgrProvider propertyMgrProvider, FamilyProvider familyProvider, Long orgId){
		this.parser = parser;
		this.dbProvider = dbProvider;
		this.familyProvider = familyProvider;
		this.propertyMgrProvider = propertyMgrProvider;
		this.orgId = orgId;
	}
	
	public void importPmBills(MultipartFile[] files){
		if(files == null)
			return ;
		if(dbProvider == null ||propertyMgrProvider == null || familyProvider == null || orgId == null)
			return ;
		
		if(parser == null)
			parser = new DefaultImportPmBillsParser();
		this.createPmBills(parser.parse(files));
	}

	private void createPmBills(List<CommunityPmBill> billList) {
		
		Calendar cal = Calendar.getInstance();
		User user  = UserContext.current().getUser();
		Timestamp timeStamp = new Timestamp(new Date().getTime());

		List<CommunityAddressMapping> mappingList = propertyMgrProvider.listCommunityAddressMappings(orgId);

		if(billList != null && billList.size() > 0){

			this.dbProvider.execute( s -> {
				for (CommunityPmBill bill : billList){
					if(mappingList != null && mappingList.size() > 0)
					{
						for (CommunityAddressMapping mapping : mappingList)
						{
							if(bill != null && bill.getAddress().equals(mapping.getOrganizationAddress())){
								Long addressId = mapping.getAddressId();
								bill.setOrganizationId(orgId);
								bill.setEntityId(mapping.getAddressId());
								bill.setEntityType(PmBillEntityType.ADDRESS.getCode());

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

								bill.setItemList(null);

								//往期欠款处理
								if(bill.getOweAmount() == null){
									CommunityPmBill beforeBill = this.propertyMgrProvider.findFamilyNewestBill(addressId, orgId);
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
										propertyMgrProvider.createPropBillItem(communityPmBillItem);
									}
								}
								break;
							}
						}
					}
				}

				return s;
			});

		}
		
	}
	
	

}
