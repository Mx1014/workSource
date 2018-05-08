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

-- by st.zheng 路福联合广场 停车场配置
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('parking.fujica.parkingCode', '17000104590501', 'parkingCode', '0', NULL);
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('parking.fujica.appid', 'fujica_dacfe36d3f15ae51', 'appid', '0', NULL);
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('parking.fujica.appSecret', '69416bc8b9e44d56b7672d18b1106668', 'appSecret', '0', NULL);
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('parking.fujica.appid', 'MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALATu5WNNx+XcrASNJieGRsY7VryQjcs+pABXGh4BOCqAW3FgAAc21aIG0uxvYHdQUw1I7J80RiQJ506aIktGTmZKJTjez7JNF7GO4ieGmLMI3ds8iXyJrfUMp/1U3Cq6vja8vuRIKYVQCxZe8un98Nr03F7oMlKpBydJQKVdlAHAgMBAAECgYBPcqfqhAyCWbCrF5vZ3URQwL+gkL0l7kqknaiXjsgMo0j/weTOqDaj5cgDMJDkvvPOsg+IYt9qKOlm/Urb0piVb9v9iAnp6gIppcyHrPUesgYRSbqR/HhdGtnzn/4f45YpkXA6URMfc04xBrIUCw5DmnqonJZJkjd6jjXiL65rYQJBAN8eg99yFH45D2cdlPBTN0LVWXgqy3N22byYh0j6kgo25WOPMcuIZPM/XdSoTfhYeHixvEwrmCAkCmSkf524prECQQDKBnzZRj3eCij1z2SZJCRZhRQUhFmK0AYYTXw9R0DfEaZNrEAe8PEypS94lYROvNV3TnSnK2FHKeH8YhHXEoA3AkBRHzMrRrsUuYJUJ3lDd74b2p5RBp46OPgpjfuCGTiH5jW44RNlwQ2TM3LWIutWZDRJDbY8q40AApqUxQpxOfXBAkA8bB5RGZoNW7qOcj3jM5UPlSbBUCg7xSXdhOdAqJv1W6ECoB75YhSxkggVp5pPtlid+0AWc3n/v74QLwCo86aXAkEAlxY3hoxQjJiCjD2PbL0GLkMKNBqJgLcDWqeoHcal+iY2Fpr3U/iCJZNoBuovC2Qvc5J1y61PojrvFNyFMEkWTQ==', 'appid', '0', NULL);
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('parking.fujica.url', 'http://mops-test.fujica.com.cn:8021/Api', 'url', '0', NULL);

