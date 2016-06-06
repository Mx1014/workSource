package com.everhomes.payment.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class T {
public static void main(String[] args){
	//  http://test.ippit.cn:30821/iccard/service
	Socket socket;
	try {
		socket = new Socket("test.ippit.cn", 30821);
	
	OutputStream os = socket.getOutputStream(); 
	boolean autoflush = true; 
	PrintWriter out = new PrintWriter(socket.getOutputStream(), autoflush);
	 
	BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream())); 
	String json = "msg="+Test.getJson();
	// send an HTTP request to the web server
//	StringBuffer sb = new StringBuffer();
//	sb.append("POST /iccard/service HTTP/1.1").append("\n");
//	sb.append("Host: test.ippit.cn:30821").append("\n");
//	sb.append("User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0").append("\n");
//	sb.append("Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8").append("\n");
//	sb.append("Accept-Language: zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3").append("\n");
//	sb.append("Accept-Encoding: gzip, deflate").append("\n");
//	sb.append("Connection: keep-alive").append("\n");
//	sb.append("Content-Type: application/x-www-form-urlencoded").append("\n");
//	sb.append("Content-Length: "+json.length()).append("\n");
//	sb.append("\r\n");
//	sb.append(json);
	
	out.println("POST /iccard/service HTTP/1.1");
	out.println("Host: test.ippit.cn:30821");
	out.println("User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0");
	out.println("Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
	out.println("Accept-Language: zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
	out.println("Accept-Encoding: gzip, deflate");
	out.println("Connection: keep-alive");
	out.println("Content-Type: application/x-www-form-urlencoded");
	out.println("Content-Length: "+json.length());
	//out.println("Connection: Close");
	out.println();
	out.println(json);
//	out.print(sb.toString());
	// read the response 
	StringBuffer sb1 = new StringBuffer(8096); 
	//while (loop) { 
		//if (reader.ready()) { 
			String s; 
			while ((s = reader.readLine()) != null) { 
				sb1.append(s).append("\r\n"); 
			} 
		//} 
	//}
	// display the response to the out console 
	System.out.println(sb1.toString()); 
	socket.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} 
}
}
