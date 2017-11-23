package com.everhomes.parking;

import java.util.HashMap;
import java.util.Map;

import com.everhomes.parking.bosigao2.ParkWebService;
import com.everhomes.parking.bosigao2.ParkWebServiceSoap;
import com.everhomes.util.StringHelper;


public class Bosigao2Test {
	static ParkWebService service = new ParkWebService();
	static ParkWebServiceSoap port = service.getParkWebServiceSoap();
	public static void main(String[] args) {
		test();
	}
	static void test1(){
		Map map = new HashMap<>();
		map.put("clientID", "207");
		String data = StringHelper.toJsonString(map);
		
		String json = port.parkingSystemRequestService("", "Parking_GetMonthCardDescript", data, "33311");
		System.out.println(json);
	}
	static void test(){
		
		Map map = new HashMap<>();
		map.put("clientID", "207");
		map.put("cardCode", "");
		map.put("plateNo", "苏E01733");
		map.put("flag", "2");
		String data = StringHelper.toJsonString(map);
		
		String json = port.parkingSystemRequestService("", "Parking_GetMonthCard", data, "33311");
		System.out.println(json);
		
		Map map2 = new HashMap<>();
		map2.put("clientID", "207");
		map2.put("cardCode", "1111");
		map2.put("plateNo", "");
		map2.put("flag", "1");
		map2.put("payMos", "1");
		map2.put("amount", "11000");
		map2.put("payDate", "20160817094721");
		map2.put("chargePaidNo", "100");
		
		String data2 = StringHelper.toJsonString(map2);
		String json2 = port.parkingSystemRequestService("", "Parking_MonthlyFee", data2, "33311");
		System.out.println(json2);
	}
}
