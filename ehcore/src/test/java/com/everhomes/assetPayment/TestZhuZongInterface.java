package com.everhomes.assetPayment;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.everhomes.http.HttpUtils;
import com.everhomes.rest.asset.zhuzong.QueryHouseByPhoneNumberDTO;
import com.everhomes.util.StringHelper;

public class TestZhuZongInterface {
	
	public static void main(String[] args) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("AccountCode", "sdgj");
		params.put("phone", "15650723221");
		try {
			String response = HttpUtils.post("http://139.129.204.232:8007/LsInterfaceServer/phoneServer/QueryHouseByPhoneNumber", params, 60, false);
			System.out.println(response);
			
			QueryHouseByPhoneNumberDTO queryHouseByPhoneNumberDTO = (QueryHouseByPhoneNumberDTO) StringHelper.fromJsonString(response, QueryHouseByPhoneNumberDTO.class);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
