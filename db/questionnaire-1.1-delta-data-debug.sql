INSERT INTO `eh_configurations` (`name`, `value`, `description`) VALUES ('questionnaire.detail.url', '/questionnaire-survey/build/index.html#/question/%s/0', '问卷地址URL');
INSERT INTO `eh_configurations` (`name`, `value`, `description`) VALUES ('questionnaire.send.message.express', '0 0 1 * * ?', '定时任务表达式');
INSERT INTO `eh_configurations` (`name`, `value`, `description`) VALUES ('questionnaire.remind.time.interval', '24', '通知value小时内，没有回答问卷的用户');

