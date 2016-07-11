package com.everhomes.util.excel.handler;

import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.organization.pm.CommunityPmBill;
import com.everhomes.organization.pm.CommunityPmBillItem;
import com.everhomes.rest.organization.pm.PmBillsDTO;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.excel.MySheetContentsHandler;
import com.everhomes.util.excel.RowResult;
import com.everhomes.util.excel.SAXHandlerEventUserModel;


public class PropMgrBillHandler
{
	/** 日志 */
	private static final Logger LOGGER = LoggerFactory.getLogger(PropMgrBillHandler.class);

	public static ArrayList processorExcel(File file)
	{
		ArrayList resultList = new ArrayList();
		MySheetContentsHandler sheetContenthandler=new MySheetContentsHandler();
		SAXHandlerEventUserModel userModel=new SAXHandlerEventUserModel(sheetContenthandler);
		try
		{
			userModel.processASheets(file,0);
			resultList=sheetContenthandler.getResultList();
		} catch (Exception e)
		{
			LOGGER.error("failed to processor the file ", e);
		}
		return resultList;
	}

	public static ArrayList processorExcel(InputStream is)
	{
		ArrayList resultList = new ArrayList();
		MySheetContentsHandler sheetContenthandler=new MySheetContentsHandler();
		SAXHandlerEventUserModel userModel=new SAXHandlerEventUserModel(sheetContenthandler);
		try
		{
			userModel.processASheets(is,0);
			resultList=sheetContenthandler.getResultList();
		} catch (Exception e)
		{
			LOGGER.error("failed to processor the file ", e);
		}
		return resultList;
	}

	public static List<CommunityPmBill> processorPropBill(long userId, long communityId,ArrayList resultList,int rowCount,int previousRow) {
		List<CommunityPmBill> billList = new ArrayList<CommunityPmBill>();
		if(resultList != null && resultList.size() > 0)
		{
			int row = resultList.size();
			//得到时间行数据
			RowResult titleResult = (RowResult)resultList.get(ProcessBillModel1.getDateRow());

			if((row - previousRow) % rowCount == 0)
			{
				int aptCount = (row -previousRow)/rowCount; 
				for (int rowIndex = 0; rowIndex < aptCount ; rowIndex++) {

					//生成账单总信息
					RowResult addressResult = (RowResult)resultList.get(ProcessBillModel1.getAddressRow(rowIndex));
					CommunityPmBill bill = new CommunityPmBill();
					bill.setDateStr(titleResult.getC());
					bill.setAddress(addressResult.getB());
					RowResult descriptionResult = (RowResult)resultList.get(ProcessBillModel1.getDescriptionRow(rowIndex));
					bill.setDescription(descriptionResult.getA());
					RowResult totalResult = (RowResult)resultList.get(ProcessBillModel1.getTotalRow(rowIndex));
					bill.setDueAmount(new BigDecimal(totalResult.getG()));
					//bill.setTotalAmount(new BigDecimal(totalResult.getG()));
					bill.setOrganizationId(communityId);
					bill.setCreatorUid(userId);
					bill.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
					bill.setNotifyCount(0);
					bill.setNotifyTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
					//生成账单项信息
					List<CommunityPmBillItem> itemList = new ArrayList<CommunityPmBillItem>();
					CommunityPmBillItem adminItem =  createPropMgrBillItem(resultList,ProcessBillModel1.getItemRow(rowIndex));
					CommunityPmBillItem rubbishItem =  createPropMgrBillItem(resultList,ProcessBillModel1.getItemRow(rowIndex)+1);
					CommunityPmBillItem sewageItem =  createPropMgrBillItem(resultList,ProcessBillModel1.getItemRow(rowIndex)+2);
					CommunityPmBillItem waterItem =  createPropMgrBillItem(resultList,ProcessBillModel1.getItemRow(rowIndex)+3);
					CommunityPmBillItem electricityItem =  createPropMgrBillItem(resultList,ProcessBillModel1.getItemRow(rowIndex)+4);
					CommunityPmBillItem gasItem =  createPropMgrBillItem(resultList,ProcessBillModel1.getItemRow(rowIndex)+5);
					itemList.add(adminItem);
					itemList.add(rubbishItem);
					itemList.add(sewageItem);
					itemList.add(waterItem);
					itemList.add(electricityItem);
					itemList.add(gasItem);
					bill.setItemList(itemList);

					billList.add(bill);
				}
			}

		}
		else
		{
			LOGGER.error("excel data format is not correct.rowCount=" +resultList.size()+" ,rowCount=" +rowCount +",previousRow=" +previousRow);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, 
					"物业水电费账单文件格式不正确。请检查该文件行数，总行数=标题行+单元行的整数倍。 文件总行数=" +resultList.size()+" ,单元行=" +rowCount +",标题行=" +previousRow);
		}
		return billList;
	}

	public static CommunityPmBillItem createPropMgrBillItem(ArrayList resultList, int i)
	{
		CommunityPmBillItem item = null ;
		if(resultList != null && resultList.size() > 0)
		{
			RowResult itemResult = (RowResult)resultList.get(i);
			item = new CommunityPmBillItem();
			item.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			item.setItemName(RowResult.trimString(itemResult.getA()));
			item.setStartCount(getBigDecimalValue(RowResult.trimString(itemResult.getB())));
			item.setEndCount(getBigDecimalValue(RowResult.trimString(itemResult.getC())));
			item.setUseCount(getBigDecimalValue(RowResult.trimString(itemResult.getD())));
			item.setPrice(getBigDecimalValue(RowResult.trimString(itemResult.getE())));
			item.setTotalAmount(getBigDecimalValue(RowResult.trimString(itemResult.getF())));
		}
		return item;
	}

	public static BigDecimal getBigDecimalValue(String value)
	{
		BigDecimal bigDecimal = new BigDecimal(0);
		if(value != null)
		{
			bigDecimal = new BigDecimal(value);
		}
		return bigDecimal;
	}

	public static String getPropBillDate(ArrayList resultList,int dateRow)
	{
		String date = "" ;
		if(resultList != null && resultList.size() > dateRow)
		{
			RowResult titleResult = (RowResult)resultList.get(ProcessBillModel1.getDateRow());
			date = titleResult.getC();
		}
		return date;
	}

	public static List<CommunityPmBill> processorPmBills(ArrayList resultList) {
		List<CommunityPmBill> billList = new ArrayList<CommunityPmBill>();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		
		if(resultList != null && resultList.size() > 0)
		{
			int rowCount = resultList.size();
			int startRow = 1;

			for (int rowIndex = startRow; rowIndex < rowCount ; rowIndex++) {
				RowResult rowResult = (RowResult)resultList.get(rowIndex);
				if(rowResult.getB() == null)
					continue;
				
				//生成账单总信息
				CommunityPmBill bill = new CommunityPmBill();
				bill.setId(null);

				try {
					bill.setStartDate(new Date(format.parse(rowResult.getA()).getTime()));
					bill.setEndDate(new Date(format.parse(rowResult.getB()).getTime()));
					bill.setPayDate(new Date(format.parse(rowResult.getC()).getTime()));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				bill.setAddress(rowResult.getD());
				bill.setDueAmount(rowResult.getE() == null ? null:new BigDecimal(rowResult.getE()));
				bill.setOweAmount(rowResult.getF() == null ? null:new BigDecimal(rowResult.getF()));
				bill.setDescription(rowResult.getG());
				
				billList.add(bill);
			}
		}

	return billList;
}

}
