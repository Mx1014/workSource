-- issue-26471 服务热线V1.5（客服聊天记录保存和导出) by huangmingbo 2018.5.8  start
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('hotline', '10001', 'zh_CN', '相同号码已存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('hotline', '10002', 'zh_CN', '查询记录时未指定客服或热线');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('hotline', '10003', 'zh_CN', '查询记录时未指定用户');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('hotline', '10004', 'zh_CN', '需要更新/删除的记录不存在');

INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (40310, '公共热线', 40300, '/40000/40300/40310', 1, 3, 2, 0, '2018-04-02 17:18:58', NULL, NULL, '2018-05-02 17:18:58', 0, 1, '1', NULL, '');
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (40320, '专属客服', 40300, '/40000/40300/40320', 1, 3, 2, 0, '2018-04-02 17:18:58', NULL, NULL, '2018-05-02 17:18:58', 0, 1, '1', NULL, '');

SET @mp_id = (SELECT MAX(id) FROM eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@mp_id:=@mp_id+1, 40310, 0, 4030040310, '公共热线', 0, '2018-04-02 17:18:58');
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@mp_id:=@mp_id+1, 40320, 0, 4030040320, '专属客服', 0, '2018-04-02 17:18:58');
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@mp_id:=@mp_id+1, 40320, 0, 4030040321, '历史会话', 0, '2018-04-02 17:18:58');

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (4030040310, 0, '公共热线 全部权限', '公共热线 全部权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (4030040320, 0, '专属客服 客服管理', '专属客服 客服管理', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (4030040321, 0, '专属客服 历史会话', '专属客服 历史会话', NULL);