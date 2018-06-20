-- 能耗管理升级离线包 by  jiarui 20180619
update eh_version_urls set download_url = replace(download_url,'1-0-3','1-0-4'),
  info_url = replace(info_url,'1-0-3','1-0-4'),
  target_version = '1.0.3' where realm_id = (select id from eh_version_realm where realm = 'energyManagement');

-- 通用脚本
-- by 刘一麟 20180619
-- issue-30717 新增应用:园区巴士 
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES ('16030650', '园区班车', '16030000', NULL, 'bus-guard', '1', '2', '/16000000/16030000/16030650', 'zuolin', '23', '41015', '3', 'system', 'module', NULL) COMMENT '';
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES ('45015000', '园区班车', '45000000', NULL, 'bus-guard', '1', '2', '/45000000/45015000', 'park', '15', '41015', '2', 'system', 'module', NULL);
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES ('41015', '园区班车', '40000', '/10000/41015', '1', '2', '2', '10', NULL, '{\"isSupportQR\":1,\"isSupportSmart\":0}', '77', NULL, '0', '0', '0', '0', 'community_control');
-- END
