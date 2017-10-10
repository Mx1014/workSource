// @formatter:off
package com.everhomes.rest.questionnaire;

/**
 *
 * <ul>
 * <li>enterprise: 1, 问卷调查对象->企业</li>
 * <li>user: 2, 问卷调查对象->用户</li>
 * </ul>
 */
public enum QuestionnaireRespondentType {
    ENTERPRISE((byte)1), USER((byte)2);

    private byte code;

    private QuestionnaireRespondentType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }

    public static QuestionnaireRespondentType fromCode(Byte code) {
        if (code != null) {
            for (QuestionnaireRespondentType targetType : QuestionnaireRespondentType.values()) {
                if (targetType.getCode() == code) {
                    return targetType;
                }
            }
        }
        return null;
    }
}