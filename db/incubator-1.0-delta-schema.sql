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

CREATE TABLE `eh_incubator_apply_attachments` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `incubator_apply_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'owner id, e.g incubator_apply_id',
  `type`  tinyint(4) NULL COMMENT '类型，1-business_licence，2-plan_book',
  `name` varchar(128) DEFAULT NULL,
  `file_size` int(11) NOT NULL DEFAULT '0',
  `content_type` varchar(32) DEFAULT NULL COMMENT 'attachment object content type',
  `content_uri` varchar(1024) DEFAULT NULL COMMENT 'attachment object link info on storage',
  `download_count` int(11) NOT NULL DEFAULT '0',
  `creator_uid` bigint(20) NOT NULL DEFAULT '0',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
