package com.everhomes.parking;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.pmtask.webservice.WorkflowAppDraftWebService;
import com.everhomes.pmtask.webservice.WorkflowAppDraftWebServicePortType;
import com.everhomes.util.RuntimeErrorException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by Administrator on 2017/2/21.
 */
public class Test {
    static CloseableHttpClient httpclient = HttpClients.createDefault();


    public static void main(String[] args) {
        JSONObject param = new JSONObject();

//        JSONObject header = new JSONObject();
//        header.put("SERVICE_ID","70111002");
//
//        JSONObject data = new JSONObject();
//        data.put("version","1.4");
//        data.put("licensekey","44030520000420161231235959102643");
//        data.put("car_id","鄂A00002");
//        data.put("park_name","测试停车场");
//        data.put("start_date","20170828000000");
//        data.put("buy_num","2");
//
//        param.put("REQ_MSG_HDR", header);
//        param.put("REQ_COMM_DATA", data);
//
//        JSONArray array = new JSONArray();
//        array.add(param);
//
//        JSONObject p = new JSONObject();
//        p.put("REQUESTS",array);

//        System.out.println(p.toJSONString());
//        System.out.println(post(p, null));
//       System.out.println(listFee());
//        System.out.println(getCard());
//        System.out.println(listFee());
//        int[] arr = {4,5,6,23,1,2,3};
//        System.out.println(bubble(arr));
//        System.out.println(getTempFee());
        WorkflowAppDraftWebService service = new WorkflowAppDraftWebService();
        WorkflowAppDraftWebServicePortType port = service.getWorkflowAppDraftWebServiceHttpPort();
        String result = port.worflowAppDraft(param.toJSONString());
        System.out.println(result);
    }

    public static String getTempFee() {
        JSONObject param = new JSONObject();

        JSONObject header = new JSONObject();
        header.put("SERVICE_ID","70111005");

        JSONObject data = new JSONObject();
        data.put("version","1.4");
        data.put("licensekey","44030520000420161231235959102643");
        data.put("car_id","鄂ADDDDD");

        param.put("REQ_MSG_HDR", header);
        param.put("REQ_COMM_DATA", data);

        JSONArray array = new JSONArray();
        array.add(param);

        JSONObject p = new JSONObject();
        p.put("REQUESTS",array);

        return post(p, null);
    }

    public static String getCard() {
        JSONObject param = new JSONObject();

        JSONObject header = new JSONObject();
        header.put("SERVICE_ID","70111003");

        JSONObject data = new JSONObject();
        data.put("version","1.4");
        data.put("licensekey","44030520000420161231235959102643");
        data.put("car_id","鄂A00002");

        param.put("REQ_MSG_HDR", header);
        param.put("REQ_COMM_DATA", data);

        JSONArray array = new JSONArray();
        array.add(param);

        JSONObject p = new JSONObject();
        p.put("REQUESTS",array);

        return post(p, null);
    }


    public static String listFee() {
        JSONObject param = new JSONObject();

        JSONObject header = new JSONObject();
        header.put("SERVICE_ID","70111001");

        JSONObject data = new JSONObject();
        data.put("version","1.4");
        data.put("licensekey","44030520000420161231235959102643");
        data.put("park_name","测试停车场");

        param.put("REQ_MSG_HDR", header);
        param.put("REQ_COMM_DATA", data);

        JSONArray array = new JSONArray();
        array.add(param);

        JSONObject p = new JSONObject();
        p.put("REQUESTS",array);

        return post(p, null);

    }

    public static String post(JSONObject param, String type) {
        HttpPost httpPost = new HttpPost("http://120.55.114.140:8085/kesb_req");
        String result = new String();

        CloseableHttpResponse response = null;
        try {
            StringEntity stringEntity = new StringEntity(param.toString(), "utf8");
            httpPost.setEntity(stringEntity);
            response = httpclient.execute(httpPost);

            int status = response.getStatusLine().getStatusCode();

            if (status == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    result = EntityUtils.toString(entity, "utf8");
                }
            }
        } catch (IOException e) {
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                    "Parking request error.");
        }finally {
            try {
                response.close();
            } catch (IOException e) {
            }
        }
        return result;
    }
    private static int bubble(int[] list) {
        int size = list.length;
        for (int i = size - 1; i > 0 ; i--) {
            for (int j = 0; j < i; j++) {
                if (list[j] > list[j+1]) {
                    int temp = list[j];
                    list[j] = list[j+1];
                    list[j+1] = temp;
                }
            }
        }
        return list[size-1];
    }

}
