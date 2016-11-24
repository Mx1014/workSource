
-- added by wh 2016-11-24 增加资源编号关联性
 
ALTER TABLE `eh_rentalv2_resource_numbers` ADD COLUMN `group_id` INTEGER COMMENT '同一个groupid的两个编号资源被认为是一组资源';


--  
-- 资源预订签到权限表-- 这个表里有的 才能签到
-- 

CREATE TABLE `eh_rentalv2_privileges` (
  `id` BIGINT(20) NOT NULL COMMENT 'id of the record',
  `owner_type` VARCHAR(32) NOT NULL COMMENT 'i.e. organization/community',
  `owner_id` BIGINT(20) NOT NULL  ,
  `target_type` VARCHAR(32) NOT NULL COMMENT 'i.e. user',
  `target_id` BIGINT(20)  NOT NULL  COMMENT 'target object(user/group) id', 
  `creator_uid` BIGINT(20) NOT NULL COMMENT 'assignment creator uid',
  `create_time` DATETIME DEFAULT NULL COMMENT 'record create time',
  PRIMARY KEY (`id`)  
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;
