package com.everhomes.rest.techpark.rental;

import java.math.BigDecimal;

import com.everhomes.util.StringHelper;
/**
 * <ul>  
* <li>	version	:	版本号	</li> 
* <li>	errorCode	:	响应结果,200-成功,500-失败	</li> 
* <li>	errorScope	:	错误范围	</li> 
* <li>	errorDescription	:	错误描述	</li> 
* <li>	errorDetails	:	错误详情	</li> 
* <li>	response	:	响应数据	-退款url</li> 
 * </ul>
 */
public class PayZuolinRefundResponse {
	private 	String	version	;
	private 	Integer	errorCode	;
	private 	String	errorScope	;
	private 	String	errorDescription	;
	private 	String	errorDetails	;
	private 	String	response	;
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public Integer getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorScope() {
		return errorScope;
	}
	public void setErrorScope(String errorScope) {
		this.errorScope = errorScope;
	}
	public String getErrorDescription() {
		return errorDescription;
	}
	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}
	public String getErrorDetails() {
		return errorDetails;
	}
	public void setErrorDetails(String errorDetails) {
		this.errorDetails = errorDetails;
	}
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	
}
