package com.everhomes.organization.pm.handler;

import java.io.InputStream;
import java.util.List;
import java.util.concurrent.Callable;

public class HandlerCallable implements Callable<String>{
	
	private InputStream in ;
	private long orgId;
	private String filePath;
	
	public HandlerCallable(InputStream in,long orgId,String filePath){
		this.in = in;
		this.orgId = orgId;
		this.filePath = filePath;
	}
	
	@SuppressWarnings("rawtypes")
	public String call() throws Exception {
		HandlerType handlerType = HandlerType.fromOrgId(orgId);
		if(handlerType == null){
			handlerType = HandlerType.OTHER;
		}
		try{
			PmBillsBaseHandler handler = (PmBillsBaseHandler) Class.forName(PmBillsBaseHandler.PARSER_NAME_PREFIX+handlerType.getParserCode()).newInstance();
			List list = handler.preParse(in);
			handler.parse(list,filePath);
		}
		catch (Exception e){
			return e.getMessage();
		}
		return "success";
	}
	
	
}
