-- 所有环境
-- added by wh 2018-6-12  :薪酬工资条发放消息
SET @template_id = (SELECT MAX(id) FROM eh_locale_templates);
INSERT INTO `eh_locale_templates`(`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@template_id := @template_id + 1, 'welfare.msg', 1, 'zh_CN', '发福利消息', '$你收到了${subject},快去查看吧!', 0);
-- 菜单配置 2018年11月30日 以后不用写菜单了,找何配置
-- INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category`, `app_type`, `client_handler_type`, `system_app_flag`, `icon_uri`, `host`, `enable_enterprise_pay_flag`) VALUES('273000','企业福利','310000','/100/310000/79880000','1','3','2','10','2018-09-26 16:51:46',NULL,NULL,'2018-09-26 16:51:46','0','0','0',NULL,'org_control','1','1','module','0','0',NULL,NULL,NULL,NULL);
-- INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`, `scene_type`) VALUES('79884000','企业福利','48000000',NULL,'enterprise-welfare','1','2','/40000010/48000000/79884000','park','8','273000','3','system','module','2','1');
-- INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`, `scene_type`) VALUES('79885000','企业福利','16040000',NULL,'enterprise-welfare','1','2','/23000000/16040000/79885000','zuolin','8','273000','3','system','module','2','1');


-- end