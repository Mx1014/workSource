package com.everhomes.asset.pmsy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.util.RuntimeErrorException;

public class PmsyHttpUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(PmsyHttpUtil.class);

	public static String post(String url,String p0, String p1, String p2, String p3, String p4, String p5, String p6, String p7){
		CloseableHttpClient httpclient = HttpClients.createDefault();
		String result = null;
        try {

            HttpPost httpPost = new HttpPost(url);
            List <NameValuePair> nvps = new ArrayList <NameValuePair>();
            nvps.add(new BasicNameValuePair("strToken", "syswin"));
            nvps.add(new BasicNameValuePair("p0", p0));
            nvps.add(new BasicNameValuePair("p1", p1));
            nvps.add(new BasicNameValuePair("p2", p2));
            nvps.add(new BasicNameValuePair("p3", p3));
            nvps.add(new BasicNameValuePair("p4", p4));
            nvps.add(new BasicNameValuePair("p5", p5));
            nvps.add(new BasicNameValuePair("p6", p6));
            nvps.add(new BasicNameValuePair("p7", p7));
            if(LOGGER.isDebugEnabled()) {
    			LOGGER.debug("Request to siyuan, url={}, param={}", url, nvps);
    		}
            httpPost.setEntity(new UrlEncodedFormEntity(nvps,"utf-8"));
            CloseableHttpResponse response = httpclient.execute(httpPost);

            int status = response.getStatusLine().getStatusCode();
            if(status != 200){
            	LOGGER.error("the request of siyuan is {}.",response.getStatusLine());
    			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, 
    					"the request of siyuan is fail.");
            }
            if(status == 200){
                HttpEntity entity = response.getEntity();
                byte[] bytes = new byte[1024];
                StringBuilder sb = new StringBuilder();
                if (entity != null) {
                	InputStream instream = entity.getContent();
                	BufferedReader reader =new BufferedReader(new InputStreamReader(instream,"utf8"));
//                	int l = 0;
//                	while((l = instream.read(bytes, 0, bytes.length)) != -1){
//                		sb.append(new String(bytes,0,l,"UTF-8"));
//                	}
                	String s;
                	while((s = reader.readLine()) != null){
                		sb.append(s);
                	}
                	//System.out.println(sb.toString());
                	if(LOGGER.isDebugEnabled()) {
	        			LOGGER.debug("Response from siyuan, result={}", sb.toString());
	        		}
                			//sb.substring(sb.indexOf(">{")+2, sb.indexOf("</string>"));
                	result = sb.substring(sb.indexOf(">{")+1, sb.indexOf("</string>"));
                	if(LOGGER.isDebugEnabled()) {
	        			LOGGER.debug("Response from siyuan, result->substring={}", result);
	        		}
                			// do something useful with the response
                	instream.close();
                }
                	// do something useful with the response body
                	// and ensure it is fully consumed
                EntityUtils.consume(entity);
             }
             response.close();
        } catch (IOException e) {
			e.printStackTrace();
		} finally {
            try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        return result;
	}
	public static void main(String[] args) {
		
//String json1 = PmsyHttpUtil.post("UserRev_GetFeeList", "00100120091000000001", "","", "00100020090900000003", "01", "", "");
//System.out.println(json1);
//Gson gson = new Gson();
//		System.out.println(json1);
//		Map map_ = gson.fromJson(json1, Map.class);
//		System.out.println(map_.toString());
//		List list_ = (List) map_.get("UserRev_GetFeeList");
//		Map map2_ = (Map) list_.get(0);
//		List list2_ = (List) map2_.get("Syswin");
//		sendPost();
//		String json = post("UserRev_OwnerVerify","尹秀容","13800010001","","","","","");
//		Gson gson = new Gson();
//		Map map = gson.fromJson(json, Map.class);
//		System.out.println(map);
//		List list = (List) map.get("UserRev_OwnerVerify");
//		Map map2 = (Map) list.get(0);
//		List list2 = (List) map2.get("Syswin");
//		Map map3 = (Map) list2.get(0);
//		System.out.println(map3.get("ProjectID"));
//		System.out.println(map3.get("ProjectName"));
//		System.out.println(map3.get("CttID"));
//		System.out.println(map3.get("CusID"));
//		System.out.println(map3.get("ResID"));
//		System.out.println(map3.get("ResCode"));
//		System.out.println(map3.get("ResName"));
//		System.out.println(map3.get("BuildingCode"));
//		System.out.println(map3.get("BuildingName"));
//		System.out.println(map3.get("IDCardName"));
//		System.out.println(map3.get("IDCardNo"));
//		System.out.println(map3.get("CusProperty"));
//		StringBuilder resourceName = new StringBuilder();
//		resourceName.append(map3.get("ProjectName"))
//					.append(" ")
//					.append(map3.get("BuildingName"))
//					.append(" ")
//					.append(map3.get("ResName"));
//		System.out.println(resourceName.toString());
//		MonthlyBill mb1 = new MonthlyBill();
//		mb1.setBillDateStr(1414771200000L);
//		MonthlyBill mb2 = new MonthlyBill();
//		mb2.setBillDateStr(1412092800000L);
//		MonthlyBill mb3 = new MonthlyBill();
//		mb3.setBillDateStr(1420041600000L);
//		List<MonthlyBill> list = new ArrayList<MonthlyBill>();
//		list.add(mb1);
//		list.add(mb2);
//		list.add(mb3);
//		Collections.sort(list);
//		list.stream().forEach(r -> System.out.println(r.getBillDateStr()));
	}
	
	
	 /**
     * 向指定 URL 发送POST方法的请求
     * 
     * @param url
     *            发送请求的 URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost() {
    	String url = "http://sysuser.kmdns.net:9090/NetApp/SYS86Service.asmx/GetSYS86Service";
    	String param = "strToken=syswin&p0=UserRev_GetFeeList&p1=00100120091000000001&p2=&p3=&p4=00100020090900000003&p5=01&p6=&p7=";
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            System.out.println(result);
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }    
	
}
