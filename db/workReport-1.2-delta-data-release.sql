UPDATE `eh_locale_templates` SET `text`='${applierName}给你提交了${reportName}（${reportTime}）' WHERE `scope`='work.report.notification' AND `code`='1';

UPDATE `eh_locale_templates` SET `text`='${applierName}更新了Ta的${reportName}（${reportTime}）' WHERE `scope`='work.report.notification' AND `code`='2';
