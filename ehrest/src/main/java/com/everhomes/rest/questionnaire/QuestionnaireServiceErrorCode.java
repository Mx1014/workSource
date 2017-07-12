package com.everhomes.rest.questionnaire;

public interface QuestionnaireServiceErrorCode {
	String SCOPE = "questionnaire";
	int QUESTIONNAIRE_NAME_EMPTY = 1; //问卷名称不能为空
	int QUESTIONNAIRE_NAME_LENGTH_BEYOND_50 = 2; //问卷名称不能超过50个字
	int QUESTION_NAME_EMPTY = 3; //题目名称不能为空
	int NO_QUESTIONS = 4; //至少需要有一个题目
	int NO_OPTIONS = 5; //至少需要有一个选项
	int OPTION_NAME_EMPTY = 6; //选项名称不能为空
}
