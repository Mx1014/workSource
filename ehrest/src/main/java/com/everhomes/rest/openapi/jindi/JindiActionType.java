package com.everhomes.rest.openapi.jindi;

public enum JindiActionType {
	ACTION_TYPE_BUSINESS(JindiActionTypeCode.ACTION_TYPE_BUSINESS_CODE),	//电商交易
	ACTION_TYPE_SITE_RENTAL(JindiActionTypeCode.ACTION_TYPE_SITE_RENTAL_CODE),	//场所预订
	ACTION_TYPE_FORUM_POST(JindiActionTypeCode.ACTION_TYPE_FORUM_POST_CODE),	//论坛发帖
	ACTION_TYPE_FORUM_COMMENT(JindiActionTypeCode.ACTION_TYPE_FORUM_COMMENT_CODE),	//论坛评论
	ACTION_TYPE_ACTIVITY(JindiActionTypeCode.ACTION_TYPE_ACTIVITY_CODE),	//活动
	ACTION_TYPE_ACTIVITY_SIGNUP(JindiActionTypeCode.ACTION_TYPE_ACTIVITY_SIGNUP_CODE),	//活动报名
	ACTION_TYPE_REPAIR(JindiActionTypeCode.ACTION_TYPE_REPAIR_CODE),	//报修
	ACTION_TYPE_OFFICE_RENTAL(JindiActionTypeCode.ACTION_TYPE_OFFICE_RENTAL_CODE),	//办公租赁
	ACTION_TYPE_STATION_RENTAL(JindiActionTypeCode.ACTION_TYPE_STATION_RENTAL_CODE);	//工位预订
	
	public static class JindiActionTypeCode{
		public static final String ACTION_TYPE_BUSINESS_CODE = "business";	//电商交易
		public static final String ACTION_TYPE_SITE_RENTAL_CODE = "site_rental";	//场所预订
		public static final String ACTION_TYPE_FORUM_POST_CODE = "forum_post";	//论坛发帖
		public static final String ACTION_TYPE_FORUM_COMMENT_CODE = "forum_comment";	//论坛评论
		public static final String ACTION_TYPE_ACTIVITY_CODE = "activity";	//活动
		public static final String ACTION_TYPE_ACTIVITY_SIGNUP_CODE = "activity_signup";	//活动报名
		public static final String ACTION_TYPE_REPAIR_CODE = "repair";	//报修
		public static final String ACTION_TYPE_OFFICE_RENTAL_CODE = "office_rental";	//办公租赁
		public static final String ACTION_TYPE_STATION_RENTAL_CODE = "station_rental";	//工位预订
	}
	
	private String code;
	
	private JindiActionType(String code){
		this.code = code;
	}
	
	public String getCode() {
		return this.code;
	}
	
	public static JindiActionType fromCode(String code){
		if (code != null) {
			for (JindiActionType jindiActionType : JindiActionType.values()) {
				if (jindiActionType.getCode().equals(code)) {
					return jindiActionType;
				}
			}
		}
		return null;
	}
}
