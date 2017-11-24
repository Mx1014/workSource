update  `eh_organizations` set `order` = '0' where `order` is NULL;

set @id := (SELECT MAX(id) from `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id := @id + 1), 'address', '20005', 'zh_CN', '门牌已关联合同，无法删除');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id := @id + 1), 'building', '10003', 'zh_CN', '楼栋下有门牌已关联合同，无法删除');
-- by wentian
SET @config_id = (SELECT MAX(id) from `eh_configurations`);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES (@config_id:=@config_id+1, 'asset.payment.zjh.api.2.2.4.15', '/rest/LeaseContractChargeInfo/getLeaseContractBillOnFiProperty', '2.2.4.15接口', '999983', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES (@config_id:=@config_id+1, 'asset.payment.zjh.api.2.2.4.10', '/rest/LeaseContractChargeInfo/getLeaseContractReceivableGroupForStatistics', '2.2.4.10接口', '999983', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES (@config_id:=@config_id+1, 'asset.payment.zjh.api.2.2.4.9', '/rest/LeaseContractChargeInfo/getLeaseContractReceivableGroup', '2.2.4.9接口', '999983', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES (@config_id:=@config_id+1, 'asset.payment.zjh.api.2.2.4.8', '/rest/LeaseContractChargeInfo/getLeaseContractReceivable', '2.2.4.8接口', '999983', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES (@config_id:=@config_id+1, 'asset.payment.zjh.url', 'http://183.62.222.87:5902/sf', '科兴缴费url', '999983', NULL);

UPDATE `eh_communities` SET namespace_community_type='ebei', namespace_community_token = '6255265c-80bc-11e7-8e8b-020b31d69c8a' where `namespace_id` = 999983 and `name`= '科兴科学园';

SET @locale_id = (SELECT MAX(`id`) FROM `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@locale_id:=@locale_id+1,'assetv2','10006','zh_CN','远程请求一碑系统错误');
-- 正中会 配置账单管理和账单统计 by Wentian Wang
SET @eh_menu_scope_id = (SELECT MAX(id) from `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES (@eh_menu_scope_id:=@eh_menu_scope_id+1, '20400', '', 'EhNamespaces', '999983', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES (@eh_menu_scope_id:=@eh_menu_scope_id+1, '204011', '', 'EhNamespaces', '999983', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES (@eh_menu_scope_id:=@eh_menu_scope_id+1, '204021', '', 'EhNamespaces', '999983', '2');
