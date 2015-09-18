package com.everhomes.techpark.punch.company;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;

@RestController
@RequestMapping("/company")
public class CompanyController extends ControllerBase {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CompanyController.class);
	
	@Autowired
	private CompanyService companyProvider;
	
	@RequestMapping("importContacts")
	@RestReturn(value=String.class)
	public RestResponse importContacts(@Valid ImportContactsCommand cmd,@RequestParam(value = "attachment") MultipartFile[] files) {
		companyProvider.importContacts(cmd,files);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	@RequestMapping("updateContacts")
	@RestReturn(value=String.class)
	public RestResponse updateContacts(@Valid UpdateContactsCommand cmd) {
		companyProvider.updateContacts(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	@RequestMapping("deleteContacts")
	@RestReturn(value=String.class)
	public RestResponse deleteContacts(@Valid DeleteContactsCommand cmd) {
		companyProvider.deleteContacts(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	@RequestMapping("createContacts")
	@RestReturn(value=String.class)
	public RestResponse createContacts(@Valid CreateContactsCommand cmd) {
		companyProvider.createContacts(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

}
