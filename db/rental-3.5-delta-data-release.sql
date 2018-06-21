-- rental3.5 by st.zheng
update eh_locale_strings set text = '用户姓名' where scope = 'rental.flow' and `code` = 'user';
update eh_locale_strings set text = '联系方式' where scope = 'rental.flow' and `code` = 'contact';
update eh_locale_strings set text = '公司名称' where scope = 'rental.flow' and `code` = 'organization';
INSERT INTO `eh_locale_strings` ( `scope`, `code`, `locale`, `text`) VALUES ( 'rental.flow', 'spec', 'zh_CN', '资源规格');
INSERT INTO `eh_locale_strings` ( `scope`, `code`, `locale`, `text`) VALUES ( 'rental.flow', 'address', 'zh_CN', '资源地址');
update eh_locale_strings set text = '免费物资' where scope = 'rental.flow' and `code` = 'goodItem';
update eh_locale_strings set text = '付费商品' where scope = 'rental.flow' and `code` = 'item';

SET @ns_id = 0;
SET @module_id = 40400;
SET @entity_type = 'flow_button'; -- 节点参数为：flow_node， 按钮参数为：flow_button
SET @flow_predefined_params_id = IFNULL((SELECT MAX(id) FROM `eh_flow_predefined_params`), 1);
INSERT INTO `eh_flow_predefined_params` (`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `entity_type`, `display_name`, `name`, `text`, `status`, `creator_uid`, `create_time`, `update_uid`, `update_time`)
  VALUES ((@flow_predefined_params_id := @flow_predefined_params_id + 1), @ns_id, 0, '', @module_id, 'any-module', @entity_type, '线上支付', 'onlinePay', '{"nodeType":"ONLINE_PAY"}', 2, NULL, NULL, NULL, NULL);
INSERT INTO `eh_flow_predefined_params` (`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `entity_type`, `display_name`, `name`, `text`, `status`, `creator_uid`, `create_time`, `update_uid`, `update_time`)
  VALUES ((@flow_predefined_params_id := @flow_predefined_params_id + 1), @ns_id, 0, '', @module_id, 'any-module', @entity_type, '线下支付', 'offlinePay', '{"nodeType":"OFFLINE_PAY"}', 2, NULL, NULL, NULL, NULL);
INSERT INTO `eh_flow_predefined_params` (`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `entity_type`, `display_name`, `name`, `text`, `status`, `creator_uid`, `create_time`, `update_uid`, `update_time`)
  VALUES ((@flow_predefined_params_id := @flow_predefined_params_id + 1), @ns_id, 0, '', @module_id, 'any-module', @entity_type, '调整费用', 'changeAmount', '{"nodeType":"CHANGE_AMOUNT"}', 2, NULL, NULL, NULL, NULL);

delete from eh_flow_predefined_params where module_id = 40400 and name = '已完成';