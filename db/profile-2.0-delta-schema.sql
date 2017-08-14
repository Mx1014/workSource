DROP TABLE IF EXISTS `eh_profile_contacts_sticky`;
CREATE TABLE `eh_profile_contacts_sticky` (
	`id` BIGINT NOT NULL COMMENT 'id',
	`namespace_id` INTEGER NOT NULL DEFAULT 0,
	`organization_id` BIGINT NOT NULL COMMENT '节点id',
	`detail_id` BIGINT NOT NULL COMMENT '成员detailId',
  `create_time` datetime COMMENT '创建时间',
  `update_time` datetime COMMENT '修改时间',
  `operator_uid` BIGINT COMMENT '操作人id',
	PRIMARY KEY (`id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4;