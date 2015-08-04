package com.everhomes.organization.pm.handler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.organization.pm.CommunityPmBill;
import com.everhomes.organization.pm.CommunityPmBillItem;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.excel.RowResult;

@Component(ImportPmBillsBaseHandler.IMPORT_PM_BILLS_HANDLER_PREFIX+ImportPmBillsBaseHandler.HANDLER_2)
public class ImportPmBillsHandler2 extends ImportPmBillsBaseHandler{

	private static final Logger LOGGER = LoggerFactory.getLogger(ImportPmBillsBaseHandler.class);

	@SuppressWarnings("rawtypes")
	@Override
	public List<CommunityPmBill> parse(List list) {
		
		Set<String> addressSet = new HashSet<String>();

		List<CommunityPmBill> bills = new ArrayList<CommunityPmBill>();

		int monthRowIndex = 3;//(3,C)
		int beforeStartRowIndex = 3;
		int billRowSize = 8;
		int billSize = (list.size()-monthRowIndex)/billRowSize;
		int itemSize = 6;
		if(list == null || list.isEmpty()){
			return null;
		}

		String monthStr =((RowResult)list.get(monthRowIndex-1)).getC();
		this.checkMonthStr(monthStr);//校验月份
		long timeMills = this.convertMonthStrToTimeMills(monthStr);
		java.sql.Date startDate = new java.sql.Date(this.getStartDateTimeMills(timeMills));
		java.sql.Date endDate = new java.sql.Date(this.getEndDateTimeMills(timeMills));
		java.sql.Date payDate = new java.sql.Date(this.getPayDateTimeMills(timeMills));

		for(int i = 0;i<billSize;i++){
			CommunityPmBill bill = new CommunityPmBill();

			//address : (7,B)
			int addressRowIndex = beforeStartRowIndex + 7 + i*billRowSize;
			//description : (8,A)
			int descriptionRowIndex = beforeStartRowIndex + 8 + i*billRowSize;
			//totalAmount : (1,G)
			int totalAmountRowIndex = beforeStartRowIndex + 1 + i*billRowSize;
			//item:1-6
			int itemStartRowIndex = beforeStartRowIndex + 1 + i*billRowSize;

			RowResult addressRow = (RowResult) list.get(addressRowIndex-1);
			RowResult descriptionRow = (RowResult) list.get(descriptionRowIndex-1);
			RowResult totalAmountRow = (RowResult) list.get(totalAmountRowIndex-1);
			
			//地址和总额为空，表示后面的记录都是空行
			if((addressRow.getB() == null || addressRow.getB().trim().isEmpty()) && (totalAmountRow.getG() == null || totalAmountRow.getG().trim().isEmpty())){
				break;
			}
			
			bill.setStartDate(startDate);
			bill.setEndDate(endDate);
			bill.setPayDate(payDate);
			this.checkAddress(addressRow.getB());//校验地址
			bill.setAddress(addressRow.getB().trim());
			bill.setDescription(descriptionRow.getA() == null ? null:descriptionRow.getA().trim());
			this.checkTotalAmount(totalAmountRow.getG());
			bill.setDueAmount(new BigDecimal(totalAmountRow.getG().trim()));
			bill.setItemList(this.getPmBillItems(itemStartRowIndex,itemSize,list));
			
			//验证excel没有重复记录
			addressSet.add(addressRow.getB());
			
			bills.add(bill);
		}
		
		if(bills != null && addressSet != null && bills.size() != addressSet.size()){
			LOGGER.error("address could not repeat.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"address could not repeat.");
		}

		return bills;
	}

	private long getPayDateTimeMills(long timeMills) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(timeMills);
		cal.set(Calendar.MONTH, cal.get(Calendar.MONTH)+1);
		cal.set(Calendar.DAY_OF_MONTH, 7);
		cal.set(Calendar.HOUR_OF_DAY, cal.getActualMaximum(Calendar.HOUR_OF_DAY));
		cal.set(Calendar.MINUTE, cal.getActualMaximum(Calendar.MINUTE));
		cal.set(Calendar.SECOND, cal.getActualMaximum(Calendar.SECOND));
		cal.set(Calendar.MILLISECOND, cal.getActualMaximum(Calendar.MILLISECOND));
		return cal.getTimeInMillis();
	}

	private long getEndDateTimeMills(long timeMills) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(timeMills);
		cal.set(Calendar.DAY_OF_MONTH,cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		cal.set(Calendar.HOUR_OF_DAY, cal.getActualMaximum(Calendar.HOUR_OF_DAY));
		cal.set(Calendar.MINUTE, cal.getActualMaximum(Calendar.MINUTE));
		cal.set(Calendar.SECOND, cal.getActualMaximum(Calendar.SECOND));
		cal.set(Calendar.MILLISECOND, cal.getActualMaximum(Calendar.MILLISECOND));
		return cal.getTimeInMillis();
	}

	private long getStartDateTimeMills(long timeMills) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(timeMills);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTimeInMillis();
	}

	private long convertMonthStrToTimeMills(String monthStr) {
		StringBuilder builder = new StringBuilder(monthStr);
		builder.delete(builder.length()-2, builder.length());
		try{
			int month = Integer.valueOf(builder.toString());
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			cal.set(Calendar.MONTH, month-1);
			return cal.getTimeInMillis();
		}
		catch(NumberFormatException e){
			LOGGER.error("month format is wrong.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"month format is wrong.");
		}
	}

	@SuppressWarnings("rawtypes")
	private List<CommunityPmBillItem> getPmBillItems(int itemStartRowIndex,int itemSize, List list) {
		if(list == null || itemStartRowIndex+itemSize > list.size()){
			return null;
		}

		List<CommunityPmBillItem> items = new ArrayList<CommunityPmBillItem>();
		for(int i=itemStartRowIndex-1;i<itemSize;i++){
			RowResult row = (RowResult) list.get(i);
			CommunityPmBillItem item = new CommunityPmBillItem();
			this.checkItemName(row.getA());
			item.setItemName(row.getA().trim());
			item.setStartCount(this.convertStrToBigDecimal(row.getB()));
			item.setEndCount(this.convertStrToBigDecimal(row.getC()));
			//this.checkUserCount(row.getD());
			item.setUseCount(this.convertStrToBigDecimal(row.getD()));
			//this.checkPrice(row.getE());
			item.setPrice(this.convertStrToBigDecimal(row.getE()));
			//this.checkItemTotalAmount(row.getF());
			item.setTotalAmount(this.convertStrToBigDecimal(row.getF()));

			items.add(item);
		}

		return items;
	}

	private BigDecimal convertStrToBigDecimal(String str) {
		if(str == null || str.trim().isEmpty()){
			return BigDecimal.ZERO;
		}
		return new BigDecimal(str.trim());
	}
	
	private void checkTotalAmount(String totalAmount) {
		if(totalAmount == null || totalAmount.trim().isEmpty()){
			LOGGER.error("total is null or empty.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"total is null or empty.");
		}
		try{
			Double.valueOf(totalAmount);
		}
		catch(NumberFormatException e){
			LOGGER.error("total should be float.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"total should be float.");
		}
	}

	private void checkAddress(String address) {
		if(address == null || address.trim().isEmpty()){
			LOGGER.error("address is null or empty.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"address is null or empty.");
		}
		
	}

	private void checkMonthStr(String monthStr) {
		if(monthStr == null || monthStr.trim().isEmpty()){
			LOGGER.error("month is null or empty.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"month is null or empty.");
		}
		
	}
	
	private void checkItemTotalAmount(String totalAmount) {
		if(totalAmount == null || totalAmount.trim().isEmpty()){
			LOGGER.error("item total is null or empty.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"item total is null or empty.");
		}
		try{
			Double.valueOf(totalAmount.trim());
		}
		catch(NumberFormatException e){
			LOGGER.error("item total should be float.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"item total should be float.");
		}
	}

	private void checkPrice(String s) {
		if(s == null || s.trim().isEmpty()){
			LOGGER.error("item price total is null or empty.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"item price is null or empty.");
		}
		try{
			Double.valueOf(s.trim());
		}
		catch(NumberFormatException e){
			LOGGER.error("item price should be float.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"item price should be float.");
		}
	}

	private void checkUserCount(String s) {
		if(s == null || s.trim().isEmpty()){
			LOGGER.error("item use count total is null or empty.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"item use count is null or empty.");
		}
		try{
			Double.valueOf(s.trim());
		}
		catch(NumberFormatException e){
			LOGGER.error("item use count should be float.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"item use count should be float.");
		}
	}

	private void checkItemName(String itemName) {
		if(itemName == null || itemName.trim().isEmpty()){
			LOGGER.error("item name is null or empty.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"item name is null or empty.");
		}
	}




}
