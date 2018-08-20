-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: OPERATION
-- DESCRIPTION: 此SECTION放升级相关的操作要求，如调接口、查询数据确认、修改配置文件、更新特殊程序等
-- AUTHOR: 张智伟
-- REMARK: 调用接口/evh/punch/punchDayLogInitializeByMonth 不需要输入参数 初始化某个月的每日统计数据,上线时手动调用进行初始化


-- --------------------- SECTION END ---------------------------------------------------------

-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ALL
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境
-- AUTHOR: 杨崇鑫
-- REMARK: 根据域空间判断是否展示能耗数据，测试环境初始化鼎峰汇支持展示能耗数据
delete from eh_service_module_exclude_functions where namespace_id=999951 and module_id=20400 and function_id=101;-- 后台

SET @id = ifnull((SELECT MAX(id) FROM `eh_payment_app_views`),0); -- APP
INSERT INTO `eh_payment_app_views`(`id`, `namespace_id`, `community_id`, `has_view`, `view_item`, `remark1_type`, `remark1_identifier`, `remark2_type`, `remark2_identifier`, `remark3_type`, `remark3_identifier`) 
VALUES (@id := @id + 1, 999951, NULL, 1, 'ENERGY', NULL, NULL, NULL, NULL, NULL, NULL);


-- AUTHOR: 黄良铭
-- REMARK: 修改logo_url字段长度由128改为512
ALTER TABLE eh_app_urls MODIFY COLUMN logo_url VARCHAR(512) ;


-- AUTHOR: 吴寒 2018年8月17日
-- REMARK: 锁掌柜门禁对接
-- 会议室1
SET @door_id = (SELECT MAX(id) FROM `eh_door_access`);
INSERT INTO `eh_door_access` (`id`, `namespace_id`, `uuid`, `door_type`, `hardware_id`, `name`, `display_name`, `description`, `avatar`, `address`, `active_user_id`, `creator_user_id`, `longitude`, `latitude`, `geohash`, `aes_iv`, `link_status`, `owner_type`, `owner_id`, `role`, `create_time`, `status`, `acking_secret_version`, `expect_secret_key`, `groupId`, `floor_id`, `mac_copy`, `enable_amount`, `local_server_id`, `has_qr`) VALUES((@door_id := @door_id + 1),'999972',UUID(),'18','DE:85:D2:31:3E:C0','会议室1','会议室1','会议室1',NULL,NULL,'505361','505361',NULL,NULL,NULL,'','0','0','240111044331050361','0','2018-06-01 06:01:11','1','1','1','0',NULL,NULL,NULL,NULL,'1');
SET @aclink_id = (SELECT MAX(id) FROM `eh_aclinks`);
INSERT INTO `eh_aclinks` (`id`, `namespace_id`, `door_id`, `device_name`, `manufacturer`, `firware_ver`, `create_time`, `status`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `driver`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`) VALUES((@aclink_id := @aclink_id+1),'999972',@door_id,'会议室1','uclbrt','1',NULL,'1','{"sid":"3a56074d26aebd50027c23a3679b5fa5","token":"768446d02dca22504658ff46cca019","communityNo":"1316881497","buildNo":"001","floorNo":"001","roomNo":"A1"}',NULL,NULL,NULL,NULL,'zuolin',NULL,NULL,NULL,NULL,NULL);

-- 会议室2
SET @door_id = (SELECT MAX(id) FROM `eh_door_access`);
INSERT INTO `eh_door_access` (`id`, `namespace_id`, `uuid`, `door_type`, `hardware_id`, `name`, `display_name`, `description`, `avatar`, `address`, `active_user_id`, `creator_user_id`, `longitude`, `latitude`, `geohash`, `aes_iv`, `link_status`, `owner_type`, `owner_id`, `role`, `create_time`, `status`, `acking_secret_version`, `expect_secret_key`, `groupId`, `floor_id`, `mac_copy`, `enable_amount`, `local_server_id`, `has_qr`) VALUES((@door_id := @door_id + 1),'999972',UUID(),'18','DE:85:D2:31:3E:C0','会议室2','会议室2','会议室2',NULL,NULL,'505361','505361',NULL,NULL,NULL,'','0','0','240111044331050361','0','2018-06-01 06:01:11','1','1','1','0',NULL,NULL,NULL,NULL,'1');
SET @aclink_id = (SELECT MAX(id) FROM `eh_aclinks`);
INSERT INTO `eh_aclinks` (`id`, `namespace_id`, `door_id`, `device_name`, `manufacturer`, `firware_ver`, `create_time`, `status`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `driver`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`) VALUES((@aclink_id := @aclink_id+1),'999972',@door_id,'会议室2','uclbrt','1',NULL,'1','{"sid":"3a56074d26aebd50027c23a3679b5fa5","token":"768446d02dca22504658ff46cca019","communityNo":"1316881497","buildNo":"001","floorNo":"001","roomNo":"A2"}',NULL,NULL,NULL,NULL,'zuolin',NULL,NULL,NULL,NULL,NULL);

-- 会议室3
SET @door_id = (SELECT MAX(id) FROM `eh_door_access`);
INSERT INTO `eh_door_access` (`id`, `namespace_id`, `uuid`, `door_type`, `hardware_id`, `name`, `display_name`, `description`, `avatar`, `address`, `active_user_id`, `creator_user_id`, `longitude`, `latitude`, `geohash`, `aes_iv`, `link_status`, `owner_type`, `owner_id`, `role`, `create_time`, `status`, `acking_secret_version`, `expect_secret_key`, `groupId`, `floor_id`, `mac_copy`, `enable_amount`, `local_server_id`, `has_qr`) VALUES((@door_id := @door_id + 1),'999972',UUID(),'18','DE:85:D2:31:3E:C0','会议室3','会议室3','会议室3',NULL,NULL,'505361','505361',NULL,NULL,NULL,'','0','0','240111044331050361','0','2018-06-01 06:01:11','1','1','1','0',NULL,NULL,NULL,NULL,'1');
SET @aclink_id = (SELECT MAX(id) FROM `eh_aclinks`);
INSERT INTO `eh_aclinks` (`id`, `namespace_id`, `door_id`, `device_name`, `manufacturer`, `firware_ver`, `create_time`, `status`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `driver`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`) VALUES((@aclink_id := @aclink_id+1),'999972',@door_id,'会议室3','uclbrt','1',NULL,'1','{"sid":"3a56074d26aebd50027c23a3679b5fa5","token":"768446d02dca22504658ff46cca019","communityNo":"1316881497","buildNo":"001","floorNo":"001","roomNo":"A3"}',NULL,NULL,NULL,NULL,'zuolin',NULL,NULL,NULL,NULL,NULL);

-- 会议室4
SET @door_id = (SELECT MAX(id) FROM `eh_door_access`);
INSERT INTO `eh_door_access` (`id`, `namespace_id`, `uuid`, `door_type`, `hardware_id`, `name`, `display_name`, `description`, `avatar`, `address`, `active_user_id`, `creator_user_id`, `longitude`, `latitude`, `geohash`, `aes_iv`, `link_status`, `owner_type`, `owner_id`, `role`, `create_time`, `status`, `acking_secret_version`, `expect_secret_key`, `groupId`, `floor_id`, `mac_copy`, `enable_amount`, `local_server_id`, `has_qr`) VALUES((@door_id := @door_id + 1),'999972',UUID(),'18','DE:85:D2:31:3E:C0','会议室4','会议室4','会议室4',NULL,NULL,'505361','505361',NULL,NULL,NULL,'','0','0','240111044331050361','0','2018-06-01 06:01:11','1','1','1','0',NULL,NULL,NULL,NULL,'1');
SET @aclink_id = (SELECT MAX(id) FROM `eh_aclinks`);
INSERT INTO `eh_aclinks` (`id`, `namespace_id`, `door_id`, `device_name`, `manufacturer`, `firware_ver`, `create_time`, `status`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `driver`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`) VALUES((@aclink_id := @aclink_id+1),'999972',@door_id,'会议室4','uclbrt','1',NULL,'1','{"sid":"3a56074d26aebd50027c23a3679b5fa5","token":"768446d02dca22504658ff46cca019","communityNo":"1316881497","buildNo":"001","floorNo":"001","roomNo":"A4"}',NULL,NULL,NULL,NULL,'zuolin',NULL,NULL,NULL,NULL,NULL);

-- 会议室5
SET @door_id = (SELECT MAX(id) FROM `eh_door_access`);
INSERT INTO `eh_door_access` (`id`, `namespace_id`, `uuid`, `door_type`, `hardware_id`, `name`, `display_name`, `description`, `avatar`, `address`, `active_user_id`, `creator_user_id`, `longitude`, `latitude`, `geohash`, `aes_iv`, `link_status`, `owner_type`, `owner_id`, `role`, `create_time`, `status`, `acking_secret_version`, `expect_secret_key`, `groupId`, `floor_id`, `mac_copy`, `enable_amount`, `local_server_id`, `has_qr`) VALUES((@door_id := @door_id + 1),'999972',UUID(),'18','DE:85:D2:31:3E:C0','会议室5','会议室5','会议室5',NULL,NULL,'505361','505361',NULL,NULL,NULL,'','0','0','240111044331050361','0','2018-06-01 06:01:11','1','1','1','0',NULL,NULL,NULL,NULL,'1');
SET @aclink_id = (SELECT MAX(id) FROM `eh_aclinks`);
INSERT INTO `eh_aclinks` (`id`, `namespace_id`, `door_id`, `device_name`, `manufacturer`, `firware_ver`, `create_time`, `status`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `driver`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`) VALUES((@aclink_id := @aclink_id+1),'999972',@door_id,'会议室5','uclbrt','1',NULL,'1','{"sid":"3a56074d26aebd50027c23a3679b5fa5","token":"768446d02dca22504658ff46cca019","communityNo":"1316881497","buildNo":"001","floorNo":"001","roomNo":"A5"}',NULL,NULL,NULL,NULL,'zuolin',NULL,NULL,NULL,NULL,NULL);

-- 会议室6
SET @door_id = (SELECT MAX(id) FROM `eh_door_access`);
INSERT INTO `eh_door_access` (`id`, `namespace_id`, `uuid`, `door_type`, `hardware_id`, `name`, `display_name`, `description`, `avatar`, `address`, `active_user_id`, `creator_user_id`, `longitude`, `latitude`, `geohash`, `aes_iv`, `link_status`, `owner_type`, `owner_id`, `role`, `create_time`, `status`, `acking_secret_version`, `expect_secret_key`, `groupId`, `floor_id`, `mac_copy`, `enable_amount`, `local_server_id`, `has_qr`) VALUES((@door_id := @door_id + 1),'999972',UUID(),'18','DE:85:D2:31:3E:C0','会议室6','会议室6','会议室6',NULL,NULL,'505361','505361',NULL,NULL,NULL,'','0','0','240111044331050361','0','2018-06-01 06:01:11','1','1','1','0',NULL,NULL,NULL,NULL,'1');
SET @aclink_id = (SELECT MAX(id) FROM `eh_aclinks`);
INSERT INTO `eh_aclinks` (`id`, `namespace_id`, `door_id`, `device_name`, `manufacturer`, `firware_ver`, `create_time`, `status`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `driver`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`) VALUES((@aclink_id := @aclink_id+1),'999972',@door_id,'会议室6','uclbrt','1',NULL,'1','{"sid":"3a56074d26aebd50027c23a3679b5fa5","token":"768446d02dca22504658ff46cca019","communityNo":"1316881497","buildNo":"001","floorNo":"001","roomNo":"A6"}',NULL,NULL,NULL,NULL,'zuolin',NULL,NULL,NULL,NULL,NULL);

-- 会议室7-1
SET @door_id = (SELECT MAX(id) FROM `eh_door_access`);
INSERT INTO `eh_door_access` (`id`, `namespace_id`, `uuid`, `door_type`, `hardware_id`, `name`, `display_name`, `description`, `avatar`, `address`, `active_user_id`, `creator_user_id`, `longitude`, `latitude`, `geohash`, `aes_iv`, `link_status`, `owner_type`, `owner_id`, `role`, `create_time`, `status`, `acking_secret_version`, `expect_secret_key`, `groupId`, `floor_id`, `mac_copy`, `enable_amount`, `local_server_id`, `has_qr`) VALUES((@door_id := @door_id + 1),'999972',UUID(),'18','DE:85:D2:31:3E:C0','会议室7-1','会议室7-1','会议室7-1',NULL,NULL,'505361','505361',NULL,NULL,NULL,'','0','0','240111044331050361','0','2018-06-01 06:01:11','1','1','1','0',NULL,NULL,NULL,NULL,'1');
SET @aclink_id = (SELECT MAX(id) FROM `eh_aclinks`);
INSERT INTO `eh_aclinks` (`id`, `namespace_id`, `door_id`, `device_name`, `manufacturer`, `firware_ver`, `create_time`, `status`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `driver`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`) VALUES((@aclink_id := @aclink_id+1),'999972',@door_id,'会议室7-1','uclbrt','1',NULL,'1','{"sid":"3a56074d26aebd50027c23a3679b5fa5","token":"768446d02dca22504658ff46cca019","communityNo":"1316881497","buildNo":"001","floorNo":"001","roomNo":"A71"}',NULL,NULL,NULL,NULL,'zuolin',NULL,NULL,NULL,NULL,NULL);

-- 会议室7-2
SET @door_id = (SELECT MAX(id) FROM `eh_door_access`);
INSERT INTO `eh_door_access` (`id`, `namespace_id`, `uuid`, `door_type`, `hardware_id`, `name`, `display_name`, `description`, `avatar`, `address`, `active_user_id`, `creator_user_id`, `longitude`, `latitude`, `geohash`, `aes_iv`, `link_status`, `owner_type`, `owner_id`, `role`, `create_time`, `status`, `acking_secret_version`, `expect_secret_key`, `groupId`, `floor_id`, `mac_copy`, `enable_amount`, `local_server_id`, `has_qr`) VALUES((@door_id := @door_id + 1),'999972',UUID(),'18','DE:85:D2:31:3E:C0','会议室7-2','会议室7-2','会议室7-2',NULL,NULL,'505361','505361',NULL,NULL,NULL,'','0','0','240111044331050361','0','2018-06-01 06:01:11','1','1','1','0',NULL,NULL,NULL,NULL,'1');
SET @aclink_id = (SELECT MAX(id) FROM `eh_aclinks`);
INSERT INTO `eh_aclinks` (`id`, `namespace_id`, `door_id`, `device_name`, `manufacturer`, `firware_ver`, `create_time`, `status`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `driver`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`) VALUES((@aclink_id := @aclink_id+1),'999972',@door_id,'会议室7-2','uclbrt','1',NULL,'1','{"sid":"3a56074d26aebd50027c23a3679b5fa5","token":"768446d02dca22504658ff46cca019","communityNo":"1316881497","buildNo":"001","floorNo":"001","roomNo":"A72"}',NULL,NULL,NULL,NULL,'zuolin',NULL,NULL,NULL,NULL,NULL);

-- 会议室8
SET @door_id = (SELECT MAX(id) FROM `eh_door_access`);
INSERT INTO `eh_door_access` (`id`, `namespace_id`, `uuid`, `door_type`, `hardware_id`, `name`, `display_name`, `description`, `avatar`, `address`, `active_user_id`, `creator_user_id`, `longitude`, `latitude`, `geohash`, `aes_iv`, `link_status`, `owner_type`, `owner_id`, `role`, `create_time`, `status`, `acking_secret_version`, `expect_secret_key`, `groupId`, `floor_id`, `mac_copy`, `enable_amount`, `local_server_id`, `has_qr`) VALUES((@door_id := @door_id + 1),'999972',UUID(),'18','DE:85:D2:31:3E:C0','会议室8','会议室8','会议室8',NULL,NULL,'505361','505361',NULL,NULL,NULL,'','0','0','240111044331050361','0','2018-06-01 06:01:11','1','1','1','0',NULL,NULL,NULL,NULL,'1');
SET @aclink_id = (SELECT MAX(id) FROM `eh_aclinks`);
INSERT INTO `eh_aclinks` (`id`, `namespace_id`, `door_id`, `device_name`, `manufacturer`, `firware_ver`, `create_time`, `status`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `driver`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`) VALUES((@aclink_id := @aclink_id+1),'999972',@door_id,'会议室8','uclbrt','1',NULL,'1','{"sid":"3a56074d26aebd50027c23a3679b5fa5","token":"768446d02dca22504658ff46cca019","communityNo":"1316881497","buildNo":"001","floorNo":"001","roomNo":"A8"}',NULL,NULL,NULL,NULL,'zuolin',NULL,NULL,NULL,NULL,NULL);

-- 会议室10
SET @door_id = (SELECT MAX(id) FROM `eh_door_access`);
INSERT INTO `eh_door_access` (`id`, `namespace_id`, `uuid`, `door_type`, `hardware_id`, `name`, `display_name`, `description`, `avatar`, `address`, `active_user_id`, `creator_user_id`, `longitude`, `latitude`, `geohash`, `aes_iv`, `link_status`, `owner_type`, `owner_id`, `role`, `create_time`, `status`, `acking_secret_version`, `expect_secret_key`, `groupId`, `floor_id`, `mac_copy`, `enable_amount`, `local_server_id`, `has_qr`) VALUES((@door_id := @door_id + 1),'999972',UUID(),'18','DE:85:D2:31:3E:C0','会议室10','会议室10','会议室10',NULL,NULL,'505361','505361',NULL,NULL,NULL,'','0','0','240111044331050361','0','2018-06-01 06:01:11','1','1','1','0',NULL,NULL,NULL,NULL,'1');
SET @aclink_id = (SELECT MAX(id) FROM `eh_aclinks`);
INSERT INTO `eh_aclinks` (`id`, `namespace_id`, `door_id`, `device_name`, `manufacturer`, `firware_ver`, `create_time`, `status`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `driver`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`) VALUES((@aclink_id := @aclink_id+1),'999972',@door_id,'会议室10','uclbrt','1',NULL,'1','{"sid":"3a56074d26aebd50027c23a3679b5fa5","token":"768446d02dca22504658ff46cca019","communityNo":"1316881497","buildNo":"001","floorNo":"002","roomNo":"A10"}',NULL,NULL,NULL,NULL,'zuolin',NULL,NULL,NULL,NULL,NULL);

-- --------------------- SECTION END ---------------------------------------------------------

-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: zuolin-base
-- DESCRIPTION: 此SECTION只在左邻基线（非独立署部）执行的脚本
-- AUTHOR:
-- REMARK:

-- --------------------- SECTION END ---------------------------------------------------------

-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: dev
-- DESCRIPTION: 此SECTION只在开发库中执行的脚本
-- AUTHOR:
-- REMARK:

-- --------------------- SECTION END ---------------------------------------------------------

-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: zijing
-- DESCRIPTION: 此SECTION只在清华信息港(紫荆)-999984执行的脚本
-- AUTHOR:
-- REMARK:

-- --------------------- SECTION END ---------------------------------------------------------

-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: guangda
-- DESCRIPTION: 此SECTION只在光大-999979执行的脚本
-- AUTHOR:
-- REMARK:

-- --------------------- SECTION END ---------------------------------------------------------

-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: szbay
-- DESCRIPTION: 此SECTION只在深圳湾-999966执行的脚本
-- AUTHOR:
-- REMARK:

-- --------------------- SECTION END ---------------------------------------------------------

-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: chuangyechang
-- DESCRIPTION: 此SECTION只在成都创业场-999964执行的脚本
-- AUTHOR:
-- REMARK:

-- --------------------- SECTION END ---------------------------------------------------------

-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: anbang
-- DESCRIPTION: 此SECTION只在安邦物业-999949执行的脚本
-- AUTHOR:
-- REMARK:

-- --------------------- SECTION END ---------------------------------------------------------

