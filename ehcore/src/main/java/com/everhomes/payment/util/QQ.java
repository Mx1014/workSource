package com.everhomes.payment.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;

public class QQ {
	public static void main(String[] args) {
		list();

	}
	public static void list(){
		try{
		      int port = 8080;
		      Socket socket = new Socket("127.0.0.1", port);
		      
		      String data = "userName=尹秀容&userContact=13800010001&payerId=";

		      // 发送数据头
		      BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		      wr.write("POST /evh/pmsy/listAddresses HTTP/1.1\r\n");
		      wr.write("Host: 127.0.0.1:8080\r\n");
		      wr.write("User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0\r\n");
		      wr.write("Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\r\n");
		      wr.write("Content-Length: " + data.length() + "\r\n");
		      wr.write("Content-Type: application/x-www-form-urlencoded\r\n");
		      wr.write("Connection: keep-alive\r\n");
		      wr.write("Cookie: JSESSIONID=609A490C746183A64A9356A53164DDF7; token=KbypzoLj1LPqV5XvHGRmaNk7b23SEEJE9_scVzQdexIXOQ-DYs9nfi4iHZW4sUMo3CxODB6ih-y3YHPNQQAivsCMZHo2KyFp9PdRVxQYml4FatjcF5SRCh4Dt8hrrbvj; namespace_id=0\r\n");
		      wr.write("\r\n"); // 以空行作为分割
		      // 发送数据
		      wr.write(data);
		      wr.flush();
		      // 读取返回信息
		      BufferedReader rd = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		      String line;
		      while ((line = rd.readLine()) != null) {
		        System.out.println(line);
		      }
		      wr.close();
		      rd.close();
		    } catch (Exception e) {
		    	e.printStackTrace();
		    }
	}
	
	 public static String sendPost() {
	    	String url = "http://localhost:8080/evh/pmsy/listAddresses";
	    	String param = "userName=尹秀容&userContact=13800010001&payerId=";
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
	            conn.setRequestProperty("Cookie", "JSESSIONID=609A490C746183A64A9356A53164DDF7; token=KbypzoLj1LPqV5XvHGRmaNk7b23SEEJE9_scVzQdexIXOQ-DYs9nfi4iHZW4sUMo3CxODB6ih-y3YHPNQQAivsCMZHo2KyFp9PdRVxQYml4FatjcF5SRCh4Dt8hrrbvj; namespace_id=0");
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