package com.everhomes.rest.xfyun;

import com.everhomes.util.StringHelper;


/**
 * <ul>
 *<li>MsgId           消息id，字符串类型</li>
 *<li>CreateTime      消息创建时间，整型</li>
 *<li>AppId           开发者应用Id，字符串类型</li>
 *<li>UserId          AIUI唯一用户标注，字符串类型</li>
 *<li>UserParams      开发者自定义参数，通过客户端的userparams参数上传，Base64格式字符串</li>
 *<li>FromSub         上游业务类型，目前包括两种（iat：听写结果，kc：语义结果），字符串类型</li>
 *<li>Msg             消息内容，json object参考Msg消息内容格式</li>
 *<li>SessionParams   本次会话交互参数，Base64格式字符串，解码后为json格式
 * </ul>
 */
public class AfterDealResponse {
	private String MsgId;
	private Long CreateTime;
	private String AppId;
	private String UserId;
	private String UserParams;
	private String FromSub;
	private String Msg;
	private String SessionParams;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public String getMsgId() {
		return MsgId;
	}

	public void setMsgId(String msgId) {
		MsgId = msgId;
	}

	public Long getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(Long createTime) {
		CreateTime = createTime;
	}

	public String getAppId() {
		return AppId;
	}

	public void setAppId(String appId) {
		AppId = appId;
	}

	public String getUserId() {
		return UserId;
	}

	public void setUserId(String userId) {
		UserId = userId;
	}


	public String getFromSub() {
		return FromSub;
	}

	public void setFromSub(String fromSub) {
		FromSub = fromSub;
	}

	public String getSessionParams() {
		return SessionParams;
	}

	public void setSessionParams(String sessionParams) {
		SessionParams = sessionParams;
	}

	public boolean isEmpty() {
		return false;
	}

	public String getUserParams() {
		return UserParams;
	}

	public void setUserParams(String userParams) {
		UserParams = userParams;
	}

	public String getMsg() {
		return Msg;
	}

	public void setMsg(String msg) {
		Msg = msg;
	}


}


/*

>HTTP消息格式定义如下：
    
    POST /yourserveruri?xx=xx HTTP 1.1
    
    Connection: close
    
    Host:xxx.xxx.xxx
    
    Content-Type: application/json
    
    Content-Length: 111
    
    {消息主体}
    
1.可以根据Content-Type的类型来解析相应的消息主体。例如：当POST请求的Content-Type为application/json时,要根据Json格式解析消息主体。
    
 > 1.消息主体格式
    
    {
    
    "MsgId":"1234567",
    
    "CreateTime":1348831860,
    
    "AppId":"12345678",
    
    "UserId":"d123455",
    
    "SessionParams":"Y21kPXNzYixzdWI9aWF0LHBsYXRmb3JtPWFuZG9yaWQ=",
    
    "UserParams":"PG5hbWU+eGlhb2JpYW5iaWFuPC9uYW1lPg==",
    
    "FromSub":"iat",
    
    "Msg":{},
    
    }
    
> 格式说明
    
    参数              描述
    
    MsgId           消息id，字符串类型
    
    CreateTime      消息创建时间，整型
    
    AppId           开发者应用Id，字符串类型
    
    UserId          AIUI唯一用户标注，字符串类型
    
    UserParams      开发者自定义参数，通过客户端的userparams参数上传，Base64格式字符串
    
    FromSub         上游业务类型，目前包括两种（iat：听写结果，kc：语义结果），字符串类型
    
    Msg             消息内容，json object参考Msg消息内容格式
    
    SessionParams   本次会话交互参数，Base64格式字符串，解码后为json格式
    
> 2.Msg消息内容格式
    
    文本内容
    
    {
    
    "Type":"text",
    
    "ContentType":"Json",
    
    "Content":"eyJzbiI6MiwibHMiOnRydWUsImJnIjowLCJlZCI6MCwid3MiOlt7ImJnIjowLCJjdyI6W3sic2MiOjAsInciOiLvvJ8ifV19XX0="
    
    }
    
    参数              描述
    
    Type            text
    
    ContentType     内容格式，Json plain xml
    
    Content         Base64内容字符串
    

*/