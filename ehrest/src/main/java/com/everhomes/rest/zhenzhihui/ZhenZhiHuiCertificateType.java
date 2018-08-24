package com.everhomes.rest.zhenzhihui;

/**
 * <ul>
 * 	<li>ID_CARD: 10, 身份证</li>
 *  <li>OFFICER: 11, 军官证</li>
 *  <li>SOLDER: 12, 士兵证</li>
 *  <li>POLICE: 13, 警官证</li>
 *  <li>HM_VISA: 14, 港澳通行证</li>
 *  <li>TAIWAN_VISA: 15, 台湾通行证</li>
 *  <li>HONG_KONG: 16, 香港身份证</li>
 *  <li>MACAO: 17, 澳门身份证</li>
 *  <li>TAIWAN: 18, 台湾身份证</li>
 *  <li>DISCHARGE: 19, 退伍证</li>
 *  <li>PASSPORT: 20, 护照</li>
 *  <li>DRIVER: 21, 驾驶证</li>
 *  <li>OTHER: 40, 其他</li>
 * </ul>
 */
public enum ZhenZhiHuiCertificateType {
    ID_CARD(10, "身份证"), OFFICER(11,"军官证") ,SOLDER(12, "士兵证"),POLICE(13, "警官证"), HM_VISA(14, "港澳通行证"), TAIWAN_VISA(15, "台湾通行证"), HONG_KONG(16, "香港身份证"),
    MACAO(17, "澳门身份证"), TAIWAN(18,"台湾身份证"),
     DISCHARGE(19, "退伍证"),PASSPORT(20, "护照") ,DRIVER(21, "驾驶证"), OTHER(40,"其他");
    private Integer code;
    private String name;

    ZhenZhiHuiCertificateType(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public static ZhenZhiHuiCertificateType fromCode(Integer code) {
    	if(null == code){
    		return null;
    	}
        for (ZhenZhiHuiCertificateType flag : ZhenZhiHuiCertificateType.values()) {
            if (flag.code == code.byteValue()) {
                return flag;
            }
        }
        return ID_CARD;
    }
}
