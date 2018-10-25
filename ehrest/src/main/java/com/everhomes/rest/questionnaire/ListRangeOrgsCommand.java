package com.everhomes.rest.questionnaire;


/**
 * <ul>
 *  <li>questionnaireId: 问卷Id</li>
 * </ul>
 */
public class ListRangeOrgsCommand {

    private Long questionnaireId;

    public Long getQuestionnaireId() {
        return questionnaireId;
    }

    public void setQuestionnaireId(Long questionnaireId) {
        this.questionnaireId = questionnaireId;
    }
}
