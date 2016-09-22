INSERT INTO `eh_search_types` (`id`, `namespace_id`, `name`, `content_type`, `status`, `create_time`) VALUES (1, 0, '快讯', 'news', '1', UTC_TIMESTAMP());

INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('news.url', '/park-news/index.html/#/news/share/', 'news page url', 0, NULL);
-- added by wh  2016-9-21 配置园区快讯广场图标测试用 增加categoryId =1
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (112138, 999989, '0', '0', '0', '/home', 'Bizs', 'news', '园区快讯', 'cs://1/image/aW1hZ2UvTVRvNVpEWTROR0prTnpGak0yVm1NRGhsWVRNMU5EY3lPR0kzT1RSalkySmxZdw', '1', '1', 48, '{"categoryId":1,"timeWidgetStyle":"date"}', '1', '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'default');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (112147, 999989, '0', '0', '0', '/home', 'Bizs', 'news', '园区快讯', 'cs://1/image/aW1hZ2UvTVRvNVpEWTROR0prTnpGak0yVm1NRGhsWVRNMU5EY3lPR0kzT1RSalkySmxZdw', '1', '1', 48, '{"categoryId":1,"timeWidgetStyle":"date"}', '1', '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'pm_admin');

-- added by wh  2016-9-21 给ibase园区快讯 增加categoryId=0,展示方式为日期时间
UPDATE  `eh_launch_pad_layouts` SET layout_json = '{"versionCode":"2016081701","versionName":"3.0.0","layoutName":"ServiceMarketLayout","displayName":"服务市场","groups":[{"groupName":"","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":1,"separatorFlag":1,"separatorHeight":21},{"groupName":"商家服务","widget":"Navigator","instanceConfig":{"itemGroup":"Bizs"},"style":"Default","defaultOrder":5,"separatorFlag":1,"separatorHeight":21},{"groupName":"","widget":"Bulletins","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":1,"separatorFlag":1,"separatorHeight":21},{"groupName":"","widget":"News","instanceConfig":{"timeWidgetStyle":"datetime","categoryId":0,"itemGroup":"Default"},"style":"Default","defaultOrder":1,"separatorFlag":1,"separatorHeight":21}]}' WHERE namespace_id = 999989 AND NAME = 'ServiceMarketLayout';

-- added by wh  2016-9-22 配置园区快讯web广场图标测试用 增加categoryId =1
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (112137, 999989, '0', '0', '0', '/home', 'Bizs', 'news', '园区快讯web', 'cs://1/image/aW1hZ2UvTVRvNVpEWTROR0prTnpGak0yVm1NRGhsWVRNMU5EY3lPR0kzT1RSalkySmxZdw', '1', '1', 14, '{"url":"http://beta.zuolin.com/park-news/index.html?hideNavigationBar=1#sign_suffix"}', '1', '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'default');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (112146, 999989, '0', '0', '0', '/home', 'Bizs', 'news', '园区快讯web', 'cs://1/image/aW1hZ2UvTVRvNVpEWTROR0prTnpGak0yVm1NRGhsWVRNMU5EY3lPR0kzT1RSalkySmxZdw', '1', '1', 14, '{"url":"http://beta.zuolin.com/park-news/index.html?hideNavigationBar=1#sign_suffix"}', '1', '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'pm_admin');

    
-- 在业主类型表中添加数据      2016/09/02 by xq.tian
INSERT INTO `eh_organization_owner_type` (`id`, `namespace_id`, `name`, `display_name`, `status`) VALUES ('1', '0', 'owner', '业主', '1');
INSERT INTO `eh_organization_owner_type` (`id`, `namespace_id`, `name`, `display_name`, `status`) VALUES ('2', '0', 'tenement', '租客', '1');
INSERT INTO `eh_organization_owner_type` (`id`, `namespace_id`, `name`, `display_name`, `status`) VALUES ('3', '0', 'relative', '亲属', '1');
INSERT INTO `eh_organization_owner_type` (`id`, `namespace_id`, `name`, `display_name`, `status`) VALUES ('4', '0', 'friend', '朋友', '1');
INSERT INTO `eh_organization_owner_type` (`id`, `namespace_id`, `name`, `display_name`, `status`) VALUES ('5', '0', 'nurse', '保姆', '1');
INSERT INTO `eh_organization_owner_type` (`id`, `namespace_id`, `name`, `display_name`, `status`) VALUES ('6', '0', 'agency', '地产中介', '1');
INSERT INTO `eh_organization_owner_type` (`id`, `namespace_id`, `name`, `display_name`, `status`) VALUES ('7', '0', 'readyauth', '待审核', '1');
INSERT INTO `eh_organization_owner_type` (`id`, `namespace_id`, `name`, `display_name`, `status`) VALUES ('8', '0', 'other', '其他', '1');

-- 插入模板       2016/09/02 by xq.tian
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('orgOwnerBehavior', 'immigration', 'zh_CN', '迁入');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('orgOwnerBehavior', 'emigration', 'zh_CN', '迁出');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('orgOwnerAddressAuthType', '2', 'zh_CN', '无效');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('orgOwnerAddressAuthType', '0', 'zh_CN', '未认证');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('orgOwnerAddressAuthType', '1', 'zh_CN', '认证');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('orgOwnerAddressLivingStatus', '0', 'zh_CN', '否');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('orgOwnerAddressLivingStatus', '1', 'zh_CN', '是');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('ownerCarPrimaryFlag', '0', 'zh_CN', '否');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('ownerCarPrimaryFlag', '1', 'zh_CN', '是');


-- 插入审批类型对应的汉字文本，add by tt, 20160901
SET @max_id = (SELECT max(id) from eh_locale_strings);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@max_id:=@max_id+1, 'approval.type', '1', 'zh_CN', '请假');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@max_id:=@max_id+1, 'approval.type', '2', 'zh_CN', '异常');

-- 审批错误提示
set @id = (select max(id) from `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10000', 'zh_CN', '请选择请假类型');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10001', 'zh_CN', '请输入请假理由');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10002', 'zh_CN', '请选择请假时间');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10003', 'zh_CN', '请假开始时间必须小于请假结束时间');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10004', 'zh_CN', '所选时间包含已请假时间，请重新选择');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10005', 'zh_CN', '请假时间是非工作时间，请重新选择');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10006', 'zh_CN', '请假时长为0');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10007', 'zh_CN', '请输入申请理由');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10008', 'zh_CN', '类型名称不能为空');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10009', 'zh_CN', '类型名称已存在');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10010', 'zh_CN', '该审批人设置关联了审批规则，请先删除对应的审批规则，再删除该审批人设置');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10011', 'zh_CN', '审批名称不能为空');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10012', 'zh_CN', '审批名称超过8字');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10013', 'zh_CN', '审批名称已存在');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10014', 'zh_CN', '审批规则名称不能为空');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10015', 'zh_CN', '审批规则名称超过8字');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10016', 'zh_CN', '审批规则名称已存在');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10017', 'zh_CN', '请假审批不能选择无');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10018', 'zh_CN', '忘打卡审批不能为无');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10019', 'zh_CN', '请输入驳回理由');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10020', 'zh_CN', '请选择需要审批的申请单');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'approval', '10021', 'zh_CN', '类型名称不能超过8个字');

-- 审批发送消息模板
set @id := (select max(id) from `eh_locale_templates`);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'approval.notification', 11, 'zh_CN', '用户提交请假申请', '${creatorName}提交了请假申请，请假时间：${time}，请及时到后台进行处理。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'approval.notification', 12, 'zh_CN', '请假申请到下一级别', '${creatorName}提交了请假申请，请假时间：${time}，${approver}已同意，请及时到后台进行处理。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'approval.notification', 13, 'zh_CN', '请假申请通过', '您提交的请假申请已通过审批，请假时间：${time}。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'approval.notification', 14, 'zh_CN', '请假申请驳回', '您提交的请假申请被${approver}驳回，理由是：${reason}，请假时间：${time}。', 0);

INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'approval.notification', 21, 'zh_CN', '用户提交异常申请', '${creatorName}提交了异常申请，请及时到后台进行处理。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'approval.notification', 22, 'zh_CN', '异常申请到下一级别', '${creatorName}提交了异常申请，${approver}已同意，请及时到后台进行处理。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'approval.notification', 23, 'zh_CN', '异常申请通过', '您对${punchDate}提交的异常申请已通过审批。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'approval.notification', 24, 'zh_CN', '异常申请驳回', '您对${punchDate}提交的异常申请被${approver}驳回，理由是：${reason}。', 0);
