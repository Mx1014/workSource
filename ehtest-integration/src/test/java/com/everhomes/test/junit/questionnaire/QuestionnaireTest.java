package com.everhomes.test.junit.questionnaire;

import java.util.ArrayList;
import java.util.List;

import org.jooq.Record;
import org.jooq.Result;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;

import com.everhomes.rest.RestResponseBase;
import com.everhomes.rest.questionnaire.CreateQuestionnaireCommand;
import com.everhomes.rest.questionnaire.CreateQuestionnaireResponse;
import com.everhomes.rest.questionnaire.CreateQuestionnaireRestResponse;
import com.everhomes.rest.questionnaire.CreateTargetQuestionnaireCommand;
import com.everhomes.rest.questionnaire.CreateTargetQuestionnaireResponse;
import com.everhomes.rest.questionnaire.CreateTargetQuestionnaireRestResponse;
import com.everhomes.rest.questionnaire.DeleteQuestionnaireCommand;
import com.everhomes.rest.questionnaire.GetQuestionnaireDetailCommand;
import com.everhomes.rest.questionnaire.GetQuestionnaireDetailResponse;
import com.everhomes.rest.questionnaire.GetQuestionnaireDetailRestResponse;
import com.everhomes.rest.questionnaire.GetQuestionnaireResultDetailCommand;
import com.everhomes.rest.questionnaire.GetQuestionnaireResultDetailResponse;
import com.everhomes.rest.questionnaire.GetQuestionnaireResultDetailRestResponse;
import com.everhomes.rest.questionnaire.GetQuestionnaireResultSummaryCommand;
import com.everhomes.rest.questionnaire.GetQuestionnaireResultSummaryResponse;
import com.everhomes.rest.questionnaire.GetQuestionnaireResultSummaryRestResponse;
import com.everhomes.rest.questionnaire.GetTargetQuestionnaireDetailCommand;
import com.everhomes.rest.questionnaire.GetTargetQuestionnaireDetailResponse;
import com.everhomes.rest.questionnaire.GetTargetQuestionnaireDetailRestResponse;
import com.everhomes.rest.questionnaire.ListBlankQuestionAnswersCommand;
import com.everhomes.rest.questionnaire.ListBlankQuestionAnswersResponse;
import com.everhomes.rest.questionnaire.ListBlankQuestionAnswersRestResponse;
import com.everhomes.rest.questionnaire.ListOptionTargetsCommand;
import com.everhomes.rest.questionnaire.ListOptionTargetsResponse;
import com.everhomes.rest.questionnaire.ListOptionTargetsRestResponse;
import com.everhomes.rest.questionnaire.ListQuestionnairesCommand;
import com.everhomes.rest.questionnaire.ListQuestionnairesResponse;
import com.everhomes.rest.questionnaire.ListQuestionnairesRestResponse;
import com.everhomes.rest.questionnaire.ListTargetQuestionnairesCommand;
import com.everhomes.rest.questionnaire.ListTargetQuestionnairesResponse;
import com.everhomes.rest.questionnaire.ListTargetQuestionnairesRestResponse;
import com.everhomes.rest.questionnaire.QuestionnaireDTO;
import com.everhomes.rest.questionnaire.QuestionnaireOptionDTO;
import com.everhomes.rest.questionnaire.QuestionnaireQuestionDTO;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhQuestionnaires;
import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.StringHelper;

public class QuestionnaireTest extends BaseLoginAuthTestCase {
	@Value("${namespace.id:1000000}")
    private Integer namespaceId;
	
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
	private static final String LIST_OPTION_TARGETS_URL = "/questionnaire/listOptionTargets";
	//8. 填空题答案列表-园区
	private static final String LIST_BLANK_QUESTION_ANSWERS_URL = "/questionnaire/listBlankQuestionAnswers";
	//9. 问卷调查列表-企业
	private static final String LIST_TARGET_QUESTIONNAIRES_URL = "/questionnaire/listTargetQuestionnaires";
	//10. 问卷调查详情-企业
	private static final String GET_TARGET_QUESTIONNAIRE_DETAIL_URL = "/questionnaire/getTargetQuestionnaireDetail";
	//11. 提交问卷调查-企业
	private static final String CREATE_TARGET_QUESTIONNAIRE_URL = "/questionnaire/createTargetQuestionnaire";


	//1. 问卷调查列表-园区（已完成）
	//@Test
	public void testListQuestionnaires() {
		String url = LIST_QUESTIONNAIRES_URL;
		logon();
		ListQuestionnairesCommand cmd = new ListQuestionnairesCommand();
		cmd.setNamespaceId(namespaceId);
		cmd.setOwnerType("community");
		cmd.setOwnerId(1L);
//		cmd.setPageAnchor(114L);
		cmd.setPageSize(2);

		ListQuestionnairesRestResponse response = httpClientService.restPost(url, cmd, ListQuestionnairesRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		ListQuestionnairesResponse myResponse = response.getResponse();
		assertNotNull(myResponse);
		assertNotNull(myResponse.getQuestionnaires());
		assertEquals(2, cmd.getPageSize().intValue());

	}

	//2. 问卷调查详情-园区（已完成）
	//@Test
	public void testGetQuestionnaireDetail() {
		String url = GET_QUESTIONNAIRE_DETAIL_URL;
		logon();
		GetQuestionnaireDetailCommand cmd = new GetQuestionnaireDetailCommand();
		cmd.setNamespaceId(namespaceId);
		cmd.setQuestionnaireId(113L);

		GetQuestionnaireDetailRestResponse response = httpClientService.restPost(url, cmd, GetQuestionnaireDetailRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		GetQuestionnaireDetailResponse myResponse = response.getResponse();
		assertNotNull(myResponse);
		assertNotNull(myResponse.getQuestionnaire());
		assertEquals(5, myResponse.getQuestionnaire().getQuestions().size());
		assertEquals(3, myResponse.getQuestionnaire().getQuestions().get(0).getOptions().size());


	}

	//3. 创建问卷调查-园区（已完成）
	//@Test
	public void testCreateQuestionnaire() {
		String url = CREATE_QUESTIONNAIRE_URL;
		logon();
		CreateQuestionnaireCommand cmd = new CreateQuestionnaireCommand();
		QuestionnaireDTO questionnaireDTO = new QuestionnaireDTO();
		questionnaireDTO.setNamespaceId(namespaceId);
		questionnaireDTO.setOwnerType("community");
		questionnaireDTO.setOwnerId(1L);
		questionnaireDTO.setQuestionnaireName("第一份问卷");
		questionnaireDTO.setDescription("我是第一份问卷");
		questionnaireDTO.setStatus((byte)1);
		List<QuestionnaireQuestionDTO> questionnaireQuestionDTOList = new ArrayList<>();
		
		QuestionnaireQuestionDTO questionnaireQuestionDTO = new QuestionnaireQuestionDTO();
		questionnaireQuestionDTO.setQuestionType((byte)1);
		questionnaireQuestionDTO.setQuestionName("我是单选题");
		List<QuestionnaireOptionDTO> questionnaireOptionDTOList = new ArrayList<>();
		QuestionnaireOptionDTO questionnaireOptionDTO = new QuestionnaireOptionDTO();
		questionnaireOptionDTO.setOptionName("选项1");
		questionnaireOptionDTOList.add(questionnaireOptionDTO);
		QuestionnaireOptionDTO questionnaireOptionDTO2 = new QuestionnaireOptionDTO();
		questionnaireOptionDTO2.setOptionName("选项2");
		questionnaireOptionDTOList.add(questionnaireOptionDTO2);
		QuestionnaireOptionDTO questionnaireOptionDTO3 = new QuestionnaireOptionDTO();
		questionnaireOptionDTO3.setOptionName("选项3");
		questionnaireOptionDTOList.add(questionnaireOptionDTO3);
		questionnaireQuestionDTO.setOptions(questionnaireOptionDTOList);
		questionnaireQuestionDTOList.add(questionnaireQuestionDTO);
		
		QuestionnaireQuestionDTO questionnaireQuestionDTO2 = new QuestionnaireQuestionDTO();
		questionnaireQuestionDTO2.setQuestionType((byte)2);
		questionnaireQuestionDTO2.setQuestionName("我是复选题");
		List<QuestionnaireOptionDTO> questionnaireOptionDTOList2 = new ArrayList<>();
		QuestionnaireOptionDTO questionnaireOptionDTO4 = new QuestionnaireOptionDTO();
		questionnaireOptionDTO4.setOptionName("选项1");
		questionnaireOptionDTOList2.add(questionnaireOptionDTO4);
		QuestionnaireOptionDTO questionnaireOptionDTO5 = new QuestionnaireOptionDTO();
		questionnaireOptionDTO5.setOptionName("选项2");
		questionnaireOptionDTOList2.add(questionnaireOptionDTO5);
		QuestionnaireOptionDTO questionnaireOptionDTO6 = new QuestionnaireOptionDTO();
		questionnaireOptionDTO6.setOptionName("选项3");
		questionnaireOptionDTOList2.add(questionnaireOptionDTO6);
		questionnaireQuestionDTO2.setOptions(questionnaireOptionDTOList2);
		questionnaireQuestionDTOList.add(questionnaireQuestionDTO2);
		
		QuestionnaireQuestionDTO questionnaireQuestionDTO4 = new QuestionnaireQuestionDTO();
		questionnaireQuestionDTO4.setQuestionType((byte)4);
		questionnaireQuestionDTO4.setQuestionName("我是图片单选题");
		List<QuestionnaireOptionDTO> questionnaireOptionDTOList4 = new ArrayList<>();
		QuestionnaireOptionDTO questionnaireOptionDTO41 = new QuestionnaireOptionDTO();
		questionnaireOptionDTO41.setOptionName("选项1");
		questionnaireOptionDTO41.setOptionUri("http://beta-cs.zuolin.com:80/image/aW1hZ2UvTVRvelkyTmpZelptWmpRME9UZG1PV1U1TXpJeU1HRTNZekV6TkRBeE9XRXhPQQ?token=2JdRW4GairgBo3qPkaDDMcJv4vhu8vU-POfRazGZEzes_udZWepmGMDY7SgjhNvBmt9M5AX9Y-IX7hHEdaExVpumxa768JSlFlhF-oSLyuY");
		questionnaireOptionDTOList4.add(questionnaireOptionDTO41);
		QuestionnaireOptionDTO questionnaireOptionDTO42 = new QuestionnaireOptionDTO();
		questionnaireOptionDTO42.setOptionName("选项2");
		questionnaireOptionDTO42.setOptionUri("http://beta-cs.zuolin.com:80/image/aW1hZ2UvTVRvelkyTmpZelptWmpRME9UZG1PV1U1TXpJeU1HRTNZekV6TkRBeE9XRXhPQQ?token=2JdRW4GairgBo3qPkaDDMcJv4vhu8vU-POfRazGZEzes_udZWepmGMDY7SgjhNvBmt9M5AX9Y-IX7hHEdaExVpumxa768JSlFlhF-oSLyuY");
		questionnaireOptionDTOList4.add(questionnaireOptionDTO42);
		QuestionnaireOptionDTO questionnaireOptionDTO43 = new QuestionnaireOptionDTO();
		questionnaireOptionDTO43.setOptionName("选项3");
		questionnaireOptionDTO43.setOptionUri("http://beta-cs.zuolin.com:80/image/aW1hZ2UvTVRvelkyTmpZelptWmpRME9UZG1PV1U1TXpJeU1HRTNZekV6TkRBeE9XRXhPQQ?token=2JdRW4GairgBo3qPkaDDMcJv4vhu8vU-POfRazGZEzes_udZWepmGMDY7SgjhNvBmt9M5AX9Y-IX7hHEdaExVpumxa768JSlFlhF-oSLyuY");
		questionnaireOptionDTOList4.add(questionnaireOptionDTO43);
		questionnaireQuestionDTO4.setOptions(questionnaireOptionDTOList4);
		questionnaireQuestionDTOList.add(questionnaireQuestionDTO4);
		
		QuestionnaireQuestionDTO questionnaireQuestionDTO5 = new QuestionnaireQuestionDTO();
		questionnaireQuestionDTO5.setQuestionType((byte)5);
		questionnaireQuestionDTO5.setQuestionName("我是图片复选题");
		List<QuestionnaireOptionDTO> questionnaireOptionDTOList5 = new ArrayList<>();
		QuestionnaireOptionDTO questionnaireOptionDTO51 = new QuestionnaireOptionDTO();
		questionnaireOptionDTO51.setOptionName("选项1");
		questionnaireOptionDTO51.setOptionUri("http://beta-cs.zuolin.com:80/image/aW1hZ2UvTVRvelkyTmpZelptWmpRME9UZG1PV1U1TXpJeU1HRTNZekV6TkRBeE9XRXhPQQ?token=2JdRW4GairgBo3qPkaDDMcJv4vhu8vU-POfRazGZEzes_udZWepmGMDY7SgjhNvBmt9M5AX9Y-IX7hHEdaExVpumxa768JSlFlhF-oSLyuY");
		questionnaireOptionDTOList5.add(questionnaireOptionDTO51);
		QuestionnaireOptionDTO questionnaireOptionDTO52 = new QuestionnaireOptionDTO();
		questionnaireOptionDTO52.setOptionName("选项2");
		questionnaireOptionDTO52.setOptionUri("http://beta-cs.zuolin.com:80/image/aW1hZ2UvTVRvelkyTmpZelptWmpRME9UZG1PV1U1TXpJeU1HRTNZekV6TkRBeE9XRXhPQQ?token=2JdRW4GairgBo3qPkaDDMcJv4vhu8vU-POfRazGZEzes_udZWepmGMDY7SgjhNvBmt9M5AX9Y-IX7hHEdaExVpumxa768JSlFlhF-oSLyuY");
		questionnaireOptionDTOList5.add(questionnaireOptionDTO52);
		QuestionnaireOptionDTO questionnaireOptionDTO53 = new QuestionnaireOptionDTO();
		questionnaireOptionDTO53.setOptionName("选项3");
		questionnaireOptionDTO53.setOptionUri("http://beta-cs.zuolin.com:80/image/aW1hZ2UvTVRvelkyTmpZelptWmpRME9UZG1PV1U1TXpJeU1HRTNZekV6TkRBeE9XRXhPQQ?token=2JdRW4GairgBo3qPkaDDMcJv4vhu8vU-POfRazGZEzes_udZWepmGMDY7SgjhNvBmt9M5AX9Y-IX7hHEdaExVpumxa768JSlFlhF-oSLyuY");
		questionnaireOptionDTOList5.add(questionnaireOptionDTO53);
		questionnaireQuestionDTO5.setOptions(questionnaireOptionDTOList5);
		questionnaireQuestionDTOList.add(questionnaireQuestionDTO5);
		
		QuestionnaireQuestionDTO questionnaireQuestionDTO3 = new QuestionnaireQuestionDTO();
		questionnaireQuestionDTO3.setQuestionType((byte)3);
		questionnaireQuestionDTO3.setQuestionName("我是填空题");
		questionnaireQuestionDTOList.add(questionnaireQuestionDTO3);
		
		questionnaireDTO.setQuestions(questionnaireQuestionDTOList);
		cmd.setQuestionnaire(questionnaireDTO);

		CreateQuestionnaireRestResponse response = httpClientService.restPost(url, cmd, CreateQuestionnaireRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		CreateQuestionnaireResponse myResponse = response.getResponse();
		assertNotNull(myResponse);
		assertNotNull(myResponse.getQuestionnaire());
		assertEquals(5, myResponse.getQuestionnaire().getQuestions().size());
		assertEquals(3, myResponse.getQuestionnaire().getQuestions().get(0).getOptions().size());
		
		Record record = dbProvider.getDslContext().select().from(Tables.EH_QUESTIONNAIRES).fetchOne();
		assertNotNull(record);
		EhQuestionnaires questionnaire = ConvertHelper.convert(record, EhQuestionnaires.class);
		assertEquals("第一份问卷", questionnaire.getQuestionnaireName());

	}

	//4. 删除问卷调查-园区（已完成）
	//@Test
	public void testDeleteQuestionnaire() {
		String url = DELETE_QUESTIONNAIRE_URL;
		logon();
		DeleteQuestionnaireCommand cmd = new DeleteQuestionnaireCommand();
		cmd.setNamespaceId(namespaceId);
		cmd.setQuestionnaireId(114L);

		RestResponseBase response = httpClientService.restPost(url, cmd, RestResponseBase.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		Record record = dbProvider.getDslContext().select().from(Tables.EH_QUESTIONNAIRES).where(Tables.EH_QUESTIONNAIRES.ID.eq(114L)).fetchOne();
		assertNotNull(record);
		EhQuestionnaires questionnaire = ConvertHelper.convert(record, EhQuestionnaires.class);
		assertEquals((byte)0, questionnaire.getStatus().byteValue());

	}

	//5. 问卷调查结果详情-园区（已完成）
	//@Test
	public void testGetQuestionnaireResultDetail() {
		String url = GET_QUESTIONNAIRE_RESULT_DETAIL_URL;
		logon();
		GetQuestionnaireResultDetailCommand cmd = new GetQuestionnaireResultDetailCommand();
		cmd.setNamespaceId(namespaceId);
		cmd.setQuestionnaireId(113L);
		cmd.setKeywords("");
//		cmd.setPageAnchor(1L);
		cmd.setPageSize(10);

		GetQuestionnaireResultDetailRestResponse response = httpClientService.restPost(url, cmd, GetQuestionnaireResultDetailRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		GetQuestionnaireResultDetailResponse myResponse = response.getResponse();
		assertNotNull(myResponse);


	}

	//6. 问卷调查结果统计-园区()
	//@Test
	public void testGetQuestionnaireResultSummary() {
		String url = GET_QUESTIONNAIRE_RESULT_SUMMARY_URL;
		logon();
		GetQuestionnaireResultSummaryCommand cmd = new GetQuestionnaireResultSummaryCommand();
		cmd.setNamespaceId(namespaceId);
		cmd.setQuestionnaireId(113L);
		cmd.setPageSize(10);

		GetQuestionnaireResultSummaryRestResponse response = httpClientService.restPost(url, cmd, GetQuestionnaireResultSummaryRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		GetQuestionnaireResultSummaryResponse myResponse = response.getResponse();
		assertNotNull(myResponse);


	}

	//7. 某个选项的企业列表-园区（已完成）
	//@Test
	public void testListOptionTargets() {
		String url = LIST_OPTION_TARGETS_URL;
		logon();
		ListOptionTargetsCommand cmd = new ListOptionTargetsCommand();
		cmd.setNamespaceId(namespaceId);
		cmd.setOptionId(119L);
//		cmd.setPageAnchor(1L);
		cmd.setPageSize(10);

		ListOptionTargetsRestResponse response = httpClientService.restPost(url, cmd, ListOptionTargetsRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		ListOptionTargetsResponse myResponse = response.getResponse();		
		assertNotNull(myResponse);


	}

	//8. 填空题答案列表-园区（已完成）
	//@Test
	public void testListBlankQuestionAnswers() {
		String url = LIST_BLANK_QUESTION_ANSWERS_URL;
		logon();
		ListBlankQuestionAnswersCommand cmd = new ListBlankQuestionAnswersCommand();
		cmd.setNamespaceId(namespaceId);
		cmd.setQuestionId(117L);
//		cmd.setPageAnchor(1L);
		cmd.setPageSize(10);

		ListBlankQuestionAnswersRestResponse response = httpClientService.restPost(url, cmd, ListBlankQuestionAnswersRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		ListBlankQuestionAnswersResponse myResponse = response.getResponse();
		assertNotNull(myResponse);


	}

	//9. 问卷调查列表-企业（已完成）
	//@Test
	public void testListTargetQuestionnaires() {
		String url = LIST_TARGET_QUESTIONNAIRES_URL;
		logon();
		ListTargetQuestionnairesCommand cmd = new ListTargetQuestionnairesCommand();
		cmd.setNamespaceId(namespaceId);
		cmd.setOwnerType("community");
		cmd.setOwnerId(1L);
		cmd.setTargetType("organization");
		cmd.setTargetId(2L);
//		cmd.setPageAnchor(1L);
		cmd.setPageSize(10);

		ListTargetQuestionnairesRestResponse response = httpClientService.restPost(url, cmd, ListTargetQuestionnairesRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		ListTargetQuestionnairesResponse myResponse = response.getResponse();
		assertNotNull(myResponse);
		
		List<QuestionnaireDTO> questionnaireDTOs = myResponse.getQuestionnaires();
		if (questionnaireDTOs != null) {
			for (QuestionnaireDTO questionnaireDTO : questionnaireDTOs) {
				assertEquals((byte)2, questionnaireDTO.getStatus().byteValue());
			}
		}
		
	}

	//10. 问卷调查详情-企业（已完成）
	//@Test
	public void testGetTargetQuestionnaireDetail() {
		String url = GET_TARGET_QUESTIONNAIRE_DETAIL_URL;
		logon();
		GetTargetQuestionnaireDetailCommand cmd = new GetTargetQuestionnaireDetailCommand();
		cmd.setNamespaceId(namespaceId);
		cmd.setQuestionnaireId(113L);
		cmd.setTargetType("organization");
		cmd.setTargetId(2L);

		GetTargetQuestionnaireDetailRestResponse response = httpClientService.restPost(url, cmd, GetTargetQuestionnaireDetailRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		GetTargetQuestionnaireDetailResponse myResponse = response.getResponse();
		assertNotNull(myResponse);
		assertEquals(5, myResponse.getQuestionnaire().getQuestions().size());

	}

	//11. 提交问卷调查-企业（已完成）
	//@Test
	public void testCreateTargetQuestionnaire() {
		String url = CREATE_TARGET_QUESTIONNAIRE_URL;
		logon();
		CreateTargetQuestionnaireCommand cmd = new CreateTargetQuestionnaireCommand();
		cmd.setTargetType("organization");
		cmd.setTargetId(1L);
		QuestionnaireDTO questionnaireDTO = new QuestionnaireDTO();
		questionnaireDTO.setId(113L);
		List<QuestionnaireQuestionDTO> questionnaireQuestionDTOList = new ArrayList<>();
		
		QuestionnaireQuestionDTO questionnaireQuestionDTO = new QuestionnaireQuestionDTO();
		questionnaireQuestionDTO.setId(113L);
		List<QuestionnaireOptionDTO> questionnaireOptionDTOList = new ArrayList<>();
		QuestionnaireOptionDTO questionnaireOptionDTO = new QuestionnaireOptionDTO();
		questionnaireOptionDTO.setId(113L);
		questionnaireOptionDTOList.add(questionnaireOptionDTO);
		questionnaireQuestionDTO.setOptions(questionnaireOptionDTOList);
		questionnaireQuestionDTOList.add(questionnaireQuestionDTO);
		
		QuestionnaireQuestionDTO questionnaireQuestionDTO2 = new QuestionnaireQuestionDTO();
		questionnaireQuestionDTO2.setId(114L);
		List<QuestionnaireOptionDTO> questionnaireOptionDTOList2 = new ArrayList<>();
		QuestionnaireOptionDTO questionnaireOptionDTO21 = new QuestionnaireOptionDTO();
		questionnaireOptionDTO21.setId(117L);
		questionnaireOptionDTOList2.add(questionnaireOptionDTO21);
		questionnaireQuestionDTO2.setOptions(questionnaireOptionDTOList2);
		questionnaireQuestionDTOList.add(questionnaireQuestionDTO2);
		
		QuestionnaireQuestionDTO questionnaireQuestionDTO4 = new QuestionnaireQuestionDTO();
		questionnaireQuestionDTO4.setId(115L);
		List<QuestionnaireOptionDTO> questionnaireOptionDTOList4 = new ArrayList<>();
		QuestionnaireOptionDTO questionnaireOptionDTO41 = new QuestionnaireOptionDTO();
		questionnaireOptionDTO41.setId(119L);
		questionnaireOptionDTOList4.add(questionnaireOptionDTO41);
		questionnaireQuestionDTO4.setOptions(questionnaireOptionDTOList4);
		questionnaireQuestionDTOList.add(questionnaireQuestionDTO4);
		
		QuestionnaireQuestionDTO questionnaireQuestionDTO5 = new QuestionnaireQuestionDTO();
		questionnaireQuestionDTO5.setId(116L);
		List<QuestionnaireOptionDTO> questionnaireOptionDTOList5 = new ArrayList<>();
		QuestionnaireOptionDTO questionnaireOptionDTO51 = new QuestionnaireOptionDTO();
		questionnaireOptionDTO51.setId(1113L);
		questionnaireOptionDTOList5.add(questionnaireOptionDTO51);
		questionnaireQuestionDTO5.setOptions(questionnaireOptionDTOList5);
		questionnaireQuestionDTOList.add(questionnaireQuestionDTO5);
		
		QuestionnaireQuestionDTO questionnaireQuestionDTO3 = new QuestionnaireQuestionDTO();
		questionnaireQuestionDTO3.setId(117L);
		List<QuestionnaireOptionDTO> questionnaireOptionDTOList3 = new ArrayList<>();
		QuestionnaireOptionDTO questionnaireOptionDTO31 = new QuestionnaireOptionDTO();
		questionnaireOptionDTO31.setId(1115L);
		questionnaireOptionDTO31.setOptionContent("我是填空题答案，哈哈~~");
		questionnaireOptionDTOList3.add(questionnaireOptionDTO31);
		questionnaireQuestionDTO3.setOptions(questionnaireOptionDTOList3);
		questionnaireQuestionDTOList.add(questionnaireQuestionDTO3);
		
		questionnaireDTO.setQuestions(questionnaireQuestionDTOList);
		cmd.setQuestionnaire(questionnaireDTO);

		CreateTargetQuestionnaireRestResponse response = httpClientService.restPost(url, cmd, CreateTargetQuestionnaireRestResponse.class);
		assertNotNull(response);
		assertTrue("response= " + StringHelper.toJsonString(response), httpClientService.isReponseSuccess(response));

		CreateTargetQuestionnaireResponse myResponse = response.getResponse();
		assertNotNull(myResponse);

		Result<Record> result = dbProvider.getDslContext()
				.select()
				.from(Tables.EH_QUESTIONNAIRE_ANSWERS)
				.where(Tables.EH_QUESTIONNAIRE_ANSWERS.TARGET_ID.eq(1L))
				.and(Tables.EH_QUESTIONNAIRE_ANSWERS.QUESTIONNAIRE_ID.eq(113L))
				.fetch();
		assertEquals(5, result.size());
	}



	
	
	
	
	@Before
	public void setUp() {
		super.setUp();
	}

	@Override
	protected void initCustomData() {
		String jsonFilePath = "data/json/1.0.0-questionnaire-test-data-170220.txt";
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
