-- 二维码页面  add by yanjun 20170727
SET @id = (SELECT MAX(id) FROM eh_configurations);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES( @id:= @id + 1,'group.scanJoin.url','/mobile/static/message/src/addGroup.html','group.scanJoin.url','0',NULL);

INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES( @id:= @id + 1,'group.scanDownload.url','/mobile/static/downloadLink/src/downLink.html','group.scanDownload.url','0',NULL);

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
