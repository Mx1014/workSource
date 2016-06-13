INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'organization.notification', 17, 'zh_CN', '有用户申请认证公司时给公司管理员发消息', '“${userName}”申请加入公司“${organizationName}”');

update `eh_forum_posts` set integral_tag2 = child_count+1;