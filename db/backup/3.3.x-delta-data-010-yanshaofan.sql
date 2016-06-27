INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`, `namespace_id`) VALUES( 'sms.default', 6, 'zh_CN', '处理任务时发送的短信','${operatorUName}给你分配了一个任务，请直接联系用户${targetUName}（电话${targetUToken}），帮他处理该问题。', 0);
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`, `namespace_id`) VALUES( 'sms.default.yzx', 6, 'zh_CN', '处理任务时发送的短信-左邻',22716, 0);
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`, `namespace_id`) VALUES( 'sms.default.yzx', 6, 'zh_CN', '处理任务时发送的短信-科技园',22717, 1000000);
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`, `namespace_id`) VALUES( 'sms.default.yzx', 6, 'zh_CN', '处理任务时发送的短信-讯美',22718, 999999);
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`, `namespace_id`) VALUES( 'sms.default.yzx', 6, 'zh_CN', '处理任务时发送的短信-金隅嘉业',22719, 999995);
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`, `namespace_id`) VALUES( 'sms.default.yzx', 6, 'zh_CN', '处理任务时发送的短信-海岸城',22720, 999993);




#
#20160418
#
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'organization', '100001', 'zh_CN', '没有权限！');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'organization', '100201', 'zh_CN', '任务已经被处理啦，请刷新！');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'organization', '100202', 'zh_CN', '请指定分配的人员！');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'organization', '100203', 'zh_CN', '请做正确的处理方式！');


#
#20160422
#
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`, `namespace_id`) VALUES( 'group.notification', 34, 'zh_CN', '删除俱乐部管理员收到的消息','${userName}已删除俱乐部“${groupName}”', 0);
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`, `namespace_id`) VALUES( 'group.notification', 35, 'zh_CN', '删除俱乐部创建人收到的消息','已删除俱乐部“${groupName}”', 0);



#
#20160426
#
UPDATE `eh_acl_roles` SET `name` = '超级管理员' where `id` = 1001;
UPDATE `eh_acl_roles` SET `name` = '普通管理员' where `id` = 1002;
UPDATE `eh_acl_roles` SET `name` = '超级管理员' where `id` = 1005;
UPDATE `eh_acl_roles` SET `name` = '普通管理员' where `id` = 1006;




