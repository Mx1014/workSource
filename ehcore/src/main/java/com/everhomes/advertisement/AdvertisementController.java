package com.everhomes.advertisement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;

@RestDoc(value="Advertisement controller", site="core")
@RestController
@RequestMapping("/advertisement")
public class AdvertisementController extends ControllerBase{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AdvertisementController.class);

}
