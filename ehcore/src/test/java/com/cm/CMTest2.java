package com.cm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tempuri.HelloWorld;
import org.tempuri.OfficeAppCMService;
import org.tempuri.OfficeAppCMServiceSoap;

import com.everhomes.http.HttpUtils;
import com.everhomes.rest.asset.zhuzong.HouseDTO;
import com.everhomes.rest.asset.zhuzong.QueryHouseByPhoneNumberDTO;
import com.everhomes.util.StringHelper;

public class CMTest2 {
	
	static void CMDispContract() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("Date", "2018-08-01");
		//params.put("phone", "15650723221");
		try {
			String response = HttpUtils.post("http://10.50.12.39/cm/WebService/OfficeApp-CM/OfficeApp_CMService.asmx", params, 600, false);
			System.out.println(response);
			/*QueryHouseByPhoneNumberDTO queryHouseByPhoneNumberDTO = (QueryHouseByPhoneNumberDTO) StringHelper.fromJsonString(response, QueryHouseByPhoneNumberDTO.class);
			List<HouseDTO> houseDTOs = queryHouseByPhoneNumberDTO.getResult();
			System.out.println(houseDTOs.get(0).getClient_name());*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	public static void main(String[] args) {
		// CMDispContract();
		// QueryCostByHouseList();
		// QueryCostDetailByID();

		// 创建一个MobileCodeWS工厂
		OfficeAppCMService factory = new OfficeAppCMService();
		// 根据工厂创建一个MobileCodeWSSoap对象
		OfficeAppCMServiceSoap mobileCodeWSSoap = factory.getOfficeAppCMServiceSoap();
		// 调用WebService提供的getMobileCodeInfo方法查询手机号码的归属地

		String helloWorld = mobileCodeWSSoap.createContract("测试11111111111");

		// String searchResult =
		// mobileCodeWSSoap.getMobileCodeInfo("15177196635", null);
		System.out.println(helloWorld);
	}

}
