package com.everhomes.organization.pm.handler;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.everhomes.util.excel.MySheetContentsHandler;
import com.everhomes.util.excel.SAXHandlerEventUserModel;

public abstract class PmBillsBaseHandler {
	
	public static final String PARSER_NAME_PREFIX = "com.everhomes.organization.pm.handler.PmBillsHandler";

	@SuppressWarnings("rawtypes")
	public List preParse(InputStream in){
		List list = new ArrayList();
		MySheetContentsHandler sheetContenthandler=new MySheetContentsHandler();
		SAXHandlerEventUserModel userModel=new SAXHandlerEventUserModel(sheetContenthandler);
		try{
			userModel.processASheets(in,0);
			list=sheetContenthandler.getResultList();
		} catch (Exception e){
			
		}
		return list;
	}
	
	@SuppressWarnings("rawtypes")
	abstract public void parse(List list,String filePath) throws Exception;
}
