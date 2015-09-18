package com.everhomes.techpark.company;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.techpark.company.CreateGroupContactCommand;
import com.everhomes.techpark.company.DeleteGroupContactCommand;
import com.everhomes.techpark.company.GroupContactService;
import com.everhomes.techpark.company.ImportGroupContactsCommand;
import com.everhomes.techpark.company.ListGroupContactsCommand;
import com.everhomes.techpark.company.ListGroupContactsCommandResponse;
import com.everhomes.techpark.company.UpdateGroupContactCommand;

@RestController
@RequestMapping("/gcontact")
public class GroupContactController extends ControllerBase {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GroupContactController.class);
	
	@Autowired
	private GroupContactService groupContactService;
	
	@RequestMapping(value="importGroupContacts", method = RequestMethod.POST)
	//@RequestMapping(value="importPmBills", method = RequestMethod.POST)
	@RestReturn(value=String.class)
	public RestResponse importGroupContacts(@Valid ImportGroupContactsCommand cmd,@RequestParam(value = "attachment") MultipartFile[] files) {
		groupContactService.importGroupContacts(cmd,files);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	@RequestMapping("updateGroupContact")
	@RestReturn(value=String.class)
	public RestResponse updateGroupContact(@Valid UpdateGroupContactCommand cmd) {
		groupContactService.updateGroupContact(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	@RequestMapping("deleteGroupContact")
	@RestReturn(value=String.class)
	public RestResponse deleteGroupContact(@Valid DeleteGroupContactCommand cmd) {
		groupContactService.deleteGroupContact(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	@RequestMapping("createGroupContact")
	@RestReturn(value=String.class)
	public RestResponse createGroupContact(@Valid CreateGroupContactCommand cmd) {
		groupContactService.createGroupContact(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	@RequestMapping("listGroupContacts")
	@RestReturn(value=ListGroupContactsCommandResponse.class)
	public RestResponse listGroupContacts(@Valid ListGroupContactsCommand cmd) {
		ListGroupContactsCommandResponse list = groupContactService.listGroupContacts(cmd);
		RestResponse response = new RestResponse(list);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

}
