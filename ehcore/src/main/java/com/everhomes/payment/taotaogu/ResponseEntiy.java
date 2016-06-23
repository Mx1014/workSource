package com.everhomes.payment.taotaogu;

import java.util.List;
import java.util.Map;

import com.everhomes.util.StringHelper;

public class ResponseEntiy {
	private String RespCode;
	private String AppName;
	private String Version;
	private String MsgType;
	private String MsgID;
	private Object Result;
	
	public String getAppName() {
		return AppName;
	}
	public void setAppName(String appName) {
		AppName = appName;
	}
	public String getRespCode() {
		return RespCode;
	}
	public void setRespCode(String respCode) {
		RespCode = respCode;
	}
	
	public String getVersion() {
		return Version;
	}
	public void setVersion(String version) {
		Version = version;
	}
	public String getMsgType() {
		return MsgType;
	}
	public void setMsgType(String msgType) {
		MsgType = msgType;
	}
	public Object getResult() {
		return Result;
	}
	public void setResult(Object result) {
		Result = result;
	}
	
	public String getMsgID() {
		return MsgID;
	}
	public void setMsgID(String msgID) {
		MsgID = msgID;
	}
	public boolean isSuccess(){
		if("00".equals(this.RespCode))
			return true;
		return false;
	}
	@SuppressWarnings("rawtypes")
	public Map getData(){
		if(null == this.Result)
			return null;
		List list = (List) this.Result;
		Map map = null;
		if(!list.isEmpty()){
			map = (Map) list.get(0);
		}
		return map;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
