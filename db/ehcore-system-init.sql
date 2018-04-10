SET foreign_key_checks = 0;

use ehcore;

INSERT INTO `eh_configurations`(`namespace_id`, `name`, `value`, `description`) VALUES (0, 'db.init.timestamp', UTC_TIMESTAMP(), 'Database seeding timestamp');
INSERT INTO `eh_configurations`(`namespace_id`, `name`, `value`, `description`) VALUES (0, 'sms.itemName.type','MW','sms itemName');
INSERT INTO `eh_configurations`(`namespace_id`, `name`, `value`, `description`) VALUES (0, 'mw.port','9003','sms itemName');
INSERT INTO `eh_configurations`(`namespace_id`, `name`, `value`, `description`) VALUES (0, 'mw.password','223651','mw password');
INSERT INTO `eh_configurations`(`namespace_id`, `name`, `value`, `description`) VALUES (0, 'mw.user','J02300','mw user');
INSERT INTO `eh_configurations`(`namespace_id`, `name`, `value`, `description`) VALUES (0, 'mw.host','61.145.229.29','mw host ,special line');
INSERT INTO `eh_configurations` (`namespace_id`, `name`, `value`, `description`) VALUES (0, 'etag.timeout', '300', 'the timeout for etag');
INSERT INTO `eh_configurations` (`namespace_id`, `name`, `value`, `description`) VALUES (0, 'home.url', 'http://10.1.1.91:8080', 'the home url');
INSERT INTO `eh_configurations` (`namespace_id`, `name`, `value`, `description`) VALUES (0, 'user.score.url', '/app/static/score/score.html', 'the relative path for user score');
INSERT INTO `eh_configurations` (`namespace_id`, `name`, `value`, `description`) VALUES (0, 'post.share.url', '/mobile/static/share_post/share_post.html#', 'the relative path for sharing topic');
INSERT INTO `eh_configurations` (`namespace_id`, `name`, `value`, `description`) VALUES (0, 'yzx.vcode.templateid', '9547', 'vcode sms template id');
INSERT INTO `eh_configurations` (`namespace_id`, `name`, `value`, `description`) VALUES (0, 'yzx.account.sid', 'b0b423e8f630d920aaba8aafe8f37701', NULL);
INSERT INTO `eh_configurations` (`namespace_id`, `name`, `value`, `description`) VALUES (0, 'yzx.token', '1527ed4b1bb855fab12a5683bb3c7ead', NULL);
INSERT INTO `eh_configurations` (`namespace_id`, `name`, `value`, `description`) VALUES (0, 'yzx.appid', '89f85f91ed974429992831fb662dbb83', NULL);
INSERT INTO `eh_configurations` (`namespace_id`, `name`, `value`, `description`) VALUES (0, 'yzx.version', '2014-06-30', NULL);
INSERT INTO `eh_configurations` (`namespace_id`, `name`, `value`, `description`) VALUES (0, 'yzx.server', 'api.ucpaas.com', NULL);
INSERT INTO `eh_configurations` (`namespace_id`, `name`, `value`, `description`) VALUES (0, 'mw.vcode.template.content', '您的验证码为${vcode}，10分钟内有效，感谢您的使用。', NULL);
INSERT INTO `eh_configurations` (`namespace_id`, `name`, `value`, `description`) VALUES (0, 'marketdata.item.version', '2015080311', 'the version of all luanchpad items');
INSERT INTO `eh_configurations` (`namespace_id`, `name`, `value`, `description`) VALUES (0, 'app.agreements.url', '/app/static/app_agreements/app_agreements.html', 'the relative path for app agreements');

INSERT INTO `eh_configurations` (`namespace_id`, `name`, `value`, `description`) VALUES (0, 'apply.shop.url', '/app/static/apply_store/apply_store.html', 'the relative path for user shop apply');
INSERT INTO `eh_configurations` (`namespace_id`, `name`, `value`, `description`) VALUES (0, 'manage.shop.url', '/app/static/my_store/my_store.html', 'the relative path for user shop manage');
INSERT INTO `eh_configurations` (`namespace_id`, `name`, `value`, `description`) VALUES (0, 'user.order.url', '/app/static/indent/indent.html', 'the relative path for user order');
INSERT INTO `eh_configurations` (`namespace_id`, `name`, `value`, `description`) VALUES (0, 'user.coupon.url', '/app/static/coupon/coupon.html', 'the relative path for user coupon');
INSERT INTO `eh_configurations` (`namespace_id`, `name`, `value`, `description`) VALUES (0, 'business.detail.url', '/web/app/user/index.html#/store/details/', 'business details');
INSERT INTO `eh_configurations` (`namespace_id`, `name`, `value`, `description`) VALUES (0, 'business.image.url', 'http://121.199.69.107:8081/imageService/', 'business image url');
INSERT INTO `eh_configurations` (`namespace_id`, `name`, `value`, `description`) VALUES (0, 'biz.back.url', 'http://biz.zuolin.com/Zl-MallMgt/bizmgt/main/tokenlogin.ihtml', 'biz.back.url');
-- INSERT INTO `eh_configurations` (`namespace_id`, `name`,`value`,`description`) VALUES (0, 'prefix.url', 'http://biz.zuolin.com/zl-ec?hideNavigationBar=0&sourceUrl=http://biz.zuolin.com', null);
-- INSERT INTO `eh_configurations` (`namespace_id`, `name`,`value`,`description`) VALUES (0, 'common.pay.url', 'http://pay.zuolin.com/EDS_PAY/', null);
-- INSERT INTO `eh_configurations` (`namespace_id`, `name`,`value`,`description`) VALUES (0, 'apply.shop.url', '%2fweb%2fapp%2flib%2fhtml%2fcoming_soon%2findex.html', null);
-- INSERT INTO `eh_configurations` (`namespace_id`, `name`,`value`,`description`) VALUES (0, 'manage.shop.url', '%2fweb%2fapp%2flib%2fhtml%2fcoming_soon%2findex.html', null);
-- INSERT INTO `eh_configurations` (`namespace_id`, `name`,`value`,`description`) VALUES (0, 'user.order.url', '%2fweb%2fapp%2flib%2fhtml%2fcoming_soon%2findex.html', null);
-- INSERT INTO `eh_configurations` (`namespace_id`, `name`,`value`,`description`) VALUES (0, 'user.coupon.url', '%2fweb%2fapp%2flib%2fhtml%2fcoming_soon%2findex.html', null);
-- INSERT INTO `eh_configurations` (`namespace_id`, `name`,`value`,`description`) VALUES (0, 'business.image.url', 'http://bizimg.zuolin.com/imageService/', 'image url');
-- INSERT INTO `eh_configurations` (`namespace_id`, `name`,`value`,`description`) VALUES (0, 'business.detail.url', '%2fweb%2fapp%2flib%2fhtml%2fcoming_soon%2findex.html', 'detail url');

INSERT INTO `eh_configurations`(`namespace_id`, `name`, `value`, `description`) VALUES (0, 'user.avatar.male.url', 'cs://1/image/aW1hZ2UvTVRvME1qVTBZalpqT1dGa05USm1aVEE1WVRnMU9EWmhOVE0zTm1Nd1pXSTVZUQ','默认男头像url'); 
INSERT INTO `eh_configurations`(`namespace_id`, `name`, `value`, `description`) VALUES (0, 'user.avatar.female.url', 'cs://1/image/aW1hZ2UvTVRvMU1EQTVZVEZrTkdVek9EQXhZbVE0WlRZd1l6UXdOVE0zWVdJNFkyTmlNUQ','默认女头像url'); 
INSERT INTO `eh_configurations`(`namespace_id`, `name`, `value`, `description`) VALUES (0, 'user.def.pwd', '123456','用户默认密码');
INSERT INTO `eh_configurations`(`namespace_id`, `name`, `value`, `description`) VALUES (0, 'hotusers', '', 'phone numbers split by ,');



ALTER TABLE `eh_acl_privileges` AUTO_INCREMENT = 4096;
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`) VALUES(1, 0, 'All');
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`) VALUES(2, 0, 'Visible');
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`) VALUES(3, 0, 'Read');
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`) VALUES(4, 0, 'Create');
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`) VALUES(5, 0, 'Write');
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`) VALUES(6, 0, 'Delete');

INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`) VALUES(100, 2, 'forum.topic.new');
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`) VALUES(101, 2, 'forum.topic.delete');
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`) VALUES(102, 2, 'forum.reply.new');
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`) VALUES(103, 2, 'forum.reply.delete');

ALTER TABLE `eh_acl_roles` AUTO_INCREMENT = 4096;
INSERT INTO `eh_acl_roles`(`id`, `app_id`, `name`) VALUES(1, 0, 'Anonymous');
INSERT INTO `eh_acl_roles`(`id`, `app_id`, `name`) VALUES(2, 0, 'SystemAdmin');
INSERT INTO `eh_acl_roles`(`id`, `app_id`, `name`) VALUES(3, 0, 'AuthenticatedUser');
INSERT INTO `eh_acl_roles`(`id`, `app_id`, `name`) VALUES(4, 0, 'ResourceCreator');
INSERT INTO `eh_acl_roles`(`id`, `app_id`, `name`) VALUES(5, 0, 'ResourceAdmin');
INSERT INTO `eh_acl_roles`(`id`, `app_id`, `name`) VALUES(6, 0, 'ResourceOperator');
INSERT INTO `eh_acl_roles`(`id`, `app_id`, `name`) VALUES(7, 0, 'ResourceUser');
INSERT INTO `eh_acl_roles`(`id`, `app_id`, `name`) VALUES(8, 0, 'SystemExtension');

ALTER TABLE `eh_apps` AUTO_INCREMENT = 4096;


ALTER TABLE `eh_namespaces` AUTO_INCREMENT = 4096;

#
# populate default system user root/password
#
INSERT INTO `eh_users`(`id`, `namespace_id`, `uuid`, `account_name`, `nick_name`, `status`, `create_time`, `salt`, `password_hash`, `avatar`) VALUES (1, 0, UUID(), 'root', 'system user', 1, 
    NOW(), '3023538e14053565b98fdfb2050c7709', '3f2d9e5202de37dab7deea632f915a6adc206583b3f228ad7e101e5cb9c4b199', 'cs://1/image/aW1hZ2UvTVRwak5qQm1OVGRqT1RjelpqWXpORFV3WXpsaU9UQm1Nalk1WVRsalltWmlOZw');
INSERT INTO `eh_users`(`id`, `namespace_id`, `uuid`, `account_name`, `nick_name`, `status`, `create_time`, `salt`, `password_hash`, `avatar`) VALUES (2, 0, UUID(), 'system assistant', '系统小助手', 1, 
    NOW(), '3023538e14053565b98fdfb2050c7709', '3f2d9e5202de37dab7deea632f915a6adc206583b3f228ad7e101e5cb9c4b199', 'cs://1/image/aW1hZ2UvTVRwak5qQm1OVGRqT1RjelpqWXpORFV3WXpsaU9UQm1Nalk1WVRsalltWmlOZw');	
INSERT INTO `eh_users`(`id`, `namespace_id`, `uuid`, `account_name`, `nick_name`, `status`, `create_time`, `salt`, `password_hash`, `avatar`) VALUES (3, 0, UUID(), 'business assistant', '电商小助手', 1, 
    NOW(), '3023538e14053565b98fdfb2050c7709', '3f2d9e5202de37dab7deea632f915a6adc206583b3f228ad7e101e5cb9c4b199', 'cs://1/image/aW1hZ2UvTVRwak5qQm1OVGRqT1RjelpqWXpORFV3WXpsaU9UQm1Nalk1WVRsalltWmlOZw');	
	
#
# Reserve IDs
#
INSERT INTO `eh_sequences`(`domain`, `start_seq`) VALUES('EhUsers', 10000);
INSERT INTO `eh_sequences`(`domain`, `start_seq`) VALUES('EhForums', 10000);

SET foreign_key_checks = 1;
