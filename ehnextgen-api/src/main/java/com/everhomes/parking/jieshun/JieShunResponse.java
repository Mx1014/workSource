package com.everhomes.parking.jieshun;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.util.StringHelper;

public class JieShunResponse {
	
	private Integer resultCode;
	private String message;
	private JSONArray itemsJSONArray; // dataItems
	private JSONObject firstItemAttribute; //dataItems的第一个attribute
	
	private JSONArray firstSubItemsJSONArray; // subItems
	private JSONObject firstSubItemAttribute; //第一个subItems的第一个subItem的attribute
	private String totalJson; //用于保存返回的所有内容
	private String token; //如果是登陆请求有token字段
	
	public boolean isEmpty() {
		return null == firstItemAttribute;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}


	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTotalJson() {
		return totalJson;
	}

	public void setTotalJson(String totalJson) {
		this.totalJson = totalJson;
	}

	public JSONArray getItemsJSONArray() {
		return itemsJSONArray;
	}

	public void setItemsJSONArray(JSONArray itemsJSONArray) {
		this.itemsJSONArray = itemsJSONArray;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}


	public Integer getResultCode() {
		return resultCode;
	}


	public void setResultCode(Integer resultCode) {
		this.resultCode = resultCode;
	}


	public JSONObject getFirstItemAttribute() {
		return firstItemAttribute;
	}


	public void setFirstItemAttribute(JSONObject firstItemAttribute) {
		this.firstItemAttribute = firstItemAttribute;
	}


	public JSONObject getFirstSubItemAttribute() {
		return firstSubItemAttribute;
	}


	public void setFirstSubItemAttribute(JSONObject firstSubItemAttribute) {
		this.firstSubItemAttribute = firstSubItemAttribute;
	}

	public JSONArray getFirstSubItemsJSONArray() {
		return firstSubItemsJSONArray;
	}

	public void setFirstSubItemsJSONArray(JSONArray firstSubItemsJSONArray) {
		this.firstSubItemsJSONArray = firstSubItemsJSONArray;
	}

}
