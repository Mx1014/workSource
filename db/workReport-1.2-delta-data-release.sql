-- AUTHOR: ryan  20180827
-- REMARK: 执行 /workReport/syncWorkReportReceiver 接口, 用以同步工作汇报接收人公司信息

-- AUTHOR: ryan  20180827
-- REMARK: 执行 /workReport/updateWorkReportReceiverAvatar 接口, 用以更新工作汇报接收人头像

UPDATE `eh_locale_templates` SET `text`='${applierName}给你提交了${reportName}（${reportTime}）', `code` = 101 WHERE `scope`='work.report.notification' AND `code`='1';
UPDATE `eh_locale_templates` SET `text`='${applierName}更新了Ta的${reportName}（${reportTime}）', `code` = 102 WHERE `scope`='work.report.notification' AND `code`='2';
UPDATE `eh_locale_templates` SET `text`='${commentatorName}在Ta的${reportName}（${reportTime}）中 回复了你', `code` = 1, `description` = '评论消息类型1' WHERE `scope`='work.report.notification' AND `code`='3';
UPDATE `eh_locale_templates` SET `text`='${commentatorName}在Ta的${reportName}（${reportTime}）中 发表了评论', `code` = 2, `description` = '评论消息类型2' WHERE `scope`='work.report.notification' AND `code`='4';
UPDATE `eh_locale_templates` SET `text`='${commentatorName}在你的${reportName}（${reportTime}）中 回复了你', `code` = 3, `description` = '评论消息类型3' WHERE `scope`='work.report.notification' AND `code`='5';
UPDATE `eh_locale_templates` SET `text`='${commentatorName}在${applierName}的${reportName}（${reportTime}）中 回复了你', `code` = 4, `description` = '评论消息类型4' WHERE `scope`='work.report.notification' AND `code`='6';
UPDATE `eh_locale_templates` SET `text`='${commentatorName}在你的${reportName}（${reportTime}）中 发表了评论', `code` = 5, `description` = '评论消息类型5' WHERE `scope`='work.report.notification' AND `code`='7';


UPDATE `eh_work_reports` SET `validity_setting` = '{"endTime":"10:00","endType":1,"startTime":"15:00","startType":0}' WHERE `report_type` = 0;
UPDATE `eh_work_reports` SET `validity_setting` = '{"endMark":"1","endTime":"10:00","endType":1,"startMark":"5","startTime":"15:00","startType":0}' WHERE `report_type` = 1;
UPDATE `eh_work_reports` SET `validity_setting` = '{"endMark":"1","endTime":"10:00","endType":1,"startMark":"31","startTime":"15:00","startType":0}' WHERE `report_type` = 2;

UPDATE `eh_work_report_templates` SET `icon_uri`='cs://1/image/aW1hZ2UvTVRwaFlqazJNVFk1WkdabFpEbGhNamc0T1RjNVltWmlOakl3TmpobE1qUXpOUQ' WHERE (`id`='1') LIMIT 1;
UPDATE `eh_work_report_templates` SET `icon_uri`='cs://1/image/aW1hZ2UvTVRvMU1tRTVPV0kwWlRjeU16WmlOR05rTnpWbE9HUTFZV1ExTlRZMFpHUm1Odw' WHERE (`id`='2') LIMIT 1;
UPDATE `eh_work_report_templates` SET `icon_uri`='cs://1/image/aW1hZ2UvTVRvd1lXRmtObUkwWkRaaFlXRXdPRFV5TmpjMU5qa3hNelk1TVRRNE5XUTRNdw' WHERE (`id`='3') LIMIT 1;

UPDATE eh_work_reports SET `icon_uri`='cs://1/image/aW1hZ2UvTVRwaFlqazJNVFk1WkdabFpEbGhNamc0T1RjNVltWmlOakl3TmpobE1qUXpOUQ' WHERE report_template_id = 1;
UPDATE eh_work_reports SET `icon_uri`='cs://1/image/aW1hZ2UvTVRvMU1tRTVPV0kwWlRjeU16WmlOR05rTnpWbE9HUTFZV1ExTlRZMFpHUm1Odw' WHERE report_template_id = 2;
UPDATE eh_work_reports SET `icon_uri`='cs://1/image/aW1hZ2UvTVRvd1lXRmtObUkwWkRaaFlXRXdPRFV5TmpjMU5qa3hNelk1TVRRNE5XUTRNdw' WHERE report_template_id = 3;
