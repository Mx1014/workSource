// @formatter:off
package com.everhomes.rest.questionnaire;

/**
 *
 * <ul>
 * <li>APP: 1, app用户</li>
 * <li>WX: 2, 微信用户</li>
 * </ul>
 */
public enum QuestionnaireUserType {
    APP((byte)1), WX((byte)2);

    private byte code;

    private QuestionnaireUserType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }

    public static QuestionnaireUserType fromCode(Byte code) {
        if (code != null) {
            for (QuestionnaireUserType targetType : QuestionnaireUserType.values()) {
                if (targetType.getCode() == code) {
                    return targetType;
                }
            }
        }
        return null;
    }
}