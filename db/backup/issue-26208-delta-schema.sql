-- Designer: zhiwei zhang
-- Description: ISSUE#26208 日程1.0

CREATE TABLE `eh_remind_settings` (
  `id` INT NOT NULL COMMENT '主键' ,
  `name` VARCHAR(64) NOT NULL COMMENT '名称，如 提前一天（09:00）' ,
  `offset_day` TINYINT NOT NULL DEFAULT 0 COMMENT '提前几天' ,
  `fix_time` TIME NULL COMMENT '提醒的固定时间，格式:09:00:00' ,
  `default_order` INT NOT NULL DEFAULT 0 COMMENT '排序字段' ,
  `creator_uid` BIGINT NOT NULL COMMENT '记录创建人userId' ,
  `create_time` DATETIME NOT NULL COMMENT '记录创建时间' ,
  `operator_uid` BIGINT NULL COMMENT '记录更新人userId' ,
  `operate_time` DATETIME NULL COMMENT '记录更新时间' ,
  PRIMARY KEY (`id`) ,
  UNIQUE KEY `u_eh_name` (`name`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT = '日程提醒时间设置表';


CREATE TABLE `eh_remind_categories` (
  `id` BIGINT NOT NULL COMMENT '主键' ,
  `namespace_id` INT NOT NULL DEFAULT 0 COMMENT '域空间ID' ,
  `owner_type` VARCHAR(64) NOT NULL COMMENT '默认EhOrganizations' ,
  `owner_id` BIGINT NOT NULL COMMENT 'owner_type对应的ID' ,
  `user_id` BIGINT NOT NULL COMMENT '分类拥有人的用户ID' ,
  `name` VARCHAR(64) NOT NULL COMMENT '日程分类的名称' ,
  `colour` VARCHAR(16) NULL COMMENT '日程分类的颜色RGB的argb-hex值，如 #FFF58F3E' ,
  `share_short_display` VARCHAR(64) NULL COMMENT '默认共享人概要信息：如 xx等3人' ,
  `default_order` INT NOT NULL DEFAULT 0 COMMENT '排序字段' ,
  `creator_uid` BIGINT NOT NULL COMMENT '记录创建人userId' ,
  `create_time` DATETIME NOT NULL COMMENT '记录创建时间' ,
  `operator_uid` BIGINT NULL COMMENT '记录更新人userId' ,
  `operate_time` DATETIME NULL COMMENT '记录更新时间' ,
  PRIMARY KEY (`id`) ,
  KEY `i_eh_owner_user_id` (`namespace_id`, `owner_type`, `owner_id`, `user_id`) ,
  UNIQUE KEY `u_eh_name` (`namespace_id`, `owner_type`, `owner_id`, `user_id`, `name`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT = '日程分类表';

CREATE TABLE `eh_remind_category_default_shares` (
  `id` BIGINT NOT NULL COMMENT '主键' ,
  `remind_category_id` BIGINT NOT NULL COMMENT '日程分类的ID,id of eh_remind_categories' ,
  `shared_source_type` VARCHAR(32) NOT NULL COMMENT '默认MEMBER_DETAIL' ,
  `shared_source_id` BIGINT NOT NULL COMMENT 'source_type对应的ID，员工档案ID' ,
  `shared_contract_name` VARCHAR(45) NOT NULL COMMENT '默认共享人的姓名' ,
  `creator_uid` BIGINT NOT NULL COMMENT '记录创建人userId' ,
  `create_time` DATETIME NOT NULL COMMENT '记录创建时间' ,
  PRIMARY KEY (`id`) ,
  KEY `i_eh_remind_category_id` (`remind_category_id`)
  ) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT = '日程分类默认共享人设置表';


CREATE TABLE `eh_reminds` (
  `id` BIGINT NOT NULL COMMENT '主键' ,
  `namespace_id` INT NOT NULL DEFAULT 0 COMMENT '域空间' ,
  `owner_type` VARCHAR(64) NOT NULL COMMENT '默认EhOrganizations' ,
  `owner_id` BIGINT NOT NULL COMMENT 'owner_type对应的ID' ,
  `user_id` BIGINT NOT NULL COMMENT '日程所有人的用户ID' ,
  `contact_name` VARCHAR(64) NOT NULL COMMENT '日程所有人的姓名' ,
  `plan_description` VARCHAR(512) NOT NULL COMMENT '日程描述' ,
  `plan_date` DATETIME NULL COMMENT '日程的计划日期' ,
  `expect_day_of_month` TINYINT NULL COMMENT '最初的重复日程计划的日期，DAY值' ,
  `remind_summary` VARCHAR(64) NULL COMMENT '提醒的文本概要',
  `repeat_type` TINYINT NOT NULL DEFAULT 0 COMMENT '重复类型：0-无，1-每日，2-每周，3-每月，4-每年' ,
  `remind_type_id` INT COMMENT '提醒类型ID',
  `remind_type` VARCHAR(32) NULL COMMENT '提醒类型的名称，如  提前一天（09:00）' ,
  `remind_time` DATETIME NULL COMMENT '提醒时间，根据选择的提醒类型计算得到' ,
  `act_remind_time` DATETIME NULL COMMENT '实际提醒时间，即实际出发提醒的时间' ,
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT ' 1: UNDO 未完成 , 2 : DONE 已完成  ' ,
  `remind_category_id` BIGINT NOT NULL COMMENT '日程分类ID' ,
  `track_remind_id` BIGINT NULL COMMENT '关注的日程ID' ,
  `track_remind_user_id` BIGINT NULL COMMENT '关注的日程的所有人的uid' ,
  `track_contract_name` VARCHAR(45) NULL COMMENT '关注的日程所有人的姓名' ,
  `share_short_display` VARCHAR(64) NULL COMMENT '共享人概要信息：如 xx等3人' ,
  `share_count` INT NOT NULL DEFAULT 0 COMMENT '共享人数量',
  `default_order` INT NOT NULL DEFAULT 0 COMMENT '排序字段' ,
  `creator_uid` BIGINT NOT NULL COMMENT '记录创建人userId' ,
  `create_time` DATETIME NOT NULL COMMENT '记录创建时间' ,
  `operator_uid` BIGINT NULL COMMENT '记录更新人userId' ,
  `operate_time` DATETIME NULL COMMENT '记录更新时间' ,
  PRIMARY KEY (`id`) ,
  KEY `i_eh_owner_user_id` (`namespace_id`, `owner_type`, `owner_id`, `user_id`) ,
  KEY `i_eh_remind_time` (`remind_time`),
  KEY `i_eh_track_remind_id` (`track_remind_id`),
  KEY `i_eh_remind_category_id` (`remind_category_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT = '日程表';



CREATE TABLE `eh_remind_shares` (
  `id` BIGINT NOT NULL COMMENT '主键' ,
  `namespace_id` INT NOT NULL DEFAULT 0 COMMENT '域空间' ,
  `owner_type` VARCHAR(64) NOT NULL COMMENT '默认EhOrganizations' ,
  `owner_id` BIGINT NOT NULL COMMENT 'owner_type对应的ID' ,
  `remind_id` BIGINT NOT NULL COMMENT '日程ID' ,
  `owner_user_id` BIGINT NOT NULL COMMENT '分享人的userId' ,
  `owner_contract_name` VARCHAR(64) NOT NULL COMMENT '分享人的姓名' ,
  `shared_source_type` VARCHAR(32) NOT NULL COMMENT '默认MEMBER_DETAIL，被分享人ID类型' ,
  `shared_source_id` BIGINT NOT NULL COMMENT 'source_type对应的ID，被分享人员工档案ID' ,
  `shared_source_name` VARCHAR(128) NOT NULL COMMENT '被分享人员工姓名' ,
  `creator_uid` BIGINT NOT NULL COMMENT '记录创建人userId' ,
  `create_time` DATETIME NOT NULL COMMENT '记录创建时间' ,
  PRIMARY KEY (`id`) ,
  KEY `i_eh_shared_source_id` (`namespace_id`, `owner_type`, `owner_id`, `shared_source_type`,`shared_source_id`) ,
  KEY `i_eh_remind_id` (`remind_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT = '日程共享人记录表';


 CREATE TABLE `eh_remind_demo_create_logs` (
  `id` BIGINT NOT NULL COMMENT '主键' ,
  `namespace_id` INT NOT NULL DEFAULT 0 COMMENT '域空间' ,
  `owner_type` VARCHAR(64) NOT NULL COMMENT '默认EhOrganizations' ,
  `owner_id` BIGINT NOT NULL COMMENT 'owner_type对应的ID' ,
  `user_id` BIGINT NOT NULL COMMENT '日程所有人的用户ID' ,
  `creator_uid` BIGINT NOT NULL COMMENT '记录创建人userId' ,
  `create_time` DATETIME NOT NULL COMMENT '记录创建时间' ,
  PRIMARY KEY (`id`) ,
  KEY `i_eh_owner_user_id` (`namespace_id`, `owner_type`, `owner_id`, `user_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT = '日程案例创建记录，避免案例重复创建';


-- End by: zhiwei zhang
