-- merge from organization-delta-data-release.sql by lqs 20161128
-- by sfyan 获取电商平台的店铺信息接口
INSERT INTO `eh_configurations` (`namespace_id`, `name`, `value`, `description`) VALUES (0, 'get.businesses.info.api', 'zl-ec/rest/openapi/shop/listByCondition', '获取店铺信息');

INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) 
VALUES ('300', 'organization', '600001', 'zh_CN', '通用岗位已存在');

-- merge from activityentry-delta-data-release.sql by xiongying20161128
update eh_activities a set forum_id = (select forum_id from eh_forum_posts where id = a.post_id);
update eh_activities a set creator_tag = (select creator_tag from eh_forum_posts where id = a.post_id);
update eh_activities a set target_tag = (select target_tag from eh_forum_posts where id = a.post_id);
update eh_activities a set visible_region_type = (select visible_region_type from eh_forum_posts where id = a.post_id);
update eh_activities a set visible_region_id = (select visible_region_id from eh_forum_posts where id = a.post_id);

-- merge from sa1.7-delta-data-release.sql by xiongying20161128
-- 新增预约看楼模板 add by xiongying 20161116
INSERT INTO `eh_request_templates` (`id`, `template_type`, `name`, `button_title`, `email_flag`, `msg_flag`, `fields_json`, `status`, `creator_uid`, `create_time`)
    VALUES ('9', 'Apartment', '预约看楼', '预约看楼', '1', '1', ' {"fields":[{"fieldName":"name","fieldDisplayName":"姓名","fieldType":"string","fieldContentType":"text","fieldDesc":"userName","requiredFlag":"1","dynamicFlag":"1"},{"fieldName":"mobile","fieldDisplayName":"手机号","fieldType":"string","fieldContentType":"text","fieldDesc":"mobile","requiredFlag":"1","dynamicFlag":"1"},{"fieldName":"organizationName","fieldDisplayName":"企业名称","fieldType":"string","fieldContentType":"text","fieldDesc":"organizationName","requiredFlag":"1","dynamicFlag":"1"},{"fieldName":"areaSize","fieldDisplayName":"面积需求","fieldType":"number","fieldContentType":"text","fieldDesc":"请输入面积需求","requiredFlag":"1","dynamicFlag":"0"},{"fieldName":"remarks","fieldDisplayName":"备注","fieldType":"string","fieldContentType":"text","fieldDesc":"（选填）其他说明","requiredFlag":"0","dynamicFlag":"0"}]}                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             ', '1', '1', UTC_TIMESTAMP());
INSERT INTO `eh_request_templates_namespace_mapping` (`id`, `namespace_id`, `template_id`) VALUES (10, '999985', '9');

--
-- 修改能耗管理的入口页面地址  add by xq.tian  2016/11/30
--
UPDATE `eh_launch_pad_items` SET `action_data`='{"url":"http://core.zuolin.com/energy-management/index.html?hideNavigationBar=1#/address_choose#sign_suffix"}' WHERE `item_name` = 'Energy' AND `namespace_id` = '999992';
