INSERT INTO `eh_rentalv2_structure_template` (`id`, `name`, `display_name`, `icon_uri`, `default_order`) VALUES ('1', '电视', '电视', '1', '1');
INSERT INTO `eh_rentalv2_structure_template` (`id`, `name`, `display_name`, `icon_uri`, `default_order`) VALUES ('2', '投屏', '投屏', '2', '2');
INSERT INTO `eh_rentalv2_structure_template` (`id`, `name`, `display_name`, `icon_uri`, `default_order`) VALUES ('3', '白板', '白板', '3', '3');
INSERT INTO `eh_rentalv2_structure_template` (`id`, `name`, `display_name`, `icon_uri`, `default_order`) VALUES ('4', '投影仪', '投影仪', '4', '4');
INSERT INTO `eh_rentalv2_structure_template` (`id`, `name`, `display_name`, `icon_uri`, `default_order`) VALUES ('5', '电话会议', '电话会议', '5', '5');
INSERT INTO `eh_rentalv2_structure_template` (`id`, `name`, `display_name`, `icon_uri`, `default_order`) VALUES ('6', '视频会议', '视频会议', '6', '6');
INSERT INTO `eh_rentalv2_structure_template` (`id`, `name`, `display_name`, `icon_uri`, `default_order`) VALUES ('7', '音响', '音响', '7', '7');
INSERT INTO `eh_rentalv2_structure_template` (`id`, `name`, `display_name`, `icon_uri`, `default_order`) VALUES ('8', '麦克风', '麦克风', '8', '8');

-- 数据初始化
set @id = 0;
INSERT INTO `eh_rentalv2_structures` (`id`, `template_id`, `source_type`, `source_id`, `name`, `display_name`, `icon_uri`, `is_surport`, `default_order`, `resource_type`)
select @id := @id + 1,1,source_type,source_id,'电视','电视','',1,1,'default' from  eh_rentalv2_default_rules where source_type = 'resource_rule' and resource_type = 'default';
INSERT INTO `eh_rentalv2_structures` (`id`, `template_id`, `source_type`, `source_id`, `name`, `display_name`, `icon_uri`, `is_surport`, `default_order`, `resource_type`)
select @id := @id + 1,1,source_type,id,'电视','电视','',1,1,'default' from  eh_rentalv2_default_rules where source_type = 'default_rule' and resource_type = 'default';

INSERT INTO `eh_rentalv2_structures` (`id`, `template_id`, `source_type`, `source_id`, `name`, `display_name`, `icon_uri`, `is_surport`, `default_order`, `resource_type`)
select @id := @id + 1,2,source_type,source_id,'投屏','投屏','',1,2,'default' from  eh_rentalv2_default_rules where source_type = 'resource_rule' and resource_type = 'default';
INSERT INTO `eh_rentalv2_structures` (`id`, `template_id`, `source_type`, `source_id`, `name`, `display_name`, `icon_uri`, `is_surport`, `default_order`, `resource_type`)
select @id := @id + 1,2,source_type,id,'投屏','投屏','',1,2,'default' from  eh_rentalv2_default_rules where source_type = 'default_rule' and resource_type = 'default';

INSERT INTO `eh_rentalv2_structures` (`id`, `template_id`, `source_type`, `source_id`, `name`, `display_name`, `icon_uri`, `is_surport`, `default_order`, `resource_type`)
select @id := @id + 1,3,source_type,source_id,'白板','白板','',1,3,'default' from  eh_rentalv2_default_rules where source_type = 'resource_rule' and resource_type = 'default';
INSERT INTO `eh_rentalv2_structures` (`id`, `template_id`, `source_type`, `source_id`, `name`, `display_name`, `icon_uri`, `is_surport`, `default_order`, `resource_type`)
select @id := @id + 1,3,source_type,id,'白板','白板','',1,3,'default' from  eh_rentalv2_default_rules where source_type = 'default_rule' and resource_type = 'default';

INSERT INTO `eh_rentalv2_structures` (`id`, `template_id`, `source_type`, `source_id`, `name`, `display_name`, `icon_uri`, `is_surport`, `default_order`, `resource_type`)
select @id := @id + 1,4,source_type,source_id,'投影仪','投影仪','',1,4,'default' from  eh_rentalv2_default_rules where source_type = 'resource_rule' and resource_type = 'default';
INSERT INTO `eh_rentalv2_structures` (`id`, `template_id`, `source_type`, `source_id`, `name`, `display_name`, `icon_uri`, `is_surport`, `default_order`, `resource_type`)
select @id := @id + 1,4,source_type,id,'投影仪','投影仪','',1,4,'default' from  eh_rentalv2_default_rules where source_type = 'default_rule' and resource_type = 'default';

INSERT INTO `eh_rentalv2_structures` (`id`, `template_id`, `source_type`, `source_id`, `name`, `display_name`, `icon_uri`, `is_surport`, `default_order`, `resource_type`)
select @id := @id + 1,5,source_type,source_id,'电话会议','电话会议','',1,5,'default' from  eh_rentalv2_default_rules where source_type = 'resource_rule' and resource_type = 'default';
INSERT INTO `eh_rentalv2_structures` (`id`, `template_id`, `source_type`, `source_id`, `name`, `display_name`, `icon_uri`, `is_surport`, `default_order`, `resource_type`)
select @id := @id + 1,5,source_type,id,'电话会议','电话会议','',1,5,'default' from  eh_rentalv2_default_rules where source_type = 'default_rule' and resource_type = 'default';

INSERT INTO `eh_rentalv2_structures` (`id`, `template_id`, `source_type`, `source_id`, `name`, `display_name`, `icon_uri`, `is_surport`, `default_order`, `resource_type`)
select @id := @id + 1,6,source_type,source_id,'视频会议','视频会议','',1,6,'default' from  eh_rentalv2_default_rules where source_type = 'resource_rule' and resource_type = 'default';
INSERT INTO `eh_rentalv2_structures` (`id`, `template_id`, `source_type`, `source_id`, `name`, `display_name`, `icon_uri`, `is_surport`, `default_order`, `resource_type`)
select @id := @id + 1,6,source_type,id,'视频会议','视频会议','',1,6,'default' from  eh_rentalv2_default_rules where source_type = 'default_rule' and resource_type = 'default';

INSERT INTO `eh_rentalv2_structures` (`id`, `template_id`, `source_type`, `source_id`, `name`, `display_name`, `icon_uri`, `is_surport`, `default_order`, `resource_type`)
select @id := @id + 1,7,source_type,source_id,'音响','音响','',1,7,'default' from  eh_rentalv2_default_rules where source_type = 'resource_rule' and resource_type = 'default';
INSERT INTO `eh_rentalv2_structures` (`id`, `template_id`, `source_type`, `source_id`, `name`, `display_name`, `icon_uri`, `is_surport`, `default_order`, `resource_type`)
select @id := @id + 1,7,source_type,id,'音响','音响','',1,7,'default' from  eh_rentalv2_default_rules where source_type = 'default_rule' and resource_type = 'default';

INSERT INTO `eh_rentalv2_structures` (`id`, `template_id`, `source_type`, `source_id`, `name`, `display_name`, `icon_uri`, `is_surport`, `default_order`, `resource_type`)
select @id := @id + 1,8,source_type,source_id,'麦克风','麦克风','',1,8,'default' from  eh_rentalv2_default_rules where source_type = 'resource_rule' and resource_type = 'default';
INSERT INTO `eh_rentalv2_structures` (`id`, `template_id`, `source_type`, `source_id`, `name`, `display_name`, `icon_uri`, `is_surport`, `default_order`, `resource_type`)
select @id := @id + 1,8,source_type,id,'麦克风','麦克风','',1,8,'default' from  eh_rentalv2_default_rules where source_type = 'default_rule' and resource_type = 'default';

update eh_locale_templates set text = '您已完成支付，成功预约${useTime}的${resourceName}，请按照预约的时段使用资源，感谢您的使用。' where `scope` = 'rental.notification' and `code` = 5;