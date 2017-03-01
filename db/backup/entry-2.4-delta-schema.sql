ALTER TABLE `eh_enterprise_op_requests` ADD COLUMN `contract_id` BIGINT COMMENT 'eh_contracts id';

ALTER TABLE `eh_yellow_pages` ADD COLUMN `building_id` BIGINT COMMENT 'eh_buildings id';


CREATE TABLE `eh_enterprise_op_request_buildings` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `enterprise_op_requests_id` BIGINT NOT NULL COMMENT 'eh_enterprise_op_requests id',
  `building_id` BIGINT  COMMENT 'building id ', 
  `status` TINYINT ,
  `creator_uid` BIGINT ,
  `create_time` DATETIME ,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4; 