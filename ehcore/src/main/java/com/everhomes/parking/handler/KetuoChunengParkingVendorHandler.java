// @formatter:off
package com.everhomes.parking.handler;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.parking.ParkingCardRequest;
import com.everhomes.parking.ParkingRechargeOrder;
import com.everhomes.parking.ParkingVendorHandler;
import com.everhomes.parking.ketuo.KetuoRequestConfig;
import com.everhomes.rest.organization.VendorType;
import com.everhomes.rest.parking.ParkingOrderType;
import com.everhomes.rest.parking.ParkingRechargeRateDTO;
import com.everhomes.rest.parking.ParkingRechargeType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
			if (order.getOrderType().equals(ParkingOrderType.RECHARGE.getCode()) &&
					order.getRechargeType().equals(ParkingRechargeType.MONTHLY.getCode())) {
				tempRow.createCell(5).setCellValue(datetimeSF.format(order.getStartPeriod()));
				tempRow.createCell(6).setCellValue(datetimeSF.format(order.getEndPeriod()));
			}else{
				tempRow.createCell(5).setCellValue("");
				tempRow.createCell(6).setCellValue("");
			}
			tempRow.createCell(7).setCellValue(null == order.getMonthCount()?"":order.getMonthCount().toString());
			if (order.getOrderType().equals(ParkingOrderType.RECHARGE.getCode()) &&
					order.getRechargeType().equals(ParkingRechargeType.TEMPORARY.getCode())) {
				tempRow.createCell(8).setCellValue(datetimeSF.format(order.getStartPeriod()));
				tempRow.createCell(9).setCellValue(datetimeSF.format(order.getEndPeriod()));
				tempRow.createCell(10).setCellValue(order.getParkingTime());
			}else{
				tempRow.createCell(8).setCellValue("");
				tempRow.createCell(9).setCellValue("");
				tempRow.createCell(10).setCellValue("");
			}
			tempRow.createCell(11).setCellValue(String.valueOf(order.getPrice().doubleValue()));
			VendorType type = VendorType.fromCode(order.getPaidType());
			tempRow.createCell(12).setCellValue(null==type?"":type.getDescribe());
			tempRow.createCell(13).setCellValue(ParkingRechargeType.fromCode(order.getRechargeType()).getDescribe());
		}
	}
}
