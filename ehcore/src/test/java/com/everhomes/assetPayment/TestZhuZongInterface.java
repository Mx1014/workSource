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
import com.everhomes.rest.asset.zhuzong.CostDTO;
import com.everhomes.rest.asset.zhuzong.CostDetailDTO;
import com.everhomes.rest.asset.zhuzong.CostByHouseListDTO;
import com.everhomes.rest.asset.zhuzong.HouseDTO;
import com.everhomes.rest.asset.zhuzong.QueryCostByHouseListDTO;
import com.everhomes.rest.asset.zhuzong.QueryCostDetailByIDDTO;
import com.everhomes.util.StringHelper;

public class TestZhuZongInterface {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ShenZhenWanAssetVendor.class);
	
	static void QueryHouseByPhoneNumber() {
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
	
	static void QueryCostByHouseList() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("AccountCode", "sdgj");
		params.put("houseid", "00181137AC00AABD022A,00181137B5001AF28660,00181137B800D7DEAF9E");
		params.put("clientid", "003217972D0026608956,003217972D0026608956,003217972D0026608956");
		params.put("onlyws", "0");
		params.put("pageOprator", "next");
		params.put("currenpage", "0");
		try {
			String response = HttpUtils.post("http://139.129.204.232:8007/LsInterfaceServer/phoneServer/QueryCostByHouseList", params, 600, false);
			System.out.println(response);
			QueryCostByHouseListDTO queryCostByHouseListDTO = 
					(QueryCostByHouseListDTO) StringHelper.fromJsonString(response, QueryCostByHouseListDTO.class);
			CostByHouseListDTO costByHouseListDTO = queryCostByHouseListDTO.getResult();
			List<CostDTO> costDTOs = costByHouseListDTO.getDatas();
			Integer totalpage = costByHouseListDTO.getTotalpage();//获取总页数
			for(int currentpage = 1;currentpage < totalpage;currentpage++) {
				params.put("currenpage", new Integer(currentpage).toString());
				response = HttpUtils.post("http://139.129.204.232:8007/LsInterfaceServer/phoneServer/QueryCostByHouseList", params, 600, false);
				queryCostByHouseListDTO = 
						(QueryCostByHouseListDTO) StringHelper.fromJsonString(response, QueryCostByHouseListDTO.class);
				costByHouseListDTO = queryCostByHouseListDTO.getResult();
				costDTOs.addAll(costByHouseListDTO.getDatas());
			}
			System.out.println(costDTOs.get(0).getFeename());
			System.out.println(costDTOs.get(0).getHousename());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	static void QueryCostDetailByID() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("AccountCode", "sdgj");
		params.put("feeid", "E1LL1Q1W7CX9P4USVRX0");
		try {
			String response = HttpUtils.post("http://139.129.204.232:8007/LsInterfaceServer/phoneServer/QueryCostDetailByID", params, 600, false);
			System.out.println(response);
			QueryCostDetailByIDDTO queryCostDetailByIDDTO = 
					(QueryCostDetailByIDDTO) StringHelper.fromJsonString(response, QueryCostDetailByIDDTO.class);
			List<CostDetailDTO> costDetailDTOs = queryCostDetailByIDDTO.getResult();
			System.out.println(costDetailDTOs.get(0).getHouseName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		//QueryHouseByPhoneNumber();
		QueryCostByHouseList();
		//QueryCostDetailByID();
	}

}
