alter table eh_contracts add sponsor_uid BIGINT COMMENT '发起人id';
alter table eh_contracts add sponsor_time DATETIME COMMENT '发起时间';

CREATE TABLE `eh_address_properties` (
	`id` BIGINT (20) NOT NULL COMMENT 'id of the record',
	`namespace_id` INT NOT NULL DEFAULT '0' COMMENT 'namespaceId',
	`community_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'communityId',
	`building_id` BIGINT (20) DEFAULT NULL COMMENT '楼栋id',
	`address_id` BIGINT (20) DEFAULT NULL COMMENT '房源id',
	`charging_items_id` BIGINT (20) COMMENT '费项id',
	`authorize_price` DECIMAL (10, 2) COMMENT '授权价',
	`apartment_authorize_type` TINYINT (4) DEFAULT NULL COMMENT '房源授权价类型（1:每天; 2:每月; 3:每个季度; 4:每年;)',
	`status` TINYINT (4) COMMENT '0-无效状态 ,2-有效状态',
	`create_time` DATETIME COMMENT '创建日期',
	`creator_uid` BIGINT COMMENT '创建人',
	`operator_time` DATETIME COMMENT '最近修改时间',
	`operator_uid` BIGINT COMMENT '最近修改人',
	PRIMARY KEY (id)
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 COMMENT '楼宇属性信息表';
