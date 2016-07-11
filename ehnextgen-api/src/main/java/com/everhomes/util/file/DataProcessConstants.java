package com.everhomes.util.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;


public class DataProcessConstants
{
	//后台服务器 导入数据（小区，公寓）的常量：
	
	/**  导入数据时每行数据的最大个数: 小区-15，公寓-8*/
	public static int DATA_LENGTH_ZISE_COMMUNITY = 15;
	public static int DATA_LENGTH_ZISE_APARTMENT = 9;
	public static int DATA_LENGTH_ZISE_PROPERTY = 8;
	
	/**  导入物业商家时物业类型 : 9*/
	public static int BIZ_CATEGORY_PROPERTY = 9;
	
	/**  导入物业商家条目信息的条目类型 : 1-电话*/
	public static int BIZ_PROPERTY_ITEM_TYPE_PHONE = 1;
	
	
	
	/**  导入小区数据时每一列数据对应的索引值。*/
	//第一列是序号，后台不作处理 （解析内容时第一个中文字符解析有问题。）
	public static int DATA_COMMUNITY_INDEX_FIRST = 0;
	public static int DATA_COMMUNITY_INDEX_CITY = 1;
	public static int DATA_COMMUNITY_INDEX_AREA = 2;
	public static int DATA_COMMUNITY_INDEX_TYPE = 3;
	public static int DATA_COMMUNITY_INDEX_NAME = 4;
	public static int DATA_COMMUNITY_INDEX_ADDRESS = 5;
	public static int DATA_COMMUNITY_INDEX_DESCRIPTION = 6;
	public static int DATA_COMMUNITY_INDEX_LATITUDE = 7;
	public static int DATA_COMMUNITY_INDEX_LONGITUDE = 8;
	public static int DATA_COMMUNITY_INDEX_ALIAS = 9;
	public static int DATA_COMMUNITY_INDEX_DESC = 10;
	public static int DATA_COMMUNITY_INDEX_POSTER = 11;
	public static int DATA_COMMUNITY_INDEX_PHONES = 12;
	public static int DATA_COMMUNITY_INDEX_POST_CODE = 13;
	public static int DATA_COMMUNITY_INDEX_TAG = 14;
	
	
	/**  导入物业数据时每一列数据对应的索引值。*/
	public static int DATA_PROPERTY_INDEX_CITY = 1;
	public static int DATA_PROPERTY_INDEX_AREA = 2;
	public static int DATA_PROPERTY_INDEX_COMMUNITY_NAME = 3;
	public static int DATA_PROPERTY_INDEX_NAME = 4;
	public static int DATA_PROPERTY_INDEX_AVATAR = 5;
	public static int DATA_PROPERTY_INDEX_ITEM_NAMES = 6;
	public static int DATA_PROPERTY_INDEX_ITEM_VALUES = 7;
	
	
	/**  导入公寓数据时每一列数据对应的索引值。*/
	public static int DATA_APARTMENT_INDEX_CITY = 1;
	public static int DATA_APARTMENT_INDEX_AREA = 2;
	public static int DATA_APARTMENT_INDEX_COMMUNITY_NAME = 3;
	public static int DATA_APARTMENT_INDEX_BUILDING_STR = 4;
	public static int DATA_APARTMENT_INDEX_APARTMENT_STR = 5;
	public static int DATA_APARTMENT_INDEX_NAME = 6;
	public static int DATA_APARTMENT_INDEX_ADDR_STR = 7;
	public static int DATA_APARTMENT_INDEX_FLOOR = 8;
	
	
	
	/** 导入公寓ne的原因类型 : 1-后台导入*/
	public static int APARTMENT_NE_REASON_CODE = 1;
	
	/** ncm的原因类型 : 1-后台导入*/
	public static int COMMUNITY_NCM_REASON_CODE = 1;
}