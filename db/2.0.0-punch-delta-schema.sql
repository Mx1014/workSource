ALTER TABLE `eh_punch_statistics` CHANGE `work_count` `work_count` DOUBLE DEFAULT NULL COMMENT '实际上班天数';
-- 增加一列存储升级内容                                                                                                              
ALTER TABLE `eh_version_urls`	ADD COLUMN `upgrade_description` TEXT NULL DEFAULT NULL AFTER `info_url`;                    
                                                                                                                           
-- 增加app名称和发布时间列                                                                                                           
ALTER TABLE `eh_version_urls`	ADD COLUMN `app_name` VARCHAR(50) NULL,ADD COLUMN `publish_time` DATETIME NULL;                      
--                                                                                                                                               
-- member of eh_users sharding group                                                                                                             
-- Pretending someone to login and operate as real user                                                                                          
--                                                                                                                                               
DROP TABLE IF EXISTS `eh_user_impersonations`;                                                                                                   
CREATE TABLE `eh_user_impersonations`(                                                                                                           
  `id` BIGINT NOT NULL COMMENT 'id of the record',                                                                                               
  `namespace_id` INTEGER NOT NULL DEFAULT 0,                                                                                                     
  `owner_type` VARCHAR(64) NOT NULL COMMENT 'USER',                                                                                              
  `owner_id` BIGINT NOT NULL DEFAULT 0,                                                                                                          
  `target_type` VARCHAR(64) NOT NULL COMMENT 'USER',                                                                                             
  `target_id` BIGINT NOT NULL DEFAULT 0,                                                                                                         
  `description` TEXT,                                                                                                                            
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: inactive, 1: active',                                                                          
  `create_time` DATETIME,                                                                                                                        
                                                                                                                                                 
  PRIMARY KEY (`id`)                                                                                                                             
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;                                                                                                         