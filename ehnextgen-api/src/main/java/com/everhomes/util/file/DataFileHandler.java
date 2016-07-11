package com.everhomes.util.file;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.hsr.geohash.GeoHash;

import com.atomikos.datasource.ResourceException;
import com.everhomes.address.Address;
import com.everhomes.address.AddressDataInfo;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityDataInfo;
import com.everhomes.community.CommunityGeoPoint;
import com.everhomes.organization.pm.CommunityPmContact;
import com.everhomes.region.Region;
import com.everhomes.rest.address.AddressAdminStatus;
import com.everhomes.rest.region.RegionScope;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.user.User;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

public class DataFileHandler {
	
	
	
	/** 日志 */
	 private static final Logger LOGGER = LoggerFactory.getLogger(DataFileHandler.class);
	
	 	/**
		 * 数组数据列表 生成 List<CommunityDataInfo> 实体列表
		 * 
		 * @param dataList 数组数据列表 , cityList 城市列表
		 * @return List<CommunityDataInfo> Data对应的 CommunityData 实体列表
		 */

		public static List<CommunityDataInfo> getComunityDataByFile(List<String[]> dataList, List<Region> cityList,
				List<Region> areaList,User user) throws IOException, ResourceException
		{
			List<CommunityDataInfo> list = new ArrayList<CommunityDataInfo>();
			if(dataList != null && dataList.size() > 0)
			{
				for (String[] rowDatas : dataList)
				{
					if (rowDatas.length == DataProcessConstants.DATA_LENGTH_ZISE_COMMUNITY)
					{
						CommunityDataInfo Community = convertCommunityDataByRow(rowDatas, cityList, areaList,user);
						list.add(Community);
					} else
					{
						LOGGER.error("data format is not correct.Community = " + Arrays.toString(rowDatas));
						throw new ResourceException("小区文件字段数不正确。请检查该小区格式。字段数是"+rowDatas.length +" ,小区是   " + Arrays.toString(rowDatas));
					}
				}
			}
			else
			{
				LOGGER.error("data  file  parse is not correct.Community = " +dataList);
				throw new ResourceException("小区文件解析不到数据。请检查该小区文件夹路径。文件内容：  " + dataList);
			}
			
			return list;
		}

		/**
		 * 读取每一行数据 生成 Community 实体
		 * 
		 * @param rowDatas[] 每一行数据数组 ,cityList 可用城市列表 , areaList 可用区县列表
		 * 
		 * @return Community 实体列表
		 */
		public static CommunityDataInfo convertCommunityDataByRow(String rowDatas[], List<Region> cityList,
				List<Region> areaList,User user)
		{
			CommunityDataInfo communityData = new CommunityDataInfo();
			if (rowDatas.length == DataProcessConstants.DATA_LENGTH_ZISE_COMMUNITY)
			{
				// 将每一行的数据 转换成 CommunityData

				// 第一列是序号，后台不作处理

				String cityName = rowDatas[DataProcessConstants.DATA_COMMUNITY_INDEX_CITY];
				String areaName = rowDatas[DataProcessConstants.DATA_COMMUNITY_INDEX_AREA];

				// 根据城市和区县名称得到 对应的城市和区县信息
				setCityAndArea(cityName, areaName, cityList, areaList, communityData);

				String name = rowDatas[DataProcessConstants.DATA_COMMUNITY_INDEX_NAME];
				if (!isStrEmpty(name))
				{
					communityData.setName(name);
				}

				String address = rowDatas[DataProcessConstants.DATA_COMMUNITY_INDEX_ADDRESS];
				if (!isStrEmpty(address))
				{
					communityData.setAddress(address);
				}

				String description = rowDatas[DataProcessConstants.DATA_COMMUNITY_INDEX_DESCRIPTION];
				if (!isStrEmpty(description))
				{
					communityData.setDescription(description);
				}

				String latitude = rowDatas[DataProcessConstants.DATA_COMMUNITY_INDEX_LATITUDE];
				if (!isStrEmpty(latitude))
				{
					communityData.setLatitude(Double.parseDouble(latitude));
				}

				String longitude = rowDatas[DataProcessConstants.DATA_COMMUNITY_INDEX_LONGITUDE];
				if (!isStrEmpty(longitude))
				{
					communityData.setLongitude(Double.parseDouble(longitude));
				}

				String aliasName = rowDatas[DataProcessConstants.DATA_COMMUNITY_INDEX_ALIAS];
				if (!isStrEmpty(aliasName))
				{
					communityData.setAliasName(aliasName);
				}

				String detailDescription = rowDatas[DataProcessConstants.DATA_COMMUNITY_INDEX_DESC];
				if (!isStrEmpty(detailDescription))
				{
					communityData.setDetailDescription(detailDescription);
				}

				String phones = rowDatas[DataProcessConstants.DATA_COMMUNITY_INDEX_PHONES];
				if (!isStrEmpty(phones))
				{
					String phone[] = phones.split("/");
					List<String> phoneList = new ArrayList<String>();
					for (String str : phone)
					{
						phoneList.add(str);
					}
					communityData.setPhones(phoneList);
				}

				String zipcode = rowDatas[DataProcessConstants.DATA_COMMUNITY_INDEX_POST_CODE];
				if (!isStrEmpty(zipcode))
				{
					communityData.setZipcode(zipcode);
				}
				
				communityData.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
				communityData.setCreatorUid(user.getId());

			} else
			{
				LOGGER.error("小区文件格式不正确。每行共"+ DataProcessConstants.DATA_LENGTH_ZISE_COMMUNITY+"项.rowDatas = " + Arrays.toString(rowDatas));
				throw new ResourceException("data format is not correct.rowDatas = " + Arrays.toString(rowDatas));
			}
			return communityData;
		}

		public static void setCityAndArea(String cityName, String areaName, List<Region> cityList, List<Region> areaList,
				Community communityData)
		{
			Region city = getCityByData(cityName, cityList);
			if (city != null && city.getScopeCode() == RegionScope.CITY.getCode())
			{
				long cityId = city.getId();
				communityData.setCityId(cityId);
				communityData.setCityName(city.getName());
				Region area = getAreaByData(cityId, areaName, areaList);
				if (area != null && area.getScopeCode() == RegionScope.AREA.getCode())
				{
					communityData.setAreaId(area.getId());
					communityData.setAreaName(area.getName());
				} else
				{
					throw new ResourceException("数据文件的区县名字不正确。小区 = " + cityName + "\t" + areaName + "\t"
							+ communityData.getName());
				}
			} else
			{
				throw new ResourceException("数据文件的城市名字不正确。小区 = " + cityName + "\t" + areaName + "\t"
						+ communityData.getName());
			}
		}

		
		/**
		 * <p>
		 * 判断字符串是否为空。
		 * </p>
		 * <p>
		 * 字符串为null或者只有空白字符，则该字符串为空。
		 * </p>
		 * 
		 * @param str 要判断的字符串
		 * @return 如果字符串为空则返回true，否则返回false
		 */
		public static boolean isStrEmpty(String str)
		{
			if (str == null || str.trim().length() == 0)
			{
				return true;
			} else
			{
				return false;
			}
		}
		
		/**
		 * 通过小区的城市名匹配数据库城市列表，得到城市
		 * 
		 * @param cityName 小区的城市名
		 * @param cityList 数据库城市列表
		 * @return Region 区域实体
		 */
		public static Region getCityByData(String cityName, List<Region> cityList)
		{
			Region city = null;
			for (Region cityDB : cityList)
			{
				if (cityName != null)
				{
					if (cityName.equals(cityDB.getName()))
					{
						city = cityDB;
						break;
					}
				}
			}
			return city;
		}

		/**
		 * 通过小区的城市id 和区县名称 匹配数据库区县列表，得到区县
		 * 
		 * @param cityId 小区的城市id areaName 小区的区县名
		 * @param areaList 数据库区域列表
		 * @return Region 区域实体
		 */
		public static Region getAreaByData(long cityId, String areaName, List<Region> areaList)
		{
			Region area = null;
			for (Region areaDB : areaList)
			{
				if (areaName != null)
				{
					if (areaName.equals(areaDB.getName()) && cityId == areaDB.getParentId())
					{
						area = areaDB;
						break;
					}
				}
			}
			return area;
		}
		
		/**
		 * 将文本导入的 小区信息 转化成小区
		 * 
		 * @param CommunityDataInfo Data导入的 小区信息
		 * @return Community 小区
		 */
		public static Community getCommunityByData(CommunityDataInfo communityData)
		{
			if(communityData != null){
				return ConvertHelper.convert(communityData, Community.class);
			}
			else{
				return null;
			}
		}
		

		/**
		 * 将文本导入的 小区信息 转化成小区点坐标
		 * 
		 * @param CommunityDataInfo 参数 Data导入的 小区信息
		 * @return communityGeoPoint 小区点信息
		 */
		public static CommunityGeoPoint getCommunityPosByData(CommunityDataInfo communityData)
		{
			CommunityGeoPoint communityGeoPoint = new CommunityGeoPoint();

			double latitude = communityData.getLatitude();
			if (latitude != 0)
			{
				communityGeoPoint.setLatitude(latitude);
			}

			double longitude = communityData.getLongitude();
			if (latitude != 0)
			{
				communityGeoPoint.setLongitude(longitude);
			}
			String geohash = GeoHash.geoHashStringWithCharacterPrecision(latitude, longitude, 12);
			communityGeoPoint.setGeohash(geohash);
			return communityGeoPoint;
		}

		/**
		 * 将文本导入的 小区信息 转化成物业条目 : 必须是有物业电话的小区
		 * 
		 * @param CommunityDataInfo 参数 Data导入的 小区信息,NEDBModel address 生成的小区物业信息
		 * @return NEDBModel ne的数据库实体
		 */
		public static List<CommunityPmContact> getPropItemsByData(User user, CommunityDataInfo communityData)
		{
			List<CommunityPmContact> propItems = new ArrayList<CommunityPmContact>();

			List<String> phones = communityData.getPhones();
			if (phones != null && phones.size() > 0)
			{
				for (String phone : phones)
				{
					CommunityPmContact propContact = new CommunityPmContact();
					propContact.setContactName("物业服务中心");
					propContact.setOrganizationId(communityData.getId());
					propContact.setContactToken(phone);
					propContact.setContactType(IdentifierType.MOBILE.getCode());
					propContact.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
					propContact.setCreatorUid(user.getId());
					propItems.add(propContact);
				}

			}
			return propItems;
		}
		
		/**
		 * 读取每一行数据 生成 AddressDataInfo 公寓 实体
		 * 
		 * @param rowDatas[] 每一行数据数组 
		 * @return AddressDataInfo 公寓 实体
		 */
		public static AddressDataInfo convertAddressDataByRow(String rowDatas[])
		{
			AddressDataInfo addressDataInfo = new AddressDataInfo();
			if (rowDatas.length == DataProcessConstants.DATA_LENGTH_ZISE_APARTMENT)
			{
			
				// 第一列是序号，后台不作处理
				String cityName = rowDatas[DataProcessConstants.DATA_APARTMENT_INDEX_CITY];

				addressDataInfo.setCityName(cityName);

				String areaName = rowDatas[DataProcessConstants.DATA_APARTMENT_INDEX_AREA];
				// Area area = getAreaByData(city.getCityId(), areaName, areaList);
				// communityData.setAreaId(area.getAreaId());
				addressDataInfo.setAreaName(areaName);

				String addrCommunityName = rowDatas[DataProcessConstants.DATA_APARTMENT_INDEX_COMMUNITY_NAME];
				addressDataInfo.setCommunityName(addrCommunityName);
				String addrBuildingStr = rowDatas[DataProcessConstants.DATA_APARTMENT_INDEX_BUILDING_STR];
				addressDataInfo.setBuildingName(addrBuildingStr);

				String addrApartmentStr = rowDatas[DataProcessConstants.DATA_APARTMENT_INDEX_APARTMENT_STR];
				addressDataInfo.setApartmentName(addrApartmentStr);

//				String name = rowDatas[DataProcessConstants.DATA_APARTMENT_INDEX_NAME];
//				addressDataInfo.setAddress(name);

				String addrStr = rowDatas[DataProcessConstants.DATA_APARTMENT_INDEX_ADDR_STR];
				addressDataInfo.setAddress(addrStr);
				
				String floor = rowDatas[DataProcessConstants.DATA_APARTMENT_INDEX_FLOOR];
				addressDataInfo.setApartmentFloor(floor);

			} else
			{
				LOGGER.error("公寓文件格式不正确。每行共"+ DataProcessConstants.DATA_LENGTH_ZISE_APARTMENT +"项。rowDatas = " + Arrays.toString(rowDatas));
				throw new ResourceException("data format is not correct.rowDatas = " + Arrays.toString(rowDatas));
			}
			return addressDataInfo;
		}
		
		public static Address convertNEDBModelDataInfo(Community community, CommunityGeoPoint communityGeoPoint,AddressDataInfo addressDataInfo, User user)
		{
			Address address = null ;
			if(addressDataInfo != null && community != null){
				address =  ConvertHelper.convert(addressDataInfo, Address.class);
				address.setCityId(community.getCityId());
				address.setCommunityId(community.getId());
				address.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
				address.setCreatorUid(user.getId());
				address.setStatus(AddressAdminStatus.ACTIVE.getCode());
				
				//添加address 的经纬度
				if(communityGeoPoint != null){
					address.setLatitude(communityGeoPoint.getLatitude());
					address.setLongitude(communityGeoPoint.getLongitude());
					address.setGeohash(communityGeoPoint.getGeohash());
				}
			}
			
			
			return address;
		}
}
