-- 通用脚本  
-- ADD BY 唐岑
-- # 资产管理V3.0、V3.1 
ALTER TABLE `eh_buildings` ADD COLUMN `floor_number` int(11) DEFAULT NULL COMMENT '该楼栋的楼层数目';

ALTER TABLE `eh_buildings` DROP INDEX `u_eh_community_id_name`;

ALTER TABLE `eh_buildings` ADD INDEX `u_eh_community_id_name` (`community_id`, `name`) USING BTREE ;

CREATE TABLE `eh_address_living_status` (
  `id` bigint(20) NOT NULL,
  `livingStatus` tinyint(4) DEFAULT NULL COMMENT '房源状态',
  `text` text COMMENT '房源状态中文',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='房源状态记录表';

-- END by 唐岑