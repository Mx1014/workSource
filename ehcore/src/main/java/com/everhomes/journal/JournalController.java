package com.everhomes.journal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.journal.CreateJournalCommand;
import com.everhomes.rest.journal.DeleteJournalCommand;
import com.everhomes.rest.journal.GetJournalCommand;
import com.everhomes.rest.journal.GetJournalConfigCommand;
import com.everhomes.rest.journal.JournalConfigDTO;
import com.everhomes.rest.journal.JournalDTO;
import com.everhomes.rest.journal.ListJournalsCommand;
import com.everhomes.rest.journal.ListJournalsResponse;
import com.everhomes.rest.journal.UpdateJournalCommand;
import com.everhomes.rest.journal.UpdateJournalConfigCommand;

@RestDoc(value="Journal controller", site="journal")
@RestController
@RequestMapping("/journal")
public class JournalController extends ControllerBase{
	
	@Autowired
	private JournalService journalService;
	
	/**
     * <b>URL: /journal/listJournals</b>
     * <p>获取电子报列表</p>
     */
    @RequestMapping("listJournals")
    @RestReturn(value=ListJournalsResponse.class)
    public RestResponse listJournals(ListJournalsCommand cmd) {
    	ListJournalsResponse resp = journalService.listJournals(cmd);
        RestResponse response = new RestResponse(resp);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;   
    }
    
    /**
     * <b>URL: /journal/getJournal</b>
     * <p>获取电子报详情</p>
     */
    @RequestMapping("getJournal")
    @RestReturn(value=JournalDTO.class)
    public RestResponse getJournal(GetJournalCommand cmd) {
    	JournalDTO dto = journalService.getJournal(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /journal/updateJournal</b>
     * <p>更新电子报</p>
     */
    @RequestMapping("updateJournal")
    @RestReturn(value=String.class)
    public RestResponse updateJournal(UpdateJournalCommand cmd) {
    	journalService.updateJournal(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /journal/createJournal</b>
     * <p>新建电子报</p>
     */
    @RequestMapping("createJournal")
    @RestReturn(value=String.class)
    public RestResponse createJournal(CreateJournalCommand cmd) {
    	journalService.createJournal(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /journal/deleteJournal</b>
     * <p>删除电子报</p>
     */
    @RequestMapping("deleteJournal")
    @RestReturn(value=String.class)
    public RestResponse deleteJournal(DeleteJournalCommand cmd) {
    	journalService.deleteJournal(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /journal/updateJournalConfig</b>
     * <p>更新电子报须知</p>
     */
    @RequestMapping("updateJournalConfig")
    @RestReturn(value=String.class)
    public RestResponse updateJournalConfig(UpdateJournalConfigCommand cmd) {
    	journalService.updateJournalConfig(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /journal/getJournalConfig</b>
     * <p>获取电子报须知</p>
     */
    @RequestMapping("getJournalConfig")
    @RestReturn(value=JournalConfigDTO.class)
    public RestResponse getJournalConfig(GetJournalConfigCommand cmd) {
    	JournalConfigDTO dto = journalService.getJournalConfig(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}
