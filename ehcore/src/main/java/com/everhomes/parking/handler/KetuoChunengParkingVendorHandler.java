// @formatter:off
package com.everhomes.parking.handler;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.parking.ParkingVendorHandler;
import com.everhomes.parking.ketuo.KetuoCard;
import com.everhomes.parking.ketuo.KetuoRequestConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 储能 正中会 停车对接
 */
@Component(ParkingVendorHandler.PARKING_VENDOR_PREFIX + "KETUO")
public class KetuoChunengParkingVendorHandler extends Ketuo2ParkingVendorHandler{
	private static final Logger LOGGER = LoggerFactory.getLogger(KetuoChunengParkingVendorHandler.class);

	@Autowired
    private ConfigurationProvider configProvider;

//	@Override
//	public KetuoCard getCard(String plateNumber) {
//		KetuoCard card = super.getCard(plateNumber);
//
//		//储能月卡没有对接免费金额,设置成0
//		if (null != card) {
//			card.setFreeMoney(0);
//		}
//		return card;
//	}

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
}
