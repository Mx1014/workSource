INSERT INTO eh_content_server_resources(id, owner_id, resource_id, resource_md5, resource_type, resource_size, resource_name, metadata) 
	VALUES(2, 197424, 'image/MTpjNjBmNTdjOTczZjYzNDUwYzliOTBmMjY5YTljYmZiNg', 'MTo5ZTA3NzMwYWNhYjUyNzExYmM3YzJkZDEyNzZmYzdmMw', 1, 0, 'jpg', '{"height":"400","width":"400"}');
	
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag) 
	VALUES (2, 0, 0, 0, 0, '/home', 'GovAgencies', 'GARC', '业委会', 'cs://1/image/aW1hZ2UvTVRwaVlXVXpOV05rT1Rrd1pETXdZV0prTmpGbU9HVTBabVl4TldJNU56WmtNQQ', 1, 1, 2, '{"itemLocation":"/home/Garc", "layoutName":"GarcLayout", "title":"业委会", "entityTag":"GARC"}', 0, 0, 1, 1, '', 0,NULL);
	
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES('sms.default', 1, 'zh_CN', '验证码', '您的验证码为${vcode}，10分钟内有效，感谢您的使用。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES('sms.default.yzx', 1, 'zh_CN', '验证码-左邻', '9547');

INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES('sms.default', 2, 'zh_CN', '给物业维修人员发短信-左邻', '业主${ownerName}发布了新的${subject}帖，您已被分配处理该业主的需求，请尽快联系该业主。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES('sms.default.yzx', 2, 'zh_CN', '给物业维修人员发短信-左邻', '10832');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES('sms.default', 4, 'zh_CN', '给被分配任务人员发短信-左邻', '业主${phone}发布了新的${topicType}帖，您已被分配处理该业主的需求，请尽快联系该业主。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES('sms.default.yzx', 4, 'zh_CN', '给被分配任务人员发短信-左邻', '10832');

INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`,`namespace_id`)
	VALUES (195701, UUID(), '13590168191', 'lqs', 'cs://1/image/aW1hZ2UvTVRvMU1EQTVZVEZrTkdVek9EQXhZbVE0WlRZd1l6UXdOVE0zWVdJNFkyTmlNUQ', 1, 45, '1', '0',  'zh_CN',  '3023538e14053565b98fdfb2050c7709', '3f2d9e5202de37dab7deea632f915a6adc206583b3f228ad7e101e5cb9c4b199', UTC_TIMESTAMP(),999999);
INSERT INTO `eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`, `create_time`, `namespace_id`)
	VALUES (211101,  195701,  '0',  '13590168191',  '221616',  3, UTC_TIMESTAMP(),999999);


-- 20151224后待上线
delete from eh_locale_templates where `scope` like 'sms.default%';
	
ALTER TABLE `eh_locale_templates` ADD COLUMN `namespace_id` INTEGER NOT NULL DEFAULT 0;
ALTER TABLE `eh_locale_templates` DROP INDEX `u_eh_lstr_identifier`;
ALTER TABLE `eh_locale_templates` ADD UNIQUE `u_eh_template_identifier`(`namespace_id`, `scope`, `code`, `locale`);

INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(0, 'sms.default', 1, 'zh_CN', '验证码', '您的验证码为${vcode}，10分钟内有效，感谢您的使用。');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(0, 'sms.default.yzx', 1, 'zh_CN', '验证码-左邻', '9547');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(1000000, 'sms.default.yzx', 1, 'zh_CN', '验证码-科技园', '18077');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999999, 'sms.default.yzx', 1, 'zh_CN', '验证码-讯美', '18086');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999995, 'sms.default.yzx', 1, 'zh_CN', '验证码-金隅嘉业', '18091');

INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(0, 'sms.default', 4, 'zh_CN', '给被分配任务人员发短信-左邻', '业主${phone}发布了新的${topicType}帖，您已被分配处理该业主的需求，请尽快联系该业主。');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(0, 'sms.default.yzx', 4, 'zh_CN', '给被分配任务人员发短信-左邻', '10832');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(1000000, 'sms.default.yzx', 4, 'zh_CN', '给被分配任务人员发短信-科技园', '18529');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999999, 'sms.default.yzx', 4, 'zh_CN', '给被分配任务人员发短信-讯美', '18530');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999995, 'sms.default.yzx', 4, 'zh_CN', '给被分配任务人员发短信-金隅嘉业', '18532');

ALTER TABLE `eh_organizations` ADD COLUMN `department_type` VARCHAR(64);

drop table eh_account_vedioconf;
drop table eh_enterprise_videoconf_account;
drop table eh_videoconf_account;
drop table eh_videoconf_account_rule;
drop table eh_videoconf_enterprise;
drop table eh_videoconf_order;


ALTER TABLE `eh_rental_bills` CHANGE `paid_money` `paid_money` DECIMAL(10,2) DEFAULT NULL;
ALTER TABLE `eh_rental_bills` CHANGE `pay_total_money` `pay_total_money` DECIMAL(10,2) DEFAULT NULL;
ALTER TABLE `eh_rental_bills` CHANGE `site_total_money` `site_total_money` DECIMAL(10,2) DEFAULT NULL;
ALTER TABLE `eh_rental_bills` CHANGE `reserve_money` `reserve_money` DECIMAL(10,2) DEFAULT NULL;

ALTER TABLE `eh_rental_items_bills` CHANGE `total_money` `total_money` DECIMAL(10,2) DEFAULT NULL;
ALTER TABLE `eh_rental_site_items` CHANGE `price` `price` DECIMAL(10,2) DEFAULT NULL;
ALTER TABLE `eh_rental_site_rules` CHANGE `price` `price` DECIMAL(10,2) DEFAULT NULL;
ALTER TABLE `eh_rental_sites_bills` CHANGE `total_money` `total_money` DECIMAL(10,2) DEFAULT NULL; 

INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'group.notification', 33, 'zh_CN', '兴趣圈分享', '兴趣圈“${groupName}”推荐');
INSERT INTO `eh_configurations`(`namespace_id`, `name`, `value`, `description`) VALUES (0, 'sms.vcodetest.flag','true','是否可以发送测试验证码短信');


-- 20151230后待上线
ALTER TABLE `eh_regions` ADD COLUMN `namespace_id` int(11) NOT NULL DEFAULT '0';
ALTER TABLE `eh_nearby_community_map` ADD COLUMN `namespace_id` int(11) NOT NULL DEFAULT '0';
ALTER TABLE `eh_communities` ADD COLUMN `namespace_id` int(11) NOT NULL DEFAULT '0';
ALTER TABLE `eh_buildings` ADD COLUMN `namespace_id` int(11) NOT NULL DEFAULT '0';
-- ALTER TABLE `eh_addresses` ADD COLUMN `namespace_id` int(11) NOT NULL DEFAULT '0';
ALTER TABLE `eh_regions` DROP INDEX `u_eh_region_name`;
ALTER TABLE `eh_regions` ADD UNIQUE `u_eh_region_name`(`namespace_id`, `parent_id`, `name`);

INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) VALUES ('14767', '0', '广东', 'GUANGDONG', 'GD', '/广东', '1', '1', '', '', '2', '2', '999994');
INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) VALUES ('14765', '14767', '深圳市', 'SHENZHENSHI', 'SZS', '/广东/深圳市', '2', '2', NULL, '0755', '2', '1', '999994');
INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) VALUES ('14766', '14765', '龙岗区', 'LONGGANGQU', 'LGQ', '/广东/深圳市/龙岗区', '3', '3', NULL, '0755', '2', '0', '999994');

INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) VALUES ('14801', '0', '广东', 'GUANGDONG', 'GD', '/广东', '1', '1', '', '', '2', '2', '1000000');
INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) VALUES ('14802', '14801', '深圳市', 'SHENZHENSHI', 'SZS', '/广东/深圳市', '2', '2', NULL, '0755', '2', '1', '1000000');
INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) VALUES ('14803', '14802', '南山区', 'NAMSHANQU', 'NSQ', '/广东/深圳市/南山区', '3', '3', NULL, '0755', '2', '0', '1000000');

INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) VALUES ('14901', '0', '广东', 'GUANGDONG', 'GD', '/广东', '1', '1', '', '', '2', '2', '999999');
INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) VALUES ('14902', '14901', '深圳市', 'SHENZHENSHI', 'SZS', '/广东/深圳市', '2', '2', NULL, '0755', '2', '1', '999999');
INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) VALUES ('14903', '14902', '南山区', 'NAMSHANQU', 'NSQ', '/广东/深圳市/南山区', '3', '3', NULL, '0755', '2', '0', '999999');

INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999994, 'sms.default.yzx', 1, 'zh_CN', '验证码-左邻', '9547');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999994, 'sms.default.yzx', 4, 'zh_CN', '给被分配任务人员发短信-左邻', '10832');

update eh_buildings set namespace_id=1000000 where community_id=240111044331048623;
update eh_addresses set namespace_id=1000000 where community_id=240111044331048623;
update eh_communities set namespace_id=1000000 where id=240111044331048623;

update eh_buildings set namespace_id=999999 where community_id=240111044331049963;
update eh_addresses set namespace_id=999999 where community_id=240111044331049963;
update eh_communities set namespace_id=999999 where id=240111044331049963;

