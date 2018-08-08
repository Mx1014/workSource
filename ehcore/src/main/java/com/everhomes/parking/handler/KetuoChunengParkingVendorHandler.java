// @formatter:off
package com.everhomes.parking.handler;

import com.everhomes.parking.ParkingRechargeOrder;
import com.everhomes.parking.ParkingVendorHandler;
import com.everhomes.parking.ketuo.KetuoRequestConfig;
import com.everhomes.rest.organization.VendorType;
import com.everhomes.rest.parking.ParkingOrderType;
import com.everhomes.rest.parking.ParkingPaySourceType;
import com.everhomes.rest.parking.ParkingRechargeOrderStatus;
import com.everhomes.rest.parking.ParkingRechargeType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 储能 正中会 停车对接
 */
@Component(ParkingVendorHandler.PARKING_VENDOR_PREFIX + "KETUO")
public class KetuoChunengParkingVendorHandler extends KetuoParkingVendorHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(KetuoChunengParkingVendorHandler.class);

	protected KetuoRequestConfig getKetuoRequestConfig() {

		String url = configProvider.getValue("parking.chuneng.url", "");
		String key = configProvider.getValue("parking.chuneng.key", "");
		String user = configProvider.getValue("parking.chuneng.user", "");
		String pwd = configProvider.getValue("parking.chuneng.pwd", "");

		KetuoRequestConfig config = new KetuoRequestConfig();
		config.setUrl(url);
		config.setKey(key);
		config.setUser(user);
		config.setPwd(pwd);

		return config;
	}

	@Override
	public void setCellValues(List<ParkingRechargeOrder> list, Sheet sheet) {
		SimpleDateFormat datetimeSF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for(int i=0, size = list.size();i<size;i++){
			Row tempRow = sheet.createRow(i + 1);
			ParkingRechargeOrder order = list.get(i);
			tempRow.createCell(0).setCellValue(order.getOrderToken());
			tempRow.createCell(1).setCellValue(order.getPlateNumber());
			tempRow.createCell(2).setCellValue(order.getPlateOwnerName());
			tempRow.createCell(3).setCellValue(order.getPayerPhone());
			tempRow.createCell(4).setCellValue(datetimeSF.format(order.getCreateTime()));
			tempRow.createCell(5).setCellValue("");
			tempRow.createCell(6).setCellValue("");
			if (order.getRechargeType()!=null &&
					order.getRechargeType().byteValue()==ParkingRechargeType.MONTHLY.getCode()) {
				if(order.getStartPeriod()!=null) {
					tempRow.createCell(5).setCellValue(datetimeSF.format(order.getStartPeriod()));
				}
				if(order.getEndPeriod()!=null) {
					tempRow.createCell(6).setCellValue(datetimeSF.format(order.getEndPeriod()));
				}
			}
			tempRow.createCell(7).setCellValue(null == order.getMonthCount()?"":order.getMonthCount().toString());
			tempRow.createCell(8).setCellValue("");
			tempRow.createCell(9).setCellValue("");
			tempRow.createCell(10).setCellValue("");
			if (order.getRechargeType()!=null &&
					order.getRechargeType().byteValue()==ParkingRechargeType.TEMPORARY.getCode()) {
				if(order.getStartPeriod()!=null) {
					tempRow.createCell(8).setCellValue(datetimeSF.format(order.getStartPeriod()));
				}
				if(order.getEndPeriod()!=null) {
					tempRow.createCell(9).setCellValue(datetimeSF.format(order.getEndPeriod()));
				}
				if(order.getParkingTime()!=null) {
					tempRow.createCell(10).setCellValue(order.getParkingTime());
				}
			}
			tempRow.createCell(11).setCellValue(String.valueOf(order.getPrice().doubleValue()));
			tempRow.createCell(12).setCellValue(order.getOriginalPrice()==null?
					String.valueOf(order.getPrice().doubleValue())
					:String.valueOf(order.getOriginalPrice().doubleValue()));
			VendorType type = VendorType.fromCode(order.getPaidType());
			tempRow.createCell(13).setCellValue(null==type?"":type.getDescribe());
			ParkingRechargeType parkingRechargeType = ParkingRechargeType.fromCode(order.getRechargeType());
			tempRow.createCell(14).setCellValue(parkingRechargeType==null?"":parkingRechargeType.getDescribe());
			ParkingRechargeOrderStatus orderStatus = ParkingRechargeOrderStatus.fromCode(order.getStatus());
			tempRow.createCell(15).setCellValue(orderStatus==null?"":orderStatus.getDescription());
			ParkingPaySourceType sourceType = ParkingPaySourceType.fromCode(order.getPaySource());
			tempRow.createCell(16).setCellValue(sourceType==null?"":sourceType.getDesc());
		}
	}
}
