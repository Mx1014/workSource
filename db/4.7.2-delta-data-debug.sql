-- 用户申诉手机号成功消息内容  add by xq.tian  2017/07/12
SET @locale_max_id = (SELECT max(id) FROM `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
VALUES ((@locale_max_id := @locale_max_id + 1), 'user', '3', 'zh_CN', '您修改手机号的申请已被拒绝，若有疑问请联系客服，感谢您的使用。');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
VALUES ((@locale_max_id := @locale_max_id + 1), 'user', '300005', 'zh_CN', '请回到第一步重试');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
VALUES ((@locale_max_id := @locale_max_id + 1), 'user', '300006', 'zh_CN', '验证码错误或已过期');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
VALUES ((@locale_max_id := @locale_max_id + 1), 'user', '300007', 'zh_CN', '审核申诉失败');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
VALUES ((@locale_max_id := @locale_max_id + 1), 'user', '300008', 'zh_CN', '该手机号码已被注册');


-- 申诉短信模板 add by xq.tian  2017/07/12
SET @max_template_id = (SELECT max(id) FROM `eh_locale_templates`);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.yzx', 53, 'zh_CN', '申诉-科技园', '90012', 1000000);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.yzx', 53, 'zh_CN', '申诉-海岸', '90015', 999993);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.yzx', 53, 'zh_CN', '申诉-深业', '90018', 999992);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.yzx', 53, 'zh_CN', '申诉-威新', '90019', 999991);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.yzx', 53, 'zh_CN', '申诉-储能', '90020', 999990);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.yzx', 53, 'zh_CN', '申诉-ibase', '90021', 999989);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.yzx', 53, 'zh_CN', '申诉-爱特家', '90022', 999988);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.yzx', 53, 'zh_CN', '申诉-清华', '90091', 999984);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.yzx', 53, 'zh_CN', '申诉-创源', '90026', 999986);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.yzx', 53, 'zh_CN', '申诉-华润', '90027', 999985);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.yzx', 53, 'zh_CN', '申诉-T空间', '90028', 999982);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.yzx', 53, 'zh_CN', '申诉-正中会', '90029', 999983);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.yzx', 53, 'zh_CN', '申诉-星商汇', '90030', 999981);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.yzx', 53, 'zh_CN', '申诉-全至100', '90031', 999980);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.yzx', 53, 'zh_CN', '申诉-光大', '90034', 999979);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.yzx', 53, 'zh_CN', '申诉-Volgo', '90035', 1);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.yzx', 53, 'zh_CN', '申诉-名网邦', '90037', 999976);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@max_template_id := @max_template_id + 1), 'sms.default.yzx', 53, 'zh_CN', '申诉-荣超', '90038', 999975);


--
-- 修改用户认证的菜单data_type
--
UPDATE `eh_web_menus` SET `data_type` = 'react:/identification-management/user-identification' WHERE `id` = 35000;
UPDATE `eh_web_menus` SET `name` = '员工认证', `data_type` = 'react:/identification-management/employee-identification' WHERE `id` = 50500;

-- 企业人才申请的发消息，add by tt, 20170710
select max(id) into @id from `eh_locale_templates`;
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id+1, 'talent.notification', 1, 'zh_CN', '企业人才申请时发消息', '企业人才|收到一条企业人才的申请单', 0);


-- 以下菜单未经李磊同意不得在现网执行！！！！！！！！！！！
-- 先删除之前配的菜单，add by tt, 20170527
delete from `eh_web_menus` where id in (40730, 40731, 40732, 40733, 40734);
delete from `eh_acl_privileges` where name = '企业人才 管理员';
delete from `eh_web_menu_privileges` where menu_id in (40730, 40731, 40732, 40733, 40734);
delete from `eh_web_menu_scopes` where menu_id in (40730, 40731, 40732, 40733, 40734) and owner_id = 999990;

-- 菜单，add by tt, 20170527
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`) VALUES (40730, '企业人才', 40000, NULL, 'react:/enterprise-management/talent', 1, 2, '/40000/40730', 'park', 498, 40730);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`) VALUES (40731, '人才管理', 40730, NULL, 'react:/enterprise-management/talent-list', 1, 2, '/40000/40730/40731', 'park', 499, 40730);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`) VALUES (40732, '申请设置', 40730, NULL, 'react:/enterprise-management/apply-setting', 1, 2, '/40000/40730/40732', 'park', 500, 40730);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`) VALUES (40733, '消息推送', 40730, NULL, 'react:/enterprise-management/message-push', 1, 2, '/40000/40730/40733', 'park', 501, 40730);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`) VALUES (40734, '申请记录', 40730, NULL, 'react:/enterprise-management/apply-record', 1, 2, '/40000/40730/40734', 'park', 502, 40730);

-- 权限，add by tt, 20170527
select max(id) into @pri_id from `eh_acl_privileges`;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@pri_id:=@pri_id+1, 0, '企业人才 管理员', '企业人才 业务模块权限', NULL);

-- 菜单关联权限，add by tt, 20170527
select max(id) into @id from `eh_web_menu_privileges`;
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`) VALUES (@id:=@id+1, @pri_id, 40730, '企业人才', 1, 1, '企业人才 全部权限', 714);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`) VALUES (@id:=@id+1, @pri_id, 40731, '企业人才', 1, 1, '企业人才 全部权限', 715);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`) VALUES (@id:=@id+1, @pri_id, 40732, '企业人才', 1, 1, '企业人才 全部权限', 716);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`) VALUES (@id:=@id+1, @pri_id, 40733, '企业人才', 1, 1, '企业人才 全部权限', 717);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`) VALUES (@id:=@id+1, @pri_id, 40734, '企业人才', 1, 1, '企业人才 全部权限', 718);

-- 权限赋予超管，add by tt, 20170527
select max(id) into @id from `eh_acls`;
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`, `namespace_id`, `role_type`, `scope`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `comment_tag1`, `comment_tag2`, `comment_tag3`, `comment_tag4`, `comment_tag5`) VALUES (@id+1, 'EhOrganizations', NULL, 1, @pri_id, 1001, 0, 1, '2017-06-05 09:41:07', 0, 'EhAclRoles', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

-- 显示菜单，add by tt, 20170527
select max(id) into @id from `eh_web_menu_scopes`;
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES (@id:=@id+1, 40730, '', 'EhNamespaces', 999990, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES (@id:=@id+1, 40731, '', 'EhNamespaces', 999990, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES (@id:=@id+1, 40732, '', 'EhNamespaces', 999990, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES (@id:=@id+1, 40733, '', 'EhNamespaces', 999990, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES (@id:=@id+1, 40734, '', 'EhNamespaces', 999990, 2);
-- end 执行！！！！！！！！！！！