// @formatter:off
package com.everhomes.rest.visitorsys;

import com.everhomes.util.StringHelper;

import java.sql.Timestamp;

/**
 * <ul>
 * <li>id: (必填)办公地点ID</li>
 * <li>officeLocationName: (必填)名称</li>
 * <li>addresses: (必填)地点</li>
 * <li>longitude: (必填)精度</li>
 * <li>latitude: (必填)纬度</li>
 * <li>geohash: (必填)经纬度哈希值</li>
 * <li>mapAddresses: (必填)地图选点名称</li>
 * </ul>
 */
public class CreateOrUpdateOfficeLocationCommand extends BaseOfficeLocationDTO {
}
