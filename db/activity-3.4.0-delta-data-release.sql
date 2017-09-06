-- 因为后面会出现clone帖的功能，现在现将原有帖子设置为正常帖  add by yanjun 20170809
UPDATE eh_forum_posts set clone_flag = 2 where clone_flag is null;
UPDATE eh_activities set clone_flag = 2 where clone_flag is null;

-- 活动报名导入异常信息
set @id = (select MAX(id) FROM eh_locale_strings);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES
	((@id := @id + 1), 'activity', '20', 'zh_CN', '手机号为空');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES
	((@id := @id + 1), 'activity', '21', 'zh_CN', '手机号无效');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES
	((@id := @id + 1), 'activity', '22', 'zh_CN', '真实姓名为空');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES
	((@id := @id + 1), 'activity', '23', 'zh_CN', '报名信息已经存在，可在报名详情页修改该用户信息');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES
	((@id := @id + 1), 'activity', '24', 'zh_CN', '该用户在Excel表重复');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES
	((@id := @id + 1), 'activity', '25', 'zh_CN', '报名信息存在，现已更新');

UPDATE eh_locale_strings SET text = '呃，报名信息已经存在，可在报名详情页修改该用户信息' WHERE scope = 'activity' AND code = 10031;


-- 管理后台增加菜单 add by yanjun 20170906
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`) VALUES ('7000000', '园区服务模块管理', '0', null, null, '1', '2', '/7000000', 'zuolin', '20', '10600', '1', 'project', 'classify');
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`) VALUES ('7100000', '活动模块', '7000000', NULL, 'activity-application', '1', '2', '/7000000/7100000', 'zuolin', '1', '10600', '2', 'project', 'module');
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`) VALUES ('7110000', '活动模块', '7100000', NULL, 'activity-application', '1', '2', '/7000000/7100000/7110000', 'zuolin', '1', '10600', '3', 'project', 'page');