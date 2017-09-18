-- 入孵申请表  add by yanjun 20170913
CREATE TABLE `eh_incubator_applies` (
`id`  bigint(22) NOT NULL ,
`uuid`  varchar(128) NOT NULL DEFAULT '' ,
`namespace_id`  int(11) NULL ,
`apply_user_id` bigint(22)  NOT NULL,
`team_name`  varchar(255) NULL ,
`project_type`  varchar(255) NULL ,
`project_name`  varchar(255) NULL ,
`business_licence_uri`  varchar(255) NULL ,
`plan_book_uri`  varchar(255) NULL ,
`charger_name`  varchar(255) NULL ,
`charger_phone`  varchar(18) NULL ,
`charger_email`  varchar(255) NULL ,
`charger_wechat`  varchar(255) NULL ,
`approve_user_id`  bigint(22) NULL ,
`approve_status`  tinyint(4) NULL COMMENT '审批状态，0-待审批，1-拒绝，2-通过' ,
`approve_time`  datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP ,
`approve_opinion`  varchar(255) NULL ,
`create_time`  datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP ,
`re_apply_id`  bigint(22) NULL COMMENT '重新申请的Id',
PRIMARY KEY (`id`)
);
-- 入孵申请项目类型 add by yanjun 20170913
CREATE TABLE `eh_incubator_project_types` (
`id`  bigint(22) NOT NULL ,
`uuid`  varchar(128) NOT NULL DEFAULT '' ,
`name`  varchar(255) NOT NULL ,
`create_time`  datetime NULL ON UPDATE CURRENT_TIMESTAMP ,
PRIMARY KEY (`id`)
);

