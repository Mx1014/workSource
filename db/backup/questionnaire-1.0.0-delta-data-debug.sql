-- 问卷调查的错误提示， add by tt, 20170220
select max(id) into @id from eh_locale_strings;
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'questionnaire', '1', 'zh_CN', '问卷名称不能为空');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'questionnaire', '2', 'zh_CN', '问卷名称不能超过50个字');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'questionnaire', '3', 'zh_CN', '题目名称不能为空');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'questionnaire', '4', 'zh_CN', '至少需要有一个题目');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'questionnaire', '5', 'zh_CN', '至少需要有一个选项');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'questionnaire', '6', 'zh_CN', '选项名称不能为空');


--
-- 问卷调查菜单，add by tt, 20170221
--
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`)
VALUES (40150, '企业问卷调查', 40000, NULL, 'react:/questionnaire-survey/questionnaire-management/40150', 1, 2, '/40000/40150', 'park', 495);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`)
VALUES (80800, '问卷调查', 80000, NULL, 'react:/questionnaire-survey/questionnaire-list', 1, 2, '/80000/80800', 'park', 810);

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (20025, 0, '企业问卷调查 管理员', '企业问卷调 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (20026, 0, '问卷调查 管理员', '企业问卷调 业务模块权限', NULL);

SET @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES((@web_menu_privilege_id := @web_menu_privilege_id + 1),'20025','40150','企业问卷调查','1','1','企业问卷调查 全部权限','710');
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES((@web_menu_privilege_id := @web_menu_privilege_id + 1),'20026','80800','问卷调查','1','1','问卷调查 全部权限','710');

SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`, `owner_type`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`, `role_type`)
VALUES ((@acl_id := @acl_id + 1), 'EhOrganizations', 1, 20025, 1001, 0, 1, NOW(), 'EhAclRoles');
INSERT INTO `eh_acls` (`id`, `owner_type`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`, `role_type`)
VALUES ((@acl_id := @acl_id + 1), 'EhOrganizations', 1, 20026, 1005, 0, 1, NOW(), 'EhAclRoles');

SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 40150, '', 'EhNamespaces', 1000000, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 80800, '', 'EhNamespaces', 1000000, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
VALUES ((@menu_scope_id := @menu_scope_id + 1), 80000, '', 'EhNamespaces', 1000000, 2);

-- 以下数据不一定用得到，add by tt, 20170221
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`) VALUES (80000, '园区服务', 0, 'fa fa-group', NULL, 1, 2, '/80000', 'park', 800, NULL);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`) VALUES (11911, 10120, 80000, '园区服务', 1, 1, '园区服务 全部权限', 710);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10120, 0, '园区服务 管理员', '园区服务 业务模块权限', NULL);
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`, `namespace_id`, `role_type`, `scope`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `comment_tag1`, `comment_tag2`, `comment_tag3`, `comment_tag4`, `comment_tag5`) VALUES (9983, 'EhOrganizations', NULL, 1, 10120, 1005, 0, 1, '2017-01-06 18:36:05', 0, 'EhAclRoles', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
-- 以上数据不一定用得到
