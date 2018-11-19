
-- AUTHOR: 杨崇鑫
-- REMARK: 缺陷 #42416 【中天】更新自然季，合同刷新账单报错。
ALTER TABLE `preupdate`.`eh_contracts` MODIFY COLUMN `rent` decimal(20, 2) NULL DEFAULT NULL COMMENT '租金' AFTER `rent_size`;

-- AUTHOR: 李清岩
-- REMARK: 门禁v3.0.2 issue-34771
-- REMARK: 增加门禁组门禁关系表
CREATE TABLE `eh_aclink_group_doors` (
	`id` BIGINT  NOT NULL,
	`namespace_id` INT  DEFAULT NULL COMMENT '域空间id',
	`owner_id` BIGINT  NOT NULL DEFAULT '0' COMMENT '门禁组所属机构id',
	`owner_type` TINYINT DEFAULT NULL COMMENT '门禁组所属机构类型 0园区 1公司',
	`group_id` BIGINT DEFAULT NULL COMMENT '门禁组id',
	`door_id` BIGINT DEFAULT NULL COMMENT '门禁组所属门禁id',
	`status` TINYINT NOT NULL DEFAULT '1' COMMENT '状态 0删除 1有效',
	`creator_uid` BIGINT  DEFAULT NULL COMMENT '创建者id',
	`create_time` datetime DEFAULT NULL COMMENT '创建时间',
	`operator_uid` BIGINT  DEFAULT NULL COMMENT '修改者id',
	`operator_time` datetime DEFAULT NULL COMMENT '修改时间',
	PRIMARY KEY (`id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 COMMENT = '门禁组门禁关系表';

-- AUTHOR: 李清岩
-- REMARK: 门禁v3.0.2 issue-34771
-- REMARK: 门禁iPad自定义logo
ALTER TABLE `eh_aclink_ipads` ADD COLUMN `logo_uri` VARCHAR(2048) DEFAULT NULL COMMENT 'logo uri' ;
ALTER TABLE `eh_aclink_ipads` ADD COLUMN `logo_url` VARCHAR(2048) DEFAULT NULL COMMENT 'logo url' ;

-- AUTHOR: 李清岩
-- REMARK: 门禁v3.0.2 issue-34771
-- REMARK: 调整eh_door_access列名
ALTER TABLE `eh_door_access` CHANGE adress_detail address_detail VARCHAR(64);

