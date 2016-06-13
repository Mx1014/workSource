package com.everhomes.techpark.punch.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.controller.ControllerBase; 
import com.everhomes.discover.RestDoc;
import com.everhomes.techpark.punch.PunchService;
@RestDoc(value = "Punch controller", site = "ehccore")
@RestController
@RequestMapping("/admin/techpark/punch")
public class PunchAdminController extends ControllerBase {

	private static final Logger LOGGER = LoggerFactory.getLogger(PunchAdminController.class);

	@Autowired
	private PunchService punchService;
	
	
}
