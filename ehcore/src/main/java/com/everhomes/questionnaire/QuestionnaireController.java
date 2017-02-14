// @formatter:off
package com.everhomes.questionnaire;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.questionnaire.ListQuestionnairesCommand;
import com.everhomes.rest.questionnaire.ListQuestionnairesResponse;

@RestController
@RequestMapping("/questionnaire")
public class QuestionnaireController extends ControllerBase {
	
	@Autowired
	private QuestionnaireService questionnaireService;
	
	/**
	 * <p>1.问卷调查列表</p>
	 * <b>URL: /questionnaire/listQuestionnaires</b>
	 */
	@RequestMapping("listQuestionnaires")
	@RestReturn(ListQuestionnairesResponse.class)
	public RestResponse listQuestionnaires(ListQuestionnairesCommand cmd){
		return new RestResponse(questionnaireService.listQuestionnaires(cmd));
	}

}