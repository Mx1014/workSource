package com.everhomes.assetPayment;

import static org.junit.Assert.fail;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.asset.szwwyjf.SZWQuery;
import com.everhomes.rest.asset.ShowBillDetailForClientResponse;

public class SZWQueryTest {
	
	private SZWQuery szyQuery = new SZWQuery();

	@Test
	public void testSZWQuery() {
		fail("Not yet implemented");
	}

	@Test
	public void testShowBillForClientV2() {
		fail("Not yet implemented");
	}

	@Test
	public void testListAllBillsForClient() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetBillDetailForClient() {
		JSONObject jsonObject = new JSONObject();
		//由于查询的是全部，金蝶对接需要时间段，所以设置一个足够大的时间范围
		jsonObject.put("startDate", "1900-01-01");
		jsonObject.put("endDate", "2900-01-01");
		//初始默认展示所有的未缴账单 (1：已缴，0：未缴，2：全部)
		jsonObject.put("state", "2");
		//通过WebServices接口查询数据
		//测试参数
		jsonObject.put("cusName", "深圳市石榴裙餐饮有限公司");
		jsonObject.put("type", "0");
		jsonObject.put("fid", "QgwAAAAIZD4x0Rp+");
		ShowBillDetailForClientResponse response = szyQuery.getBillDetailForClient(jsonObject.toString());
	}

}
