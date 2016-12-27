package com.everhomes.pmtask.webservice;

import java.util.HashMap;
import java.util.Map;

import com.everhomes.util.StringHelper;


public class Test {
	static WorkflowAppDraftWebService service = new WorkflowAppDraftWebService();
	static WorkflowAppDraftWebServicePortType port = service.getWorkflowAppDraftWebServiceHttpPort();
	public static void main(String[] args) {
		test1();
	}
	static void test1(){
		String s = "{\"fileFlag\":\"2\",\"fileTitle\":\"test2016120501\",\"submitUserId\":\"13999999999\",\"headContent\":[{\"userName\":\"超级用户\",\"userId\":\"2\",\"phone\":\"13999999999\",\"company\":\"深圳公司\"}],\"formContent\":[{\"chooseStoried\":\"A306\",\"serviceType\":\"维修\",\"serviceClassify\":\"检修\",\"serviceContent\":\"年久失修\",\"fileType\":\"物业应急维修审批流程\",\"taskUrgencyLevel\":\"普通\",\"liaisonContent\":\"联络内容\",\"backDate\":\"2016-12-05\"}],\"enclosure\":[{\"fileName\":\"\",\"fileSuffix\":\"\",\"fileContent\":\"\"}]}";
		String json = port.worflowAppDraft(s);
		System.out.println(json);
	}
}
