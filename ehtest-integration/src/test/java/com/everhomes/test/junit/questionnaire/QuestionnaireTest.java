package com.everhomes.test.junit.approval;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;

import com.everhomes.rest.RestResponseBase;
import com.everhomes.rest.questionnaire.CreateQuestionnaireCommand;
import com.everhomes.rest.questionnaire.CreateQuestionnaireResponse;
import com.everhomes.rest.questionnaire.CreateTargetQuestionnaireCommand;
import com.everhomes.rest.questionnaire.CreateTargetQuestionnaireResponse;
import com.everhomes.rest.questionnaire.DeleteQuestionnaireCommand;
import com.everhomes.rest.questionnaire.GetQuestionnaireDetailCommand;
import com.everhomes.rest.questionnaire.GetQuestionnaireDetailResponse;
import com.everhomes.rest.questionnaire.GetQuestionnaireResultDetailCommand;
import com.everhomes.rest.questionnaire.GetQuestionnaireResultDetailResponse;
import com.everhomes.rest.questionnaire.GetQuestionnaireResultSummaryCommand;
import com.everhomes.rest.questionnaire.GetQuestionnaireResultSummaryResponse;
import com.everhomes.rest.questionnaire.GetTargetQuestionnaireDetailCommand;
import com.everhomes.rest.questionnaire.GetTargetQuestionnaireDetailResponse;
import com.everhomes.rest.questionnaire.ListBlankQuestionAnswersCommand;
import com.everhomes.rest.questionnaire.ListBlankQuestionAnswersResponse;
import com.everhomes.rest.questionnaire.ListOptionTargetsCommand;
import com.everhomes.rest.questionnaire.ListOptionTargetsResponse;
import com.everhomes.rest.questionnaire.ListQuestionnairesCommand;
import com.everhomes.rest.questionnaire.ListQuestionnairesResponse;
import com.everhomes.rest.questionnaire.ListTargetQuestionnairesCommand;
import com.everhomes.rest.questionnaire.ListTargetQuestionnairesResponse;
import com.everhomes.rest.questionnaire.QuestionnaireDTO;
import com.everhomes.rest.questionnaire.QuestionnaireOptionDTO;
import com.everhomes.rest.questionnaire.QuestionnaireQuestionDTO;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.DateHelper;
import com.everhomes.util.StringHelper;

public class QuestionnaireTest extends BaseLoginAuthTestCase {

	//1. 问卷调查列表-园区
	private static final String LIST_QUESTIONNAIRES_URL = "/questionnaire/listQuestionnaires";
	//2. 问卷调查详情-园区
	private static final String GET_QUESTIONNAIRE_DETAIL_URL = "/questionnaire/getQuestionnaireDetail";
	//3. 创建问卷调查-园区
	private static final String CREATE_QUESTIONNAIRE_URL = "/questionnaire/createQuestionnaire";
	//4. 删除问卷调查-园区
	private static final String DELETE_QUESTIONNAIRE_URL = "/questionnaire/deleteQuestionnaire";
	//5. 问卷调查结果详情-园区
	private static final String GET_QUESTIONNAIRE_RESULT_DETAIL_URL = "/questionnaire/getQuestionnaireResultDetail";
	//6. 问卷调查结果统计-园区
	private static final String GET_QUESTIONNAIRE_RESULT_SUMMARY_URL = "/questionnaire/getQuestionnaireResultSummary";
	//7. 某个选项的企业列表-园区
	private static final String LIST_OPTION_ORGANIZATIONS_URL = "/questionnaire/listOptionOrganizations";
	//8. 填空题答案列表-园区
	private static final String LIST_BLANK_QUESTION_ANSWERS_URL = "/questionnaire/listBlankQuestionAnswers";
	//9. 问卷调查列表-企业
	private static final String LIST_TARGET_QUESTIONNAIRES_URL = "/questionnaire/listTargetQuestionnaires";
	//10. 问卷调查详情-企业
	private static final String GET_TARGET_QUESTIONNAIRE_DETAIL_URL = "/questionnaire/getTargetQuestionnaireDetail";
	//11. 提交问卷调查-企业
	private static final String CREATE_TARGET_QUESTIONNAIRE_URL = "/questionnaire/createTargetQuestionnaire";


	//1. 问卷调查列表-园区
	//@Test
	public void testListQuestionnaires() {
		String url = LIST_QUESTIONNAIRES_URL;
		logon();
		ListQuestionnairesCommand cmd = new ListQuestionnairesCommand();
		cmd.setNamespaceId(0);
		cmd.setOwnerType("");
		cmd.setOwnerId(1L);
		cmd.setPageAnchor(1L);
		cmd.setPageSize(0);

		ListQuestionnairesRestResponse response = httpClientService.restPost(url, cmd, ListQuestionnairesRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		ListQuestionnairesResponse myResponse = response.getResponse();
		assertNotNull(myResponse);


	}

	//2. 问卷调查详情-园区
	//@Test
	public void testGetQuestionnaireDetail() {
		String url = GET_QUESTIONNAIRE_DETAIL_URL;
		logon();
		GetQuestionnaireDetailCommand cmd = new GetQuestionnaireDetailCommand();
		cmd.setNamespaceId(0);
		cmd.setQuestionnaireId(1L);

		GetQuestionnaireDetailRestResponse response = httpClientService.restPost(url, cmd, GetQuestionnaireDetailRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		GetQuestionnaireDetailResponse myResponse = response.getResponse();
		assertNotNull(myResponse);


	}

	//3. 创建问卷调查-园区
	//@Test
	public void testCreateQuestionnaire() {
		String url = CREATE_QUESTIONNAIRE_URL;
		logon();
		CreateQuestionnaireCommand cmd = new CreateQuestionnaireCommand();
		cmd.setNamespaceId(0);
		cmd.setOwnerType("");
		cmd.setOwnerId(1L);
		QuestionnaireDTO questionnaireDTO = new QuestionnaireDTO();
		questionnaireDTO.setId(1L);
		questionnaireDTO.setNamespaceId(0);
		questionnaireDTO.setOwnerType("");
		questionnaireDTO.setOwnerId(1L);
		questionnaireDTO.setQuestionnaireName("");
		questionnaireDTO.setDescription("");
		questionnaireDTO.setCollectionCount(0);
		questionnaireDTO.setStatus((byte)1);
		questionnaireDTO.setPublishTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		questionnaireDTO.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		questionnaireDTO.setSubmitTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		List<QuestionnaireQuestionDTO> questionnaireQuestionDTOList = new ArrayList<>();
		QuestionnaireQuestionDTO questionnaireQuestionDTO = new QuestionnaireQuestionDTO();
		questionnaireQuestionDTO.setId(1L);
		questionnaireQuestionDTO.setQuestionType((byte)1);
		questionnaireQuestionDTO.setQuestionName("");
		List<QuestionnaireOptionDTO> questionnaireOptionDTOList = new ArrayList<>();
		QuestionnaireOptionDTO questionnaireOptionDTO = new QuestionnaireOptionDTO();
		questionnaireOptionDTO.setId(1L);
		questionnaireOptionDTO.setOptionName("");
		questionnaireOptionDTO.setOptionUrl("");
		questionnaireOptionDTO.setCheckedCount(0);
		questionnaireOptionDTO.setOptionContent("");
		questionnaireOptionDTO.setChecked((byte)1);
		questionnaireOptionDTOList.add(questionnaireOptionDTO);
		questionnaireQuestionDTO.setOptions(questionnaireOptionDTOList);
		questionnaireQuestionDTO.setNextPageAnchor(1L);
		questionnaireQuestionDTOList.add(questionnaireQuestionDTO);
		questionnaireDTO.setQuestions(questionnaireQuestionDTOList);
		cmd.setQuestionnaire(questionnaireDTO);

		CreateQuestionnaireRestResponse response = httpClientService.restPost(url, cmd, CreateQuestionnaireRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		CreateQuestionnaireResponse myResponse = response.getResponse();
		assertNotNull(myResponse);


	}

	//4. 删除问卷调查-园区
	//@Test
	public void testDeleteQuestionnaire() {
		String url = DELETE_QUESTIONNAIRE_URL;
		logon();
		DeleteQuestionnaireCommand cmd = new DeleteQuestionnaireCommand();
		cmd.setNamespaceId(0);
		cmd.setQuestionnaireId(1L);

		RestResponseBase response = httpClientService.restPost(url, cmd, RestResponseBase.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));



	}

	//5. 问卷调查结果详情-园区
	//@Test
	public void testGetQuestionnaireResultDetail() {
		String url = GET_QUESTIONNAIRE_RESULT_DETAIL_URL;
		logon();
		GetQuestionnaireResultDetailCommand cmd = new GetQuestionnaireResultDetailCommand();
		cmd.setNamespaceId(0);
		cmd.setQuestionnaireId(1L);
		cmd.setKeywords("");
		cmd.setPageAnchor(1L);
		cmd.setPageSize(0);

		GetQuestionnaireResultDetailRestResponse response = httpClientService.restPost(url, cmd, GetQuestionnaireResultDetailRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		GetQuestionnaireResultDetailResponse myResponse = response.getResponse();
		assertNotNull(myResponse);


	}

	//6. 问卷调查结果统计-园区
	//@Test
	public void testGetQuestionnaireResultSummary() {
		String url = GET_QUESTIONNAIRE_RESULT_SUMMARY_URL;
		logon();
		GetQuestionnaireResultSummaryCommand cmd = new GetQuestionnaireResultSummaryCommand();
		cmd.setNamespaceId(0);
		cmd.setQuestionnaireId(1L);

		GetQuestionnaireResultSummaryRestResponse response = httpClientService.restPost(url, cmd, GetQuestionnaireResultSummaryRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		GetQuestionnaireResultSummaryResponse myResponse = response.getResponse();
		assertNotNull(myResponse);


	}

	//7. 某个选项的企业列表-园区
	//@Test
	public void testListOptionOrganizations() {
		String url = LIST_OPTION_ORGANIZATIONS_URL;
		logon();
		ListOptionTargetsCommand cmd = new ListOptionTargetsCommand();
		cmd.setNamespaceId(0);
		cmd.setOptionId(1L);
		cmd.setPageAnchor(1L);
		cmd.setPageSize(0);

		ListOptionOrganizationsRestResponse response = httpClientService.restPost(url, cmd, ListOptionOrganizationsRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		ListOptionTargetsResponse myResponse = response.getResponse();
		assertNotNull(myResponse);


	}

	//8. 填空题答案列表-园区
	//@Test
	public void testListBlankQuestionAnswers() {
		String url = LIST_BLANK_QUESTION_ANSWERS_URL;
		logon();
		ListBlankQuestionAnswersCommand cmd = new ListBlankQuestionAnswersCommand();
		cmd.setNamespaceId(0);
		cmd.setQuestionId(1L);
		cmd.setPageAnchor(1L);
		cmd.setPageSize(0);

		ListBlankQuestionAnswersRestResponse response = httpClientService.restPost(url, cmd, ListBlankQuestionAnswersRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		ListBlankQuestionAnswersResponse myResponse = response.getResponse();
		assertNotNull(myResponse);


	}

	//9. 问卷调查列表-企业
	//@Test
	public void testListTargetQuestionnaires() {
		String url = LIST_TARGET_QUESTIONNAIRES_URL;
		logon();
		ListTargetQuestionnairesCommand cmd = new ListTargetQuestionnairesCommand();
		cmd.setNamespaceId(0);
		cmd.setOwnerType("");
		cmd.setOwnerId(1L);
		cmd.setTargetType("");
		cmd.setTargetId(1L);
		cmd.setPageAnchor(1L);
		cmd.setPageSize(0);

		ListTargetQuestionnairesRestResponse response = httpClientService.restPost(url, cmd, ListTargetQuestionnairesRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		ListTargetQuestionnairesResponse myResponse = response.getResponse();
		assertNotNull(myResponse);


	}

	//10. 问卷调查详情-企业
	//@Test
	public void testGetTargetQuestionnaireDetail() {
		String url = GET_TARGET_QUESTIONNAIRE_DETAIL_URL;
		logon();
		GetTargetQuestionnaireDetailCommand cmd = new GetTargetQuestionnaireDetailCommand();
		cmd.setNamespaceId(0);
		cmd.setQuestionnaireId(1L);
		cmd.setTargetType("");
		cmd.setTargetId(1L);

		GetTargetQuestionnaireDetailRestResponse response = httpClientService.restPost(url, cmd, GetTargetQuestionnaireDetailRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		GetTargetQuestionnaireDetailResponse myResponse = response.getResponse();
		assertNotNull(myResponse);


	}

	//11. 提交问卷调查-企业
	//@Test
	public void testCreateTargetQuestionnaire() {
		String url = CREATE_TARGET_QUESTIONNAIRE_URL;
		logon();
		CreateTargetQuestionnaireCommand cmd = new CreateTargetQuestionnaireCommand();
		cmd.setNamespaceId(0);
		cmd.setQuestionnaireId(1L);
		cmd.setTargetType("");
		cmd.setTargetId(1L);
		QuestionnaireDTO questionnaireDTO = new QuestionnaireDTO();
		questionnaireDTO.setId(1L);
		questionnaireDTO.setNamespaceId(0);
		questionnaireDTO.setOwnerType("");
		questionnaireDTO.setOwnerId(1L);
		questionnaireDTO.setQuestionnaireName("");
		questionnaireDTO.setDescription("");
		questionnaireDTO.setCollectionCount(0);
		questionnaireDTO.setStatus((byte)1);
		questionnaireDTO.setPublishTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		questionnaireDTO.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		questionnaireDTO.setSubmitTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		List<QuestionnaireQuestionDTO> questionnaireQuestionDTOList = new ArrayList<>();
		QuestionnaireQuestionDTO questionnaireQuestionDTO = new QuestionnaireQuestionDTO();
		questionnaireQuestionDTO.setId(1L);
		questionnaireQuestionDTO.setQuestionType((byte)1);
		questionnaireQuestionDTO.setQuestionName("");
		List<QuestionnaireOptionDTO> questionnaireOptionDTOList = new ArrayList<>();
		QuestionnaireOptionDTO questionnaireOptionDTO = new QuestionnaireOptionDTO();
		questionnaireOptionDTO.setId(1L);
		questionnaireOptionDTO.setOptionName("");
		questionnaireOptionDTO.setOptionUrl("");
		questionnaireOptionDTO.setCheckedCount(0);
		questionnaireOptionDTO.setOptionContent("");
		questionnaireOptionDTO.setChecked((byte)1);
		questionnaireOptionDTOList.add(questionnaireOptionDTO);
		questionnaireQuestionDTO.setOptions(questionnaireOptionDTOList);
		questionnaireQuestionDTO.setNextPageAnchor(1L);
		questionnaireQuestionDTOList.add(questionnaireQuestionDTO);
		questionnaireDTO.setQuestions(questionnaireQuestionDTOList);
		cmd.setQuestionnaire(questionnaireDTO);

		CreateTargetQuestionnaireRestResponse response = httpClientService.restPost(url, cmd, CreateTargetQuestionnaireRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		CreateTargetQuestionnaireResponse myResponse = response.getResponse();
		assertNotNull(myResponse);


	}
	
	@Before
	public void setUp() {
		super.setUp();
	}

	@Override
	protected void initCustomData() {
		String jsonFilePath = "data/json/1.0.0-approval-test-data-160907.txt";
		String fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
		dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);
	}
	
	private void initListData() {
		String jsonFilePath = "data/json/1.0.0-approval-test-data-list-160907.txt";
		String fileAbsolutePath = dbProvider.getAbsolutePathFromClassPath(jsonFilePath);
		dbProvider.loadJsonFileToDatabase(fileAbsolutePath, false);
	}

	
	@After
	public void tearDown() {
		logoff();
	}

	private void logon() {
		String userIdentifier = "tt";
		String plainTexPassword = "123456";
		Integer namespaceId = 999995;
		logon(namespaceId, userIdentifier, plainTexPassword);
	}
	 
}
