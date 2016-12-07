package com.everhomes.parking;

import java.net.URL;

import com.alibaba.fastjson.JSONObject;
import com.bosigao.cxf.Service1;
import com.bosigao.cxf.Service1Soap;
import com.bosigao.cxf.rest.BosigaoCardInfo;
import com.everhomes.organization.pm.pay.ResultHolder;

public class Test2 {
	static String[] arr = {"粤BD225W","粤B1H0V6","粤BMP272","粤B726ZF","粤BG7Y32","粤BJ659P","粤B182VN","粤BVN732","粤B217EL","粤B590VB","粤BT65Y0","粤B20P15","粤BV51E8","粤BM05P5","粤BJ270M","粤BBV528","粤B105CU","粤B8Y1T1","粤BQ5G52","粤BD898K","粤BK256P","粤B36P88","粤B2UC59","粤B898LG","粤B45G62","粤BK122T","粤B482E6","粤B692BW","粤B3AM70","粤BKP750","粤B568DE","粤B636SZ","粤BP3N50","粤B07W05","粤BRZ660","粤B0C15W","粤BP2218","粤BE83S8","粤BN97R9","粤B0640P","粤B6RL21","粤B8X9K6","粤B9WW21","粤B7JM09","粤B850CY","粤A985S5","粤BH77Z1","粤A3910Y","粤BN377Y","粤BG539K","粤B251NP","粤B852TV","粤B618KX","粤B739YU","粤BV220M","粤B4Z332","粤BE06K6","粤BE06K6","粤B700NB","粤B967NN","粤BH79B5","粤B4271Q","粤B8726J","粤BM22N8","粤BV9R36"};
	public static void main(String[] args) {
		URL wsdlURL = Service1.WSDL_LOCATION;
		Service1 ss = new Service1(wsdlURL, Service1.SERVICE);
	    Service1Soap port = ss.getService1Soap12();
//	    String json = port.cardPayMoney("", "粤B20P15", "2", "0", "20161130", "20170228", System.currentTimeMillis()+"", "sign");
	    for(String s: arr) {
	    	String json = port.getCardInfo("", s, "2", "sign");
		    
		    ResultHolder resultHolder = JSONObject.parseObject(json, ResultHolder.class);
		    
		    BosigaoCardInfo card = null;
		    
		    if(resultHolder.isSuccess()){
		    	String cardJson = JSONObject.parseObject(resultHolder.getData().toString()).get("card").toString();
		        card = JSONObject.parseObject(cardJson, BosigaoCardInfo.class);
		    }
		    System.out.println("update eh_parking_recharge_orders set plate_owner_name = '"+card.getUserName()+"' where plate_number = "+s+"; ");
	    }
	    
	}
}



