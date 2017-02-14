// @formatter:off
package com.everhomes.questionnaire;

import org.springframework.stereotype.Component;

import com.everhomes.rest.questionnaire.ListQuestionnairesCommand;
import com.everhomes.rest.questionnaire.ListQuestionnairesResponse;

@Component
public class QuestionnaireServiceImpl implements QuestionnaireService {

	@Override
	public ListQuestionnairesResponse listQuestionnaires(ListQuestionnairesCommand cmd) {
	
		return new ListQuestionnairesResponse();
	}

}