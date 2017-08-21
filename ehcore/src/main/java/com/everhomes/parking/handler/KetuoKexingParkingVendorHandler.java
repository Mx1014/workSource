// @formatter:off
package com.everhomes.parking.handler;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.db.DbProvider;
import com.everhomes.flow.*;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.parking.*;
import com.everhomes.parking.ketuo.*;
import com.everhomes.rest.flow.FlowAutoStepDTO;
import com.everhomes.rest.flow.FlowStepType;
import com.everhomes.rest.organization.VendorType;
import com.everhomes.rest.parking.*;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 科兴 正中会 停车对接
 */
@Component(ParkingVendorHandler.PARKING_VENDOR_PREFIX + "KETUO2")
public class KetuoKexingParkingVendorHandler extends Ketuo2ParkingVendorHandler{
	private static final Logger LOGGER = LoggerFactory.getLogger(KetuoKexingParkingVendorHandler.class);

	@Autowired
    private ConfigurationProvider configProvider;

	protected KetuoRequestConfig getKetuoRequestConfig() {

		String url = configProvider.getValue("parking.kexing.url", "");
		String key = configProvider.getValue("parking.kexing.key", "");
		String user = configProvider.getValue("parking.kexing.user", "");
		String pwd = configProvider.getValue("parking.kexing.pwd", "");

		KetuoRequestConfig config = new KetuoRequestConfig();
		config.setUrl(url);
		config.setKey(key);
		config.setUser(user);
		config.setPwd(pwd);

		return config;
	}
}
