package com.everhomes.util.excel.handler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.everhomes.organization.pm.CommunityPmBill;


public class ProcessBillModel1
{
	//对应 华大公寓 的物业缴费通知解析excel。
	
	/** EXCEL表格每多少行对应一个实体对象 */
	public static final int PROCESS_TO_OBJECT_PER_ROW = 8;
	
	/** EXCEL表格最上面与数据无关的标题行数 */
	public static final int PROCESS_PREVIOUS_ROW_TITLE = 3;
	
	/** EXCEL表格的物业缴费时间（月份）的所在行数 */
	public static final int PROCESS_DATE_ROW_NUM = 3;
	
	/** 物业账单的账单生成时间 */
	public static String dateStr;
	//得到账单时间行偏移
	public static  int getDateRow() 
	{
		return PROCESS_DATE_ROW_NUM - 1;
	}
	
	//得到账单时间行偏移
	public static int getAddressRow(int CurrentRow) 
	{
		return (PROCESS_PREVIOUS_ROW_TITLE-1) + CurrentRow * PROCESS_TO_OBJECT_PER_ROW + (PROCESS_TO_OBJECT_PER_ROW - 1);
	}
	
	
	//得到账单说明行偏移
	public static int getDescriptionRow(int CurrentRow) 
	{
		return (PROCESS_PREVIOUS_ROW_TITLE-1) + CurrentRow * PROCESS_TO_OBJECT_PER_ROW + PROCESS_TO_OBJECT_PER_ROW;
	}
	
	//得到账单总费用行偏移
	public static int getTotalRow(int CurrentRow) 
	{
		return (PROCESS_PREVIOUS_ROW_TITLE-1) + CurrentRow * PROCESS_TO_OBJECT_PER_ROW + 1;
	}
	
	//得到账单项条目行偏移
	public static int getItemRow(int CurrentRow) 
	{
		return PROCESS_PREVIOUS_ROW_TITLE + CurrentRow * PROCESS_TO_OBJECT_PER_ROW ;
	}
	
	//生成账单对象数组
	public static List<CommunityPmBill> processorPropBill(File file,long userId,long communityId)
	{
		List<CommunityPmBill> billList = null ;
		ArrayList resultList = PropMgrBillHandler.processorExcel(file);
		setDateStr(PropMgrBillHandler.getPropBillDate(resultList, getDateRow()));
		billList = PropMgrBillHandler.processorPropBill(userId, communityId, resultList, PROCESS_TO_OBJECT_PER_ROW, PROCESS_PREVIOUS_ROW_TITLE);
		return billList;
	}
	
	
	public static String getDateStr()
	{
		return dateStr;
	}

	public static void setDateStr(String dateStr)
	{
		ProcessBillModel1.dateStr = dateStr;
	}
	
	
	
}
