package com.everhomes.module;

import com.alibaba.fastjson.JSON;
import com.everhomes.flow.FlowModuleInfo;
import com.everhomes.rest.acl.*;
import com.everhomes.rest.address.CommunityDTO;
import com.everhomes.rest.module.*;
import com.everhomes.rest.portal.ServiceModuleAppDTO;
import com.everhomes.rest.portal.TreeServiceModuleAppsResponse;
import com.everhomes.util.GsonUtil;
import com.everhomes.util.StringHelper;
import org.jooq.tools.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * 业务路由接口，可参考实现类ActivityRouterListener
 */
public interface RouterListener {

	/**
	 * 返回当前业务的模块Id
	 * @return
	 */
	Long getModuleId();

//	/**
//	 * 业务定义所有路由。
//	 * 系统中提供了一个通过名称和模块快速获取路由的方法RouterInfoServiceImpl.getRouterInfo
//	 * 在这个接口中只用返回name和path，这两个参数是固定的，不依赖应用什么
//	 */
//	List<RouterInfo> listRouterInfos();


	/**
	 * 拼接路由参数，此处默认为将json中的参数全部拼接，业务也可以实现自己的
	 * @param queryJson
	 */
	default String getQueryString(String queryJson){

		Map<String, Object> parse = (Map)JSON.parse(queryJson);

		if(parse == null || parse.size() == 0){
			return null;
		}
		StringBuffer queryBuffer = new StringBuffer();

		for (Map.Entry entry: parse.entrySet()){
			try {
				queryBuffer.append(entry.getKey());
				queryBuffer.append("=");
				queryBuffer.append(URLEncoder.encode(entry.getValue().toString(), "UTF-8"));
				queryBuffer.append("&");
			} catch (UnsupportedEncodingException e) {
				//给出错误提示信息
				e.printStackTrace();
			}
		}

		if(queryBuffer.length() > 0 ){
			return queryBuffer.substring(0, queryBuffer.length() - 1);
		}
		return null;

	}
}
