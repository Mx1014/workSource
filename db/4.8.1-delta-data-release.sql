-- 初始化range字段 by st.zheng
update `eh_service_alliances` set `range`=owner_id where owner_type='community';

update `eh_service_alliances` set `range`='all' where owner_type='orgnization';

-- 将原来的审批置为删除 by.st.zheng
update `eh_service_alliance_jump_module` set `signal`=0 where module_name = '审批';

set @eh_service_alliance_jump_id = (select max(id) from eh_service_alliance_jump_module);
INSERT INTO `eh_service_alliance_jump_module` (`id`, `module_name`,`signal`, `module_url`, `parent_id`)
VALUES (@eh_service_alliance_jump_id + 1, '审批', 2,'zl://approval/create?approvalId={}&sourceId={}', '0');

-- 更改菜单 by.st.zheng
UPDATE `eh_web_menus` SET `data_type`='react:/approval-management/approval-list/40500/EhOrganizations' WHERE `id`='40541' and name='审批列表';

--
-- 用户输入内容变量 add by xq.tian  2017/08/05
--
SELECT MAX(id) FROM `eh_flow_variables` INTO @flow_variables_id;
INSERT INTO `eh_flow_variables` (`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `name`, `label`, `var_type`, `script_type`, `script_cls`, `status`)
VALUES ((@flow_variables_id := @flow_variables_id + 1), 0, 0, '', 0, '', 'text_button_msg_input_content', '文本备注内容', 'text_button_msg', 'bean_id', 'flow-variable-text-button-msg-user-input-content', 1);




-- merge from msg-2.1
-- 二维码页面  add by yanjun 20170727
SET @id = (SELECT MAX(id) FROM eh_configurations);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES( @id:= @id + 1,'group.scanJoin.url','/mobile/static/message/src/addGroup.html','group.scanJoin.url','0',NULL);

INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES( @id:= @id + 1,'group.scanDownload.url','/mobile/static/qq_down/index.html','group.scanDownload.url','0',NULL);

-- 群聊默认名称   add by yanjun 20170801
SET @id = (SELECT MAX(id) FROM eh_locale_templates);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id := @id + 1),'group.notification','50','zh_CN','你邀请用户加入群','你邀请${userNameList}加入了群聊','0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id := @id + 1),'group.notification','51','zh_CN','其他用户邀请用户加入群','${inviterName}邀请${userNameList}加入了群聊','0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id := @id + 1),'group.notification','52','zh_CN','被邀请入群','${inviterName}邀请你加入了群聊','0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id := @id + 1),'group.notification','53','zh_CN','退出群聊','${userName}已退出群聊','0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id := @id + 1),'group.notification','54','zh_CN','退出群聊','群聊名称已修改为“${groupName}”','0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id := @id + 1),'group.notification','55','zh_CN','删除成员','你将${userNameList}移出了群聊','0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id := @id + 1),'group.notification','56','zh_CN','删除成员','你被${userName}移出了群聊','0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id := @id + 1),'group.notification','57','zh_CN','解散群','你加入的群聊“${groupName}”已解散','0');

SET @id = (SELECT MAX(id) FROM eh_locale_strings);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES((@id := @id + 1),'group','20001','zh_CN','群聊');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES((@id := @id + 1),'group','20002','zh_CN','你已成为新群主');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES((@id := @id + 1),'group','20003','zh_CN','你通过扫描二维码加入了群聊');

-- 活动分享页app信息栏-volgo更新为“工作中的生活态度”  add by yanjun 20170815
UPDATE eh_app_urls set description = '工作中的生活态度' where namespace_id = 1;
