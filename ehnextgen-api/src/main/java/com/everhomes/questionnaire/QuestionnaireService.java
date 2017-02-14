// @formatter:off
package com.everhomes.questionnaire;

import com.everhomes.rest.questionnaire.ListQuestionnairesCommand;
import com.everhomes.rest.questionnaire.ListQuestionnairesResponse;

public interface QuestionnaireService {


	public ListQuestionnairesResponse listQuestionnaires(ListQuestionnairesCommand cmd);

}