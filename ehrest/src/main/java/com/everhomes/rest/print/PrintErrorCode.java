// @formatter:off
package com.everhomes.rest.print;

public class PrintErrorCode {

	public static final String SCOPE = "print";

	//eh_locale_strings
	public static final String PRINT_COURSE_LIST= "print_course_list";//打印教程 ‘|’做分割
	public static final String SCAN_COPY_COURSE_LIST= "scan_copy_course_list";//扫描复印教程 ‘|’做分割
	public static final String PRINT_SUBJECT= "print_subject";//打印订单
	public static final String PRINT_SURFACE= "print_surface";//*面
	
	//eh_configurations
	public static final String PRINT_SIYIN_SERVER_URL= "print.siyin.server.url";//司印服务器地址 : http://siyin.zuolin.com:8119
	public static final String PRINT_SIYIN_TIMEOUT= "print.siyin.timeout";//二维码失效时间 : 10
	public static final String PRINT_SIYIN_TIMEOUT_UNIT= "print.siyin.timeout.unit";//二维码失效时间的单位（1-秒/2-分/3-小时） ：2
	public static final String PRINT_DEFAULT_PRICE= "print.default.price";//打印默认价格 ： 0.1
	public static final String PRINT_INFORM_URL= "print.inform.url";//二维码内容 ：http://core.zuolin.com/evh/siyinprint/informPrint?identifierToken=
	public static final String PRINT_LOGON_SCAN_TIMOUT= "print.logon.scan.timout";//二维码是否被扫描检测的延迟时间,单位毫秒 ：10000
	public static final String PRINT_SIYIN_PATTERN= "print.siyin.pattern";//1:司印方配置成不解锁打印机，直接打印的模式，2:司印方配置成发送文档到打印机，需要解锁再打印的模式。
	public static final String PRINT_SIYIN_JOB_COUNT_TIMEOUT= "print.siyin.job.count.timeout";//用户正在打印任务数量放到redis中的，设置一个默认超时时间 10 单位分钟
}
