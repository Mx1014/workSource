package com.everhomes.util.excel.handler;

import java.io.File;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.organization.pm.CommunityPmOwner;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.excel.MySheetContentsHandler;
import com.everhomes.util.excel.RowResult;
import com.everhomes.util.excel.SAXHandlerEventUserModel;

public class PropMrgOwnerHandler
{
	/** 日志 */
	 private static final Logger LOGGER = LoggerFactory.getLogger(PropMrgOwnerHandler.class);
	
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
	
	public static List<CommunityPmOwner> processorPropMgrContact(long userId, long communityId,ArrayList resultList) {
		List<CommunityPmOwner> contactList = new ArrayList<CommunityPmOwner>();
		if(resultList != null && resultList.size() > 0) {
			int row = resultList.size();
			if(resultList != null && resultList.size() > 0) {
				for (int rowIndex = 1; rowIndex < row ; rowIndex++) {
						RowResult result = (RowResult)resultList.get(rowIndex);
						CommunityPmOwner owner = new CommunityPmOwner();
						owner.setContactName(RowResult.trimString(result.getA()));
						owner.setContactType(processtype(RowResult.trimString(result.getB())));
						owner.setContactToken(RowResult.trimString(result.getC()));
						owner.setAddress(RowResult.trimString(result.getD()));
						owner.setContactDescription(RowResult.trimString(result.getE()));
						owner.setCreatorUid(userId);
						owner.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
						owner.setOrganizationId(communityId);
						contactList.add(owner);
				}
			}
		}
		else
		{
			LOGGER.error("excel data format is not correct.rowCount=" +resultList.size());
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, 
                    "excel data format is not correct");
		}
		return contactList;
	}

	private static Byte processtype(String typeName) {
		if(typeName != null && typeName.equals(IdentifierType.EMAIL))
		{
			return IdentifierType.EMAIL.getCode();
		}
		else
		{
			return IdentifierType.MOBILE.getCode();
		}
		
	}

}
