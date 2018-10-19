// @formatter:off
package com.everhomes.questionnaire;

import com.everhomes.rest.organization.OrganizationAndDetailDTO;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.rest.questionnaire.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface QuestionnaireService {


	public ListQuestionnairesResponse listQuestionnaires(ListQuestionnairesCommand cmd);


	public GetQuestionnaireDetailResponse getQuestionnaireDetail(GetQuestionnaireDetailCommand cmd);


	public CreateQuestionnaireResponse createQuestionnaire(CreateQuestionnaireCommand cmd);


	public void deleteQuestionnaire(DeleteQuestionnaireCommand cmd);


	public GetQuestionnaireResultDetailResponse getQuestionnaireResultDetail(GetQuestionnaireResultDetailCommand cmd);


	public GetQuestionnaireResultSummaryResponse getQuestionnaireResultSummary(GetQuestionnaireResultSummaryCommand cmd);


	public ListOptionTargetsResponse listOptionTargets(ListOptionTargetsCommand cmd);


	public ListBlankQuestionAnswersResponse listBlankQuestionAnswers(ListBlankQuestionAnswersCommand cmd);


	public ListTargetQuestionnairesResponse listTargetQuestionnaires(ListTargetQuestionnairesCommand cmd);


	public GetTargetQuestionnaireDetailResponse getTargetQuestionnaireDetail(GetTargetQuestionnaireDetailCommand cmd);


	public CreateTargetQuestionnaireResponse createTargetQuestionnaire(CreateTargetQuestionnaireCommand cmd);

    public void exportQuestionnaireResultDetail(GetQuestionnaireResultDetailCommand cmd, HttpServletResponse response);

	public ListUsersbyIdentifiersResponse listUsersbyIdentifiers(ListUsersbyIdentifiersCommand cmd);

	public GetTargetQuestionnaireDetailResponse getAnsweredQuestionnaireDetail(GetTargetQuestionnaireDetailCommand cmd);

    void reScopeQuesionnaireRanges(ReScopeQuesionnaireRangesCommand cmd);

	void reSendQuesionnaireMessages();

	List<OrganizationAndDetailDTO> listRangeOrgs(ListRangeOrgsCommand cmd);
}