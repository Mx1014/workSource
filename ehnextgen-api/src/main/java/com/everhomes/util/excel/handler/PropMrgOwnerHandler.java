package com.everhomes.util.excel.handler;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.excel.MySheetContentsHandler;
import com.everhomes.util.excel.SAXHandlerEventUserModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

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
	
	public static ArrayList processorExcel(InputStream is) {
		MySheetContentsHandler sheetContenthandler=new MySheetContentsHandler();
		try {
			SAXHandlerEventUserModel userModel=new SAXHandlerEventUserModel(sheetContenthandler);
			userModel.processASheets(is, 0);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Process excel error.", e);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"Process excel error.");
		}
		return sheetContenthandler.getResultList();
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
