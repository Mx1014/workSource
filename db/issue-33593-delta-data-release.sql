INSERT INTO eh_locale_templates(`scope`, `code`,`locale`, `description`, `text`)
VALUES( 'activity.notification', 18, 'zh_CN', '活动报名人数不足最低限制人数，活动取消', '您报名的活动“${subject}”由于未达到最低人数，已被取消。');
INSERT INTO eh_locale_templates(`scope`, `code`,`locale`, `description`, `text`)
VALUES( 'activity.notification', 19, 'zh_CN', '活动报名人数不足最低限制人数，活动取消', '您报名的活动“${subject}”由于未达到最低人数，已被取消，报名费用将在三个工作日内退回您的账户上。');
INSERT INTO eh_locale_templates(`scope`, `code`,`locale`, `description`, `text`)
VALUES( 'activity.notification', 20, 'zh_CN', '活动报名人数不足最低限制人数，活动取消', '您发起的活动“${subject}”由于未达到最低人数，已被自动取消。');
INSERT INTO `eh_locale_strings` (`scope`,`code`,`locale`,`text`) VALUES ('activity',28,'zh_CN','活动取消通知');
UPDATE eh_locale_strings SET text = '来晚啦，活动已删除' WHERE scope = 'forum' and code = 10006