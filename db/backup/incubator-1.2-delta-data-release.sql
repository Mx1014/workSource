
-- 文件有效时间 add by yanjun 20171219
SET @id = (SELECT MAX(id) from eh_configurations);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES (( @id :=  @id + 1), 'filedownload.valid.interval', '7', 'filedownload valid interval', '0', NULL);


SET @id = (SELECT MAX(id) from eh_locale_strings);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id := @id + 1), 'task', '10001', 'zh_CN', '任务不存在');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id := @id + 1), 'task', '10002', 'zh_CN', '权限不足');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id := @id + 1), 'task', '10003', 'zh_CN', '任务已执行完成，不可取消');


-- 文件中心菜单  add by yanjun 20171220
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`)
VALUES ('61000', '文件中心', '60000', '/60000/61000', '0', '2', '2', '0', UTC_TIMESTAMP());

INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`)
VALUES ('61000', '文件中心', '60000', NULL, 'react:/file-center/file-list', '0', '2', '/60000/61000', 'park', '630', '61000', '2', NULL, 'module');

SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
SELECT (@menu_scope_id := @menu_scope_id + 1), 61000, '', 'EhNamespaces', id, 2 from eh_namespaces;


-- “入孵申请”菜单改成“入驻申请”  add by yanjun 20171223
UPDATE eh_web_menus set name = '入驻申请' where id = 36000 and name = '入孵申请';

UPDATE eh_launch_pad_layouts set version_code = '2017121901', layout_json  = '{"versionCode":"2017121901","layoutName":"ServiceMarketLayout","displayName":"服务市场","groups":[{"groupName":"Banner","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"separatorFlag":1,"separatorHeight":16,"defaultOrder":10},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup2","cssStyleFlag":1,"paddingTop":0,"paddingBottom":0,"paddingLeft":0,"paddingRight":0,"lineSpacing":0,"columnSpacing":0},"style":"Gallery","defaultOrder":20,"separatorFlag":0,"separatorHeight":0,"columnCount":2},{"title":"园区快讯","iconUrl": "https://content-1.zuolin.com:443/image/aW1hZ2UvTVRveU5UVmtNVFpqTnpBek1URXpOREkzTkRnMU1qWTFOMlZrWVdKa1lXTTNZdw?token=XlYdOjlDVEVb4XyQO4_dd5RI37zTkV3jCKm_-XbRyLIGVUorWGnyRCwLAgMGV86baX30BnQW4nqzF9nlXGe4M0DbZxWBVTqnL019xazIDuhE6A0OXiMQwRqGX84_1HHv","groupName":"","widget":"NewsFlash","instanceConfig":{"timeWidgetStyle":"datetime","itemGroup":"Default","categoryId":2,"newsSize":3},"defaultOrder":30,"separatorFlag":0,"separatorHeight":0}]}' where namespace_id = 999964 and `name` = 'ServiceMarketLayout' and `status` = 2;
UPDATE eh_launch_pad_items set display_flag = 1 where namespace_id = 999964;

UPDATE eh_launch_pad_items set item_label = '入驻申请', item_name = '入驻申请' where namespace_id = 999964 and item_name = '入孵申请';
UPDATE eh_launch_pad_items set item_label = '产品信息查询', item_name = '产品信息查询' where namespace_id = 999964 and item_name = '产品信息发布';
UPDATE eh_launch_pad_items set item_label = '机构信息查询', item_name = '机构信息查询' where namespace_id = 999964 and item_name = '机构信息发布';
DELETE from eh_launch_pad_items where namespace_id = 999964 and item_name in ('新闻快讯', '更多');




