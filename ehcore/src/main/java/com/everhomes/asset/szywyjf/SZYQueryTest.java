package com.everhomes.asset.szywyjf;

import static org.junit.Assert.*;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

public class SZYQueryTest {
	
	private static SZYQuery szyQuery = new SZYQuery();

	@Test
	public void testSync_TenancyContractData() {
		
		//szyQuery.sync_TenancyContractData(request);
	}

	@Test
	public void testSync_TenancyContractDetailed() {
		fail("Not yet implemented");
	}
	
	public static void main(String[] args) {
		String result = "[" + 
				"    {" + 
				"        \"number\": \"6293\"," + 
				"        \"tenancyName\": \"XXXX\"," + 
				"        \"tenCustomerDes\": \" 6293\"," + 
				"        \"tenancyType\": \" 0\"," + 
				"        \"receData\": [" + 
				"            {" + 
				"                \"SrartDate\": \"2018-01-01\"," + 
				"                \"endDate\": \"2018-01-31\"," + 
				"                \"fappamount\": \"100\"," + 
				"                \"factMount\": \"100\"," + 
				"                \"fmoneyDefine\": \"电费\"" + 
				"            }," + 
				"            {" + 
				"                \"SrartDate\": \"2018-02-01\"," + 
				"                \"endDate\": \"2018-02-28\"," + 
				"                \"fappamount\": \"100\"," + 
				"                \"factMount\": \"100\"," + 
				"                \"fmoneyDefine\": \"水费\"," + 
				"                \"fid\": \"weqwwrwerqw\"," + 
				"                \"roomNo\": \"123,466\"" + 
				"            }" + 
				"        ]" + 
				"    }," + 
				"    {" + 
				"        \"number\": \"789\"," + 
				"        \"tenancyName\": \"XXXX\"," + 
				"        \"tenCustomerDes\": \" 6293\"," + 
				"        \"tenancyType\": \" 0\"," + 
				"        \"receData\": [" + 
				"            {" + 
				"                \"SrartDate\": \"2018-01-01\"," + 
				"                \"endDate\": \"2018-01-31\"," + 
				"                \"fappamount\": \"100\"," + 
				"                \"factMount\": \"100\"," + 
				"                \"fmoneyDefine\": \"电费\"" + 
				"            }," + 
				"            {" + 
				"                \"SrartDate\": \"2018-02-01\"," + 
				"                \"endDate\": \"2018-02-28\"," + 
				"                \"fappamount\": \"100\"," + 
				"                \"factMount\": \"100\"," + 
				"                \"fmoneyDefine\": \"水费\"," + 
				"                \"fid\": \"weqwwrwerqw\"," + 
				"                \"roomNo\": \"123,466\"" + 
				"            }" + 
				"        ]" + 
				"    }" + 
				"]";
		
		/*JSONArray jsonArray = JSON.parseArray(result);
		for(int i = 0;i < jsonArray.size();i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			System.out.println(jsonObject.get("number"));
			System.out.println(jsonObject.get("tenancyName"));
			System.out.println(jsonObject.get("receData"));
			JSONArray receDataJsonArray = (JSONArray) jsonObject.get("receData");
			for(int j = 0;j < receDataJsonArray.size();j++) {
				JSONObject receDataJSONObject = receDataJsonArray.getJSONObject(j);
				System.out.println(receDataJSONObject.get("SrartDate"));
				System.out.println(receDataJSONObject.get("endDate"));
				System.out.println(receDataJSONObject.get("fappamount"));
				System.out.println(receDataJSONObject.get("factMount"));
				System.out.println(receDataJSONObject.get("fmoneyDefine"));
				System.out.println(receDataJSONObject.get("fid") != null ? receDataJSONObject.get("fid").toString() : null);
			}
		}*/
		
		result = "{" + 
				"    \"Reason\": \"返回数据失败\"," + 
				"    \"Number\": \"\"," + 
				"    \"Result\": \"0\"" + 
				"}";
		/*try {
			jsonArray = JSON.parseArray(result);
			for(int i = 0;i < jsonArray.size();i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				System.out.println(jsonObject.get("number"));
				System.out.println(jsonObject.get("tenancyName"));
				System.out.println(jsonObject.get("receData"));
				JSONArray receDataJsonArray = (JSONArray) jsonObject.get("receData");
				for(int j = 0;j < receDataJsonArray.size();j++) {
					JSONObject receDataJSONObject = receDataJsonArray.getJSONObject(j);
					System.out.println(receDataJSONObject.get("SrartDate"));
					System.out.println(receDataJSONObject.get("endDate"));
					System.out.println(receDataJSONObject.get("fappamount"));
					System.out.println(receDataJSONObject.get("factMount"));
					System.out.println(receDataJSONObject.get("fmoneyDefine"));
					System.out.println(receDataJSONObject.get("fid"));
				}
			}
		} catch (JSONException e) {
			// TODO: handle exception
			JSONObject jsonObject = JSON.parseObject(result);
			System.out.println(jsonObject.get("Reason"));
		}*/
		
		String fid = "jYoAAA";
		Long billGroupId = Long.valueOf(fid);
		
		
	}

}
