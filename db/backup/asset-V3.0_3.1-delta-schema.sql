-- 通用脚本  
-- ADD BY 唐岑
-- # 资产管理V3.0、V3.1 
ALTER TABLE `eh_buildings` ADD COLUMN `floor_number` int(11) DEFAULT NULL COMMENT '该楼栋的楼层数目';

ALTER TABLE `eh_buildings` DROP INDEX `u_eh_community_id_name`;

ALTER TABLE `eh_buildings` ADD INDEX `u_eh_community_id_name` (`community_id`, `name`) USING BTREE ;

ALTER TABLE `eh_buildings` MODIFY COLUMN `building_number` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '楼栋编号';

-- END by 唐岑