-- beta执行，停车场
INSERT INTO `eh_parking_lots` (`id`, `owner_type`, `owner_id`, `name`, `vendor_name`, `vendor_lot_token`, `status`, `creator_uid`, `create_time`, `namespace_id`, `recharge_json`, `config_json`, `order_tag`, `order_code`) VALUES ('10033', 'community', '240111044331057733', '康利城停车场', 'BEE_KANGLI', NULL, '2', '1', now(), '999978', NULL, '{\"tempfeeFlag\": 1, \"rateFlag\": 1, \"lockCarFlag\": 0, \"searchCarFlag\": 0, \"currentInfoType\": 0, \"contact\": \"0\",\"identityCardFlag\":0}', '033', '0');
INSERT INTO `eh_parking_lots` (`id`, `owner_type`, `owner_id`, `name`, `vendor_name`, `vendor_lot_token`, `status`, `creator_uid`, `create_time`, `namespace_id`, `recharge_json`, `config_json`, `order_tag`, `order_code`) VALUES ('10034', 'community', '240111044332061061', '联科停车场', 'BEE_ZHONGTIAN', NULL, '2', '1', now(), '999944', NULL, '{\"tempfeeFlag\": 1, \"rateFlag\": 1, \"lockCarFlag\": 0, \"searchCarFlag\": 0, \"currentInfoType\": 0, \"contact\": \"0\",\"identityCardFlag\":0}', '034', '0');
update eh_parking_lots SET vendor_name = 'BEE_ZHONGTIAN',`name`='联科停车场',config_json='{"tempfeeFlag": 1, "rateFlag": 1, "lockCarFlag": 1, "searchCarFlag": 0, "currentInfoType": 1, "contact": "13480106043","identityCardFlag":0}' WHERE id=10001;

-- beta执行脚本
-- ADD BY 梁燕龙
-- issue-30013 初始化短信白名单配置项
INSERT INTO `eh_configurations`(`name`,`value`,`description`,`namespace_id`)
      VALUES('sms.whitelist.test','true','短信白名单',0);
-- END BY 梁燕龙

-- beta执行 服务联盟
update eh_service_alliance_jump_module set namespace_id = 1 where  module_url = 'BIZS' and namespace_id = 0;


-- 人事2.7 数据同步 start by ryan.

-- 执行 /archives/syncArchivesConfigAndLogs 接口

-- 人事2.7 数据同步 end by ryan.


--
-- 通用脚本
-- ADD BY xq.tian  2018/06/15
-- #30750 代码仓库管理 v1.0
--
SET @eh_configurations_id = (SELECT MAX(id) FROM eh_configurations);
INSERT INTO ehcore.eh_configurations (id, name, value, description, namespace_id, display_name)
  VALUES ((@eh_configurations_id := @eh_configurations_id + 1), 'gogs.server.url', 'http://10.1.10.60:3000/api/v1', 'gogs server', 0, '');
INSERT INTO eh_configurations (id, name, value, description, namespace_id, display_name)
  VALUES ((@eh_configurations_id := @eh_configurations_id + 1), 'gogs.admin.name', 'zuolin-project', 'gogs admin name', 0, '');
INSERT INTO eh_configurations (id, name, value, description, namespace_id, display_name)
  VALUES ((@eh_configurations_id := @eh_configurations_id + 1), 'gogs.admin.token', 'afe3b2a0165958086f7cceed7843190c15197c08', 'gogs admin token', 0, '');
INSERT INTO eh_configurations (id, name, value, description, namespace_id, display_name)
  VALUES ((@eh_configurations_id := @eh_configurations_id + 1), 'gogs.backup.hookapi', 'http://10.1.10.61:8899/hooked', 'gogs backup server hook api', 0, '');
INSERT INTO eh_configurations (id, name, value, description, namespace_id, display_name)
  VALUES ((@eh_configurations_id := @eh_configurations_id + 1), 'gogs.backup.secret', 'zuolin', 'gogs backup server hook api', 0, '');

-- #30750 END

-- 多入口 映射 闻天
set @id = ifnull((select max(`id`) from `eh_service_module_app_mappings`), 0);
INSERT INTO `eh_service_module_app_mappings` (`id`, `app_origin_id_male`, `app_module_id_male`, `app_origin_id_female`, `app_module_id_female`, `create_time`, `create_uid`, `update_time`, `update_uid`) VALUES (@id:=@id+1, '0', '20400', '0', '21200', now(), '1', now(), NULL);


-- 请执行在es上执行db/search/contract.sh add by 丁建民
--- 注：上现网，后补充数据
-- 需要调用的接口：/contract/syncContracts

-- 5.6.0 上线时需要部署 Gogs 程序, 部署文档：http://s.a.com/docs/faq/baseline-21529636264 ADD BY xq.tian  2018/06/28

/*
5.6.0 上线时需要更新 contentserver 二进制, 修复了一些bug, 二进制位置在: ehnextgen/contentserver/release/server/contentserver

并且在 contentsever 的配置文件中新增两个配置项:

# 日志文件轮转大小 megabytes
logMaxSize     = 100
# 历史日志保留天数
logMaxAge      = 365

ADD BY xq.tian  2018/06/28
*/