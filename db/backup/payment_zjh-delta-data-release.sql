SET @config_id = (SELECT MAX(id) from `eh_configurations`);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES (@config_id:=@config_id+1, 'asset.payment.zjh.api.2.2.4.15', '/rest/LeaseContractChargeInfo/getLeaseContractBillOnFiProperty', '2.2.4.15接口', '999983', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES (@config_id:=@config_id+1, 'asset.payment.zjh.api.2.2.4.10', '/rest/LeaseContractChargeInfo/getLeaseContractReceivableGroupForStatistics', '2.2.4.10接口', '999983', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES (@config_id:=@config_id+1, 'asset.payment.zjh.api.2.2.4.9', '/rest/LeaseContractChargeInfo/getLeaseContractReceivableGroup', '2.2.4.9接口', '999983', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES (@config_id:=@config_id+1, 'asset.payment.zjh.api.2.2.4.8', '/rest/LeaseContractChargeInfo/getLeaseContractReceivable', '2.2.4.8接口', '999983', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES (@config_id:=@config_id+1, 'asset.payment.zjh.url', 'http://183.62.222.87:5902/sf', '科兴缴费url', '999983', NULL);

UPDATE `eh_communities` SET namespace_community_type='kexing', namespace_community_token = '6255265c-80bc-11e7-8e8b-020b31d69c8a' where `namespace_id` = 999983 and `name`= '科兴科学园';

SET @locale_id = (SELECT MAX(`id`) FROM `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@locale_id:=@locale_id+1,'assetv2','10006','zh_CN','远程请求一碑系统错误');