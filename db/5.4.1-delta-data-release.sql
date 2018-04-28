-- #issue-26479增加园区快讯错误码
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('news', '10010', 'zh_CN', '未获取到公司');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('news', '10011', 'zh_CN', '未获取到项目');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('news', '10012', 'zh_CN', '项目权限不足');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('news', '10013', 'zh_CN', '未找到服务广场版本');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('news', '10014', 'zh_CN', '未找到资讯模块');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('news', '10015', 'zh_CN', '系统转码出错');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('news', '10016', 'zh_CN', '未找到页面模块组');

-- #issue-26479更新园区快讯权限为项目隔离
UPDATE `eh_service_modules` SET `module_control_type`='community_control' WHERE  `id`=10800;
update eh_service_module_apps set  module_control_type='community_control' where module_id = 10800 and  module_control_type = 'unlimit_control';

-- #issue-26479更新园区快讯WEB跳转地址
update eh_launch_pad_items set action_data = replace(action_data,'#/newsList#sign_suffix',concat('&ns=',namespace_id,'#/newsList#sign_suffix')) where action_type = 13 and action_data like '%park-news-web%'

-- 停车订单标签 by dengs,2018.04.27
update eh_parking_lots SET order_tag=SUBSTR(id FROM 3 FOR 5);

-- 品质核查离线包版本更新  by jiarui
update eh_version_urls set download_url = replace(download_url,'1-0-0','1-0-1'),
  info_url = replace(info_url,'1-0-0','1-0-1'),
  target_version = '1.0.1' where app_name = '品质核查';

