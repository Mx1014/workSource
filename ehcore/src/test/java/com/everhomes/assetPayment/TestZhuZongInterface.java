package com.everhomes.assetPayment;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.everhomes.asset.ShenZhenWanAssetVendor;
import com.everhomes.http.HttpUtils;
import com.everhomes.rest.asset.zhuzong.QueryHouseByPhoneNumberDTO;
import com.everhomes.rest.asset.zhuzong.HouseDTO;
import com.everhomes.util.StringHelper;

public class TestZhuZongInterface {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ShenZhenWanAssetVendor.class);
	
	public static void main(String[] args) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("AccountCode", "sdgj");
		params.put("phone", "15650723221");
		try {
			String response = HttpUtils.post("http://139.129.204.232:8007/LsInterfaceServer/phoneServer/QueryHouseByPhoneNumber", params, 600, false);
			System.out.println(response);
			
			
			QueryHouseByPhoneNumberDTO queryHouseByPhoneNumberDTO = (QueryHouseByPhoneNumberDTO) StringHelper.fromJsonString(response, QueryHouseByPhoneNumberDTO.class);
			List<HouseDTO> houseDTOs = queryHouseByPhoneNumberDTO.getResult();
			System.out.println(houseDTOs.get(0).getClient_name());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
