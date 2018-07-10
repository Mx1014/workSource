CREATE TABLE `eh_pm_resoucre_reservations` (
	`id` BIGINT NOT NULL COMMENT 'id of the record',
	`enterprise_customer_id` BIGINT COMMENT 'primary id of eh_enterprise_customer',
	`address_id` BIGINT COMMENT 'id of eh_addresses',
	`start_time` TIMESTAMP NOT NULL COMMENT 'start time of this reservation',
	`end_time` TIMESTAMP NOT NULL COMMENT 'end time of this reservation',
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '1. inactive; 2: active; 3: deleted;',
	`previous_living_status` TINYINT DEFAULT NULL COMMENT 'previous living status in eh_organization_address_mapping',
	`namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace id',
  `community_id` BIGINT DEFAULT NULL COMMENT 'id of community',
  `entry_id` BIGINT DEFAULT NULL COMMENT '其他入口的备用字段',
	`creator_uid` BIGINT DEFAULT NULL COMMENT '创建者id，可以为空',
	`create_time` DATETIME NOT NULL DEFAULT NOW() COMMENT '事件发生时间',
	`update_time` DATETIME COMMENT '事件update时间',
	`update_uid` BIGINT DEFAULT NULL ,
	PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '访客管理预约编码表';