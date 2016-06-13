INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
	VALUES(200501, 0, 0, '品质核查类别', '品质核查类别', 0, 2, UTC_TIMESTAMP(), 0);
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'quality', '10001', 'zh_CN', '该业务组没有执行核查的成员');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'quality', '10002', 'zh_CN', '任务不存在');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'quality', '10003', 'zh_CN', '标准不存在');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'quality', '10004', 'zh_CN', '类型不存在');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'quality', '10005', 'zh_CN', '权重不存在');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'quality', '10006 ', 'zh_CN', '说明：');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'quality', '10007', 'zh_CN', '该类型下存在有效的标准，不能删除');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'quality', '10008', 'zh_CN', '该任务已关闭');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'quality', '10009', 'zh_CN', '生成excel信息有问题');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'quality', '10010', 'zh_CN', '下载excel信息有问题');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'quality', '10011', 'zh_CN', '不能分配自己');


INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'quality.notification', 1, 'zh_CN', '生成核查任务通知核查人', '你被安排执行“${taskName}”任务，截至日期为“${deadline}”，请及时执行');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'quality.notification', 2, 'zh_CN', '通知整改人', '你被“${userName}”安排执行“${taskName}”任务，截至日期为“${deadline}”，请及时执行');

INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'quality.notification', 3, 'zh_CN', '分配和转发任务', '“${operator}”安排${target}”执行“${taskName}”任务，截至日期为“${deadline}”');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'quality.notification', 4, 'zh_CN', '审阅不合格通知执行人', '您的任务“${taskNumber}”被“${userName}”审阅为不合格，请知悉');