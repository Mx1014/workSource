package com.everhomes.organization.pm;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.db.DbProvider;
import com.everhomes.family.FamilyProvider;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.excel.RowResult;
import com.everhomes.util.excel.handler.PropMrgOwnerHandler;

@Component("DefaultImportPmBillsParser")
public class DefaultImportPmBillsParser implements ImportPmBillsBaseParser{
	private static final Logger LOGGER = LoggerFactory.getLogger(PropertyMgrServiceImpl.class);

	@Autowired
	private PropertyMgrProvider propertyMgrProvider;
	@Autowired 
	private FamilyProvider familyProvider;
	@Autowired
	private DbProvider dbProvider;
	

	@Override
	public List verifyFiles(MultipartFile[] files) {
		ArrayList list = new ArrayList();
		try {
			list = PropMrgOwnerHandler.processorExcel(files[0].getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(list == null || list.isEmpty()){
			LOGGER.error("File is empty");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"File is empty");
		}

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		int startRow = 1;
		int rowCount = list.size();
		Set<String> addressSet = new HashSet<String>();
		
		for (int rowIndex = startRow; rowIndex < rowCount ; rowIndex++) {
			RowResult r = (RowResult)list.get(rowIndex);
			//verify A,B,C,D,E column not be null
			if(r.getA() == null || r.getA().isEmpty() || r.getB() == null || r.getB().isEmpty() ||
					r.getC() == null || r.getC().isEmpty() || r.getD() == null || r.getD().isEmpty() ||
					r.getE() == null || r.getE().isEmpty()){
				LOGGER.error("Error happend in row " + (rowIndex+1) + ".Column A,B,C,D,E could not be null or empty.");
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
						"Error happend in row " + (rowIndex+1) + ".Column A,B,C,D,E could not be null or empty.");
			}
			//verify date in A,B,C column
			try {
				format.parse(r.getA());
				format.parse(r.getB());
				format.parse(r.getC());
			} catch (ParseException e) {
				LOGGER.error("Error happend in row "+ (rowIndex+1) + ".Column A,B,C must be date format : yyyy-MM-dd.");
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
						"Error happend in row "+ (rowIndex+1) + ".Column A,B,C must be date format : yyyy-MM-dd.");
			}
			//verify E,F column must be bigDecimal
			try{
				Double.valueOf(r.getE());
				if(r.getF() != null && !r.getF().isEmpty())
					Double.valueOf(r.getF());
			}
			catch(NumberFormatException e){
				LOGGER.error("Error happend in row "+ (rowIndex+1) + ".Column E,F must be float,otherwise F column can be empty.");
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
						"Error happend in row "+ (rowIndex+1) + ".Column E,F must be float,otherwise F column can be empty.");
			}
			//verify D column.address not repeat.
			addressSet.add(r.getD());
		}
		//verify D column.address not repeat.
		if(addressSet != null && addressSet.size() != list.size()-1){
			LOGGER.error("address could not repeat");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"address could not repeat");
		}
			
		return list;
	}

	@Override
	public List<CommunityPmBill> parse(List list) {
		List<CommunityPmBill> bills = new ArrayList<CommunityPmBill>();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		if(list != null && !list.isEmpty()){
			int rowCount = list.size();
			int startRow = 1;

			for (int rowIndex = startRow; rowIndex < rowCount ; rowIndex++) {
				RowResult rowResult = (RowResult)list.get(rowIndex);
				//生成账单总信息
				CommunityPmBill bill = new CommunityPmBill();
				bill.setId(null);

				try {
					bill.setStartDate(new Date(format.parse(rowResult.getA()).getTime()));
					bill.setEndDate(new Date(format.parse(rowResult.getB()).getTime()));
					bill.setPayDate(new Date(format.parse(rowResult.getC()).getTime()));
				} catch (ParseException e) {
					LOGGER.error("date string format is wrong.must be yyyy-MM-dd format.");
					throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
							"date string format is wrong.must be yyyy-MM-dd format.");
				}
				bill.setAddress(rowResult.getD());
				bill.setDueAmount(rowResult.getE() == null ? BigDecimal.ZERO:new BigDecimal(rowResult.getE()));
				bill.setOweAmount(rowResult.getF() == null ? null:new BigDecimal(rowResult.getF()));
				bill.setDescription(rowResult.getG());
				bills.add(bill);
			}
		}

		return bills;
	}

	@Override
	public void createPmBills(List<CommunityPmBill> bills,Long orgId) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		User user  = UserContext.current().getUser();
		Timestamp timeStamp = new Timestamp(new java.util.Date().getTime());
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
