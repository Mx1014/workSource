-- 通用脚本
-- ADD BY 梁燕龙
-- issue-30635 活动V4.0 支持发布后修改
INSERT INTO eh_locale_templates(`scope`, `code`,`locale`, `description`, `text`)
	VALUES( 'activity.notification', 11, 'zh_CN', '活动发布后，修改标题', '您参加的活动“${postName}”的主题已被发起方改成“${newPostName}”。');
INSERT INTO eh_locale_templates(`scope`, `code`,`locale`, `description`, `text`)
	VALUES( 'activity.notification', 12, 'zh_CN', '活动发布后，修改时间', '您参加的活动“${postName}”的时间已被发起方改成“${startTime}~${endTime}”。');
INSERT INTO eh_locale_templates(`scope`, `code`,`locale`, `description`, `text`)
	VALUES( 'activity.notification', 13, 'zh_CN', '活动发布后，修改地址', '您参加的活动“${postName}”的地点已被发起方改成“${address}”。');
INSERT INTO eh_locale_templates(`scope`, `code`,`locale`, `description`, `text`)
	VALUES( 'activity.notification', 14, 'zh_CN', '活动发布后，修改标题和时间', '您参加的活动“${postName}”被发起方修改，详情如下：主题被改成“${newPostName}”、时间被改成“${startTime}~${endTime}”。');
INSERT INTO eh_locale_templates(`scope`, `code`,`locale`, `description`, `text`)
	VALUES( 'activity.notification', 15, 'zh_CN', '活动发布后，修改标题和地址', '您参加的活动“${postName}”被发起方修改，详情如下：主题被改成“${newPostName}”、地点被改成“${address}”。');
INSERT INTO eh_locale_templates(`scope`, `code`,`locale`, `description`, `text`)
	VALUES( 'activity.notification', 16, 'zh_CN', '活动发布后，修改时间和地址', '您参加的活动“${postName}”被发起方修改，详情如下：时间被改成“${startTime}~${endTime}”、地点被改成“${address}。');
INSERT INTO eh_locale_templates(`scope`, `code`,`locale`, `description`, `text`)
	VALUES( 'activity.notification', 17, 'zh_CN', '活动发布后，修改标题、时间和地点', '您参加的活动“${postName}”被发起方修改，详情如下：主题被改成“${newPostName}”、时间被改成“${startTime}~${endTime}”、地点被改成“${address}”。');


INSERT INTO eh_locale_strings ( `scope`, `code`, `locale`, `text`)
	VALUES ('activity', 26, 'zh_CN', '活动被修改！！');
-- END