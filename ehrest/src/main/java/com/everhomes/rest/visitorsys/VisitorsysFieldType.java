// @formatter:off
package com.everhomes.rest.visitorsys;

/**
 * <p>协议/通行证 显示字段枚举</p>
 * <ul>
 * <li>NULL("null"): 无</li>
 * <li>VISITOR_NAME("visitorName"): 访客姓名字段</li>
 * <li>VISITOR_PHONE("visitorPhone"): 访客电话字段</li>
 * <li>VISITOR_QRCODE("enterpriseName"): 来访企业字段</li>
 * <li>OFFICE_LOCATION_NAME("officeLocationName"): 办公点名称</li>
 * <li>VISIT_REASON("visitReason"): 来访事由字段</li>
 * <li>NOW_TIME("nowTime"): 当前时间字段</li>
 * <li>INVALID_TIME("invalidTime"): 自定义字段，失效时间</li>
 * <li>PLATE_NO("plateNo"): 自定义字段，车牌号</li>
 * <li>ID_NUMBER("idNumber"): 自定义字段，证件号码</li>
 * <li>REMARK("remark"): 自定义字段，备注</li>
 * <li>VISIT_FLOOR("visitFloor"): 到访楼层</li>
 * <li>VISIT_ADDRESSES("visitAddresses"): 到访门牌</li>
 * </ul>
 */
public enum VisitorsysFieldType {
    NULL("null"),
    VISITOR_NAME("visitorName"),
    VISITOR_PHONE("visitorPhone"),
    VISITOR_QRCODE("enterpriseName"),
    OFFICE_LOCATION_NAME("officeLocationName"),
    VISIT_REASON("visitReason"),
    NOW_TIME("nowTime"),
    INVALID_TIME("invalidTime"),
    PLATE_NO("plateNo"),
    ID_NUMBER("idNumber"),
    REMARK("remark"),
    VISIT_FLOOR("visitFloor"),
    VISIT_ADDRESSES("visitAddresses");

    private String code;
    VisitorsysFieldType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static VisitorsysFieldType fromCode(String code) {
        if(code != null) {
            VisitorsysFieldType[] values = VisitorsysFieldType.values();
            for(VisitorsysFieldType value : values) {
                if(code.equals(value.code)) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
