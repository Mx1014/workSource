package com.everhomes.payment.util;

import java.math.BigDecimal;
import java.util.Map;

import com.google.gson.Gson;

public class Test {
public static void main(String[] args) {
	Gson gson = new Gson();
	String s = "{\"ProcStatus\": \"01\",\"Platform\": \"02\",\"TransAmt\": 0.12}";
	Map map = gson.fromJson(s, Map.class);
	BigDecimal amount = new BigDecimal(map.get("TransAmt").toString());
	System.out.println(amount);
	System.out.println(map.get("TransAmt").toString());
	Double d = new Double(0.12);
	System.out.println(d.toString());
}
}
