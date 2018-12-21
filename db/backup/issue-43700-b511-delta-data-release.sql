-- AUTHOR: 谢旭双
-- REMARK: 同事圈默认选择器文本
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES('enterprise_moments', '1', 'zh_CN', '全部');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES('enterprise_moments', '2', 'zh_CN', '我发布的');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES('enterprise_moments', '3', 'zh_CN', '我点赞的');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES('enterprise_moments', '4', 'zh_CN', '我评论的');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES('enterprise_moments', '506', 'zh_CN', '接口参数异常');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES('enterprise_moments', '40002', 'zh_CN', '你不能删除其他人的评论');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES('enterprise_moments', 'title', 'zh_CN', '同事圈');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('enterprise_default_moment', '1', 'zh_CN', '系统小助手');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('enterprise_default_moment', '2', 'zh_CN', '同事圈是公司员工交流、记录公司成长点滴的地方。在这里可以发表心情、分享经验、寻求帮助，培养融洽、和谐的办公氛围，提升团队凝聚力。');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('enterprise_default_moment', '3', 'zh_CN', '图片');

-- AUTHOR: 谢旭双
-- REMARK: 同事圈头部背景图
SET @config_id = (SELECT MAX(id) FROM `eh_configurations`);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `is_readonly`) VALUES (@config_id:=@config_id+1, 'enterprise.moment.banner', 'cs://1/image/aW1hZ2UvTVRwak1HWTRaalJsWkdRMk1XWXdOelkzWXpsak5tTTNaVEJtTVRNeE4yUmxOdw', '同事圈头部背景图', 1);

-- AUTHOR: 吴寒
-- REMARK: 同事圈菜单
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multipl_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category`, `app_type`, `client_handler_type`, `system_app_flag`, `icon_uri`, `host`, `enable_enterprise_pay_flag`) VALUES('274000','同事圈','50000','/100/50000/274000','1','3','2','10','2018-09-26 16:51:46',NULL,NULL,'2018-09-26 16:51:46','0','0','0',NULL,'org_control','1','1','module','0','0',NULL,NULL,'moments',NULL);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`, `scene_type`) VALUES('79887000','同事圈','70000010',NULL,'enterprise-payment-auth','1','2','/70000010/79887000','park','8','274000','3','system','module','2','1');
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`, `scene_type`) VALUES('79888000','同事圈','70000010',NULL,'enterprise-payment-auth','1','2','/70000010/79888000','zuolin','8','274000','3','system','module','2','1');

-- AUTHOR: 谢旭双
-- REMARK: 同事圈初始tag
INSERT INTO `ehcore`.`eh_locale_strings`(`scope`, `code`, `locale`, `text`) VALUES ('enterprise_moments', '20001', 'zh_CN', '新人报道');
INSERT INTO `ehcore`.`eh_locale_strings`(`scope`, `code`, `locale`, `text`) VALUES ('enterprise_moments', '20002', 'zh_CN', '快讯');
INSERT INTO `ehcore`.`eh_locale_strings`(`scope`, `code`, `locale`, `text`) VALUES ('enterprise_moments', '20003', 'zh_CN', '求推荐');
INSERT INTO `ehcore`.`eh_locale_strings`(`scope`, `code`, `locale`, `text`) VALUES ('enterprise_moments', '20004', 'zh_CN', '活动');
INSERT INTO `ehcore`.`eh_locale_strings`(`scope`, `code`, `locale`, `text`) VALUES ('enterprise_moments', '20005', 'zh_CN', '讨论');
INSERT INTO `ehcore`.`eh_locale_strings`(`scope`, `code`, `locale`, `text`) VALUES ('enterprise_moments', '20006', 'zh_CN', '建议');