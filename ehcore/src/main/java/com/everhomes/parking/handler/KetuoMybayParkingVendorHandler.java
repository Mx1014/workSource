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
 * 深圳湾 停车对接
 */
@Component(ParkingVendorHandler.PARKING_VENDOR_PREFIX + "Mybay")
public class KetuoMybayParkingVendorHandler extends Ketuo2ParkingVendorHandler{
	private static final Logger LOGGER = LoggerFactory.getLogger(KetuoMybayParkingVendorHandler.class);

	private Integer namespaceId = 1000000;

	@Autowired
    private ConfigurationProvider configProvider;

	@Override
	public KetuoCard getCard(String plateNumber) {
		KetuoCard card = super.getCard(plateNumber);

		//深圳湾月卡没有对接免费金额,设置成0
		if (null != card) {
			card.setFreeMoney(0);
		}
		return card;
	}

	protected KetuoRequestConfig getKetuoRequestConfig() {

		String url = configProvider.getValue(namespaceId, "parking.ketuo.url", "");
		String key = configProvider.getValue(namespaceId, "parking.ketuo.key", "");
		String user = configProvider.getValue(namespaceId, "parking.ketuo.user", "");
		String pwd = configProvider.getValue(namespaceId, "parking.ketuo.pwd", "");

		KetuoRequestConfig config = new KetuoRequestConfig();
		config.setUrl(url);
		config.setKey(key);
		config.setUser(user);
		config.setPwd(pwd);

		return config;
	}
}
