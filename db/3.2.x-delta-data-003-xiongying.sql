INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'videoConf', '10006', 'zh_CN', '左邻');

INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'videoconf.notification', 1, 'zh_CN', '会议邀请，邮件主题', '您被${userName}邀请出席左邻视频会议 ${confName}');

INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'videoconf.notification', 2, 'zh_CN', '会议邀请，会议时间', '会议时间：${begin}至${end}，${zone}');

INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'videoconf.notification', 3, 'zh_CN', '会议邀请，会议描述', ' <会议描述> 您可以通过以下方式加入会议：
方式一：点击链接加入：http://meeting.zuolin.com/j/${mobile}；方式二：访问 http://meeting.confcloud.cn 加入，输入号码：${mobile}');
