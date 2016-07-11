-- merge from 3.4.x.paymentcard-delta-data-debug.sql 20160628
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) 
    VALUES (105, 'taotaogu.keystore', 'taotaogu.keystore', 'the keystore for taotaogu(chuneng)', 0, NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) 
    VALUES (106, 'taotaogu.pin3.crt', 'taotaogu.pin3.crt', 'the pin3.crt for taotaogu(chuneng)', 0, NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) 
    VALUES (107, 'taotaogu.server.cer', 'taotaogu.server.cer', 'the server.cer for taotaogu(chuneng)', 0, NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) 
    VALUES (108, 'taotaogu.client.pfx', 'taotaogu.client.pfx', 'the client.pfx for taotaogu(chuneng)', 0, NULL);

INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) 
    VALUES (109, 'taotaogu.card.url', 'http://test.ippit.cn:30821/iccard/service', 'the card url for taotaogu(chuneng)', 0, NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) 
    VALUES (110, 'taotaogu.order.url', 'http://test.ippit.cn:8010/orderform', 'the order url for taotaogu(chuneng)', 0, NULL);

INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) 
    VALUES (198, 'paymentCard', '10000', 'zh_CN', '服务器通讯失败，请检查网络连接并重试！');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) 
    VALUES (199, 'paymentCard', '10001', 'zh_CN', '您输的旧密码有误，请重新输入！');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) 
    VALUES (200, 'paymentCard', '10002', 'zh_CN', '验证码错误，请重新输入！');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) 
    VALUES (201, 'paymentCard', '10003', 'zh_CN', '当前卡不存在！');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) 
    VALUES (202, 'paymentCard', '10004', 'zh_CN', '二维码获取失败！');

INSERT INTO `eh_payment_card_issuer_communities` (`id`, `owner_type`, `owner_id`, `issuer_id`, `create_time`) 
    VALUES (100001, 'community', '240111044331051500', 100001, '2016-06-14 17:06:44');

INSERT INTO `eh_payment_card_issuers` (`id`, `name`, `description`, `pay_url`, `alipay_recharge_account`, `weixin_recharge_account`, `vendor_name`, `vendor_data`, `create_time`, `status`) 
    VALUES (100001, 'chuneng', 'chuneng', 'adfasdf', 'adfasdf', 'asdfsdfasdf', 'TAOTAOGU', '{\"BranchCode\":\"10002900\",\"AppName\":\"ICCard\",\"Version\":\"V0.01\",\"DstId\":\"00000000\",\"CardPatternid\":\"887093\",\"chnl_type\":\"WEB\",\"chnl_id\":\"12345679\",\"merch_id\":\"862900000000001\",\"termnl_id\":\"00011071\",\"init_password\":\"111111\"}', '2016-06-14 17:07:20', '1');

	
	
-- merge from 3.4.x.sfyan-delta-data-debug.sql 20160628
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`, `namespace_id`) 
    VALUES( 'user.notification', 2, 'zh_CN', '注册天数描述', '我已加入左邻“${days}”天', 0);

INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'hottag', '10001', 'zh_CN', '该标签已是热门标签');


-- merge from 3.4.x.sfyan-delta-data-debug.sql  by sfyan 20160629
set @layout_id = (select max(id) from `eh_launch_pad_layouts`);
INSERT INTO `eh_launch_pad_layouts`(id, namespace_id, `scene_type`, name, layout_json, version_code, min_version_code, status, create_time,scope_code,scope_id,apply_policy) 
	VALUES ((@layout_id := @layout_id + 1), 0, 'default', 'ServiceMarketLayout', '{"versionCode":"2016031201","versionName":"3.3.0","layoutName":"ServiceMarketLayout","displayName":"服务市场","groups":[{"groupName":"","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":1,"separatorFlag":0,"separatorHeight":0},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"GovAgencies"},"style":"Default","defaultOrder":2,"separatorFlag":1,"separatorHeight":21,"columnCount":4},{"groupName":"滚动广告","widget":"Bulletins","instanceConfig":{"itemGroup":""},"style":"Default","defaultOrder":4,"separatorFlag":1,"separatorHeight":21},{"groupName":"商家服务","widget":"Navigator","instanceConfig":{"itemGroup":"Bizs"},"style":"Default","defaultOrder":5,"separatorFlag":0,"separatorHeight":0},{"groupName":"","widget":"Coupons","instanceConfig":{"itemGroup":"Coupons"},"style":"Default","defaultOrder":3,"separatorFlag":1,"separatorHeight":21}]}', '2015082914', '2015061701', '2', '2015-06-27 14:04:57',1,24210090697430842,3);
	
INSERT INTO `eh_launch_pad_layouts`(id, namespace_id, `scene_type`, name, layout_json, version_code, min_version_code, status, create_time,scope_code,scope_id,apply_policy) 
	VALUES ((@layout_id := @layout_id + 1), 0, 'pm_admin', 'ServiceMarketLayout', '{"versionCode":"2016031201","versionName":"3.3.0","layoutName":"ServiceMarketLayout","displayName":"服务市场","groups":[{"groupName":"商家服务","widget":"Navigator","instanceConfig":{"itemGroup":"Bizs"},"style":"Default","defaultOrder":5,"separatorFlag":0,"separatorHeight":0},{"groupName":"","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":1,"separatorFlag":0,"separatorHeight":0},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"GovAgencies"},"style":"Default","defaultOrder":2,"separatorFlag":1,"separatorHeight":21,"columnCount":4},{"groupName":"","widget":"Coupons","instanceConfig":{"itemGroup":"Coupons"},"style":"Default","defaultOrder":3,"separatorFlag":1,"separatorHeight":21},{"groupName":"滚动广告","widget":"Bulletins","instanceConfig":{"itemGroup":""},"style":"Default","defaultOrder":4,"separatorFlag":1,"separatorHeight":21}]}', '2015082914', '2015061701', '2', '2015-06-27 14:04:57',4,1000631,3);
	
set @banner_id = (select max(id) from `eh_banners`);
INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `start_time`, `end_time`, `status`, `order`, `creator_uid`, `create_time`, `delete_time`, `scene_type`,apply_policy) VALUES ((@banner_id := @banner_id + 1),0,NULL,'/home','DEFAULT',1,24210090697430842,'wowowowowowowoowowow~',NULL,'cs://1/image/aW1hZ2UvTVRvelpqY3laV0UzTmprMlptWXlZalF3WVRRNU16WTFabU5oTUdWbU1HVmpPQQ',9,'{\"forumId\":100334,\"topicId\":176119}',NULL,NULL,2,10,1,'2016-06-17 15:34:35',NULL,'default',3);
INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `start_time`, `end_time`, `status`, `order`, `creator_uid`, `create_time`, `delete_time`, `scene_type`,apply_policy) VALUES ((@banner_id := @banner_id + 1),0,NULL,'/home','DEFAULT',1,24210090697430842,'办公司啦,办公司啦...',NULL,'cs://1/image/aW1hZ2UvTVRveE5qSTRaREZqWVdFd09URXpabVZtWVRFeE9ERTFZekV6TURJMU9XWTFNUQ',9,'{\"forumId\":100334,\"topicId\":180523}',NULL,NULL,2,10,67663,'2016-06-29 00:09:58',NULL,'default',3);
INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `start_time`, `end_time`, `status`, `order`, `creator_uid`, `create_time`, `delete_time`, `scene_type`,apply_policy) VALUES ((@banner_id := @banner_id + 1),0,NULL,'/home','DEFAULT',4,1000631,'世纪春城物业唯一的banner...',NULL,'cs://1/image/aW1hZ2UvTVRveE5qSTRaREZqWVdFd09URXpabVZtWVRFeE9ERTFZekV6TURJMU9XWTFNUQ',9,'{\"forumId\":100334,\"topicId\":180523}',NULL,NULL,2,10,67663,'2016-06-29 00:09:58',NULL,'pm_admin',0);

-- by sfyan 20160707
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`) 
VALUES(1001187, 0, 'PM', '左邻管理公司', 0, '', '/1001187', 1, 2, 'ENTERPRISE', 0);

INSERT INTO `eh_organization_details` (`id`, `organization_id`, `contact`, `address`, `longitude`, `latitude`, `display_name`, `contactor`, `post_uri`, `avatar`) 
VALUES(10485, 1001187, '', '深圳市南山区科发路8号金融基地2栋7F', 113.951444, 22.536384, '深圳市南山区科发路8号金融基地2栋7F', '', '', '');	

INSERT INTO `eh_organization_members`(id, organization_id, target_type, target_id, member_group, contact_name, contact_type, contact_token, status, `namespace_id`)
VALUES(2102923, 1001187, 'USER', 190000  , 'manager', '颜少凡', 0, '13510701575', 3, 0);	

INSERT INTO `eh_organization_communities`(organization_id, community_id)VALUES(1001187, 240111044331051380);

INSERT INTO `eh_acl_role_assignments`(id, owner_type, owner_id, target_type, target_id, role_id, creator_uid, create_time)
VALUES(10655, 'EhOrganizations', 1001187, 'EhUsers', 190000  , 1001, 1, UTC_TIMESTAMP());


INSERT INTO `eh_groups` (`id`, `uuid`, `namespace_id`, `name`, `display_name`, `avatar`, `description`, `creator_uid`, `private_flag`, `join_policy`, `discriminator`, `visibility_scope`, `visibility_scope_id`, `category_id`, `category_path`, `status`, `member_count`, `share_count`, `post_flag`, `tag`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `update_time`, `create_time`, `delete_time`, `visible_region_type`, `visible_region_id`) VALUES('1001301','4b6cebff-9113-1225-adde-00163e005fce','0','武汉大学','武汉大学',NULL,NULL,'1','1','1','enterprise',NULL,NULL,NULL,NULL,'1','2','0','0',NULL,NULL,NULL,NULL,'176531',NULL,NULL,NULL,NULL,NULL,NULL,'2015-12-15 15:51:35','2015-11-12 22:05:33',NULL,'0','240111044331048623');
UPDATE eh_organizations SET group_id = 1001301 WHERE id = 1001027;

 
-- add by wuhan 20160711
-- luanch pad item add office cubicle  icon_uri = yuan qu ru zhu
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`,
 `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`)
  VALUES('2022','999989','0','0','0','/home','Bizs','工位申请','工位申请','cs://1/image/aW1hZ2UvTVRvNFlXSXdaRGRqTnpKaU56UmtZMll3WlRNeU1XUmxOekU0WXpNNU9HWmpZUQ','1','1','28','{"rentType":"office_cubicle"}',
  '0','0','1','1','','10',NULL,NULL,NULL,'0','park_pm_admin');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`,
 `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`)
  VALUES('2023','999989','0','0','0','/home','Bizs','工位申请','工位申请','cs://1/image/aW1hZ2UvTVRvNFlXSXdaRGRqTnpKaU56UmtZMll3WlRNeU1XUmxOekU0WXpNNU9HWmpZUQ','1','1','28','{"rentType":"office_cubicle"}',
  '0','0','1','1','','10',NULL,NULL,NULL,'0','park_tourist');
 